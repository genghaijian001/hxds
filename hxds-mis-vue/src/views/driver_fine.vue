<template>
	<div v-if="isAuth(['ROOT', 'DRIVER_FINE:SELECT'])">
		<el-form :inline="true" :model="dataForm" :rules="dataRule" ref="dataForm">
			<el-form-item prop="orderId"><el-input v-model="dataForm.orderId" placeholder="订单编号" size="medium" clearable="clearable" /></el-form-item>
			<el-form-item prop="tel"><el-input v-model="dataForm.tel" placeholder="司机电话" size="medium" class="input" clearable="clearable" /></el-form-item>
			<el-form-item>
				<el-date-picker v-model="dataForm.date" type="daterange" range-separator="~" start-placeholder="开始日期" end-placeholder="结束日期" size="medium"></el-date-picker>
			</el-form-item>
			<el-form-item>
				<el-select v-model="dataForm.status" class="input" placeholder="状态" size="medium" clearable="clearable">
					<el-option label="未缴纳" value="1" />
					<el-option label="已缴纳" value="2" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button size="medium" type="primary" @click="searchHandle()">查询</el-button>
				<el-button size="medium" type="primary" :disabled="!isAuth(['ROOT', 'DEPT:INSERT'])" @click="addHandle()">新增</el-button>
				<el-button size="medium" type="danger" :disabled="!isAuth(['ROOT', 'DEPT:DELETE'])" @click="deleteHandle()">批量删除</el-button>
			</el-form-item>
		</el-form>
		<el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle" cell-style="padding: 4px 0" size="medium" style="width: 100%;">
			<el-table-column type="selection" :selectable="selectable" header-align="center" align="center" width="50" />
			<el-table-column type="index" header-align="center" align="center" width="100" label="序号">
				<template #default="scope">
					<span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="name" header-align="center" align="center" label="司机姓名" min-width="130" />
			<el-table-column prop="orderId" header-align="center" align="center" label="订单编号" min-width="200" />
			<el-table-column prop="amount" header-align="center" align="center" label="罚款金额" min-width="130" />
			<el-table-column prop="remark" header-align="center" align="center" label="备注信息" min-width="300" />
			<el-table-column prop="status" header-align="center" align="center" label="状态" min-width="130" />
			<el-table-column prop="createTime" header-align="center" align="center" label="日期时间" min-width="200" />
			<el-table-column header-align="center" align="center" width="150" label="操作">
				<template #default="scope">
					<el-button type="text" size="medium" :disabled="!isAuth(['ROOT', 'DRIVER_FINE:UPDATE']) || scope.row.status == '已缴纳'" @click="updateHandle(scope.row.id)">
						修改
					</el-button>
					<el-button type="text" size="medium" :disabled="!isAuth(['ROOT', 'DRIVER_FINE:DELETE']) || scope.row.status == '已缴纳'" @click="deleteHandle(scope.row.id)">
						删除
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
		<add-or-update ref="addOrUpdate" @refreshDataList="loadDataList"></add-or-update>
	</div>
</template>

<script>
import AddOrUpdate from './driver_fine-add-or-update.vue';
export default {
	components: {
		AddOrUpdate
	},
	data: function() {
		return {
			dataForm: {
				orderId: null,
				tel: null,
				status: null,
				date: []
			},
			dataList: [],
			pageIndex: 1,
			pageSize: 10,
			totalCount: 0,
			dataListLoading: false,
			dataListSelections: [],
			dataRule: {
				orderId: [{ required: false, pattern: '^[1-9]\\d{17}$', message: '订单编号格式错误' }],
				tel: [{ required: false, pattern: '^1\\d{10}$', message: '电话格式错误' }]
			}
		};
	},
	methods: {
		loadDataList: function() {
			let that = this;
			that.dataListLoading = true;
			let data = {
				orderId: that.dataForm.orderId == '' ? null : that.dataForm.orderId,
				tel: that.dataForm.tel == '' ? null : that.dataForm.tel,
				status: that.dataForm.status == '' ? null : that.dataForm.status,
				page: that.pageIndex,
				length: that.pageSize
			};

			if (that.dataForm.date != null && that.dataForm.date.length == 2) {
				let startDate = that.dataForm.date[0];
				let endDate = that.dataForm.date[1];
				data.startDate = dayjs(startDate).format('YYYY-MM-DD');
				data.endDate = dayjs(endDate).format('YYYY-MM-DD');
			}

			that.$http('driver/fine/searchDriverFineByPage', 'POST', data, true, function(resp) {
				let result = resp.result;
				let list = result.list;
				let status = {
					'1': '未缴纳',
					'2': '已缴纳'
				};
				for (let one of list) {
					one.status = status[one.status + ''];
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
		selectable: function(row, index) {
			if (row.status == '已缴纳') {
				return false;
			}
			return true;
		},
		selectionChangeHandle: function(val) {
			this.dataListSelections = val;
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
		addHandle: function() {
			this.$nextTick(() => {
				this.$refs.addOrUpdate.init();
			});
		},
		updateHandle: function(id) {
			this.$nextTick(() => {
				this.$refs.addOrUpdate.init(id);
			});
		},
		deleteHandle: function(id) {
			let that = this;
			let ids = id
				? [id]
				: that.dataListSelections.map(item => {
						return item.id;
				  });
			if (ids.length == 0) {
				that.$message({
					message: '没有选中记录',
					type: 'warning',
					duration: 1200
				});
			} else {
				that.$confirm(`确定要删除选中的记录？`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					that.$http('driver/fine/deleteDriverFineByIds', 'POST', { ids: ids }, true, function(resp) {
						if (resp.rows > 0) {
							that.$message({
								message: '操作成功',
								type: 'success',
								duration: 1200,
								onClose: () => {
									that.loadDataList();
								}
							});
						} else {
							that.$message({
								message: '未能删除记录',
								type: 'warning',
								duration: 1200
							});
						}
					});
				});
			}
		}
	},
	created: function() {
		this.loadDataList();
	}
};
</script>

<style></style>
