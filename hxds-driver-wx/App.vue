<script>
export default {
	onLaunch: function() {
		let gps = [];
		wx.setKeepScreenOn({
			keepScreenOn: true
		});

		// 隐私保护机制（2023年9月起强制要求）
		if (wx.onNeedPrivacyAuthorization) {
			wx.onNeedPrivacyAuthorization((resolve) => {
				uni.showModal({
					title: '隐私保护提示',
					content: '在使用代驾服务前，请阅读并同意《隐私保护指引》',
					confirmText: '同意',
					cancelText: '拒绝',
					success(res) {
						if (res.confirm) {
							resolve({ buttonId: 'agree-btn', event: 'agreeprivacyauthorization' })
						} else {
							resolve({ event: 'rejectprivacyauthorization' })
						}
					}
				})
			})
		}

		//TODO 每隔3分钟触发自定义事件，接受系统消息

		wx.getSetting({
			success(settingRes) {
				if (settingRes.authSetting['scope.userLocation'] === false) {
					// 用户明确拒绝过
					uni.showModal({
						title: '定位权限已关闭',
						content: '代驾服务需要位置权限才能接单，请在设置中开启',
						confirmText: '去开启',
						cancelText: '取消',
						success(modalRes) {
							if (modalRes.confirm) { wx.openSetting(); }
						}
					});
					uni.$emit('updateLocation', null);
					return;
				}
				wx.startLocationUpdate({
					success() { console.log('开启定位成功'); },
					fail() {
						console.log('开启定位失败');
						uni.$emit('updateLocation', null);
					}
				});
			}
		});

		wx.onLocationChange(function(resp) {
			let latitude = resp.latitude;
			let longitude = resp.longitude;
			let speed = resp.speed;

			let location = {
				latitude: latitude,
				longitude: longitude
			};

			let workStatus = uni.getStorageSync('workStatus');
			let baseUrl = 'http://127.0.0.1:8201/hxds-driver';
			if (workStatus == '开始接单') {
				// TODO 只在每分钟的前10秒上报定位信息，减小服务器压力
				// let current = new Date();
				// if (current.getSeconds() > 10) {
				// 	return;
				// }
				let settings = uni.getStorageSync('settings');
				if (!settings) {
					settings = {
						orderDistance: 0,
						rangeDistance: 5,
						orientation: ''
					};
				}
				let orderDistance = settings.orderDistance;
				let rangeDistance = settings.rangeDistance;
				let orientation = settings.orientation;
				uni.request({
					url: `${baseUrl}/driver/location/updateLocationCache`,
					method: 'POST',
					header: {
						token: uni.getStorageSync('token')
					},
					data: {
						latitude: latitude,
						longitude: longitude,
						orderDistance: orderDistance,
						rangeDistance: rangeDistance,
						orientateLongitude: orientation != '' ? orientation.longitude : null,
						orientateLatitude: orientation != '' ? orientation.latitude : null
					},
					success: function(resp) {
						if (resp.statusCode == 401) {
							uni.redirectTo({
								url: '/pages/login/login'
							});
						} else if (resp.statusCode == 200 && resp.data.code == 200) {
							let data = resp.data;
							if (data.hasOwnProperty('token')) {
								let token = data.token;
								uni.setStorageSync('token', token);
							}
							console.log('定位更新成功');
						} else {
							console.error('更新GPS定位信息失败', resp.data);
						}
					},
					fail: function(error) {
						console.error('更新GPS定位信息失败', error);
					}
				});
			} 
			else if(workStatus == '接客户'){
				let executeOrder = uni.getStorageSync('executeOrder');
				let orderId=executeOrder.id
				let data={
					orderId:orderId,
					latitude: latitude,
					longitude: longitude
				}
				uni.request({
					url: `${baseUrl}/driver/location/updateOrderLocationCache`,
					method:"POST",
					header:{
						token:uni.getStorageSync("token")
					},
					data:data,
					success:function(resp){
						if (resp.statusCode == 401) {
							uni.redirectTo({
								url: '/pages/login/login'
							});
						} else if (resp.statusCode == 200 && resp.data.code == 200) {
							let data = resp.data;
							if (data.hasOwnProperty('token')) {
								let token = data.token;
								uni.setStorageSync('token', token);
							}
							console.log('订单定位更新成功');
						} else {
							console.error('订单定位更新失败', resp.data);
						}
					},
					fail:function(error){
						console.error('订单定位更新失败', error);
					}
				})
			}
			else if (workStatus == '开始代驾') {
				//每凑够20个定位就上传一次，减少服务器的压力
				let executeOrder=uni.getStorageSync("executeOrder")
				if(executeOrder!=null){
					gps.push({
						orderId: executeOrder.id,
						customerId: executeOrder.customerId,
						latitude: latitude,
						longitude: longitude,
						speed: speed
					})
					if(gps.length==5){
						uni.request({
							url:`${baseUrl}/order/gps/insertOrderGps`,
							method: 'POST',
							header: {
							    token: uni.getStorageSync('token')
							},
							data: {
							    list: gps
							},
							success: function(resp) {
								if (resp.statusCode == 401) {
								    uni.redirectTo({
								        url: '/pages/login/login'
								    });
								}
								else if (resp.statusCode == 200 && resp.data.code == 200) {
								    let data = resp.data;
								    console.log("上传GPS成功");
								} 
								else {
								    console.error('保存GPS定位失败', resp.data);
								}
								gps.length = 0;
							},
							fail: function(error) {
							    console.error('保存GPS定位失败', error);
							}
						})
					}
				}
			}
			uni.$emit('updateLocation', location);
		});
	},
	onShow: function() {
		console.log('App Show');
	},
	onHide: function() {
		console.log('App Hide');
	}
};
</script>

<style lang="scss">
@import 'uview-ui/index.scss';
</style>
