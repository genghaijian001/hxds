package com.example.hxds.bff.driver.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
//    @Autowired
//    private TbUserDao userDao;

    /**
     * 返回一个用户所拥有的权限集合
     *
     * Fix-4: 本项目采用【登录态鉴权模式】，仅校验用户是否已登录（SaToken @SaCheckLogin）。
     * 不使用细粒度权限（@SaCheckPermission）和角色（@SaCheckRole）。
     * 若将来需要权限控制，请在此方法中实现从数据库查询用户权限的逻辑，
     * 切勿在接口上直接添加 @SaCheckPermission 注解而不修改此方法，否则所有用户都将无权访问。
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        // 当前不使用权限判断，如需扩展请实现具体逻辑
        return new ArrayList<String>();
    }


    /**
     * 返回一个用户所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        //因为本项目不需要用到角色判定，所以这里就返回一个空的ArrayList对象
        ArrayList<String> list = new ArrayList<String>();
        return list;
    }

}
