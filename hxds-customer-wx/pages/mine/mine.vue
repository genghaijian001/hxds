<template>
	<view>
		<view class="summary-container">
			<u-avatar :src="photo" size="large"></u-avatar>
			<view class="summary">
				<view class="name">{{ name }}</view>
				<view class="tel">{{ tel }}</view>
				<view class="level">
					<u-icon name="integral-fill" color="#FFA600" size="35" class="icon" />
					<text>{{level}}</text>
				</view>
			</view>
		</view>
		<u-cell-group>
			<u-cell-item icon="file-text-fill" title="订单" @click="toPage('../order_list/order_list')" />
			<u-cell-item icon="info-circle-fill" title="罚款" :value="fine" />
			<u-cell-item
				icon="coupon-fill"
				title="代金券"
				:value="voucher"
				@click="toPage('../voucher_list/voucher_list')"
			/>
			<u-cell-item icon="server-fill" title="在线客服" />
			<u-cell-item icon="trash-fill" title="清理缓存" @click="clearHandle" />
			<u-cell-item
				icon="file-text-fill"
				title="用户指南"
				@click="toPage('../user_guide/user_guide')"
			/>
			<u-cell-item icon="setting-fill" title="设置" @click="toPage('../settings/settings')" />
		</u-cell-group>
	</view>
</template>

<script>
export default {
	data() {
		return {
			photo: '',
			name: '',
			tel: '',
			level: '',
			fine: '0.00元',
			voucher: '0张'
		};
	},
	methods: {
		clearHandle: function() {
			wx.clearStorageSync();
			wx.showToast({ title: '缓存已清理', icon: 'success' });
		}
	},
	onLoad: function() {
		let that=this
		that.ajax(that.url.searchCustomerProfile,'POST',{},function(resp){
			let result=resp.data.result
			if(!result) return
			that.name=result.nickname||''
			that.photo=result.photo||''
			that.tel=result.tel||''
		},false)
		that.ajax(that.url.searchUnUseVoucherCount,'POST',{},function(resp){
			let result=resp.data.result
			that.voucher=result+'张'
		},false)
	},
	onShow: function() {
		let that=this
		that.ajax(that.url.searchCustomerProfile,'POST',{},function(resp){
			let result=resp.data.result
			if(!result) return
			that.name=result.nickname||''
			that.photo=result.photo||''
			that.tel=result.tel||''
		},false)
	}
};
</script>

<style lang="less">
@import url('mine.less');
</style>
