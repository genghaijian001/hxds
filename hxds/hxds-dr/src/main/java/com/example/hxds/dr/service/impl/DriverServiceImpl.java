package com.example.hxds.dr.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.common.util.MicroAppUtil;
import com.example.hxds.common.util.PageUtils;
import com.example.hxds.dr.db.dao.DriverDao;
import com.example.hxds.dr.db.dao.DriverSettingsDao;
import com.example.hxds.dr.db.dao.WalletDao;
import com.example.hxds.dr.db.pojo.DriverSettingsEntity;
import com.example.hxds.dr.db.pojo.WalletEntity;
import com.example.hxds.dr.service.DriverService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.CreatePersonRequest;
import com.tencentcloudapi.iai.v20200303.models.CreatePersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {

    @Value("${tencent.cloud.secretId}")
    private String secretId;

    @Value("${tencent.cloud.secretKey}")
    private String secretKey;

    @Value("${tencent.cloud.face.groupName}")
    private String groupName;

    @Value("${tencent.cloud.face.region}")
    private String region;
    @Resource
    private MicroAppUtil microAppUtil;
    @Resource
    private DriverDao driverDao;
    @Resource
    private WalletDao walletDao;
    @Resource
    private DriverSettingsDao settingsDao;


    /**
     * 添加司机记录
     * @param param
     * @return
     */
    @Override
    @Transactional
    @LcnTransaction
    public String registerNewDriver(Map param) {
        //临时授权码
        String code = MapUtil.getStr(param, "code");
        //转换为永久授权码
        String openId = microAppUtil.getOpenId(code);

        HashMap tempParam = new HashMap() {{
            put("openId", openId);
        }};
        if (driverDao.hasDriver(tempParam) != 0) {
            throw new HxdsException("该微信无法注册");
        }
        param.put("openId", openId);
        driverDao.registerNewDriver(param);//插入司机记录
        String driverId = driverDao.searchDriverId(openId);//查询司机主键值

        //添加司机设置记录
        DriverSettingsEntity settingsEntity = new DriverSettingsEntity();
        settingsEntity.setDriverId(Long.parseLong(driverId));
        JSONObject json = new JSONObject();
        json.set("orientation", "");
        json.set("listenService", true);
        json.set("orderDistance", 0);
        json.set("rangeDistance", 5);
        json.set("autoAccept", false);
        settingsEntity.setSettings(json.toString());
        settingsDao.insertDriverSettings(settingsEntity);


        //添加司机钱包记录
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setDriverId(Long.parseLong(driverId));
        walletEntity.setBalance(new BigDecimal("0"));
        walletEntity.setPassword(null);//支付密码为空，用户支付的时候，系统会自动提示用户设置密码
        walletDao.insert(walletEntity);

        return driverId;
    }

    /**
     * 司机实名认证
     * @param param
     * @return
     */
    @Override
    @Transactional
    @LcnTransaction
    public int updateDriverAuth(Map param) {
        int rows = driverDao.updateDriverAuth(param);
        return rows;
    }

    /**
     * 开通活体检测 创建面部模型
     * @param driverId
     * @param photo
     * @return
     */
    @Override
    @Transactional
    @LcnTransaction
    public String createDriverFaceModel(long driverId, String photo) {
        //查询员工的姓名、性别
        HashMap map = driverDao.searchDriverNameAndSex(driverId);
        String name = MapUtil.getStr(map, "name");
        String sex = MapUtil.getStr(map, "sex");

        //腾讯云端创建司机面部档案
        Credential cred = new Credential(secretId,secretKey);
        IaiClient client=new IaiClient(cred,region);
        try{
            CreatePersonRequest req=new CreatePersonRequest();
            req.setGroupId(groupName);  //人员库ID
            req.setPersonId(driverId + "");   //人员ID
            long gender = sex.equals("男") ? 1L : 2L;
            req.setGender(gender);
            req.setQualityControl(4L);  //  照片质量等级
            req.setUniquePersonControl(4L);   //重复人员识别
            req.setPersonName(name);   //姓名
            req.setImage(photo);    //base64图片
            CreatePersonResponse resp = client.CreatePerson(req);
            if (StrUtil.isNotBlank(resp.getFaceId())) {
                //更新司机标表的Archive字段值
                int rows = driverDao.updateDriverArchive(driverId);
                if (rows != 1) {
                    return "更新司机归档字段失败";
                }
            }
        }catch (Exception e) {
            log.error("创建腾讯云端司机档案失败", e);
            return "创建腾讯云端司机档案失败";
        }
        return "";
    }

    /**
     * 登录
     * @param code
     * @return
     */
    @Override
    public HashMap login(String code) {
        String openId = microAppUtil.getOpenId(code);
        HashMap result = driverDao.login(openId);
        if (result != null && result.containsKey("archive")){
            int temp=MapUtil.getInt(result,"archive");
            boolean archive = (temp==1) ? true : false;
            result.replace("archive",archive);
        }
        return result;
    }

    /**
     * 查询司机个人汇总信息
     * @param driverId
     * @return
     */
    @Override
    public HashMap searchDriverBaseInfo(long driverId) {
        HashMap result = driverDao.searchDriverBaseInfo(driverId);
        JSONObject summary = JSONUtil.parseObj(MapUtil.getStr(result, "summary"));
        result.replace("summary",summary);
        return result;
    }

    /**
     * 查询司机分页记录
     * @param param
     * @return
     */
    @Override
    public PageUtils searchDriverByPage(Map param) {
        long count = driverDao.searchDriverCount(param);
        ArrayList<HashMap> list=null;
        if (count==0) {
            list=new ArrayList<>();
        }else {
            list=driverDao.searchDriverByPage(param);
        }
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        PageUtils pageUtils=new PageUtils(list,count,start,length);
        return pageUtils;
    }

    /**
     * 查询司机实名认证信息
     * @param driverId
     * @return
     */
    @Override
    public HashMap searchDriverAuth(long driverId) {
        HashMap result = driverDao.searchDriverAuth(driverId);
        return result;
    }

    /**
     * 司机微服务中查询司机实名认证申请
     * @param driverId
     * @return
     */
    @Override
    public HashMap searchDriverRealSummary(long driverId) {
        HashMap map = driverDao.searchDriverRealSummary(driverId);
        return map;
    }

    /**
     * 司机微服务中更新司机备案状态
     * @param param
     * @return
     */
    @Override
    @Transactional
    @LcnTransaction
    public int updateDriverRealAuth(Map param) {
        int rows = driverDao.updateDriverRealAuth(param);
        return rows;
    }

    /**
     * mis查询司机信息  折叠面板
     * @param driverId
     * @return
     */
    @Override
    public HashMap searchDriverBriefInfo(long driverId) {
        HashMap map = driverDao.searchDriverBriefInfo(driverId);
        return map;
    }

    @Override
    public String searchDriverOpenId(long driverId) {
        String openId = driverDao.searchDriverOpenId(driverId);
        return openId;
    }
}
