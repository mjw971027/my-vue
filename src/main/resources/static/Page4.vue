<!--
  ============================================================
  文件：src/views/Page4.vue
  作用：工装申请管理页面
  说明：
    - 查询、新增、删除、撤回、打印工装申请单
    - 使用 Element Plus 组件重构原 MiniUI 页面
  ============================================================
-->
<template>
  <div class="page4-container">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="公司主体">
          <el-select v-model="searchForm.companyNo" placeholder="请选择" style="width: 200px" @change="handleCompanyChange">
            <el-option
              v-for="item in companyOptions"
              :key="item.code"
              :label="item.descChn"
              :value="item.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="申请部门">
          <el-select v-model="searchForm.deptNo" placeholder="请选择" style="width: 200px" clearable filterable>
            <el-option
              v-for="item in departmentOptions"
              :key="item.orgnCd"
              :label="item.orgnDesc"
              :value="item.orgnCd"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="工程号">
          <el-select
            v-model="searchForm.projNo"
            placeholder="请选择"
            style="width: 200px"
            clearable
            filterable
          >
            <el-option
              v-for="item in projectOptions"
              :key="item.code"
              :label="item.code"
              :value="item.code"
            />
            <template #prefix>
              <span>{{ searchForm.projNo }}</span>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="申请状态">
          <el-select v-model="searchForm.maStatus" placeholder="请选择" style="width: 150px" clearable>
            <el-option label="编制" value="01" />
            <el-option label="审批" value="02" />
            <el-option label="设计出图" value="04" />
            <el-option label="设计出图审批" value="05" />
            <el-option label="完成" value="03" />
            <el-option label="退回" value="00" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请日期">
          <el-date-picker
            v-model="searchForm.dateFrom"
            type="date"
            placeholder="开始日期"
            style="width: 140px"
            format="YYYY-MM-DD"
          />
          <span style="margin: 0 8px">至</span>
          <el-date-picker
            v-model="searchForm.dateTo"
            type="date"
            placeholder="结束日期"
            style="width: 140px"
            format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="项目名称/申请单号">
          <el-input v-model="searchForm.componentsName" placeholder="请输入" style="width: 200px" clearable />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮栏 -->
    <el-card class="action-card">
      <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
      <el-button type="success" :icon="Plus" @click="handleAdd">新增</el-button>
      <el-button type="danger" :icon="Delete" @click="handleDelete">删除</el-button>
      <el-button type="warning" :icon="RefreshLeft" @click="handleRetract">撤回</el-button>
      <el-button type="info" :icon="Printer" @click="handlePrint">打印</el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
        @current-change="handleRowChange"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="billNo" label="申请单号" width="120" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleShowDetail(row)">{{ row.billNo }}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="componentsName" label="项目名称" width="200" show-overflow-tooltip />
        <el-table-column prop="projNo" label="工程号" width="100" align="center" />
        <el-table-column prop="deptDesc" label="申请部门" width="100" align="center" />
        <el-table-column prop="appUser" label="申请人" width="100" align="center" />
        <el-table-column prop="divDesc" label="工装类别" width="100" align="center" />
        <el-table-column prop="finalNumberNo" label="最终审核数" width="100" align="center" />
        <el-table-column prop="maStatusDesc" label="申请状态" width="100" align="center" />
        <el-table-column label="流程跟踪" width="100" align="center">
          <template #default="{ row }">
            <el-button
              v-if="row.maProcessId"
              link
              type="primary"
              @click="handleShowBpm(row.maProcessId)"
            >
              流程追踪
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="120" align="center">
          <template #default="{ row }">
            {{ formatDate(row.createDate) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[50, 100, 200]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增项目对话框 -->
    <el-dialog
      v-model="createDialogVisible"
      title="新增项目"
      width="600px"
      @close="handleCancelCreate"
    >
      <el-form :model="createForm" label-width="100px">
        <el-form-item label="项目名称" required>
          <el-input v-model="createForm.programName" placeholder="请输入项目名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleCancelCreate">取消</el-button>
        <el-button type="primary" @click="handleCreate">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search, Plus, Delete, RefreshLeft, Printer } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

// 搜索表单
const searchForm = reactive({
  companyNo: '',
  deptNo: '',
  projNo: '',
  maStatus: '',
  dateFrom: '',
  dateTo: '',
  componentsName: ''
})

// 表格数据
const tableData = ref<any[]>([])
const tableLoading = ref(false)
const currentRow = ref<any>(null)

// 下拉选项
const companyOptions = ref<any[]>([])
const departmentOptions = ref<any[]>([])
const projectOptions = ref<any[]>([])

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 50,
  total: 0
})

// 新增对话框
const createDialogVisible = ref(false)
const createForm = reactive({
  programName: ''
})

/** 格式化日期 */
const formatDate = (date: string | null) => {
  if (!date) return ''
  return date.substring(0, 10)
}

/** 获取公司列表 */
const loadCompanies = async () => {
  try {
    const res = await request.get('/comp/getComps')
    companyOptions.value = res.data || []
    if (companyOptions.value.length > 0) {
      searchForm.companyNo = companyOptions.value[0].code
      await loadDepartments()
      await loadProjects()
    }
  } catch (error) {
    ElMessage.error('加载公司列表失败')
  }
}

/** 获取部门列表 */
const loadDepartments = async () => {
  if (!searchForm.companyNo) return
  try {
    const res = await request.get('/comp/getDeptCombobox', {
      params: { superOrgnCd: searchForm.companyNo }
    })
    departmentOptions.value = res.data || []
  } catch (error) {
    ElMessage.error('加载部门列表失败')
  }
}

/** 获取工程号列表 */
const loadProjects = async () => {
  try {
    const res = await request.get('/projectManager/qryAllProjNo')
    projectOptions.value = res.data || []
  } catch (error) {
    ElMessage.error('加载工程号列表失败')
  }
}

/** 公司变化 */
const handleCompanyChange = () => {
  searchForm.deptNo = ''
  loadDepartments()
}

/** 查询数据 */
const handleSearch = async () => {
  if (!searchForm.companyNo) {
    ElMessage.warning('请选择公司主体')
    return
  }

  tableLoading.value = true
  try {
    const param = {
      companyNo: searchForm.companyNo,
      dateFrom: searchForm.dateFrom ? formatDateForApi(searchForm.dateFrom) : '',
      dateTo: searchForm.dateTo ? formatDateForApi(searchForm.dateTo) : '',
      deptNo: searchForm.deptNo,
      projNo: searchForm.projNo,
      maStatus: searchForm.maStatus,
      componentsName: searchForm.componentsName,
      page: pagination.currentPage,
      size: pagination.pageSize
    }
    const res = await request.get('/components/getTComponentsData', { params: param })
    tableData.value = res.data?.list || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    ElMessage.error('查询失败')
  } finally {
    tableLoading.value = false
  }
}

/** 格式化日期为 API 格式 */
const formatDateForApi = (date: any) => {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}${month}${day}`
}

/** 新增 */
const handleAdd = () => {
  createForm.programName = ''
  createDialogVisible.value = true
}

/** 创建项目 */
const handleCreate = async () => {
  if (!createForm.programName) {
    ElMessage.warning('必须填写项目名称')
    return
  }

  try {
    const res = await request.post('/components/createBase', {
      programName: createForm.programName
    })
    if (res.data?.guid) {
      createDialogVisible.value = false
      ElMessage.success('创建成功')
      // 打开详情窗口（这里可以跳转到详情页）
      handleShowDetail(res.data)
    }
  } catch (error) {
    ElMessage.error('创建失败')
  }
}

/** 取消创建 */
const handleCancelCreate = () => {
  createDialogVisible.value = false
  createForm.programName = ''
}

/** 删除 */
const handleDelete = async () => {
  if (!currentRow.value) {
    ElMessage.warning('请选择一条数据进行删除')
    return
  }

  if (currentRow.value.appUser !== currentRow.value.empNo) {
    ElMessage.warning('不可删除他人项目')
    return
  }

  if (currentRow.value.maStatus !== '01') {
    ElMessage.warning('必须是"编制"才可以删除')
    return
  }

  try {
    await ElMessageBox.confirm('确定要删除该记录吗？', '提示', {
      type: 'warning'
    })
    const res = await request.post('/components/delApp', {
      guid: currentRow.value.guid
    })
    if (res.data?.flag === 1) {
      ElMessage.success('删除成功')
      handleSearch()
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/** 撤回 */
const handleRetract = async () => {
  if (!currentRow.value) {
    ElMessage.warning('请选择一条数据进行退回')
    return
  }

  if (currentRow.value.appUser !== currentRow.value.empNo) {
    ElMessage.warning('不可撤回他人项目')
    return
  }

  if (['01', '00', '03'].includes(currentRow.value.maStatus)) {
    ElMessage.warning('必须是在流程中才可以退回')
    return
  }

  try {
    await ElMessageBox.confirm('确定要撤回该记录吗？', '提示', {
      type: 'warning'
    })
    const res = await request.post('/components/retractApp', {
      guid: currentRow.value.guid
    })
    if (res.data?.flag === 1) {
      ElMessage.success('退回成功')
      handleSearch()
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('退回失败')
    }
  }
}

/** 打印 */
const handlePrint = () => {
  if (!currentRow.value) {
    ElMessage.warning('请选择一条数据进行打印')
    return
  }

  if (currentRow.value.maStatus !== '03') {
    ElMessage.warning('只有通过审批才能生成pdf')
    return
  }

  window.open(`/api/components/printPdf?billNo=${currentRow.value.billNo}`)
}

/** 查看详情 */
const handleShowDetail = (row: any) => {
  // TODO: 跳转到详情页面
  ElMessage.info('查看详情功能待实现')
}

/** 查看流程 */
const handleShowBpm = (pid: string) => {
  // TODO: 打开流程追踪窗口
  ElMessage.info('流程追踪功能待实现')
}

/** 表格行选择变化 */
const handleRowChange = (row: any) => {
  currentRow.value = row
}

/** 分页大小变化 */
const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  handleSearch()
}

/** 当前页变化 */
const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  handleSearch()
}

/** 初始化 */
onMounted(() => {
  loadCompanies()
  // 设置默认日期范围为最近一个月
  const now = new Date()
  const lastMonth = new Date(now.getTime())
  lastMonth.setMonth(now.getMonth() - 1)
  searchForm.dateFrom = lastMonth
  searchForm.dateTo = now
})
</script>

<style scoped>
.page4-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 16px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.action-card {
  margin-bottom: 16px;
}

.action-card .el-button {
  margin-right: 10px;
}

.table-card {
  margin-bottom: 16px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
