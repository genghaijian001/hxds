<template>
	<div v-if="isAuth(['ROOT', 'ORDER:SELECT'])">
		<el-form :inline="true" :model="dataForm" :rules="dataRule" ref="dataForm">
			<el-form-item prop="orderId">
				<el-input v-model="dataForm.orderId" placeholder="订单编号" size="medium" clearable="clearable" />
			</el-form-item>
			<el-form-item prop="driverId">
				<el-input v-model="dataForm.driverId" placeholder="司机编号" size="medium" clearable="clearable" />
			</el-form-item>
			<el-form-item prop="customerId">
				<el-input v-model="dataForm.customerId" placeholder="客户编号" size="medium" clearable="clearable" />
			</el-form-item>
			<el-form-item>
				<el-date-picker
					v-model="dataForm.date"
					type="daterange"
					range-separator="~"
					start-placeholder="开始日期"
					end-placeholder="结束日期"
					size="medium"
				></el-date-picker>
			</el-form-item>
			<el-form-item>
				<el-select
					v-model="dataForm.status"
					class="input"
					placeholder="状态"
					size="medium"
					clearable="clearable"
				>
					<el-option label="等待接单" value="1" />
					<el-option label="已接单" value="2" />
					<el-option label="司机已到达" value="3" />
					<el-option label="开始代驾" value="4" />
					<el-option label="结束代驾" value="5" />
					<el-option label="未付款" value="6" />
					<el-option label="已付款" value="7" />
					<el-option label="订单已结束" value="8" />
					<el-option label="顾客撤单" value="9" />
					<el-option label="司机撤单" value="10" />
					<el-option label="事故关闭" value="11" />
					<el-option label="其他" value="12" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button size="medium" type="primary" @click="searchHandle()">查询</el-button>
			</el-form-item>
		</el-form>
		<el-table
			:data="dataList"
			border
			v-loading="dataListLoading"
			cell-style="padding: 4px 0"
			style="width: 100%;"
			size="medium"
			@expand-change="expand"
			:row-key="getRowKeys"
			:expand-row-keys="expands"
		>
			<el-table-column prop="panel" header-align="center" align="center" type="expand">
				<template #default="scope">
					<div class="content-container">
						<div class="left">
							<div class="order-table">
								<div class="row">
									<div class="th">客户编号</div>
									<div class="td">{{ panel.customer.id }}</div>
									<div class="th">客户性别</div>
									<div class="td">{{ panel.customer.sex }}</div>
									<div class="th">客户电话</div>
									<div class="td">{{ panel.customer.tel }}</div>
								</div>
								<div class="row">
									<div class="th">车型</div>
									<div class="td">{{ panel.order.carType }}</div>
									<div class="th">车牌</div>
									<div class="td">{{ panel.order.carPlate }}</div>
									<div class="th">车辆隶属</div>
									<div class="td">{{ panel.order.city }}</div>
								</div>
								<div class="row">
									<div class="th">司机编号</div>
									<div class="td">{{ panel.driver.id }}</div>
									<div class="th">司机姓名</div>
									<div class="td">{{ panel.driver.name }}</div>
									<div class="th">司机电话</div>
									<div class="td">{{ panel.driver.tel }}</div>
								</div>
								<div class="row">
									<div class="th">接单时间</div>
									<div class="td">{{ panel.order.acceptTime }}</div>
									<div class="th">到达时间</div>
									<div class="td">{{ panel.order.arriveTime }}</div>
									<div class="th">代驾等时</div>
									<div class="td">{{ panel.order.waitingMinute }}</div>
								</div>
								<div class="row">
									<div class="th">开始时间</div>
									<div class="td">{{ panel.order.startTime }}</div>
									<div class="th">结束时间</div>
									<div class="td">{{ panel.order.endTime }}</div>
									<div class="th">代驾时长</div>
									<div class="td">{{ panel.order.driveMinute }}</div>
								</div>
								<div class="row">
									<div class="th">代驾里程</div>
									<div class="td">{{ panel.order.realMileage }}</div>
									<div class="th">订单费用</div>
									<div class="td">{{ panel.order.realFee }}</div>
									<div class="th">状态</div>
									<div class="td">{{ panel.order.status }}</div>
								</div>
								<div class="row">
									<div class="th">费用规则</div>
									<div class="td">{{ panel.order.chargeRule }}</div>
									<div class="th">取消规则</div>
									<div class="td">{{ panel.order.cancelRule }}</div>
									<div class="th">分账规则</div>
									<div class="td">{{ panel.order.profitsharingRule }}</div>
								</div>
							</div>
						</div>
						<div class="right"><div :id="panel.id" class="map"></div></div>
					</div>
				</template>
			</el-table-column>
			<el-table-column type="index" header-align="center" align="center" width="100" label="序号">
				<template #default="scope">
					<span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="id" header-align="center" align="center" min-width="200" label="订单编号" />
			<el-table-column
				prop="startPlace"
				header-align="center"
				align="center"
				min-width="220"
				show-overflow-tooltip="true"
				label="代驾起点"
			/>
			<el-table-column
				prop="endPlace"
				header-align="center"
				align="center"
				min-width="220"
				show-overflow-tooltip="true"
				label="代驾终点"
			/>
			<el-table-column prop="realMileage" header-align="center" align="center" min-width="130" label="实际里程" />
			<el-table-column prop="realFee" header-align="center" align="center" min-width="130" label="实际金额" />
			<el-table-column prop="createTime" header-align="center" align="center" min-width="150" label="日期时间" />
			<el-table-column prop="status" header-align="center" align="center" min-width="100" label="状态" />
			<el-table-column header-align="center" align="center" width="150" label="操作">
				<template #default="scope">
					<el-button
						type="text"
						size="medium"
						v-if="isAuth(['ROOT', 'ORDER:SELECT'])"
						:disabled="!['结束代驾', '未付款', '已付款', '订单已结束'].includes(scope.row.status)"
						@click="showOrderBill(scope.row.id)"
					>
						账单详情
					</el-button>
					<el-button
						type="text"
						size="medium"
						v-if="isAuth(['ROOT', 'ORDER:UPDATE'])"
						:disabled="!['等待接单', '已接单', '司机已到达', '开始代驾'].includes(scope.row.status)"
						@click="showUpdatePanel(scope.row.id)"
					>
						关闭订单
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
			@size-change="sizeChangeHandle"
			@current-change="currentChangeHandle"
			:current-page="pageIndex"
			:page-sizes="[10, 20, 50]"
			:page-size="pageSize"
			:total="totalCount"
			layout="total, sizes, prev, pager, next, jumper"
		></el-pagination>
		<order-bill ref="orderBill"></order-bill>
		<order-close ref="orderClose" @refreshDataList="loadDataList"></order-close>
	</div>
</template>

<script>
import OrderBill from './order_bill.vue';
import OrderClose from './order-close.vue';

import $ from 'jquery';
import { calculateCarPlateCity } from '../utils/car_plate.js';
let status = {
	'1': '等待接单',
	'2': '已接单',
	'3': '司机已到达',
	'4': '开始代驾',
	'5': '结束代驾',
	'6': '未付款',
	'7': '已付款',
	'8': '订单已结束',
	'9': '顾客撤单',
	'10': '司机撤单',
	'11': '事故关闭',
	'12': '其他'
};
export default {
	components: {
		OrderBill,
		OrderClose
	},
	data() {
		return {
			dataForm: {
				orderId: null,
				driverId: null,
				customerId: null,
				date: [],
				status: null
			},
			dataList: [],
			pageIndex: 1,
			pageSize: 10,
			totalCount: 0,
			dataListLoading: false,
			dataRule: {
				orderId: [{ required: false, pattern: '^[1-9]\\d{17}$', message: '订单编号格式错误' }],
				driverId: [{ required: false, pattern: '^[1-9]\\d{17}$', message: '司机编号格式错误' }],
				customerId: [{ required: false, pattern: '^[1-9]\\d{17}$', message: '客户编号格式错误' }]
			},
			expands: [],
			panel: {
				id: null,
				customer: {
					id: null,
					sex: null,
					tel: null
				},
				driver: {
					id: null,
					name: null,
					tel: null
				},
				order: {
					carPlate: null,
					carType: null,
					city: null,
					acceptTime: null,
					arriveTime: null,
					startTime: null,
					endTime: null,
					waitingMinute: null,
					driveMinute: null,
					realMileage: null,
					realFee: null,
					status: null,
					chargeRule: null,
					cancelRule: null,
					profitsharingRule: null
				}
			},
			getRowKeys(row) {
				return row.id;
			},
			gpsTimer: null
		};
	},
	methods: {
		loadDataList: function() {
			let that = this;
			that.dataListLoading = true;
			let data = {
				page: that.pageIndex,
				length: that.pageSize,
				orderId: that.dataForm.orderId == '' ? null : that.dataForm.orderId,
				driverId: that.dataForm.driverId == '' ? null : that.dataForm.driverId,
				customerId: that.dataForm.customerId == '' ? null : that.dataForm.customerId,
				status: that.dataForm.status == '' ? null : that.dataForm.status
			};
			if (that.dataForm.date != null && that.dataForm.date.length == 2) {
				let startDate = that.dataForm.date[0];
				let endDate = that.dataForm.date[1];
				data.startDate = dayjs(startDate).format('YYYY-MM-DD');
				data.endDate = dayjs(endDate).format('YYYY-MM-DD');
			}
			that.$http('order/searchOrderByPage', 'POST', data, true, function(resp) {
				let result = resp.result;
				let list = result.list;
				for (let one of list) {
					one.status = status[one.status + ''];
					if (!one.hasOwnProperty('realMileage')) {
						one.realMileage = '--';
					}
					if (!one.hasOwnProperty('realFee')) {
						one.realFee = '--';
					}
				}
				that.dataList = list;
				that.totalCount = Number(result.totalCount);
				that.dataListLoading = false;
			});
		},
		sizeChangeHandle(val) {
			this.pageSize = val;
			this.pageIndex = 1;
			this.loadDataList();
		},
		currentChangeHandle(val) {
			this.pageIndex = val;
			this.loadDataList();
		},
		searchHandle: function() {
			this.$refs['dataForm'].validate(valid => {
				if (valid) {
					this.$refs['dataForm'].clearValidate();
					this.loadDataList();
				} else {
					return false;
				}
			});
		},
		loadPanelData: function(ref, row) {
			let data = {
				orderId: row.id
			};
			ref.$http('order/searchOrderComprehensiveInfo', 'POST', data, true, function(resp) {
				let result = resp.result;
				let content = result.content;

				let customerInfo = result.customerInfo;
				ref.panel.customer.id = customerInfo.id;
				ref.panel.customer.sex = customerInfo.sex;
				ref.panel.customer.tel = customerInfo.tel;

				let driverInfo = result.driverInfo;
				ref.panel.driver.id = driverInfo.id;
				ref.panel.driver.name = driverInfo.name;
				ref.panel.driver.tel = driverInfo.tel;

				ref.panel.order.carPlate = content.carPlate;
				ref.panel.order.carType = content.carType;
				let city = calculateCarPlateCity(content.carPlate);
				ref.panel.order.city = city;

				if (content.hasOwnProperty('acceptTime')) {
					ref.panel.order.acceptTime = content.acceptTime;
				} else {
					ref.panel.order.acceptTime = '--';
				}
				if (content.hasOwnProperty('arriveTime')) {
					ref.panel.order.arriveTime = content.arriveTime;
				} else {
					ref.panel.order.arriveTime = '--';
				}
				if (content.hasOwnProperty('startTime')) {
					ref.panel.order.startTime = content.startTime;
				} else {
					ref.panel.order.startTime = '--';
				}
				if (content.hasOwnProperty('endTime')) {
					ref.panel.order.endTime = content.endTime;
				} else {
					ref.panel.order.endTime = '--';
				}
				if (content.hasOwnProperty('waitingMinute')) {
					ref.panel.order.waitingMinute = content.waitingMinute + '分钟';
				} else {
					ref.panel.order.waitingMinute = '--';
				}
				if (content.hasOwnProperty('driveMinute')) {
					ref.panel.order.driveMinute = content.driveMinute + '分钟';
				} else {
					ref.panel.order.driveMinute = '--';
				}
				if (content.hasOwnProperty('realMileage')) {
					ref.panel.order.realMileage = content.realMileage + '公里';
					row.realMileage = content.realMileage;
				} else {
					ref.panel.order.realMileage = '--';
					row.realMileage = '--';
				}
				if (content.hasOwnProperty('realFee')) {
					ref.panel.order.realFee = content.realFee + '元';
					row.realFee = content.realFee;
				} else {
					ref.panel.order.realFee = '--';
					row.realFee = '--';
				}

				ref.panel.order.status = status[content.status + ''];
				row.status = status[content.status + ''];

				if (result.hasOwnProperty('chargeRule')) {
					ref.panel.order.chargeRule = result.chargeRule.code;
				} else {
					ref.panel.order.chargeRule = '--';
				}
				if (result.hasOwnProperty('cancelRule')) {
					ref.panel.order.cancelRule = result.cancelRule.code;
				} else {
					ref.panel.order.cancelRule = '--';
				}
				if (result.hasOwnProperty('profitsharingRule')) {
					ref.panel.order.profitsharingRule = result.profitsharingRule.code;
				} else {
					ref.panel.order.profitsharingRule = '--';
				}

				ref.$nextTick(function() {
					let startPlaceLocation = content.startPlaceLocation;
					let endPlaceLocation = content.endPlaceLocation;
					let mapCenter = new TMap.LatLng(startPlaceLocation.latitude, startPlaceLocation.longitude);
					let map = new TMap.Map($(`.el-table__expanded-cell #order_${row.id}`)[0], {
						center: mapCenter, //地图显示中心点
						zoom: 13,
						viewMode: '2D',
						baseMap: {
							type: 'vector',
							features: ['base', 'label']
						}
					});

					let driveLine = result.driveLine;
					let coors = driveLine.routes[0].polyline;
					let pl = [];
					//坐标解压（返回的点串坐标，通过前向差分进行压缩，因此需要解压）
					let kr = 1000000;
					for (let i = 2; i < coors.length; i++) {
						coors[i] = Number(coors[i - 2]) + Number(coors[i]) / kr;
					}
					//将解压后的坐标生成LatLng数组
					for (let i = 0; i < coors.length; i += 2) {
						pl.push(new TMap.LatLng(coors[i], coors[i + 1]));
					}

					let polylineLayer = new TMap.MultiPolyline({
						id: 'polyline-layer', //图层唯一标识
						map: map, //绘制到目标地图
						styles: {
							style_blue: new TMap.PolylineStyle({
								color: 'rgba(190,188,188,1)',
								width: 6,
								lineCap: 'round' //线端头方式
							})
						},
						geometries: [
							{
								id: 'pl_1',
								styleId: 'style_blue',
								paths: pl
							}
						]
					});

					let markerLayer = new TMap.MultiMarker({
						map: map,
						styles: {
							startStyle: new TMap.MarkerStyle({
								width: 24,
								height: 36,
								anchor: { x: 16, y: 32 },
								src: 'https://mapapi.qq.com/web/lbs/javascriptGL/demo/img/start.png'
							}),
							endStyle: new TMap.MarkerStyle({
								width: 24,
								height: 36,
								anchor: { x: 16, y: 32 },
								src: 'https://mapapi.qq.com/web/lbs/javascriptGL/demo/img/end.png'
							}),
							carStyle: new TMap.MarkerStyle({
								width: 30,
								height: 30,
								src: '../order/driver-icon.png',
								anchor: { x: 16, y: 32 }
							})
						},
						geometries: [
							//起点标记
							{
								id: '1',
								styleId: 'startStyle',
								position: new TMap.LatLng(startPlaceLocation.latitude, startPlaceLocation.longitude)
							},
							//终点标记
							{
								id: '2',
								styleId: 'endStyle',
								position: new TMap.LatLng(endPlaceLocation.latitude, endPlaceLocation.longitude)
							}
						]
					});
					if (content.status == 4) {
						let lastGps = result.lastGps;
						markerLayer.add([
							{
								id: '3',
								styleId: 'carStyle', //指定样式id
								position: new TMap.LatLng(lastGps.latitude, lastGps.longitude)
							}
						]);
						ref.gpsTimer = setInterval(function() {
							let data = {
								orderId: row.id
							};
							ref.$http('order/searchOrderLastGps', 'POST', data, true, function(resp) {
								if (resp.hasOwnProperty('result')) {
									let lastGps = resp.result;
									markerLayer.updateGeometries([
										{
											id: '3',
											styleId: 'carStyle',
											position: new TMap.LatLng(lastGps.latitude, lastGps.longitude)
										}
									]);
								} else {
									//重新加载面板数据
									$(`.el-table__expanded-cell #order_${row.id}`).empty();
									ref.loadPanelData(ref, row);
									clearInterval(ref.gpsTimer);
								}
							});
						}, 15 * 1000);
					} else if (content.status >= 5 && content.status <= 8) {
						let orderGps = result.orderGps;
						let paths = [];
						for (let one of orderGps) {
							let temp = new TMap.LatLng(one.latitude, one.longitude);
							paths.push(temp);
						}
						let polylineLayer = new TMap.MultiPolyline({
							id: 'drive-polyline-layer', //图层唯一标识
							map: map,
							//折线样式定义
							styles: {
								style_blue: new TMap.PolylineStyle({
									color: '#3777FF', //线填充色
									width: 6 //折线宽度
								})
							},
							//折线数据定义
							geometries: [
								{
									id: 'pl_1',
									styleId: 'style_blue',
									paths: paths
								}
							]
						});
					}
				});
			});
		},
		expand: function(row, expandedRows) {
			let that = this;
			if (expandedRows.length > 0) {
				that.expands = [];
				if (row) {
					that.expands.push(row.id);
					that.panel.id = `order_${row.id}`;
					that.loadPanelData(that, row);
				} else {
					that.expands = [];
				}
			}
		}
	},
	created: function() {
		this.loadDataList();
	}
};
</script>
<style lang="less" scoped="scoped">
@import url('order.less');
</style>
