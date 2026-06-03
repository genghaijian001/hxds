// hxds-driver-wx/ocr.js
// 使用腾讯云OCR API（通过后端hxds-dr服务）识别身份证和驾驶证

const OCR_REQUEST_TIMEOUT = 120000;

/**
 * 将本地图片文件读取为 Base64 字符串
 * @param {string} filePath 本地临时文件路径
 * @returns {Promise<string>} base64字符串（不含 data:image/... 前缀）
 */
function fileToBase64(filePath) {
    return new Promise((resolve, reject) => {
        const fs = wx.getFileSystemManager();
        fs.readFile({
            filePath: filePath,
            encoding: 'base64',
            success: (res) => {
                resolve(res.data);
            },
            fail: (err) => {
                reject(err);
            }
        });
    });
}

/**
 * 调用后端腾讯云OCR接口识别身份证
 * @param {string} base64Image  图片base64
 * @param {string} cardSide     FRONT 或 BACK
 * @returns {Promise<object>}   OCR识别结果字段
 */
function callIdCardOcr(base64Image, cardSide, apiUrl) {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token');
        uni.request({
            url: apiUrl,
            method: 'POST',
            timeout: OCR_REQUEST_TIMEOUT,
            header: {
                'Content-Type': 'application/json',
                token: token
            },
            data: {
                base64Image: base64Image,
                cardSide: cardSide
            },
            success: (res) => {
                if (res.statusCode === 200 && res.data && res.data.code === 200) {
                    resolve(res.data.result);
                } else {
                    console.error('身份证OCR接口异常:', res);
                    reject('身份证识别失败');
                }
            },
            fail: (err) => {
                console.error('身份证OCR请求失败:', err);
                if (err && err.errMsg && err.errMsg.includes('timeout')) {
                    reject('身份证识别超时，请确认网关与后端服务可用后重试');
                    return;
                }
                reject('网络错误，识别失败');
            }
        });
    });
}

/**
 * 调用后端腾讯云OCR接口识别驾驶证
 * @param {string} base64Image  图片base64
 * @returns {Promise<object>}   OCR识别结果字段
 */
function callDriverLicenseOcr(base64Image, apiUrl) {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token');
        uni.request({
            url: apiUrl,
            method: 'POST',
            timeout: OCR_REQUEST_TIMEOUT,
            header: {
                'Content-Type': 'application/json',
                token: token
            },
            data: {
                base64Image: base64Image
            },
            success: (res) => {
                if (res.statusCode === 200 && res.data && res.data.code === 200) {
                    resolve(res.data.result);
                } else {
                    console.error('驾驶证OCR接口异常:', res);
                    reject('驾驶证识别失败');
                }
            },
            fail: (err) => {
                console.error('驾驶证OCR请求失败:', err);
                if (err && err.errMsg && err.errMsg.includes('timeout')) {
                    reject('驾驶证识别超时，请确认网关与后端服务可用后重试');
                    return;
                }
                reject('网络错误，识别失败');
            }
        });
    });
}

/**
 * 核心OCR处理函数
 * @param {string} filePath  图片的本地临时路径
 * @param {string} type      拍摄类型: idcardFront | idcardBack | DrcardFront | Drcardback | idcardHolding | DrcardHolding
 * @returns {Promise<object>} OCR识别结果，或持证照片返回空对象
 */
export const ocrAction = async (filePath, type, apiConfig) => {
    wx.showLoading({ title: '正在识别...' });
    try {
        if (!apiConfig || !apiConfig.ocrIdCard || !apiConfig.ocrDriverLicense) {
            throw new Error('OCR接口地址未配置');
        }
        // 持证照片不做OCR识别，直接返回空对象
        if (type === 'idcardHolding' || type === 'DrcardHolding') {
            wx.hideLoading();
            return {};
        }

        const base64Image = await fileToBase64(filePath);

        let ocrResult = {};
        if (type === 'idcardFront') {
            const raw = await callIdCardOcr(base64Image, 'FRONT', apiConfig.ocrIdCard);
            ocrResult = {
                name: raw.name || '',
                sex: raw.sex || '',
                nation: raw.nationality || '',
                birthday: raw.birth ? raw.birth.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3') : '',
                address: raw.address || '',
                idcardNo: raw.idNum || ''
            };
        } else if (type === 'idcardBack') {
            const raw = await callIdCardOcr(base64Image, 'BACK', apiConfig.ocrIdCard);
            // validDate格式: "20180101-20280101" 或 "2018.01.01-2028.01.01"
            const validDate = raw.validDate || '';
            const parts = validDate.split('-');
            ocrResult = {
                issueAuth: raw.authority || '',
                validDateStart: parts[0] ? parts[0].replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3') : '',
                validDateEnd: parts[1] ? parts[1].replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3') : ''
            };
        } else if (type === 'DrcardFront' || type === 'Drcardback') {
            const raw = await callDriverLicenseOcr(base64Image, apiConfig.ocrDriverLicense);
            ocrResult = {
                drcardType: raw.clazz || '',
                drcardName: raw.name || '',
                drcardNum: raw.licenseNum || '',
                validInDate: raw.validInDate || '',
                validPeriod: raw.validPeriod || ''
            };
        }

        wx.hideLoading();
        return ocrResult;
    } catch (err) {
        wx.hideLoading();
        console.error('OCR识别失败:', err);
        uni.showToast({
            title: typeof err === 'string' ? err : 'OCR识别失败，请重试',
            icon: 'none',
            duration: 3000
        });
        return {};
    }
};
