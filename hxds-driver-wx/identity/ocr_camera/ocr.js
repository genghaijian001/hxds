

export const ocrAction = async (filePath) => {
  wx.showLoading({
    title: '正在处理',
  })

  // TODO 将 tempFilePath 上传到 CDN 得到 url
  let img_url = ''
  // 实现上传到 cdn 的代码, 将得到的 url 写到 img_url 中

  // 调用服务市场接口 https://fuwu.weixin.qq.com/service/detail/000ce4cec24ca026d37900ed551415
  try {
    await wx.serviceMarket.invokeService({
      api: 'OcrAllInOne',
      service: 'wx79ac3de8be320b71',
      data: {
        img_url,
        data_type: 3,
        ocr_type: 1, // 详细见文档
      }
    })
    wx.hideLoading()
  } catch (e) {
    wx.hideLoading()
    throw e
  }
}

export const takePhoto = async (tempFilePath) => {
  if(tempFilePath){
    return ocrAction(tempFilePath)
  } 
  const filePath = await chooseImage()
  return ocrAction(filePath)
}
