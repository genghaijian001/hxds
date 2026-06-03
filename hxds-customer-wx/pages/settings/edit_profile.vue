<template>
	<view class="page">
		<view class="avatar-section">
			<view class="avatar-wrap" @click="choosePhoto">
				<image class="avatar-img" :src="photo || '/static/default_avatar.png'" mode="aspectFill"></image>
				<view class="avatar-mask">
					<u-icon name="camera-fill" color="#fff" size="28"></u-icon>
				</view>
			</view>
			<text class="avatar-tip">点击更换头像</text>
		</view>

		<view class="section-card">
			<view class="field-label">昵称</view>
			<input class="field-input" v-model="nickname" placeholder="请输入昵称" placeholder-class="ph" maxlength="20" />
		</view>

		<view class="section-card">
			<view class="field-label">性别</view>
			<view class="sex-row">
				<view class="sex-btn" :class="{ active: sex === '先生' }" @click="sex = '先生'">先生</view>
				<view class="sex-btn" :class="{ active: sex === '女士' }" @click="sex = '女士'">女士</view>
				<view class="sex-btn" :class="{ active: sex === '保密' }" @click="sex = '保密'">保密</view>
			</view>
		</view>

		<view class="save-btn" @click="saveProfile">
			<text class="save-text">保存个人信息</text>
		</view>

		<view class="section-card tel-section">
			<view class="field-label">手机号绑定</view>
			<view class="tel-row">
				<text class="current-tel">当前：{{ tel || '未绑定' }}</text>
			</view>
			<input class="field-input" v-model="newTel" placeholder="输入新手机号" placeholder-class="ph" maxlength="11" type="number" />
			<view class="code-row">
				<input class="field-input code-input" v-model="smsCode" placeholder="验证码" placeholder-class="ph" maxlength="6" type="number" />
				<view class="send-btn" :class="{ disabled: countdown > 0 }" @click="sendCode">
					<text>{{ countdown > 0 ? countdown + 's' : '获取验证码' }}</text>
				</view>
			</view>
			<view class="save-btn tel-save-btn" @click="saveTel">
				<text class="save-text">确认更换手机号</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			photo: '',
			nickname: '',
			sex: '保密',
			tel: '',
			newTel: '',
			smsCode: '',
			countdown: 0,
			timer: null
		};
	},
	onLoad() {
		let that = this;
		that.ajax(that.url.searchCustomerProfile, 'POST', {}, function(resp) {
			let profile = resp.data.result || {};
			that.photo = profile.photo || '';
			that.nickname = profile.nickname || '';
			that.sex = profile.sex || '保密';
			that.tel = profile.tel || '';
		}, false);
	},
	onUnload() {
		if (this.timer) {
			clearInterval(this.timer);
		}
	},
	methods: {
		choosePhoto() {
			let that = this;
			let chooseImage = function() {
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success(res) {
						that.uploadPhoto(that.getTempImagePath(res));
					},
					fail(error) {
						that.handleChoosePhotoFail(error);
					}
				});
			};
			if (typeof uni.chooseMedia === 'function') {
				uni.chooseMedia({
					count: 1,
					mediaType: ['image'],
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success(res) {
						that.uploadPhoto(that.getTempImagePath(res));
					},
					fail(error) {
						that.handleChoosePhotoFail(error);
					}
				});
			} else {
				chooseImage();
			}
		},
		getTempImagePath(res) {
			if (res && Array.isArray(res.tempFiles) && res.tempFiles.length > 0) {
				return res.tempFiles[0].tempFilePath || res.tempFiles[0].path || '';
			}
			if (res && Array.isArray(res.tempFilePaths) && res.tempFilePaths.length > 0) {
				return res.tempFilePaths[0];
			}
			return '';
		},
		parseUploadResponse(resp) {
			if (!resp || resp.data == null) {
				return {};
			}
			if (typeof resp.data === 'object') {
				return resp.data;
			}
			try {
				return JSON.parse(resp.data);
			} catch (error) {
				console.error('upload response parse failed', resp.data, error);
				return {};
			}
		},
		handleUploadUnauthorized() {
			uni.removeStorageSync('token');
			uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' });
			setTimeout(() => {
				uni.reLaunch({ url: '/pages/login/login' });
			}, 1500);
		},
		uploadPhoto(path) {
			let that = this;
			if (!path) {
				uni.showToast({ title: '未获取到图片', icon: 'none' });
				return;
			}
			uni.showLoading({ title: '上传中' });
			uni.uploadFile({
				url: that.url.updateCustomerPhoto,
				filePath: path,
				name: 'file',
				header: {
					token: uni.getStorageSync('token')
				},
				success(resp) {
					uni.hideLoading();
					let data = that.parseUploadResponse(resp);
					if (resp.statusCode === 401) {
						that.handleUploadUnauthorized();
					} else if (resp.statusCode === 200 && data.code === 200) {
						that.photo = data.result;
						uni.showToast({ title: '头像已更新', icon: 'success' });
					} else {
						uni.showToast({ title: data.error || data.msg || '上传失败', icon: 'none' });
					}
				},
				fail(error) {
					uni.hideLoading();
					uni.showToast({ title: error.errMsg || '上传失败', icon: 'none' });
				}
			});
		},
		handleChoosePhotoFail(error) {
			if (!error || !error.errMsg || error.errMsg.indexOf('cancel') !== -1) {
				return;
			}
			if (/auth deny|authorize no response|permission/i.test(error.errMsg)) {
				uni.showModal({
					title: '需要权限',
					content: '请在微信或系统设置中允许访问相机或系统相册后重试',
					confirmText: '去设置',
					success(res) {
						if (res.confirm) {
							uni.openSetting();
						}
					}
				});
				return;
			}
			uni.showToast({ title: error.errMsg, icon: 'none' });
		},
		saveProfile() {
			let that = this;
			if (!that.nickname || that.nickname.trim() === '') {
				return uni.showToast({ title: '昵称不能为空', icon: 'none' });
			}
			that.ajax(that.url.updateCustomerProfile, 'POST', {
				nickname: that.nickname,
				sex: that.sex
			}, function() {
				uni.showToast({ title: '保存成功', icon: 'success' });
				setTimeout(() => uni.navigateBack(), 1200);
			});
		},
		sendCode() {
			let that = this;
			if (that.countdown > 0) return;
			if (!/^1[0-9]{10}$/.test(that.newTel)) {
				return uni.showToast({ title: '手机号格式不正确', icon: 'none' });
			}
			that.ajax(that.url.sendSmsCode, 'POST', { tel: that.newTel }, function() {
				uni.showToast({ title: '验证码已发送', icon: 'success' });
				that.countdown = 60;
				that.timer = setInterval(() => {
					that.countdown--;
					if (that.countdown <= 0) {
						clearInterval(that.timer);
						that.timer = null;
					}
				}, 1000);
			});
		},
		saveTel() {
			let that = this;
			if (!/^1[0-9]{10}$/.test(that.newTel)) {
				return uni.showToast({ title: '手机号格式不正确', icon: 'none' });
			}
			if (!that.smsCode || that.smsCode.length !== 6) {
				return uni.showToast({ title: '请输入6位验证码', icon: 'none' });
			}
			that.ajax(that.url.updateCustomerTel, 'POST', {
				tel: that.newTel,
				smsCode: that.smsCode
			}, function() {
				that.tel = that.newTel;
				that.newTel = '';
				that.smsCode = '';
				uni.showToast({ title: '手机号已更新', icon: 'success' });
			});
		}
	}
};
</script>

<style lang="less">
.page { min-height: 100vh; background: #0D0F1A; padding-bottom: 60rpx; }

.avatar-section {
	display: flex; flex-direction: column; align-items: center;
	padding: 60rpx 0 40rpx;
	background: linear-gradient(135deg, #1A2980 0%, #26D0CE 100%);
}
.avatar-wrap {
	width: 160rpx; height: 160rpx; border-radius: 50%;
	position: relative;
	border: 4rpx solid rgba(255,255,255,0.4);
	overflow: hidden;
}
.avatar-img { width: 100%; height: 100%; }
.avatar-mask {
	position: absolute; bottom: 0; left: 0; right: 0;
	height: 56rpx; background: rgba(0,0,0,0.45);
	display: flex; align-items: center; justify-content: center;
}
.avatar-tip { color: rgba(255,255,255,0.75); font-size: 24rpx; margin-top: 16rpx; }

.section-card {
	margin: 30rpx 30rpx 0;
	background: #1A1D2E; border-radius: 20rpx;
	padding: 30rpx;
	border: 1rpx solid rgba(255,255,255,0.06);
}
.field-label { font-size: 24rpx; color: #4A4F6A; margin-bottom: 18rpx; letter-spacing: 2rpx; }
.field-input {
	width: 100%; background: #0D0F1A; border-radius: 12rpx;
	padding: 24rpx; font-size: 30rpx; color: #E8EAF2;
	border: 1rpx solid rgba(255,255,255,0.08);
	box-sizing: border-box;
}
.ph { color: #3A3F58; }

.sex-row { display: flex; gap: 20rpx; }
.sex-btn {
	flex: 1; text-align: center; padding: 22rpx 0;
	border-radius: 12rpx; font-size: 28rpx; color: #6B7280;
	background: #0D0F1A; border: 1rpx solid rgba(255,255,255,0.08);
}
.sex-btn.active {
	background: linear-gradient(135deg, #667eea, #764ba2);
	color: #fff; border-color: transparent; font-weight: 600;
}

.save-btn {
	margin: 24rpx 30rpx 0;
	background: linear-gradient(135deg, #1A2980, #26D0CE);
	border-radius: 16rpx; padding: 32rpx; text-align: center;
	box-shadow: 0 8rpx 24rpx rgba(26,41,128,0.4);
}
.save-text { font-size: 32rpx; color: #fff; font-weight: 600; letter-spacing: 2rpx; }

.tel-section { margin-top: 30rpx; }
.tel-row { margin-bottom: 20rpx; }
.current-tel { font-size: 26rpx; color: #6B7280; }

.code-row { display: flex; gap: 20rpx; margin-top: 20rpx; align-items: center; }
.code-input { flex: 1; }
.send-btn {
	background: linear-gradient(135deg, #26D0CE, #1A2980);
	border-radius: 12rpx; padding: 24rpx 28rpx;
	font-size: 26rpx; color: #fff; white-space: nowrap; font-weight: 500;
}
.send-btn.disabled { background: #2A2D40; color: #4A4F6A; }
.tel-save-btn {
	margin: 24rpx 0 0;
	background: linear-gradient(135deg, #11998e, #38ef7d);
	box-shadow: 0 8rpx 24rpx rgba(17,153,142,0.35);
}
</style>
