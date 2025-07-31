<template>
	<view>
		<map
			id="map"
			scale="15"
			:longitude="longitude"
			:latitude="latitude"
			:enable-poi="true"
			class="map"
			:style="mapStyle"
			:polyline="polyline"
			:markers="markers"
			@longpress="showHandle"
		>
			<!-- <cover-image class="location" src="../static/workbench/location.png" @tap="returnLocationHandle()" /> -->
			<cover-view class="information-container" v-show="infoStatus">
				<cover-image src="../static/move/close-icon.png" class="close-icon" @tap="hideHandle"></cover-image>
				<cover-view class="info">
					<cover-view class="label">剩余</cover-view>
					<cover-view class="value">{{ distance }}公里</cover-view>
					<cover-view class="label">，预计</cover-view>
					<cover-view class="value">{{ duration }}分钟</cover-view>
				</cover-view>
			</cover-view>
		</map>
	</view>
</template>

<script>
let QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
let qqmapsdk;
export default {
	data() {
		return {
			orderId: null,
			status: null,
			mode: null,
			map: null,
			mapStyle: '',
			startLatitude: 0,
			startLongitude: 0,
			endLatitude: 0,
			endLongitude: 0,
			latitude: 0,
			longitude: 0,
			targetLatitude: 0,
			targetLongitude: 0,
			distance: 0,
			duration: 0,
			polyline: [],
			markers: [],
			timer: null,
			infoStatus: true
		};
	},
	methods: {
		formatPolyline(polyline) {
			let coors = polyline;
			let pl = [];
			//坐标解压（返回的点串坐标，通过前向差分进行压缩）
			const kr = 1000000;
			for (let i = 2; i < coors.length; i++) {
				coors[i] = Number(coors[i - 2]) + Number(coors[i]) / kr;
			}
			//将解压后的坐标放入点串数组pl中
			for (let i = 0; i < coors.length; i += 2) {
				pl.push({
					longitude: coors[i + 1],
					latitude: coors[i]
				});
			}
			return pl;
		},
		calculateLine: function(ref) {
			if (ref.latitude == 0 || ref.longitude == 0) {
				return;
			}
			qqmapsdk.direction({
				mode: ref.mode,
				from: {
					latitude: ref.latitude,
					longitude: ref.longitude
				},
				to: {
					latitude: ref.targetLatitude,
					longitude: ref.targetLongitude
				},
				success: function(resp) {
					let route = resp.result.routes[0];
					let distance = route.distance;
					let duration = route.duration;
					let polyline = route.polyline;
					ref.distance = Math.ceil((distance / 1000) * 10) / 10;
					ref.duration = duration;

					let points = ref.formatPolyline(polyline);

					ref.polyline = [
						{
							points: points,
							width: 6,
							color: '#05B473',
							arrowLine: true
						}
					];
					ref.markers = [
						{
							id: 1,
							latitude: ref.latitude,
							longitude: ref.longitude,
							width: 35,
							height: 35,
							anchor: {
								x: 0.5,
								y: 0.5
							},
							iconPath: '../static/move/driver-icon.png'
						}
					];
				},
				fail: function(error) {
					console.log(error);
				}
			});
		},
		hideHandle: function() {
			this.infoStatus = false;
		},
		showHandle: function() {
			this.infoStatus = true;
		}
	},
	onLoad: function(options) {
		let that = this;
		that.orderId = options.orderId;
		qqmapsdk = new QQMapWX({
			key: that.tencent.map.key
		});
		let windowHeight = uni.getSystemInfoSync().windowHeight;
		that.mapStyle = `height:${windowHeight}px`;
	},
	onShow: function() {
		let that = this;
		uni.$on('updateLocation', function(location) {
			if (location != null) {
				that.latitude = location.latitude;
				that.longitude = location.longitude;
			}
		});

		let data = {
			orderId: that.orderId
		};
		that.ajax(that.url.searchOrderForMoveById, 'POST', data, function(resp) {
			let result = resp.data.result;

			let startPlaceLocation = JSON.parse(result.startPlaceLocation);
			that.startLatitude = startPlaceLocation.latitude;
			that.startLongitude = startPlaceLocation.longitude;

			let endPlaceLocation = JSON.parse(result.endPlaceLocation);
			that.endLatitude = endPlaceLocation.latitude;
			that.endLongitude = endPlaceLocation.longitude;

			let status = result.status;
			if (status == 2) {
				that.targetLatitude = that.startLatitude;
				that.targetLongitude = that.startLongitude;
				that.mode = 'bicycling';
			} else if (status == 3 || status == 4) {
				that.targetLatitude = that.endLatitude;
				that.targetLongitude = that.endLongitude;
				that.mode = 'driving';
			}
			that.calculateLine(that);
			that.timer = setInterval(function() {
				that.calculateLine(that);
			}, 6000);
		});
	},
	onHide: function() {
		let that = this;
		uni.$off('updateLocation');
		clearInterval(that.timer);
		that.timer = null;
	}
};
</script>

<style lang="less">
@import url('move.less');
</style>
