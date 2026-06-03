<template>
	<view style="width:750rpx;height:100vh">
		<canvas v-if="isShowImage" canvas-id="image-canvas" :style="'position:fixed;top:0rpx;width:' + windowWidth + 'px;height:' + windowHeight + 'px;'"></canvas>
		
		<camera v-if="cameraAuth" :flash="flash ? 'on' : 'off'" @initdone="initDoneHandle" mode="normal" device-position="back" @error="error" style="position:fixed;top:0rpx;width: 100%; height: 100vh;">
			<cover-view v-if="showCamera" style="display: flex; height: 100vh; flex-direction: column;">
				<cover-view :style="'height:' + top + 'rpx'" class="header bg-cover"></cover-view>
				<cover-view class="main-container">
					<cover-view :style="'height:' + height + 'rpx'" class="container-center"></cover-view>
					<cover-view :style="'flex: 0 0 ' + left + 'px'" class="container-left bg-cover"></cover-view>
					<cover-view :style="'flex: 0 0 ' + left + 'px'" class="container-right bg-cover"></cover-view>
				</cover-view>
				<cover-view class="footer bg-cover"></cover-view>
			</cover-view>
			
			<cover-view v-if="showCamera" style="position:fixed; top:0rpx; display: flex; height: 100vh; flex-direction: column;">
				<cover-view :style="'height:' + top + 'rpx'" class="header">
					<cover-view :style="'padding-top:' + globalStatusBarHeight + 'rpx'" class="title"> {{title}}</cover-view>
					<cover-image v-if="cameraAuth" @tap="changeFlash" style="margin-top:60rpx" class="flash btn-click" :src="flash ? '/static/resources/ScanCode_Flash@3x.png' : '/static/resources/ScanCode_FlashClose@3x.png'"></cover-image>
				</cover-view>
				<cover-view class="main-container">
					<cover-view v-if="cameraAuth" :style="'height:' + height + 'rpx'" class="container-center">
						<cover-view class="column">
							<cover-image class="frame" src="/static/resources/ScanCode_Frame.png"> </cover-image>
							<cover-image style="transform: rotate(90deg);" class="frame" src="/static/resources/ScanCode_Frame.png"> </cover-image>
						</cover-view>
						<cover-view class="bottom-column">
							<cover-image style="transform: rotate(270deg);" class="frame" src="/static/resources/ScanCode_Frame.png"> </cover-image>
							<cover-image style="transform: rotate(180deg);" class="frame" src="/static/resources/ScanCode_Frame.png"> </cover-image>
						</cover-view>
					</cover-view>
					<cover-view v-else :style="'height:' + height + 'rpx'" class="no-authwording bg-cover">
						<cover-view style="position:relative; top:-45px;" class="touch-wording">暂无权限访问摄像头，请去小程序设置中授权打开摄像头</cover-view>
					</cover-view>
					<cover-view :style="'flex: 0 0 ' + left + 'px'" class="container-left"></cover-view>
					<cover-view :style="'flex: 0 0 ' + left + 'px'" class="container-right"></cover-view>
				</cover-view>
				<cover-view class="footer">
					<cover-view v-if="cameraAuth" class="frame-wording">拍摄要求：清晰完整，避免缺边、模糊、反光。</cover-view>
					<cover-view class="tool-container">
						<cover-view class="img-container">
							<cover-image @tap="goPre" class="close tool-icon btn-click" src="/static/resources/ScanCode_Close@3x.png"> </cover-image>
							<cover-image @tap="takePhotoAction" class="cam tool-icon btn-click" src="/static/resources/ScanCode_Cam@3x.png"> </cover-image>
							<cover-image @tap="chooseImg" class="pic tool-icon btn-click" src="/static/resources/ScanCode_Pic@3x.png"> </cover-image>
						</cover-view>
						<cover-view :style="'height:' + bottom + 'px'" class="tool-blank"></cover-view>
					</cover-view>
				</cover-view>
			</cover-view>
		</camera>
		
		<cover-view v-if="!isShowImage" class="show-img">
			<cover-view style="display: flex; height: 100vh; flex-direction: column;">
				<cover-view :style="{height:top + 'rpx'}" class="header"></cover-view>
				<cover-view class="main-container">
					<cover-view :style="'height:' + height + 'rpx'" class="container-center">
						<cover-image class="photo" :src="image"></cover-image>
					</cover-view>
					<cover-view :style="'flex: 0 0 ' + left + 'px'" class="container-left"></cover-view>
					<cover-view :style="'flex: 0 0 ' + left + 'px'" class="container-right"></cover-view>
				</cover-view>
				<cover-view class="footer"></cover-view>
			</cover-view>
		</cover-view>
		
		<view v-if="!cameraAuth" style="position:fixed;top:0rpx;width: 100%; height: 100vh;background-color: #7f7f7f">
			<view style="display: flex; height: 100vh; flex-direction: column;">
				<view :style="'height:'+ top + 'rpx'" class="header"></view>
				<view class="main-container">
					<view :style="'height:' + height + 'rpx'" class="container-center"></view>
					<view :style="'flex: 0 0 ' + left + 'px'" class="container-left"></view>
					<view :style="'flex: 0 0 ' + left + 'px'" class="container-right"></view>
				</view>
				<view class="footer"></view>
			</view>
			<view style="position:fixed; top:0rpx; display: flex; height: 100vh; flex-direction: column;">
				<view :style="'height:' + top + 'rpx'" class="header">
					<view :style="'padding-top:' + globalStatusBarHeight + 'rpx'" class="title"> {{title}}</view>
				</view>
				<view class="main-container">
					<view style="position:relative; top:-45px;" class="touch-wording">
						{{isDevTools ? '当前为DevTools模拟模式，请点击下方图标从相册选择图片' : '暂无权限访问摄像头，请点击下方按钮设置权限'}}
					</view>
				</view>
				<view style="text-align: center; width: 750rpx; position:relative; top:-25px;">
					<button type="primary" style="width: 90px" open-type="openSetting">设置</button>
				</view>
				<view class="footer">
					<view class="tool-container">
						<view class="img-container">
							<image @tap="goPre" class="close tool-icon btn-click" src="/static/resources/ScanCode_Close@3x.png"> </image>
							<image @tap="takePhotoAction" class="cam tool-icon btn-click" src="/static/resources/ScanCode_Cam@3x.png"> </image>
							<image @tap="chooseImg" class="pic tool-icon btn-click" src="/static/resources/ScanCode_Pic@3x.png"> </image>
						</view>
						<view :style="'height:' + bottom + 'px'" class="tool-blank"></view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { ocrAction } from '@/ocr.js';

	// 【修改的必要性】 1. 新增 chooseImage 辅助函数
	// 这是一个新增的辅助函数，它将 uni.chooseImage 这个回调风格的API封装成了Promise。
	// 这样做的目的是为了解决您日志中出现的 "chooseImage is not defined" 的错误，
	// 并让 async/await 语法可以正确地调用相册选择功能。
	function chooseImage() {
		return new Promise((resolve, reject) => {
			uni.chooseImage({
				count: 1,
				sizeType: ['original', 'compressed'],
				sourceType: ['album'],
				success: (res) => {
					resolve(res.tempFilePaths[0]);
				},
				fail: (err) => {
					reject(err);
				}
			});
		});
	}

	export default {
		data() {
			return {
				showCamera: false,
				showTool: false,
				flash: false,
				type: null,
				title: null,
				windowWidth: 0,
				windowHeight: 0,
				top: 0,
				height: 0,
				left: 0,
				bottom: 0,
				globalStatusBarHeight: 0,
				isShowImage: false,
				image: '',
				cameraAuth: false,
				isDevTools: false
			};
		},
		onLoad: function(options) {
			this.type = options.type;
			this.title = options.title;
			// 微信基础库 3.15+ 会提示 getSystemInfoSync 废弃，优先使用新 API，失败时回退
			let sys = this.getSystemInfoSafe();
			this.windowWidth = sys.windowWidth;
			this.windowHeight = sys.windowHeight;
			this.top = sys.statusBarHeight + 45;
			this.height = this.windowWidth * (19 / 30);
			this.left = (this.windowWidth - this.height * (30 / 19)) / 2;
			this.bottom = sys.windowHeight * 0.05;
			this.globalStatusBarHeight = sys.statusBarHeight * 2;
			this.checkCameraPermission();
		},
		methods: {
			getSystemInfoSafe() {
				try {
					if (typeof wx !== 'undefined' && wx.getWindowInfo && wx.getAppBaseInfo) {
						const windowInfo = wx.getWindowInfo();
						const appBaseInfo = wx.getAppBaseInfo();
						return {
							windowWidth: windowInfo.windowWidth,
							windowHeight: windowInfo.windowHeight,
							statusBarHeight: windowInfo.statusBarHeight || appBaseInfo.statusBarHeight || 0
						};
					}
				} catch (e) {
					console.warn('getWindowInfo回退到getSystemInfoSync', e);
				}
				return uni.getSystemInfoSync();
			},
			/**
			 * 【修改的必要性】 2. 健壮的权限处理
			 * 这段代码是解决您相机权限问题的核心。它通过主动检查(getSetting)、主动请求(authorize)、
			 * 引导设置(openSetting)三步，确保在渲染<camera>组件之前，权限一定是就绪状态。
			 * 同时，在用户授权成功后，会设置 showCamera = true，解决了您提到的授权后黑屏、按钮点不了的问题。
			 */
			checkCameraPermission() {
				const that = this;
				// DevTools模拟器无真实摄像头，强制走相册模式（使用普通<image>元素，可点击）
				if (uni.getSystemInfoSync().platform === 'devtools') {
					that.isDevTools = true;
					that.cameraAuth = false;
					return;
				}
				uni.getSetting({
					success(res) {
						if (res.authSetting['scope.camera']) {
							that.cameraAuth = true;
							that.showCamera = true;
						} else if (res.authSetting['scope.camera'] === undefined) {
							uni.authorize({
								scope: 'scope.camera',
								success() {
									that.cameraAuth = true;
									that.showCamera = true;
								},
								fail() {
									uni.showToast({
										title: '您拒绝了相机授权',
										icon: 'error',
										duration: 2000
									});
									setTimeout(() => {
										uni.navigateBack({ delta: 1 });
									}, 1000);
								}
							});
						} else {
							uni.showModal({
								title: '授权提示',
								content: '请在设置中手动开启相机权限',
								success(res) {
									if (res.confirm) {
										uni.openSetting({
											success(settingRes) {
												if (settingRes.authSetting['scope.camera']) {
													that.cameraAuth = true;
													that.showCamera = true;
												} else {
													uni.navigateBack({ delta: 1 });
												}
											}
										});
									} else {
										uni.navigateBack({ delta: 1 });
									}
								}
							});
						}
					}
				});
			},

			initDoneHandle: function() {
				this.showTool = true;
			},
			chooseImg: async function() {
				try {
					let that = this;
					const tempPath = await chooseImage();
					that.showTool = false;
					that.showCamera = false;
					that.isShowImage = true;
					let ctx = uni.createCanvasContext('image-canvas', this);
					ctx.drawImage(tempPath, 0, 0, that.windowWidth, that.windowHeight);
					ctx.draw(false, async () => {
						let canvasPath = await that.canvasToTempFilePath();
						const ocrResult = await ocrAction(canvasPath, that.type, that.url);
						const eventChannel = that.getOpenerEventChannel();
						eventChannel.emit('takePhoto', {
							tempPath: tempPath,
							ocrResult: ocrResult
						});
						uni.navigateBack({ delta: 1 });
					});
				} catch (e) {
					console.log('从相册选择并识别失败', e);
					if (e.errMsg && e.errMsg.includes('cancel')) {
						uni.navigateBack({ delta: 1 });
					}
				}
			},
			takePhotoAction: async function() {
				try {
					let that = this;
					let camera = uni.createCameraContext();
					const { tempImagePath } = await new Promise((resolve, reject) => {
						camera.takePhoto({ quality: 'high', success: resolve, fail: reject });
					});
					that.showTool = false;
					that.showCamera = false;
					that.isShowImage = true;
					let ctx = uni.createCanvasContext('image-canvas', this);
					ctx.drawImage(tempImagePath, 0, 0, that.windowWidth, that.windowHeight);
					ctx.draw(false, async () => {
						let canvasPath = await that.canvasToTempFilePath();
						const ocrResult = await ocrAction(canvasPath, that.type, that.url);
						const eventChannel = that.getOpenerEventChannel();
						eventChannel.emit('takePhoto', {
							tempPath: tempImagePath,
							ocrResult: ocrResult
						});
						uni.navigateBack({ delta: 1 });
					});
				} catch (e) {
					console.log('拍照并识别失败', e);
				}
			},
			canvasToTempFilePath: function() {
				return new Promise((resolve, reject) => {
					uni.canvasToTempFilePath({
						canvasId: 'image-canvas',
						success: function(res) {
							resolve(res.tempFilePath);
						},
						fail: function(err) {
							reject(err);
						}
					}, this);
				});
			},
			goPre: function() {
				uni.navigateBack({ delta: 1 });
			},
			close: function() {
				uni.navigateBack({ delta: 1 });
			},
			changeFlash: function() {
				this.flash = !this.flash;
			},
			error(e) {
				console.log('camera加载失败', e.detail);
			}
		}
	};
</script>
<style>
	/* 【说明】这里的样式是从 .wxss 文件直接复制的，在 uni-app 中基本兼容 */
	.bg {
		display: flex;
		top: 0;
		height: 100vh;
	}

	.bg-cover {
		background-color: #000;
		opacity: 0.3;
	}

	.title {
		width: 750rpx;
		height: 88rpx;
		line-height: 70rpx;
		color: #fff;
		font-size: 34rpx;
		text-align: center;
	}

	.flash {
		width: 24px;
		height: 24px;
		margin-left: 24px;
	}

	.main-container {
		display: flex;
	}

	.footer {
		display: flex;
		flex-direction: column;
		align-items: flex-end;
		flex: 1;
	}

	.container-center {
		display: flex;
		flex-wrap: wrap;
		align-content: space-between;
		flex: 1;
	}

	.no-authwording {
		display: flex;
		flex-wrap: wrap;
		align-content: center;
		flex: 1;
	}

	.container-left,
	.container-right {
		flex: 0 0 12px;
	}

	.container-left {
		order: -1;
	}

	.column {
		flex-basis: 100%;
		display: flex;
		justify-content: space-between;
	}

	.bottom-column {
		flex-basis: 100%;
		display: flex;
		align-items: flex-end;
		justify-content: space-between;
	}

	.close {
		justify-content: flex-start;
	}

	.pic {
		justify-content: flex-end;
	}

	.tool-icon {
		align-items: center;
	}

	.cam {
		justify-content: center;
	}

	.frame {
		display: inline-block;
		width: 32rpx;
		height: 32rpx;
	}

	.right-frame {
		float: right;
	}

	.bottom-tool {
		padding: 0rpx 42rpx;
		position: fixed;
		bottom: 225rpx;
		display: flex;
		flex-direction: row;
		margin-top: 6rpx;
		justify-content: center;
		align-items: center;
	}

	.tool-container {
		flex-basis: 100%;
		display: flex;
		justify-content: flex-end;
		flex-direction: column;
		align-items: center;
		width: 750rpx;
		height: 130px;
		line-height: 74px;
	}

	.img-container {
		display: flex;
		justify-content: flex-end;
		flex-direction: row;
		align-items: center;
		width: 750rpx;
		height: 74px;
		line-height: 74px;
	}

	.frame-wording {
		justify-content: flex-start;
		width: 750rpx;
		padding-top: 15px;
		text-align: center;
		opacity: 0.5;
		font-family: PingFangSC-Regular;
		height: 20px;
		line-height: 20px;
		font-size: 14px;
		color: #FFFFFF;
	}

	.tool-blank {
		width: 750rpx;
	}

	.cam-container {
		width: 538rpx;
		text-align: center;
	}

	.close {
		padding-left: 66px;
		width: 32px;
		height: 32px;
	}

	.cam {
		margin: 0 auto;
		width: 74px;
		height: 74px;
	}

	.pic {
		padding-right: 66px;
		width: 32px;
		height: 32px;
	}

	.touch-wording {
		width: 750rpx;
		text-align: center;
		font-size: 14px;
		color: rgba(255, 255, 255, 0.90);
	}

	.show-img {
		position: fixed;
		top: 0;
		background-color: #000;
		width: 750rpx;
		height: 100vh;
	}

	.photo {
		width: 100%;
		height: 100%;
	}

	.btn-click:active {
		opacity: 0.5;
	}

	.choose-img-container {
		position: fixed;
		top: 0;
		background-color: #000;
		width: 750rpx;
		height: 100vh;
		line-height: 100%;
	}

	.choose-img {
		margin: auto 0;
		width: 750rpx;
		height: auto;
		vertical-align: middle;
	}
</style>