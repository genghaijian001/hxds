<template>
	<el-dialog
		:title="!dataForm.id ? '新增' : '修改'"
		v-if="isAuth(['ROOT', 'DRIVER_LOCKDOWN:INSERT', 'DRIVER_LOCKDOWN:UPDATE'])"
		:close-on-click-modal="false"
		v-model="visible"
		width="420px"
	>
		<el-form :model="dataForm" ref="dataForm" :rules="dataRule" label-width="80px">
			<el-form-item label="司机编号" prop="driverId">
				<el-input v-model="dataForm.driverId" :disabled="dataForm.id" size="medium" style="width:100%" clearable />
			</el-form-item>
			<el-form-item label="订单编号" prop="orderId"><el-input v-model="dataForm.orderId" size="medium" style="width:100%" clearable /></el-form-item>
			<el-form-item label="具体原因" prop="reason"><el-input v-model="dataForm.reason" :rows="2" type="textarea" size="medium" style="width:100%" clearable /></el-form-item>
			<el-form-item label="起始日期" prop="startDate"><el-date-picker v-model="dataForm.startDate" type="date" placeholder="起始日期" /></el-form-item>
			<el-form-item label="截止日期" prop="endDate"><el-date-picker v-model="dataForm.endDate" type="date" placeholder="截止日期" /></el-form-item>
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
import dayjs from 'dayjs';
export default {
	data: function() {
		return {
			visible: false,
			dataForm: {
				id: null,
				driverId: null,
				orderId: null,
				startDate: null,
				endDate: null,
				reason: null
			},
			dataRule: {
				driverId: [{ required: true, pattern: '^[1-9]\\d{17}$', message: '司机编号格式错误' }],
				orderId: [{ required: false, pattern: '^[1-9]\\d{17}$', message: '订单编号格式错误' }],
				startDate: [{ required: true, message: '起始日期必填' }],
				endDate: [{ required: true, message: '截止日期必填' }],
				reason: [
					{
						required: true,
						message: '原因不能为空'
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
					that.$http('driver/lockdown/searchDriverLockdownById', 'POST', { lockdownId: id }, true, function(resp) {
						that.dataForm.driverId = resp.result.driverId;
						that.dataForm.orderId = resp.result.orderId;
						that.dataForm.startDate = resp.result.startDate;
						that.dataForm.endDate = resp.result.endDate;
						that.dataForm.reason = resp.result.reason;
					});
				}
			});
		},
		dataFormSubmit: function() {
			let that = this;
			this.$refs['dataForm'].validate(valid => {
				if (valid) {
					let data = {
						lockdownId: that.dataForm.id,
						driverId: that.dataForm.driverId == '' ? null : that.dataForm.driverId,
						orderId: that.dataForm.orderId == '' ? null : that.dataForm.orderId,
						startDate: that.dataForm.startDate,
						endDate: that.dataForm.endDate,
						reason: that.dataForm.reason == '' ? null : that.dataForm.reason
					};
					if (that.dataForm.startDate != null && that.dataForm.startDate != '') {
						data.startDate = dayjs(that.dataForm.startDate).format('YYYY-MM-DD');
					}
					if (that.dataForm.endDate != null && that.dataForm.endDate != '') {
						data.endDate = dayjs(that.dataForm.endDate).format('YYYY-MM-DD');
					}
					if (dayjs(data.startDate).isAfter(dayjs(data.endDate))) {
						that.$message({
							message: '起始日期不能晚于截止日期',
							type: 'error',
							duration: 1200
						});
						return;
					}

					that.$http(`driver/lockdown/${!that.dataForm.id ? 'insertDriverLockdown' : 'updateDriverLockdown'}`, 'POST', data, true, function(resp) {
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
