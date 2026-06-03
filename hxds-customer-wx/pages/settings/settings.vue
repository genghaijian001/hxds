<template>
	<view class="page">
		<view class="hero-header">
			<view class="hero-title">账户设置</view>
			<view class="hero-sub">管理您的个人信息与偏好</view>
		</view>

		<view class="card">
			<view class="card-title">账户</view>
			<view class="menu-item" @click="toPage('../settings/edit_profile')">
				<view class="item-left">
					<view class="item-icon bg-blue">
						<u-icon name="account-fill" color="#fff" size="22"></u-icon>
					</view>
					<text class="item-label">个人信息</text>
				</view>
				<u-icon name="arrow-right" color="#8A8FA3" size="18"></u-icon>
			</view>
		</view>

		<view class="card">
			<view class="card-title">其他</view>
			<view class="menu-item" @click="clearHandle">
				<view class="item-left">
					<view class="item-icon bg-orange">
						<u-icon name="trash-fill" color="#fff" size="22"></u-icon>
					</view>
					<text class="item-label">清理缓存</text>
				</view>
				<u-icon name="arrow-right" color="#8A8FA3" size="18"></u-icon>
			</view>
		</view>

		<view class="logout-area">
			<view class="logout-btn" @click="logoutHandle">
				<text class="logout-text">退出登录</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	methods: {
		clearHandle() {
			uni.clearStorageSync();
			uni.showToast({ title: '缓存已清理', icon: 'success' });
		},
		logoutHandle() {
			uni.showModal({
				title: '提示',
				content: '确定要退出登录吗？',
				confirmColor: '#FF4D4F',
				success: res => {
					if (res.confirm) {
						uni.removeStorageSync('token');
						uni.reLaunch({ url: '/pages/login/login' });
					}
				}
			});
		}
	}
};
</script>

<style lang="less">
.page { min-height: 100vh; background: #0D0F1A; }

.hero-header {
	background: linear-gradient(135deg, #1A2980 0%, #26D0CE 100%);
	padding: 60rpx 40rpx 50rpx;
	position: relative;
	overflow: hidden;
	&::after {
		content: '';
		position: absolute;
		top: -40rpx; right: -40rpx;
		width: 200rpx; height: 200rpx;
		border-radius: 50%;
		background: rgba(255,255,255,0.08);
	}
}
.hero-title { font-size: 48rpx; font-weight: 700; color: #fff; letter-spacing: 2rpx; }
.hero-sub { font-size: 26rpx; color: rgba(255,255,255,0.7); margin-top: 10rpx; }

.card {
	margin: 30rpx 30rpx 0;
	background: #1A1D2E;
	border-radius: 20rpx;
	padding: 10rpx 0;
	border: 1rpx solid rgba(255,255,255,0.06);
}
.card-title { font-size: 24rpx; color: #4A4F6A; padding: 20rpx 30rpx 10rpx; letter-spacing: 2rpx; }
.menu-item { display: flex; align-items: center; justify-content: space-between; padding: 28rpx 30rpx; }
.item-left { display: flex; align-items: center; gap: 24rpx; }
.item-icon { width: 72rpx; height: 72rpx; border-radius: 18rpx; display: flex; align-items: center; justify-content: center; }
.bg-blue { background: linear-gradient(135deg, #667eea, #764ba2); }
.bg-orange { background: linear-gradient(135deg, #f6931e, #f4520b); }
.item-label { font-size: 30rpx; color: #E8EAF2; font-weight: 500; }

.logout-area { margin: 60rpx 30rpx; }
.logout-btn {
	background: linear-gradient(135deg, #FF416C, #FF4B2B);
	border-radius: 16rpx;
	padding: 32rpx;
	text-align: center;
	box-shadow: 0 8rpx 32rpx rgba(255,65,108,0.35);
}
.logout-text { font-size: 32rpx; color: #fff; font-weight: 600; letter-spacing: 4rpx; }
</style>
