<template>
	<view class="page">
		<view class="brand-header">
			<view class="brand-logo-wrap">
				<text class="brand-logo">代驾</text>
			</view>
			<text class="brand-title">欢迎加入</text>
			<text class="brand-sub">注册后即可开始使用乘客端下单服务</text>
		</view>

		<view class="workflow-card">
			<view class="wf-step">
				<view class="wf-circle active">1</view>
				<text class="wf-label">注册账号</text>
			</view>
			<view class="wf-line"></view>
			<view class="wf-step">
				<view class="wf-circle">2</view>
				<text class="wf-label">完善资料</text>
			</view>
			<view class="wf-line"></view>
			<view class="wf-step">
				<view class="wf-circle">3</view>
				<text class="wf-label">开始下单</text>
			</view>
		</view>

		<view class="section-card">
			<view class="card-title-bar"><text class="card-title">微信注册</text></view>
			<text class="auth-hint">头像为可选项，不上传也可以完成注册，后续可在个人设置中修改。</text>
			<button class="avatar-btn" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
				<view class="avatar-circle">
					<image v-if="photo" class="avatar-img" :src="photo" mode="aspectFill"></image>
					<text v-else class="avatar-placeholder">头像</text>
				</view>
				<text class="avatar-tip">{{ photo ? '点击更换头像' : '点击选择头像（可选）' }}</text>
			</button>
			<input class="form-input nickname-input" type="nickname" @input="e0" placeholder="请输入昵称" :value="nickname" />
			<button class="btn" @tap="manualRegister">完成注册</button>
		</view>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
export default {
	data() {
		return {
			nickname: '',
			photo: ''
		};
	},
	methods: {
		onChooseAvatar(e) {
			this.photo = e.detail.avatarUrl;
		},
		e0(e) {
			this.nickname = e.detail.value;
		},
		normalizeRegisterPhoto() {
			if (!this.photo) {
				return '';
			}
			return /^https?:\/\//.test(this.photo) ? this.photo : '';
		},
		manualRegister() {
			let that = this;
			if (!that.nickname) {
				that.$refs.uToast.show({ title: '请输入昵称', type: 'warning' });
				return;
			}
			uni.login({
				provider: 'weixin',
				success(resp) {
					let data = {
						code: resp.code,
						nickname: that.nickname,
						photo: that.normalizeRegisterPhoto(),
						sex: '未知',
						tel: ''
					};
					that.ajax(that.url.registerNewCustomer, 'POST', data, function(registerResp) {
						uni.setStorageSync('token', registerResp.data.token);
						that.$refs.uToast.show({
							title: '注册成功',
							type: 'success',
							callback() {
								uni.switchTab({ url: '../workbench/workbench' });
							}
						});
					});
				},
				fail() {
					that.$refs.uToast.show({ title: '微信授权失败，请重试', type: 'error' });
				}
			});
		}
	}
};
</script>

<style>
.page { background: #f0f4ff; min-height: 100vh; padding-bottom: 80rpx; }
.brand-header { background: linear-gradient(135deg, #2074FD, #1560d4); padding: 60rpx 40rpx 50rpx; text-align: center; }
.brand-logo-wrap { width: 100rpx; height: 100rpx; border-radius: 28rpx; background: rgba(255,255,255,0.2); margin: 0 auto 20rpx; }
.brand-logo { font-size: 36rpx; font-weight: bold; color: #fff; line-height: 100rpx; text-align: center; display: block; }
.brand-title { display: block; font-size: 44rpx; font-weight: bold; color: #fff; letter-spacing: 2rpx; }
.brand-sub { display: block; font-size: 26rpx; color: rgba(255,255,255,0.8); margin-top: 12rpx; }
.workflow-card { background: #fff; margin: 24rpx; border-radius: 20rpx; padding: 30rpx 20rpx; box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.06); display: flex; align-items: flex-start; justify-content: center; }
.wf-step { display: flex; flex-direction: column; align-items: center; flex-shrink: 0; }
.wf-circle { width: 60rpx; height: 60rpx; border-radius: 50%; border: 2rpx solid #ddd; font-size: 26rpx; color: #bbb; line-height: 60rpx; text-align: center; }
.wf-circle.active { background: #2074FD; border-color: #2074FD; color: #fff; font-weight: bold; }
.wf-label { font-size: 22rpx; color: #999; margin-top: 10rpx; white-space: nowrap; }
.wf-line { flex: 1; height: 2rpx; background: #eee; margin: 30rpx 10rpx 0; }
.section-card { background: #fff; border-radius: 20rpx; margin: 0 24rpx 24rpx; padding: 30rpx; box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.06); }
.card-title-bar { border-left: 6rpx solid #2074FD; padding-left: 16rpx; margin-bottom: 16rpx; }
.card-title { font-size: 32rpx; font-weight: bold; color: #222; }
.auth-hint { display: block; font-size: 26rpx; color: #999; text-align: center; margin-bottom: 30rpx; }
.form-input { border: 1rpx solid #eee; border-radius: 12rpx; padding: 22rpx; font-size: 30rpx; background: #fafafa; width: 100%; box-sizing: border-box; }
.btn { width: 100%; margin-top: 24rpx; border-radius: 50rpx; background: #2074FD; color: #fff; font-size: 32rpx; height: 90rpx; line-height: 90rpx; }
.btn::after { border: none; }
.avatar-btn { background: transparent; border: none; padding: 0; margin: 0 auto 20rpx; display: flex; flex-direction: column; align-items: center; width: 100%; }
.avatar-btn::after { border: none; }
.avatar-circle { width: 120rpx; height: 120rpx; border-radius: 50%; border: 2rpx dashed #ccc; background: #f5f5f5; display: flex; align-items: center; justify-content: center; overflow: hidden; }
.avatar-img { width: 120rpx; height: 120rpx; border-radius: 50%; }
.avatar-placeholder { font-size: 28rpx; color: #94a3b8; }
.avatar-tip { font-size: 24rpx; color: #999; margin-top: 12rpx; }
.nickname-input { width: 100%; margin-bottom: 16rpx; }
</style>
