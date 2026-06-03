<template>
	<view>
		<view class="message">
			<view class="header">
				<view class="desc">{{ sendTime }}</view>
				<view class="opt" @tap="deleteMsg()">删除</view>
			</view>
			<view class="content">
				<view class="sender">
					<image :src="senderPhoto" mode="widthFix" class="photo" />
					<text>{{ senderName }}</text>
				</view>
				<view class="msg">{{ msg }}</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			id: null,
			refId: null,
			readFlag: true,
			sendTime: '',
			senderPhoto: '',
			senderName: '',
			msg: ''
		};
	},
	onLoad(options) {
		let that = this;
		that.id = options.id;
		that.refId = options.refId;
		that.readFlag = options.readFlag === 'true' || options.readFlag === true;
		that.ajax(that.url.searchMessageById, 'POST', { id: that.id }, function(resp) {
			let result = resp.data.result;
			that.sendTime = result.sendTime;
			that.senderPhoto = result.senderPhoto;
			that.senderName = result.senderName;
			that.msg = result.msg;
			if (!that.readFlag && that.refId) {
				that.ajax(that.url.updateUnreadMessage, 'POST', { id: that.refId }, function() {
					that.readFlag = true;
				}, false);
			}
		});
	},
	methods: {
		deleteMsg() {
			let that = this;
			uni.showModal({
				title: '提示',
				content: '确定要删除此消息吗？',
				success(res) {
					if (res.confirm) {
						that.ajax(that.url.deleteMessageRefById, 'POST', { id: that.refId }, function() {
							uni.showToast({ title: '删除成功' });
							setTimeout(function() {
								uni.navigateBack();
							}, 1500);
						});
					}
				}
			});
		}
	}
};
</script>

<style lang="less">
.message {
	padding: 20rpx;
	.header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 0;
		.desc { color: #999; font-size: 26rpx; }
		.opt { color: #e84c4c; font-size: 28rpx; }
	}
	.content {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		.sender {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;
			.photo { width: 80rpx; height: 80rpx; border-radius: 50%; margin-right: 20rpx; }
			text { font-weight: bold; font-size: 30rpx; }
		}
		.msg { font-size: 28rpx; color: #333; line-height: 1.6; }
	}
}
</style>
