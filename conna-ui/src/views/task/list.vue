<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="部门Id" prop="sysDeptId">
        <treeselect
          class="w10"
          v-model="queryParams.sysDeptId"
          :options="deptOptions"
          :show-count="true"
          placeholder="请选择所属部门id"
        />
      </el-form-item>
      <el-form-item label="任务名称（关键词）" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入任务名"
          @keyup.enter.native="getTaskTable"
        />
      </el-form-item>
      <el-form-item label="任务类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择任务类型" class="w100">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="parseInt(dict.dictValue)"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="cyan"
          icon="el-icon-search"
          size="mini"
          @click="searchTask"
          v-hasPermi="['admin:task:query']"
          >搜索
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="dataList-Btn">
      <el-button
        @click="createTask"
        type="primary"
        size="mini"
        icon="el-icon-plus"
        v-hasPermi="['admin:task:add']"
        >新建任务</el-button
      >
    </div>
    <div class="dataList-Table">
      <el-table
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        :data="tableData"
        :header-cell-style="{ background: '#F3F4F7', color: '#555' }"
        fit
        @cell-dblclick="celldblclick"
        tooltip-effect="light"
      >
        <el-table-column type="expand" align="center">
          <template slot-scope="props">
            <el-form class="task-table-expand" inline label-position="left">
              <el-form-item label="更新人">
                <span>{{ props.row.updateBy }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.updateTime }}</span>
              </el-form-item>
              <el-form-item label="负责人">
                <span>{{ props.row.domainName }}</span>
              </el-form-item>
              <el-form-item label="部门">
                <span>{{ props.row.deptName }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="#" type="index" align="center"></el-table-column>
        <!-- <el-table-column label="部门" align="center" prop="deptName"></el-table-column> -->
        <el-table-column
          label="名称"
          show-overflow-tooltip
          prop="name"
          align="center"
        ></el-table-column>
        <el-table-column label="类型" prop="type" align="center" :formatter="typeFormat">
        </el-table-column>
        <el-table-column
          label="状态"
          prop="status"
          align="center"
          :formatter="statusFormat"
        >
          <template slot-scope="scope">
            <span
              ><i
                :class="[
                  scope.row.status === 2
                    ? 'iconfont icon-luzhi text-navy'
                    : scope.row.status === 5
                    ? 'iconfont icon-huifang text-navy'
                    : scope.row.status === 8
                    ? 'iconfont icon-ziyuan1282 text-navy'
                    : scope.row.status === 1
                    ? 'iconfont icon-luzhi text-warning'
                    : scope.row.status === 4
                    ? 'iconfont icon-huifang text-warning'
                    : scope.row.status === 7
                    ? 'iconfont icon-ziyuan1282 text-warning'
                    : scope.row.status === 3
                    ? 'iconfont icon-luzhi text-danger'
                    : scope.row.status === 6
                    ? 'iconfont icon-huifang text-danger'
                    : scope.row.status === 9
                    ? 'iconfont icon-ziyuan1282 text-danger'
                    : 'iconfont icon-bofang ',
                ]"
              >
                {{ scope.row.status | taskFormat }}</i
              >
            </span>
          </template>
        </el-table-column>
        <el-table-column label="是否演示任务" prop="isDemo" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.isDemo == 1 ? 'success' : 'danger'"
              effect="plain"
              :formatter="isDemoFormat"
            >
              {{ scope.row.isDemo | valueFormat }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="管理员锁定任务"
          prop="isForceLock"
          align="center"
          :formatter="isForceLockFormat"
        >
          <template slot-scope="scope">
            {{ scope.row.isForceLock | isForceLockFormat }}
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          prop="createTime"
          show-overflow-tooltip
          align="center"
        ></el-table-column>
        <el-table-column align="center" width="150" label="执行">
          <template slot-scope="scope">
            <el-tooltip class="item" effect="light" content="编辑" placement="top-start">
              <el-button
                @click="executeTheTask(scope.$index, scope.row)"
                class="el-button el-button--lvse el-button--mini"
                circle
                icon="el-icon-edit"
                size="mini"
              ></el-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              effect="light"
              content="执行录制"
              placement="top-start"
            >
              <el-button
                @click="handelTranscribe(scope.row)"
                type="warning"
                circle
                icon="el-icon-video-camera"
                size="mini"
              ></el-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              effect="light"
              content="执行详情"
              placement="top-start"
            >
              <el-button
                @click="handleDetail(scope.row)"
                type="primary"
                circle
                icon="el-icon-document"
                size="mini"
              ></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              v-hasPermi="['admin:task:edit']"
              @click="editTask(scope.$index, scope.row)"
              >修改</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              v-hasPermi="['admin:task:remove']"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getTaskTable"
      />
    </div>
    <!-- 添加或修改任务管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="35%" append-to-body>
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="110px"
        :label-position="labelPosition"
      >
        <el-form-item label="任务ID" prop="name" v-if="form.taskId">
          <el-input v-model="form.taskId" disabled />
        </el-form-item>
        <el-form-item label="任务类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择任务类型" class="w100">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="任务名" prop="name">
          <el-input v-model="form.name" placeholder="请输入任务名" />
        </el-form-item>
        <el-form-item label="所属部门id" prop="sysDeptId">
          <treeselect
            v-model="form.sysDeptId"
            :options="deptOptions"
            :show-count="true"
            placeholder="请选择归属部门"
          />
        </el-form-item>
        <!-- <el-form-item label="执行状态">
          <el-select v-model="form.status" placeholder="请选择执行状态" class="w100">
            <el-option
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item> -->

        <el-form-item label="是否演示任务" prop="isDemo">
          <el-select v-model="form.isDemo" placeholder="请选择是否演示任务" class="w100">
            <el-option
              v-for="dict in isDemoOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import pagination from "../../components/Pagination";
import { listTask, delTask, addTask, updateTask, exportTask } from "../../api/task/index";
import { getTaskExecutionByRecord } from "@/api/execution/printRecordLog";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "TaskList",
  components: {
    pagination,
    Treeselect,
  },
  data() {
    return {
      labelPosition: "left",
      taskQuery: {}, //表单内容
      userList: [], //创建人列表
      loading: false,
      departmentList: [], //部门下拉框列表
      productLineList: [], //产品线列表
      total: 0, //总页数
      tableData: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
        name: null,
        status: null,
        sysDeptId: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        isDemo: null,
        isForceLock: null,
      },
      open: false,
      title: "",
      form: {},
      rules: {
        type: [
          {
            required: true,
            message: "任务类型:0 普通任务，1带场次任务不能为空",
            trigger: "change",
          },
        ],
        name: [{ required: true, message: "任务名不能为空", trigger: "blur" }],
        sysDeptId: [{ required: true, message: "所属部门id不能为空", trigger: "blur" }],
      },
      typeOptions: [],
      statusOptions: [],
      isDemoOptions: [],
      isForceLockOptions: [],
      deptOptions: [],
    };
  },
  created() {
    this.initData();
  },
  computed: {},
  mounted() {},
  methods: {
    initData() {
      this.getTaskTable();
      this.getTreeselect();
      this.getDicts("bss_task_type").then((response) => {
        this.typeOptions = response.data;
      });
      this.getDicts("bss_task_status").then((response) => {
        this.statusOptions = response.data;
      });
      this.getDicts("bss_task_is_demo").then((response) => {
        console.log(response, "lll");
        this.isDemoOptions = response.data;
      });
      this.getDicts("bss_task_is_force_lock").then((response) => {
        this.isForceLockOptions = response.data;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.getTaskTable();
    },
    getTaskTable() {
      this.loading = true;
      console.log(this.queryParams, "this.queryParams");
      listTask(this.queryParams)
        .then((res) => {
          if (res.code == 200 && JSON.stringify(res.data) !== "{}") {
            this.tableData = res.data.data;
            this.total = res.data.total;
            setTimeout(() => {
              this.loading = false;
            }, 500);
          } else {
            this.loading = false;
            return false;
          }
        })
        .catch((err) => {
          console.log(err, "err");
          this.loading = false;
        });
    },
    // 状态返回值
    // 字典状态字典翻译
    typeFormat(row) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 锁定任务
    isForceLockFormat(row) {
      return this.selectDictLabel(this.isForceLockOptions, row.isForceLock);
    },
    // 状态值字典翻译
    statusFormat(row) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 是否为任务
    isDemoFormat(row) {
      return this.selectDictLabel(this.isDemoOptions, row.isDemo);
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
      });
    },
    // 查询任务
    searchTask() {
      this.queryParams.pageNum = 1;
      this.getTaskTable();
    },
    // 创建任务
    createTask() {
      this.reset();
      this.open = true;
      this.getTreeselect();
      this.title = "添加任务管理";
    },
    // 编辑任务
    editTask(index, row) {
      this.reset();
      this.form = row;
      this.open = true;
      this.getTreeselect();
      this.title = "修改任务管理";
    },
    // 表单重置
    reset() {
      this.form = {
        extend: null,
        taskId: null,
        isDemo: null,
        isForceLock: 0,
        name: null,
        status: 0,
        sysDeptId: null,
        type: null,
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.taskId != null) {
            updateTask(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getTaskTable();
            });
          } else {
            addTask(this.form).then((response) => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getTaskTable();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.taskId;
      this.$confirm('是否确认删除任务管理名称为"' + row.name + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delTask(ids);
        })
        .then(() => {
          this.getTaskTable();
          this.msgSuccess("删除成功");
        });
    },
    // 执行编辑
    executeTheTask(index, row) {
      this.$router.push({
        name: "ExecuteTheTaskDetails",
        query: { taskId: row.taskId, taskName: row.name },
      });
    },
    // 执行录制
    handelTranscribe(row) {
      console.log(row, "row");
      getTaskExecutionByRecord(row.taskId)
        .then((res) => {
          if (res.code == 200) {
            this.$router.push({
              name: "printRecordLog",
              query: {
                record_id: res.data.data.data.recordId,
                task_execution_id: res.data.data.data.taskExecutionId,
                type: "record",
              },
            });
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 执行详情
    handleDetail(row) {
      this.$router.push({
        name: "TaskViewDetails",
        query: { taskId: row.taskId, name: row.name },
      });
    },
    // 复制表格数据
    celldblclick(row, column, cell, event) {
      console.log(row,'row');
      let _this = this;
      this.$copyText(row[column.property]).then(
        function (e) {
           this.msgSuccess("复制成功");
        },
        function (e) {
          _this.onError();
          this.msgError("复制失败")
        }
      );
    },
  },
};
</script>

<style lang="less" scoped></style>
