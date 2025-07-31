<template>
	<div v-if="isAuth(['ROOT', 'MONITORING:SELECT'])">
		<div class="form">
			<el-form :inline="true" :model="dataForm" :rules="dataRule" ref="dataForm">
				<el-form-item prop="orderId"><el-input v-model="dataForm.orderId" placeholder="订单编号" size="medium" clearable="clearable" /></el-form-item>
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
					<el-select v-model="dataForm.safety" class="input" placeholder="内容评估" size="medium" clearable="clearable">
						<el-option label="安全" value="common" />
						<el-option label="有害" value="danger" />
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-select v-model="dataForm.needReview" class="input" placeholder="人工审核" size="medium" clearable="clearable">
						<el-option label="需要审核" value="true" />
						<el-option label="不需要审核" value="false" />
					</el-select>
				</el-form-item>
				<el-form-item><el-button size="medium" type="primary" @click="searchHandle()">查询</el-button></el-form-item>
			</el-form>
			<el-radio-group v-model="dataForm.alarm" @change="changeWalletType">
				<el-radio-button label="全部"></el-radio-button>
				<el-radio-button label="未报警"></el-radio-button>
				<el-radio-button label="已报警"></el-radio-button>
			</el-radio-group>
		</div>
		<el-table :data="dataList" border v-loading="dataListLoading" cell-style="padding: 4px 0" size="medium" style="width: 100%;">
			<el-table-column type="index" header-align="center" align="center" width="100" label="序号">
				<template #default="scope">
					<span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="orderId" header-align="center" align="center" label="订单编号" min-width="200" />
			<el-table-column prop="status" header-align="center" align="center" label="代驾状态" min-width="140" />
			<el-table-column prop="records" header-align="center" align="center" label="录音数量" min-width="140" />
			<!-- 			<el-table-column prop="minutes" header-align="center" align="center" label="总时长" min-width="140">
				<template #default="scope">
					<span>{{ scope.row.minute }}分钟</span>
				</template>
			</el-table-column> -->
			<el-table-column prop="safety" header-align="center" align="center" label="安全等级" min-width="140">
				<template #default="scope">
					<el-tag :type="scope.row.safety == '安全' ? 'success' : 'danger'">{{ scope.row.safety }}</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="reviews" header-align="center" align="center" label="待审核" min-width="140" />
			<el-table-column prop="createTime" header-align="center" align="center" label="日期" min-width="140" />
			<el-table-column header-align="center" align="center" width="300" label="操作">
				<template #default="scope">
					<el-button size="small" type="primary" :disabled="!isAuth(['ROOT', 'DEPT:UPDATE'])" @click="updateHandle(scope.row.id)">呼叫司机</el-button>
					<el-button size="small" type="primary" :disabled="!isAuth(['ROOT', 'DEPT:DELETE']) || scope.row.emps > 0" @click="deleteHandle(scope.row.id)">
						呼叫客户
					</el-button>
					<el-button size="small" type="danger">报警</el-button>
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
	</div>
</template>

<script>
export default {
	data: function() {
		return {
			dataForm: {
				orderId: null,
				date: [],
				safety: null,
				needReview: null,
				alarm: '全部'
			},
			dataList: [],
			pageIndex: 1,
			pageSize: 10,
			totalCount: 0,
			dataListLoading: false,
			dataRule: {
				orderId: [{ required: false, pattern: '^[1-9]\\d{17}$', message: '订单编号格式错误' }]
			}
		};
	},
	methods: {
		loadDataList: function() {
			let that = this;
			that.dataListLoading = true;
			let data = {
				orderId: that.dataForm.orderId == '' ? null : that.dataForm.orderId,
				safety: that.dataForm.safety == '' ? null : that.dataForm.safety,
				needReview: that.dataForm.needReview == '' ? null : that.dataForm.needReview,
				page: that.pageIndex,
				length: that.pageSize
			};
			if (that.dataForm.date != null && that.dataForm.date.length == 2) {
				let startDate = that.dataForm.date[0];
				let endDate = that.dataForm.date[1];
				data.startDate = dayjs(startDate).format('YYYY-MM-DD');
				data.endDate = dayjs(endDate).format('YYYY-MM-DD');
			}
			let alarm = {
				全部: null,
				未报警: 1,
				已报警: 2
			};

			data.alarm = alarm[that.dataForm.alarm];

			console.log(data);
			that.$http('monitoring/searchOrderMonitoringByPage', 'POST', data, true, function(resp) {
				// console.log(resp);
				let result = resp.result;
				let status = {
					'1': '代驾开始',
					'2': '代驾结束'
				};
				let safety = {
					common: '安全',
					danger: '危害'
				};
				for (let one of result.list) {
					one.status = status[one.status + ''];
					one.safety = safety[one.safety + ''];
				}
				that.dataList = result.list;
				that.totalCount = Number(result.totalCount);
				that.dataListLoading = false;
			});
		},
		searchHandle: function() {
			this.$refs['dataForm'].validate(valid => {
				if (valid) {
					this.$refs['dataForm'].clearValidate();
					if (this.pageIndex != 1) {
						this.pageIndex = 1;
					}
					this.loadDataList();
				} else {
					return false;
				}
			});
		}
		// selectable: function(row, index) {
		// 	if (row.emps > 0) {
		// 		return false;
		// 	}
		// 	return true;
		// },
		// selectionChangeHandle: function(val) {
		// 	this.dataListSelections = val;
		// },
		// sizeChangeHandle: function(val) {
		// 	this.pageSize = val;
		// 	this.pageIndex = 1;
		// 	this.loadDataList();
		// },
		// currentChangeHandle: function(val) {
		// 	this.pageIndex = val;
		// 	this.loadDataList();
		// },
		// deleteHandle: function(id) {
		// 	let that = this;
		// 	let ids = id
		// 		? [id]
		// 		: that.dataListSelections.map(item => {
		// 				return item.id;
		// 		  });
		// 	if (ids.length == 0) {
		// 		that.$message({
		// 			message: '没有选中记录',
		// 			type: 'warning',
		// 			duration: 1200
		// 		});
		// 	} else {
		// 		that.$confirm(`确定要删除选中的记录？`, '提示', {
		// 			confirmButtonText: '确定',
		// 			cancelButtonText: '取消',
		// 			type: 'warning'
		// 		}).then(() => {
		// 			that.$http('dept/deleteDeptByIds', 'POST', { ids: ids }, true, function(resp) {
		// 				if (resp.rows > 0) {
		// 					that.$message({
		// 						message: '操作成功',
		// 						type: 'success',
		// 						duration: 1200,
		// 						onClose: () => {
		// 							that.loadDataList();
		// 						}
		// 					});
		// 				} else {
		// 					that.$message({
		// 						message: '未能删除记录',
		// 						type: 'warning',
		// 						duration: 1200
		// 					});
		// 				}
		// 			});
		// 		});
		// 	}
		// },
		// addHandle: function() {
		// 	this.$nextTick(() => {
		// 		this.$refs.addOrUpdate.init();
		// 	});
		// },
		// updateHandle: function(id) {
		// 	this.$nextTick(() => {
		// 		this.$refs.addOrUpdate.init(id);
		// 	});
		// }
	},
	created: function() {
		this.loadDataList();
	}
};
</script>

<style lang="less" scoped="scoped">
.form {
	display: flex;
	justify-content: space-between;
}
</style>
