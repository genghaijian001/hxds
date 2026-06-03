<template>
	<view class="page">
		<image src="../../static/login/top.png" mode="widthFix" class="top"></image>
		<button class="btn" @tap="login">微信授权登录</button>
		<view class="small-entry" @tap="toSmsLoginPage">
			<text class="small-entry-text">手机号验证码登录 / 注册</text>
		</view>

		<view class="register-container">
			没有账号？
			<text class="link" @tap="toRegisterPage">手动注册</text>
		</view>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
export default {
	methods: {
		login() {
			let that = this;
			uni.login({
				provider: 'weixin',
				success(resp) {
					that.ajax(that.url.login, 'POST', { code: resp.code }, function(loginResp) {
						if (!loginResp.data.hasOwnProperty('token')) {
							that.$refs.uToast.show({ title: '请先注册', type: 'error' });
							return;
						}
						uni.setStorageSync('token', loginResp.data.token);
						that.$refs.uToast.show({
							title: '登录成功',
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
		},
		toSmsLoginPage() {
			uni.navigateTo({ url: './sms_login' });
		},
		toRegisterPage() {
			uni.navigateTo({ url: '../register/register' });
		}
	}
};
</script>

<style lang="less">
@import url('login.less');

.small-entry {
	width: 46%;
	margin: 24rpx auto 0;
	padding: 18rpx 20rpx;
	border-radius: 999rpx;
	background: #eef4ff;
	border: 2rpx solid #d8e6ff;
	text-align: center;
}

.small-entry-text {
	font-size: 24rpx;
	color: #2074FD;
}
</style>
