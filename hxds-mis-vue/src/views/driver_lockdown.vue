<template>
	<div v-if="isAuth(['ROOT', 'DRIVER_LOCKDOWN:SELECT'])">
		<el-form :inline="true" :model="dataForm" :rules="dataRule" ref="dataForm">
			<el-form-item prop="tel"><el-input v-model="dataForm.driverId" placeholder="司机编号" size="medium" clearable="clearable" /></el-form-item>
			<el-form-item prop="tel"><el-input v-model="dataForm.tel" placeholder="司机电话" size="medium" class="input" clearable="clearable" /></el-form-item>
			<el-form-item prop="orderId"><el-input v-model="dataForm.orderId" placeholder="订单编号" size="medium" clearable="clearable" /></el-form-item>
			<el-form-item>
				<el-select v-model="dataForm.effect" class="input" placeholder="状态" size="medium" clearable="clearable">
					<el-option label="有效" value="true" />
					<el-option label="过期" value="false" />
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
			<el-table-column prop="driverId" header-align="center" align="center" label="司机编号" min-width="200" />
			<el-table-column prop="name" header-align="center" align="center" label="司机姓名" min-width="130" />
			<el-table-column prop="orderId" header-align="center" align="center" label="订单编号" min-width="200" />
			<el-table-column prop="reason" header-align="center" align="center" label="具体原因" min-width="300" />
			<el-table-column prop="dateRange" header-align="center" align="center" label="日期范围" min-width="200" />
			<el-table-column prop="effect" header-align="center" align="center" label="状态" min-width="130" />
			<el-table-column header-align="center" align="center" width="150" label="操作">
				<template #default="scope">
					<el-button
						type="text"
						size="medium"
						:disabled="!isAuth(['ROOT', 'DRIVER_LOCKDOWN:UPDATE']) || scope.row.status == '已缴纳'"
						@click="updateHandle(scope.row.id)"
					>
						修改
					</el-button>
					<el-button
						type="text"
						size="medium"
						:disabled="!isAuth(['ROOT', 'DRIVER_LOCKDOWN:DELETE']) || scope.row.status == '已缴纳'"
						@click="deleteHandle(scope.row.id)"
					>
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
import AddOrUpdate from './driver_lockdown-add-or-update.vue';
export default {
	components: {
		AddOrUpdate
	},
	data: function() {
		return {
			dataForm: {
				driverId: null,
				tel: null,
				orderId: null,
				effect: null
			},
			dataList: [],
			pageIndex: 1,
			pageSize: 10,
			totalCount: 0,
			dataListLoading: false,
			dataListSelections: [],
			dataRule: {
				driverId: [{ required: true, pattern: '^[1-9]\\d{17}$', message: '司机编号格式错误' }],
				tel: [{ required: false, pattern: '^1\\d{10}$', message: '电话格式错误' }],
				orderId: [{ required: false, pattern: '^[1-9]\\d{17}$', message: '订单编号格式错误' }]
			}
		};
	},
	methods: {
		loadDataList: function() {
			let that = this;
			that.dataListLoading = true;

			let data = {
				driverId: that.dataForm.driverId == '' ? null : that.dataForm.driverId,
				tel: that.dataForm.tel == '' ? null : that.dataForm.tel,
				orderId: that.dataForm.orderId == '' ? null : that.dataForm.orderId,
				effect: that.dataForm.effect,
				page: that.pageIndex,
				length: that.pageSize
			};
			that.$http('driver/lockdown/searchDriverLockdownByPage', 'POST', data, true, function(resp) {
				let result = resp.result;
				let list = result.list;
				let effect = {
					'0': '过期',
					'1': '有效'
				};
				for (let one of list) {
					one.effect = effect[one.effect + ''];
					one.dateRange = one.startDate + ' 至 ' + one.endDate;
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
					that.$http('driver/lockdown/deleteDriverLockdownByIds', 'POST', { ids: ids }, true, function(resp) {
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
