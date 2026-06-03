const tencentcloud = require('tencentcloud-sdk-nodejs');
const OcrClient = tencentcloud.ocr.v20181119.Client;

// Tencent Cloud credentials (set via env variables in CloudBase console)
const SECRET_ID = process.env.TCB_SECRET_ID || process.env.TENCENTCLOUD_SECRETID;
const SECRET_KEY = process.env.TCB_SECRET_KEY || process.env.TENCENTCLOUD_SECRETKEY;
const REGION = 'ap-beijing';

function createOcrClient() {
    return new OcrClient({
        credential: { secretId: SECRET_ID, secretKey: SECRET_KEY },
        region: REGION,
        profile: { httpProfile: { endpoint: 'ocr.tencentcloudapi.com' } }
    });
}

/**
 * 身份证OCR识别
 * @param {string} base64Image - Base64编码的图片
 * @param {string} cardSide - "FRONT" 正面 或 "BACK" 背面
 */
async function ocrIdCard(base64Image, cardSide) {
    const client = createOcrClient();
    const resp = await client.IDCardOCR({ ImageBase64: base64Image, CardSide: cardSide });

    const result = {};
    if (cardSide === 'FRONT') {
        result.name = resp.Name || '';
        result.sex = resp.Sex || '';
        result.nationality = resp.Nation || '';
        result.birth = resp.Birth || '';
        result.address = resp.Address || '';
        result.idNum = resp.IdNum || '';
    } else {
        result.authority = resp.Authority || '';
        result.validDate = resp.ValidDate || '';
    }
    return result;
}

/**
 * 驾驶证OCR识别
 * @param {string} base64Image - Base64编码的图片
 */
async function ocrDriverLicense(base64Image) {
    const client = createOcrClient();
    const resp = await client.DriverLicenseOCR({ ImageBase64: base64Image });
    return {
        name: resp.Name || '',
        licenseNum: resp.CardCode || '',
        validInDate: resp.StartDate || '',
        validPeriod: resp.EndDate || '',
        clazz: resp.Class || ''
    };
}

/**
 * 通用OCR（印刷体）
 * @param {string} base64Image - Base64编码的图片
 */
async function ocrGeneral(base64Image) {
    const client = createOcrClient();
    const resp = await client.GeneralBasicOCR({ ImageBase64: base64Image });
    const textList = (resp.TextDetections || []).map(t => t.DetectedText);
    return { texts: textList, fullText: textList.join('\n') };
}

// Cloud function entry point
exports.main = async (event, context) => {
    const { action, base64Image, cardSide } = event;

    if (!base64Image) {
        return { code: -1, message: '缺少base64Image参数' };
    }

    try {
        let data;
        switch (action) {
            case 'idcard':
                if (!cardSide || !['FRONT', 'BACK'].includes(cardSide)) {
                    return { code: -1, message: 'cardSide必须为FRONT或BACK' };
                }
                data = await ocrIdCard(base64Image, cardSide);
                break;
            case 'driverLicense':
                data = await ocrDriverLicense(base64Image);
                break;
            case 'general':
                data = await ocrGeneral(base64Image);
                break;
            default:
                return { code: -1, message: '不支持的action: ' + action };
        }
        return { code: 0, message: 'success', data };
    } catch (err) {
        console.error('OCR error:', err);
        return { code: -1, message: err.message || 'OCR识别失败' };
    }
};
