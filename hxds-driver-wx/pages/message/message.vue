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
			sendTime: '',
			senderPhoto: '',
			senderName: '',
			msg: ''
		};
	},
	onLoad: function(options) {
		let that = this;
		that.id = options.id;
		let data = {
			id: options.id,
			readFlag: options.readFlag
		};
		that.ajax(that.url.searchMessageById, 'POST', data, function(resp) {
			let result = resp.data.result;
			that.sendTime = result.sendTime;
			that.senderPhoto = result.senderPhoto;
			that.senderName = result.senderName;
			that.msg = result.msg;
		});
	},
	methods: {
		deleteMsg: function() {
			let that = this;
			uni.showModal({
				title: '提示',
				content: '确定要删除此消息吗？',
				success: function(res) {
					if (res.confirm) {
						that.ajax(that.url.deleteMessageRefById, 'POST', { id: that.id }, function(resp) {
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
@import url('message.less');
</style>
