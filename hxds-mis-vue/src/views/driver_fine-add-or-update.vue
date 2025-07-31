<template>
	<el-dialog
		:title="!dataForm.id ? '新增' : '修改'"
		v-if="isAuth(['ROOT', 'DRIVER_FINE:INSERT', 'DRIVER_FINE:UPDATE'])"
		:close-on-click-modal="false"
		v-model="visible"
		width="420px"
	>
		<el-form :model="dataForm" ref="dataForm" :rules="dataRule" label-width="80px">
			<el-form-item label="司机编号" prop="driverId"><el-input v-model="dataForm.driverId" :disabled="dataForm.id" size="medium" style="width:100%" clearable /></el-form-item>
			<el-form-item label="订单编号" prop="orderId"><el-input v-model="dataForm.orderId" size="medium" style="width:100%" clearable /></el-form-item>
			<el-form-item label="罚款金额" prop="amount"><el-input v-model="dataForm.amount" size="medium" style="width:100%" clearable /></el-form-item>
			<el-form-item label="备注信息" prop="remark"><el-input v-model="dataForm.remark" size="medium" style="width:100%" clearable /></el-form-item>
			<el-form-item label="付款状态" prop="status">
				<el-radio-group v-model="dataForm.status">
					<el-radio-button label="未缴纳"></el-radio-button>
					<el-radio-button label="已缴纳"></el-radio-button>
				</el-radio-group>
			</el-form-item>
		</el-form>
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
				id: null,
				driverId: null,
				orderId: null,
				amount: null,
				remark: null,
				status: '未缴纳'
			},
			dataRule: {
				driverId: [{ required: true, pattern: '^[1-9]\\d{17}$', message: '司机编号格式错误' }],
				orderId: [{ required: false, pattern: '^[1-9]\\d{17}$', message: '订单编号格式错误' }],
				amount: [
					{
						required: true,
						pattern: '^[1-9]\\d*\\.\\d{1,2}$|^0\\.\\d{1,2}$|^[1-9]\\d*$',
						message: '金额格式错误'
					}
				],
				remark: [
					{
						required: true,
						message: '备注不能为空'
					}
				]
			}
		};
	},

	methods: {
		init: function(id) {
			let that = this;
			that.dataForm.id = id || 0;
			that.visible = true;
			that.$nextTick(() => {
				that.$refs['dataForm'].resetFields();
				if (id) {
					that.$http('driver/fine/searchDriverFineById', 'POST', { fineId: id }, true, function(resp) {
						console.log(resp)
						that.dataForm.driverId = resp.result.driverId;
						that.dataForm.orderId = resp.result.orderId;
						that.dataForm.amount = resp.result.amount;
						that.dataForm.remark = resp.result.remark;
						that.dataForm.status = resp.result.status == 1 ? '未缴纳' : '已缴纳';
					});
				}
			});
		},
		dataFormSubmit: function() {
			let that = this;
			this.$refs['dataForm'].validate(valid => {
				if (valid) {
					let data = {
						fineId: that.dataForm.id,
						driverId: that.dataForm.driverId == '' ? null : that.dataForm.driverId,
						orderId: that.dataForm.orderId == '' ? null : that.dataForm.orderId,
						amount: that.dataForm.amount == '' ? null : that.dataForm.amount,
						remark: that.dataForm.remark == '' ? null : that.dataForm.remark,
						status: that.dataForm.status == '未缴纳' ? 1 : 2
					};
					that.$http(`driver/fine/${!that.dataForm.id ? 'insertDriverFine' : 'updateDriverFine'}`, 'POST', data, true, function(resp) {
						if (resp.rows == 1) {
							that.$message({
								message: '操作成功',
								type: 'success',
								duration: 1200
							});
							that.visible = false;
							that.$emit('refreshDataList');
						} else {
							that.$message({
								message: '操作失败',
								type: 'error',
								duration: 1200
							});
						}
					});
				}
			});
		}
	}
};
</script>

<style lang="less" scoped="scoped"></style>
