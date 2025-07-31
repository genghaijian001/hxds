<template>
	<view>
		<u-notice-bar mode="horizontal" :list="notice"></u-notice-bar>
		<view class="step"><u-steps :list="numList" mode="number" :current="0"></u-steps></view>
		<view class="title">【 相关费用 】</view>
		<u-cell-group>
			<u-cell-item icon="red-packet-fill" title="路桥费" :value="tollFee + '元'" @click="enterFee('toll')"></u-cell-item>
			<u-cell-item icon="red-packet-fill" title="停车费" :value="parkingFee + '元'" @click="enterFee('parking')"></u-cell-item>
			<u-cell-item icon="red-packet-fill" title="其他费用" :value="otherFee + '元'" @click="enterFee('other')"></u-cell-item>
		</u-cell-group>
		<button class="btn" @tap="submit">输入好了</button>
		<u-toast ref="uToast" />
		<u-top-tips ref="uTips"></u-top-tips>
	</view>
</template>

<script>
export default {
	data() {
		return {
			notice: ['请认真填写相关金额，发送账单之后则不能修改，请慎重填写费用金额，精确到小数点后两位'],
			numList: [
				{
					name: '结束代驾'
				},
				{
					name: '输入费用'
				},
				{
					name: '提交账单'
				},
				{
					name: '用户付款'
				}
			],
			orderId: null,
			customerId: null,
			tollFee: '0.00',
			parkingFee: '0.00',
			otherFee: '0.00'
		};
	},
	methods: {
		enterFee: function(type) {
			let that = this;
			if (type == 'toll') {
				uni.showModal({
					title: '路桥费',
					content: that.tollFee,
					editable: true,
					placeholderText: '输入路桥费',
					showCancel: false,
					success: function(resp) {
						if (that.checkValidFee(resp.content, '路桥费')) {
							that.tollFee = resp.content;
						}
					}
				});
			} else if (type == 'parking') {
				uni.showModal({
					title: '停车费',
					content: that.parkingFee,
					editable: true,
					placeholderText: '输入停车费',
					showCancel: false,
					success: function(resp) {
						if (that.checkValidFee(resp.content, '停车费')) {
							that.parkingFee = resp.content;
						}
					}
				});
			} else if (type == 'other') {
				uni.showModal({
					title: '其他费用',
					content: that.otherFee,
					editable: true,
					placeholderText: '输入其他费用',
					showCancel: false,
					success: function(resp) {
						if (that.checkValidFee(resp.content, '其他费用')) {
							that.otherFee = resp.content;
						}
					}
				});
			}
		},
		submit: function() {
			let that = this;
			if (that.checkValidFee(that.tollFee, '路桥费') 
				&& that.checkValidFee(that.parkingFee, '停车费') 
				&& that.checkValidFee(that.otherFee, '其他费用')) {
				uni.showModal({
					title: '提示消息',
					content: '您确认要提交以上相关费用？',
					success: function(resp) {
						if (resp.confirm) {
							let data = {
								tollFee: that.tollFee,
								parkingFee: that.parkingFee,
								otherFee: that.otherFee,
								orderId: that.orderId
							};
							that.ajax(that.url.updateBillFee, 'POST', data, function(resp) {
								uni.navigateTo({
									url: `/order/order_bill/order_bill?orderId=${that.orderId}&customerId=${that.customerId}`
								});
							});
						}
					}
				});
			}
		}
	},
	onLoad: function(options) {
		this.orderId = options.orderId;
		this.customerId = options.customerId;
	},
	onShow: function() {},
	onHide: function() {}
};
</script>

<style lang="less">
@import url('enter_fee.less');
</style>
