<template>
	<view v-if="showPrivacy" class="privacy-mask">
		<view class="privacy-dialog">
			<view class="privacy-title">隐私保护提示</view>
			<view class="privacy-content">
				<text>在使用当前小程序服务之前，请仔细阅读</text>
				<text class="privacy-link" @click="openPrivacyContract">《隐私保护指引》</text>
				<text>。如你同意，请点击"同意"开始使用。</text>
			</view>
			<view class="privacy-btns">
				<button class="privacy-btn privacy-btn-reject" @click="rejectPrivacy">拒绝</button>
				<button class="privacy-btn privacy-btn-agree" id="agree-btn" open-type="agreePrivacyAuthorization"
					@agreeprivacyauthorization="agreePrivacy">同意</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				showPrivacy: false,
				resolvePrivacyAuthorization: null
			}
		},
		mounted() {
			// #ifdef MP-WEIXIN
			if (wx.onNeedPrivacyAuthorization) {
				wx.onNeedPrivacyAuthorization((resolve) => {
					this.resolvePrivacyAuthorization = resolve
					this.showPrivacy = true
				})
			}
			// #endif
		},
		methods: {
			openPrivacyContract() {
				// #ifdef MP-WEIXIN
				if (wx.openPrivacyContract) {
					wx.openPrivacyContract({
						fail: () => {
							uni.showToast({ title: '打开隐私协议失败', icon: 'none' })
						}
					})
				}
				// #endif
			},
			agreePrivacy() {
				this.showPrivacy = false
				if (this.resolvePrivacyAuthorization) {
					this.resolvePrivacyAuthorization({ buttonId: 'agree-btn', event: 'agreeprivacyauthorization' })
					this.resolvePrivacyAuthorization = null
				}
			},
			rejectPrivacy() {
				this.showPrivacy = false
				if (this.resolvePrivacyAuthorization) {
					this.resolvePrivacyAuthorization({ event: 'rejectprivacyauthorization' })
					this.resolvePrivacyAuthorization = null
				}
			}
		}
	}
</script>

<style scoped>
	.privacy-mask {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		z-index: 99999;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.privacy-dialog {
		width: 560rpx;
		background: #fff;
		border-radius: 16rpx;
		padding: 40rpx;
	}

	.privacy-title {
		text-align: center;
		font-size: 32rpx;
		font-weight: bold;
		margin-bottom: 24rpx;
	}

	.privacy-content {
		font-size: 28rpx;
		color: #666;
		line-height: 1.6;
		margin-bottom: 40rpx;
	}

	.privacy-link {
		color: #576b95;
	}

	.privacy-btns {
		display: flex;
		justify-content: space-between;
	}

	.privacy-btn {
		flex: 1;
		margin: 0 10rpx;
		border-radius: 8rpx;
		font-size: 28rpx;
		line-height: 80rpx;
		height: 80rpx;
		text-align: center;
	}

	.privacy-btn-reject {
		background: #f5f5f5;
		color: #666;
	}

	.privacy-btn-agree {
		background: #07c160;
		color: #fff;
	}
</style>
