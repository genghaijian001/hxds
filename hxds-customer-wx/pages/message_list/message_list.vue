<template>
	<view>
		<view class="message-container" v-for="one in list" :key="one.id" v-if="list.length > 0" @tap="viewMessageHandle(one.messageId, one.readFlag, one.id)">
			<view class="top">
				<view class="title">
					<image src="../../static/message_list/email-icon-1.png" mode="widthFix" v-if="!one.readFlag"></image>
					<image src="../../static/message_list/email-icon-2.png" mode="widthFix" v-if="one.readFlag"></image>
					<text>{{ one.senderName }}</text>
				</view>
				<view class="date">{{ one.sendTime }}</view>
			</view>
			<view class="content">{{ one.msg }}</view>
			<view class="bottom">
				<text>查看详情</text>
				<u-icon name="arrow-right" color="#aaa" size="28"></u-icon>
			</view>
		</view>
		<view class="empty" v-if="list.length == 0"><u-empty text="消息列表为空" mode="list"></u-empty></view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			page: 1,
			length: 50,
			list: [],
			isLastPage: false
		};
	},
	methods: {
		loadPageData(ref) {
			let data = {
				page: ref.page,
				length: ref.length
			};
			ref.ajax(ref.url.searchMessageByPage, 'POST', data, function(resp) {
				let result = resp.data.result;
				if (result == null || result.length == 0) {
					ref.isLastPage = true;
					return;
				}
				if (ref.page == 1) {
					ref.list = [];
				}
				ref.list = ref.list.concat(result);
			});
		},
		viewMessageHandle(messageId, readFlag, refId) {
			uni.navigateTo({
				url: `../message/message?id=${messageId}&readFlag=${readFlag}&refId=${refId}`
			});
		}
	},
	onShow() {
		this.page = 1;
		this.isLastPage = false;
		this.loadPageData(this);
	},
	onPullDownRefresh() {
		this.page = 1;
		this.isLastPage = false;
		this.loadPageData(this);
		uni.stopPullDownRefresh();
	},
	onReachBottom() {
		if (this.isLastPage) {
			return;
		}
		this.page = this.page + 1;
		this.loadPageData(this);
	}
};
</script>

<style lang="less">
@import url('message_list.less');
</style>
