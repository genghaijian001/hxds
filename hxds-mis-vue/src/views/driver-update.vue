<template>
	<el-dialog title="设置状态" v-if="isAuth(['ROOT', 'DRIVER:UPDATE'])" :close-on-click-modal="false" v-model="visible" width="420px">
		<el-form :model="dataForm" ref="dataForm" label-width="60px">
			<el-form-item label="状态">
				<el-radio-group v-model="dataForm.status" size="default">
					<el-radio-button label="正常"></el-radio-button>
					<el-radio-button label="禁用"></el-radio-button>
					<el-radio-button label="减少接单"></el-radio-button>
				</el-radio-group>
			</el-form-item>
		</el-form>
		<p class="desc">如果设置为禁用状态，该司机将立即下线，对于正在执行中的订单将无法完结和付款，请谨慎设置。</p>
		<template #footer>
			<span class="dialog-footer">
				<el-button size="medium" @click="visible = false">取消</el-button>
				<el-button type="primary" size="medium" @click="dataFormSubmit">确定</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script>
export default {
	data: function() {
		return {
			visible: false,
			dataForm: {
				driverId: null,
				status: null
			}
		};
	},

	methods: {
		init: function(id) {
			let that = this;
			that.dataForm.driverId = id;
			that.visible = true;
			that.$nextTick(() => {
				that.$refs['dataForm'].resetFields();
				if (id) {
					that.$http('driver/searchDriverStatus', 'POST', { driverId: id }, true, function(resp) {
						let status = {
							'1': '正常',
							'2': '禁用',
							'3': '减少接单'
						};
						that.dataForm.status = status[resp.result + ''];
						// console.log(resp);
					});
				}
			});
		},
		dataFormSubmit: function() {
			let that = this;
			let status = null;
			if (that.dataForm.status == '正常') {
				status = 1;
			} else if (that.dataForm.status == '禁用') {
				status = 2;
			} else {
				status = 3;
			}
			let data = {
				driverId: that.dataForm.driverId,
				status: status
			};
			that.$http('driver/updateDriverStatus', 'POST', data, true, function(resp) {
				if (resp.rows == 1) {
					that.$message.success('状态更新成功');
					that.visible = false;
					that.$emit('refreshDataList');
				} else {
					that.$message.error('操作失败');
				}
			});
		}
	}
};
</script>

<style lang="less" scoped="scoped">
.desc {
	color: #999;
	padding: 0 10px;
	line-height: 1.8;
}
</style>
