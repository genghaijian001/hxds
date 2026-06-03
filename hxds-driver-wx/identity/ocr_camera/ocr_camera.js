import {
  ocrAction,
} from '../../ocr'

const chooseImage = async () => {
  return new Promise((resolve, reject) => {
    wx.chooseImage({
      count: 1,
      sizeType: ['original','compressed'],
      sourceType: ['album'],
      success: (res) => {
        resolve(res.tempFilePaths[0])
      },
      fail: (res) => {
        reject(new Error(`选择图片失败: ${res.errMsg}`))
      }
    })
  })
}

Page({
  data: {
    title: '身份证识别',
    skipphotoStatus: "0",
    flash: true,
    bottom: 125,
    windowWidth: 300,
    touchPadding: 36,
    windowHeight: 200,
    isShowImage: true,
    showCamera: false,
    path: '',
    cameraAuth: true,
    top: 386,
    left: 24,
    width: 666,
    height: 464
  },
  onLoad(opt) {
    wx.setNavigationBarTitle({
      title: this.data.title
    })
    this.initData()
  },
  showTool() {
    this.setData({
      showCamera: true
    })
  },
  error(e) {
    console.log('camera加载失败')
    this.setData({
      cameraAuth: false
    })
  },
  handleSetting() {
    // 对用户的设置进行判断，如果没有授权，即使用户返回到保存页面，显示的也是“去授权”按钮；同意授权之后才显示保存按钮
    if (!e.detail.authSetting['scope.camera']) {
      wx.showModal({
        title: '',
        content: '若不打开授权，则无法拍照！',
        showCancel: false
      })
    } else {
      this.setData({
        cameraAuth: true
      })
    }
  },
  initData() {
    wx.getSystemInfo({
      success: res => {
        const ios = !!(res.system.toLowerCase().search('ios') + 1)
        const model = res.model >= 'iPhone X'
        let rate = res.windowHeight < 800 ? (res.windowHeight - 110) / 698 : 1
        let bottom = this.data.bottom * rate
        let globalStatusBarHeight = res.statusBarHeight ? res.statusBarHeight * 2 : 0
        let top = (this.data.top + globalStatusBarHeight)
        let temp = ((top - globalStatusBarHeight) * rate - 88 - 48 - 60)
        if (temp < 20) {
          top = (60 + 20 + 88 + 48) / rate + globalStatusBarHeight
          bottom = bottom - (20 - temp) * rate
        }
        this.setData({
          globalStatusBarHeight: globalStatusBarHeight,
          globalIos: true,
          bottom: bottom,
          touchPadding: this.data.touchPadding * rate,
          top: top * rate,
          height: (this.data.height * rate / 4) * 4,
          windowWidth: res.windowWidth,
          windowHeight: res.windowHeight,
        })
      },
    })
  },
  
  //改变是是否闪光
  changeFlash() {
    this.setData({
      flash: !this.data.flash
    })
  },
  goPre() {
    wx.navigateBack({
      delta: 1
    })
  },
  async chooseImg(e) {
    chooseImage()
      .then(ocrAction)
      .catch(e => {
        console.error(e)
        console.error('因为没有真的上传图片，所以走到出错的逻辑里了')
        wx.showToast({
          title: '出错啦...',
          icon: 'error'
        })
      }) 
  },
  takePhotoAction(e) {
    this.setData({
      isShowImage: true,
    })
    const ctx = wx.createCameraContext()
    ctx.takePhoto({
      quality: 'high', //高质量
      success: (res) => {
        this.loadTempImagePath(res.tempImagePath);
      },
    })
  },
  cancel() {
    wx.navigateBack()
    if (this._cb) {
      this._cb(this._callbackId, 'cancel')
    }
  },
  loadTempImagePath(filePath) {
    let that = this
    let {
      windowWidth,
      windowHeight
    } = this.data
    let rate = windowWidth / 375
    let image_x = (that.data.left / 2) * rate;
    let image_y = ((that.data.top / 2)) * rate;
    let image_width = (375 - 2 * that.data.left / 2) * rate;
    let image_height = (that.data.height / 2) * rate;
    // (image_height < 700) && (image_height > 380) && (image_height = 380)
    wx.getImageInfo({
      src: filePath,
      success(res) {
        that.canvas = wx.createCanvasContext("image-canvas", that)
        //过渡页面中，图片的路径座标和大小
        that.canvas.drawImage(filePath, 0, 0, that.data.windowWidth, that.data.windowHeight)
        wx.showLoading({
          title: '数据处理中...',
          icon: 'loading',
          duration: 10000
        })
        // 这里有一些很神奇的操作,总结就是MD拍出来的照片规格居然不是统一的过渡页面中，对裁剪框的设定
        that.canvas.setStrokeStyle('rgba(0, 0, 0, 0)')
        that.canvas.strokeRect(image_x, image_y, image_width, image_height)
        that.canvas.draw()
        setTimeout(function () {
          wx.canvasToTempFilePath({ //裁剪对参数
            canvasId: "image-canvas",
            x: image_x, //画布x轴起点
            y: image_y, //画布y轴起点
            width: image_width, //画布宽度
            height: image_height, //画布高度
            destWidth: image_width, //输出图片宽度
            destHeight: image_height, //输出图片高度
            success: function (res) {
              that.setData({
                image: res.tempFilePath,
                isShowImage: false
              })
              wx.hideLoading()
              ocrAction(res.tempFilePath)
                .then((result) => {
                  that.success(result)
                })
                .catch((e) => {
                  that.setData({
                    isShowImage: true
                  })
                  console.error('因为没有真的上传图片，所以走到出错的逻辑里了')
                  wx.showToast({
                    title: '出错啦...',
                    icon: 'loading'
                  })
                }) 
            },
            fail: function (e) {
              wx.showToast({
                title: '出错啦...',
                icon: 'loading'
              })
            }
          });
        }, 1000);
      }
    })
  },
  success(e) {
    console.error('success', e)
  },
})