<template>
	<view class="page">
		<view class="card">
			<view class="total">
				<view>总资产（CNY）</view>
				<view class="recharge" @tap="toPage('../recharge/recharge')">
					<image src="../static/wallet/recharge.png" mode="widthFix" class="icon"></image>
					<text>充值</text>
				</view>
			</view>
			<view class="balance">
				￥
				<text>{{ balance }}</text>
			</view>
			<view class="item-list">
				<view class="item">
					<view class="title">
						<image src="../static/wallet/icon-1.png" mode="widthFix" class="icon" />
						本月收入
					</view>
					<view class="amount">￥{{ incomeTotalInMonth }}</view>
				</view>
				<view class="item">
					<view class="title">
						<image src="../static/wallet/icon-2.png" mode="widthFix" class="icon" />
						本月支出
					</view>
					<view class="amount">￥{{ paymentTotalInMonth }}</view>
				</view>
				<view class="item">
					<view class="title">
						<image src="../static/wallet/icon-3.png" mode="widthFix" class="icon" />
						今日入账
					</view>
					<view class="amount">￥{{ incomeTotalInDay }}</view>
				</view>
			</view>
		</view>
		<view class="record-container">
			<view class="title">收支明细</view>
			<view class="summary">
				<view class="month" @tap="showListHandle()">
					<text>{{ monthLabel }}</text>
					<image src="../static/wallet/icon-4.png" mode="widthFix" class="icon" />
				</view>
				<text class="total">合计：{{ incomeTotalInMonth - paymentTotalInMonth }}元</text>
			</view>
			<view class="record-list">
				<block v-if="recordInMonth.length > 0">
					<view class="record" v-for="one in recordInMonth">
						<view class="left">
							<view class="date-time">
								<view class="date">
									<image src="../static/wallet/icon-5.png" mode="widthFix" class="date-icon" />
									<text>{{ one.date }}</text>
								</view>
								<view class="time">
									<image src="../static/wallet/icon-6.png" mode="widthFix" class="time-icon" />
									<text>{{ one.time }}</text>
								</view>
							</view>
							<view class="content">
								<view class="type">钱包{{ one.name }}</view>
								<view class="desc">{{ one.remark }}</view>
							</view>
						</view>
						<view class="right">
							<text :class="one.name == '入账' ? 'red' : 'green'">{{ one.amount }}</text>
							<text class="unit">元</text>
						</view>
					</view>
				</block>
				<block v-if="recordInMonth.length == 0">
					<view class="empty">
						<image src="../static/wallet/no-data.png" mode="widthFix" class="no-data"></image>
						<view>没有相关数据</view>
					</view>
				</block>
			</view>
			<u-select v-model="showList" :list="list" @confirm="confirmMonthHandle"></u-select>
		</view>
		<u-top-tips ref="uTips"></u-top-tips>
	</view>
</template>

<script>
let dayjs = require('dayjs');
export default {
	data() {
		return {
			balance: '0.00',
			incomeTotalInMonth: '0.00',
			paymentTotalInMonth: '0.00',
			incomeTotalInDay: '0.00',
			monthLabel: dayjs().format('YYYY年MM月'),
			currentMonth: dayjs().format('YYYY-MM'),
			recordInMonth: [],
			showList: false,
			list: []
		};
	},
	methods: {
		loadWalletData: function(month) {
			let that = this;
			that.ajax(that.url.searchDriverWallet, 'POST', { month: month }, function(resp) {
				let result = resp.data.result;
				that.balance = result.balance || '0.00';
				that.incomeTotalInMonth = result.incomeTotalInMonth || '0.00';
				that.paymentTotalInMonth = result.paymentTotalInMonth || '0.00';
				that.incomeTotalInDay = result.incomeTotalInDay || '0.00';
				that.recordInMonth = result.recordInMonth || [];
			});
		},
		showListHandle: function() {
			// Build last 12 months list for picker
			let list = [];
			for (let i = 0; i < 12; i++) {
				let m = dayjs().subtract(i, 'month');
				list.push({ label: m.format('YYYY年MM月'), value: m.format('YYYY-MM') });
			}
			this.list = list;
			this.showList = true;
		},
		confirmMonthHandle: function(e) {
			let that = this;
			let value = e[0].value;
			that.currentMonth = value;
			that.monthLabel = e[0].label;
			that.recordInMonth = [];
			that.loadWalletData(value);
		}
	},
	onShow: function() {
		let that = this;
		that.currentMonth = dayjs().format('YYYY-MM');
		that.monthLabel = dayjs().format('YYYY年MM月');
		that.loadWalletData(that.currentMonth);
	},
	onHide: function() {

	}
};
</script>

<style lang="less">
@import url('wallet.less');
</style>
