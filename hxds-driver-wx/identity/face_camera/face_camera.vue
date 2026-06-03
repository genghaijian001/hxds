<template>
	<view>
		<view class="face-container">
			<camera device-position="front" flash="off" class="camera" @error="error" v-if="showCamera">
				<cover-image src="../static/face_camera/bg.png" class="bg"></cover-image>
			</camera>
			<view class="image-container" v-if="showImage">
				<image mode="widthFix" class="photo" :src="photoPath"></image>
				<view class="cover"></view>
			</view>
		</view>
		<view class="desc">
			<block v-if="mode == 'verificate'">
				<image src="../static/face_camera/tips.png" mode="widthFix" class="tips"></image>
				<text>请把面部放在圆圈内</text>
				<text>拍摄脸部来确认身份</text>
			</block>
			<block v-if="mode == 'create'">
				<image src="../static/face_camera/face.png" mode="widthFix" class="face"></image>
				<text>请把完整面部放在圆圈内</text>
				<text>拍摄脸部来保存身份识别数据</text>
			</block>
		</view>
		<button class="btn" @tap="confirmHandle">{{ mode == 'create' ? '录入面部信息' : '身份核实' }}</button>
	</view>
</template>

<script>
let dayjs = require('dayjs');
export default {
	data() {
		return {
			mode: 'verificate',
			photoPath: '',
			showCamera: false,
			showImage: false,
			audio: null
		};
	},
	methods: {
		confirmHandle:function(){
			let that=this
			that.audio.stop()
			let ctx=uni.createCameraContext()
			ctx.takePhoto({
				quality:"high",
				success:function(resp){
					that.photoPath=resp.tempImagePath
					that.showCamera=false
					that.showImage=true
					uni.getFileSystemManager().readFile({
						filePath:that.photoPath,
						encoding:"base64",
						success:function(resp){
							let base64='data:image/png;base64,'+resp.data
							let url=null
							if(that.mode=="create"){
								//创建司机面部模型档案
								url = that.url.createDriverFaceModel;
							}
							else{
								//验证司机面部模型
								url = that.url.verificateDriverFace;
							}
							that.ajax(url,"POST",{photo:base64},function(resp){
								let result=resp.data.result
								if(that.mode=="create"){
									if(result!=null&&result.length>0){
										console.error(result);
										uni.showToast({
											icon: 'none',
											title: '面部录入失败，请重新录入'
										});
										setTimeout(function() {
											that.showCamera = true;
											that.showImage = false;
										}, 2000);
									}
									else{
										uni.showToast({
											title: '面部录入成功'
										});
										setTimeout(function() {
											uni.switchTab({
												url: '../../pages/workbench/workbench'
											});
										}, 2000);
									}
								}
								else{
									//TODO 判断人脸识别结果
								}
							})
						}
					})
				}
			})
		}
	},
	onLoad: function(options) {
		let that=this
		that.mode=options.mode
		let audio=uni.createInnerAudioContext();
		that.audio=audio
		audio.src="/static/voice/voice_5.mp3"
		// 检查相机权限后再显示组件
		wx.getSetting({
			success(res) {
				if (res.authSetting['scope.camera'] === false) {
					// 已拒绝，引导去设置
					uni.showModal({
						title: '需要相机权限',
						content: '人脸识别需要访问摄像头，请在设置中允许',
						confirmText: '去设置',
						cancelText: '取消',
						success(r) { if (r.confirm) wx.openSetting(); }
					});
				} else if (res.authSetting['scope.camera'] === undefined) {
					// 首次请求
					wx.authorize({
						scope: 'scope.camera',
						success() { that.showCamera = true; audio.play(); },
						fail() {
							uni.showToast({ icon: 'none', title: '未授权相机权限，无法拍照', duration: 2000 });
						}
					});
					return;
				} else {
					that.showCamera = true;
				}
				audio.play();
			}
		});
	},
	onHide: function() {
		if(this.audio!=null){
			this.audio.stop()
		}
	}
};
</script>

<style lang="less">
@import url('face_camera.less');
</style>
