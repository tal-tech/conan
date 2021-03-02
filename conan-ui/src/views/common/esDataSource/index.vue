<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width=""
    >
      <el-form-item label="ES名称" prop="esName">
        <el-input
          v-model="queryParams.esName"
          placeholder="请输入ES名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="ES Bean名称(全英文)" prop="esBeanName">
        <el-input
          v-model="queryParams.esBeanName"
          placeholder="请输入ES Bean名称(全英文)"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="ip" prop="esIp">
        <el-input
          v-model="queryParams.esIp"
          placeholder="请输入ip"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="port" prop="esPort">
        <el-input
          v-model="queryParams.esPort"
          placeholder="请输入port"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['common:esDataSource:add']"
          >新增</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['common:esDataSource:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['common:esDataSource:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['common:esDataSource:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :data="esDataSourceList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="sourceid" align="center" prop="esSourceId" />
      <el-table-column label="ES名称" align="center" prop="esName" />
      <el-table-column label="ES Bean名称(全英文)" align="center" prop="esBeanName" />
      <el-table-column label="ip" align="center" prop="esIp" />
      <el-table-column label="port" align="center" prop="esPort" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['common:esDataSource:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['common:esDataSource:remove']"
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

    <!-- 添加或修改ES 数据源配置，域名需要绑定ES数据源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="30%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="ES名称" prop="esName">
          <el-input v-model="form.esName" placeholder="请输入ES名称" />
        </el-form-item>
        <el-form-item label="ES Bean名称" prop="esBeanName">
          <el-input v-model="form.esBeanName" placeholder="请输入ES Bean名称(全英文)" />
        </el-form-item>
        <el-form-item label="ip" prop="esIp">
          <el-input v-model="form.esIp" placeholder="请输入ip" />
        </el-form-item>
        <el-form-item label="port" prop="esPort">
          <el-input v-model="form.esPort" placeholder="请输入port" />
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
  listEsDataSource,
  getEsDataSource,
  delEsDataSource,
  addEsDataSource,
  updateEsDataSource,
  exportEsDataSource,
} from "@/api/common/esDataSource";

export default {
  name: "EsDataSource",
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
      // ES 数据源配置，域名需要绑定ES数据源表格数据
      esDataSourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        esName: null,
        esBeanName: null,
        esIp: null,
        esPort: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        esIp: [{ required: true, message: "ip不能为空", trigger: "blur" }],
        esPort: [{ required: true, message: "port不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询ES 数据源配置，域名需要绑定ES数据源列表 */
    getList() {
      this.loading = true;
      listEsDataSource(this.queryParams).then((response) => {
        this.esDataSourceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      }) .catch(err => {
          this.loading = false;
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        esSourceId: null,
        esName: null,
        esBeanName: null,
        esIp: null,
        esPort: null,
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
      this.ids = selection.map((item) => item.esSourceId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加ES 数据源配置，域名需要绑定ES数据源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const esSourceId = row.esSourceId || this.ids;
      getEsDataSource(esSourceId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改ES 数据源配置，域名需要绑定ES数据源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.esSourceId != null) {
            updateEsDataSource(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEsDataSource(this.form).then((response) => {
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
      const esSourceIds = row.esSourceId || this.ids;
      this.$confirm(
        '是否确认删除ES 数据源配置，域名需要绑定ES数据源编号为"' +
          esSourceIds +
          '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delEsDataSource(esSourceIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm(
        "是否确认导出所有ES 数据源配置，域名需要绑定ES数据源数据项?",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return exportEsDataSource(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
