<template>
	<view class="sms-page">
		<view class="card">
			<view class="title">手机号验证码登录 / 注册</view>
			<view class="desc">输入手机号和验证码，确认后即可完成登录或注册</view>
			<input class="field-input" v-model="tel" maxlength="11" type="number" placeholder="请输入手机号" />
			<view class="code-row">
				<input class="field-input code-input" v-model="smsCode" maxlength="6" type="number" placeholder="请输入验证码" />
				<button class="code-btn" :disabled="countdown > 0" @tap="sendCode">
					{{ countdown > 0 ? `${countdown}s后重试` : '获取验证码' }}
				</button>
			</view>
			<button class="submit-btn" @tap="smsLoginOrRegister">确定</button>
		</view>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
export default {
	data() {
		return {
			tel: '',
			smsCode: '',
			countdown: 0,
			timer: null
		};
	},
	methods: {
		sendCode() {
			let that = this;
			if (!/^1\d{10}$/.test(that.tel)) {
				that.$refs.uToast.show({ title: '请输入正确手机号', type: 'warning' });
				return;
			}
			that.ajax(that.url.sendSmsCode, 'POST', { tel: that.tel }, function() {
				that.$refs.uToast.show({ title: '验证码已发送', type: 'success' });
				that.startCountdown();
			});
		},
		startCountdown() {
			let that = this;
			clearInterval(that.timer);
			that.countdown = 60;
			that.timer = setInterval(function() {
				if (that.countdown <= 1) {
					clearInterval(that.timer);
					that.timer = null;
					that.countdown = 0;
					return;
				}
				that.countdown -= 1;
			}, 1000);
		},
		smsLoginOrRegister() {
			let that = this;
			if (!/^1\d{10}$/.test(that.tel)) {
				that.$refs.uToast.show({ title: '请输入正确手机号', type: 'warning' });
				return;
			}
			if (!/^\d{6}$/.test(that.smsCode)) {
				that.$refs.uToast.show({ title: '请输入6位验证码', type: 'warning' });
				return;
			}
			uni.login({
				provider: 'weixin',
				success(resp) {
					that.ajax(that.url.login, 'POST', {
						code: resp.code,
						tel: that.tel,
						smsCode: that.smsCode
					}, function(loginResp) {
						if (!loginResp.data.hasOwnProperty('token')) {
							that.$refs.uToast.show({ title: '登录失败，请稍后重试', type: 'error' });
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
		}
	},
	onUnload() {
		clearInterval(this.timer);
		this.timer = null;
	}
};
</script>

<style lang="less">
.sms-page {
	min-height: 100vh;
	background: #f4f7fb;
	padding: 60rpx 30rpx;
	box-sizing: border-box;
}

.card {
	background: #fff;
	border-radius: 24rpx;
	padding: 36rpx 30rpx 40rpx;
	box-shadow: 0 18rpx 40rpx rgba(32, 116, 253, 0.08);
}

.title {
	font-size: 34rpx;
	font-weight: 600;
	color: #1f2937;
}

.desc {
	margin-top: 14rpx;
	font-size: 25rpx;
	line-height: 1.6;
	color: #64748b;
}

.field-input {
	width: 100%;
	box-sizing: border-box;
	padding: 24rpx;
	margin-top: 24rpx;
	border-radius: 18rpx;
	background: #f4f7fb;
	font-size: 28rpx;
	color: #111827;
}

.code-row {
	display: flex;
	align-items: center;
	gap: 16rpx;
	margin-top: 18rpx;
}

.code-input {
	flex: 1;
	margin-top: 0;
}

.code-btn {
	width: 220rpx;
	height: 88rpx;
	line-height: 88rpx;
	border-radius: 18rpx;
	background: #eef4ff;
	color: #2074FD;
	font-size: 26rpx;
}

.code-btn[disabled] {
	color: #94a3b8;
	background: #f1f5f9;
}

.submit-btn {
	width: 100%;
	height: 92rpx;
	line-height: 92rpx;
	margin-top: 28rpx;
	border-radius: 18rpx;
	background: #2074FD;
	color: #fff;
	font-size: 30rpx;
}

.submit-btn::after {
	border: none;
}
</style>
