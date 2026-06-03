<template>
	<view class="content">
		<button @tap="click">付款</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		onLoad() {
			
		},
		methods: {
			click:function(){
				uni.requestPayment({
					timeStamp: "",
					nonceStr: "",
					package: "",
					signType: 'MD5',
					paySign: "",
					success: function() {
						uni.showToast({
							title: '支付成功'
						});
					
					},
					fail: function() {
						uni.showToast({
							icon: 'none',
							title: '支付失败'
						});
					}
				});
			}
		}
	}
</script>

<style>

</style>
