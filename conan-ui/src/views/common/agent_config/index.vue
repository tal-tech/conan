<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width=""
    >
      <el-form-item label="kafka的groupId" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入kafka的groupId"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="机器状态" prop="isEnable">
        <el-select
          v-model="queryParams.isEnable"
          placeholder="请选择机器状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in isEnableOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="运行状态" prop="isBusy">
        <el-select
          v-model="queryParams.isBusy"
          placeholder="请选择运行状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in isBusyOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['common:agent_config:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['common:agent_config:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['common:agent_config:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['common:agent_config:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :data="agent_configList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="kafka的groupId" align="center" prop="name" />
      <el-table-column
        label="机器状态"
        align="center"
        prop="isEnable"
        :formatter="isEnableFormat"
      />
      <el-table-column
        label="运行状态"
        align="center"
        prop="isBusy"
        :formatter="isBusyFormat"
      />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['common:agent_config:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['common:agent_config:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改work机管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="groupId" prop="name">
          <el-input v-model="form.name" placeholder="请输入kafka的groupId" />
        </el-form-item>
        <el-form-item label="机器状态" prop="isEnable">
          <el-select v-model="form.isEnable" placeholder="请选择机器状态" class="w100">
            <el-option
              v-for="dict in isEnableOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="运行状态" prop="isBusy">
          <el-select v-model="form.isBusy" placeholder="请选择运行状态" class="w100">
            <el-option
              v-for="dict in isBusyOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAgent_config,
  getAgent_config,
  delAgent_config,
  addAgent_config,
  updateAgent_config,
  exportAgent_config,
} from "@/api/common/agent_config";

export default {
  name: "AgentConfig",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // work机管理表格数据
      agent_configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 机器状态，0-禁用，1-启用字典
      isEnableOptions: [],
      // 运行状态字典
      isBusyOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        isEnable: null,
        isBusy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [{ required: true, message: "kafka的groupId不能为空", trigger: "blur" }],
        isEnable: [
          {
            required: true,
            message: "机器状态，0-禁用，1-启用不能为空",
            trigger: "change",
          },
        ],
        isBusy: [{ required: true, message: "运行状态不能为空", trigger: "change" }],
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("os_agent_work_status").then((response) => {
      this.isEnableOptions = response.data;
    });
    this.getDicts("os_agent_work_run_status").then((response) => {
      this.isBusyOptions = response.data;
    });
  },
  methods: {
    /** 查询work机管理列表 */
    getList() {
      this.loading = true;
      listAgent_config(this.queryParams).then((response) => {
        this.agent_configList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      }) .catch(err => {
          this.loading = false;
        });
    },
    // 机器状态，0-禁用，1-启用字典翻译
    isEnableFormat(row, column) {
      return this.selectDictLabel(this.isEnableOptions, row.isEnable);
    },
    // 运行状态字典翻译
    isBusyFormat(row, column) {
      return this.selectDictLabel(this.isBusyOptions, row.isBusy);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        isEnable: null,
        isBusy: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加work机管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getAgent_config(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改work机管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateAgent_config(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAgent_config(this.form).then((response) => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除work机管理编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delAgent_config(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有work机管理数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportAgent_config(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
