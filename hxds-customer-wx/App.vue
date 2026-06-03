<script>
export default {
	onLaunch: function() {
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

		//开启GPS后台刷新（先检查权限状态）
		wx.getSetting({
			success(settingRes) {
				if (settingRes.authSetting['scope.userLocation'] === false) {
					uni.showModal({
						title: '定位权限已关闭',
						content: '代驾服务需要位置权限以显示地图和呼叫代驾，请在设置中开启',
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
						console.log('开启定位失败，尝试单次定位');
						wx.getLocation({
							type: 'gcj02',
							success(locRes) {
								uni.$emit('updateLocation', { latitude: locRes.latitude, longitude: locRes.longitude });
							},
							fail(e) { console.log('单次定位也失败', e); }
						});
					}
				});
			}
		});
		//GPS定位变化就自动提交给后端
		wx.onLocationChange(function(resp) {
			let location = { latitude: resp.latitude, longitude: resp.longitude };
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
