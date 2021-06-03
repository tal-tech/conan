<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width=""
    >
      <el-form-item label="ES对应日志索引名称" prop="indexName">
        <el-input
          v-model="queryParams.indexName"
          placeholder="请输入ES对应日志索引名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模版关联域名" prop="domainId">
        <el-input
          v-model="queryParams.domainId"
          placeholder="请输入模版关联域名"
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
          v-hasPermi="['admin:domain:add']"
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
          v-hasPermi="['admin:domain:edit']"
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
          v-hasPermi="['admin:domain:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin:domain:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      class="dataList-Table big"
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :header-cell-style="{ background: '#F3F4F7', color: '#555' }"
      fit
      tooltip-effect="light"
      :data="esConditionSettingList"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column type="expand" align="center">
        <template slot-scope="props">
          <el-form class="task-table-expand" inline label-position="left">
            <el-form-item label="ES中_source内接口对应的key名称">
              <span>{{ props.row.api }}</span>
            </el-form-item>
            <el-form-item label="ES中_source内请求方法对应的key名称">
              <span>{{ props.row.method }}</span>
            </el-form-item>
            <el-form-item label="ES中_source内请求体对应的key名称">
              <span>{{ props.row.requestBody }}</span>
            </el-form-item>
            <el-form-item label="ES中_source内域名对应的key名称">
              <span>{{ props.row.domain }}</span>
            </el-form-item>
            <el-form-item label="ES中_source内请求体对应的key名称">
              <span>{{ props.row.header }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="Header的正则表达式" align="center" prop="esSettingId" />
      <el-table-column label="ES对应日志索引名称" align="center" prop="indexName" />
      <!-- <el-table-column label="模版关联域名id" align="center" prop="domainId" /> -->
      <el-table-column label="接口正则表达式" align="center" prop="apiRegex" />
      <el-table-column label="域名正则表达式" align="center" prop="domainRegex" />
      <el-table-column label="请求方法的正则表达式" align="center" prop="methodRegex" />
      <el-table-column label="请求体正则表达式" align="center" prop="requestBodyRegex" />
      <el-table-column label="Header的正则表达式" align="center" prop="headerRegex" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin:domain:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:domain:remove']"
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

    <!-- 添加或修改esConditionSetting域名下ES 查询条件配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
      <!-- :rules="rules" -->
      <el-form ref="form" :model="form"  label-width="220px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="日志索引名称" prop="indexName">
              <el-input v-model="form.indexName" placeholder="请输入ES对应日志索引名称" />
            </el-form-item>
            <el-form-item label="ES中_source内接口名称 " prop="api">
              <el-input  v-model="form.api"  placeholder="请输入ES中_source内接口对应的key名称" />
            </el-form-item>
            <el-form-item label="ES中_source内域名名称 " prop="domain">
              <el-input
                v-model="form.domain"
                placeholder="请输入ES中_source内域名对应的key名称 "
              />
            </el-form-item>
            <el-form-item label="ES中_source内请求方法名称" prop="method">
              <el-input
                v-model="form.method"
                placeholder="请输入ES中_source内请求方法对应的key名称"
              />
              <!-- <el-select
                v-model="form.method"
                clearable
                placeholder="http方法"
                class="w100"
              >
                <el-option
                  v-for="item in methodOptions"
                  :key="item.dictCode"
                  :label="item.dictLabel"
                  :value="item.dictLabel"
                >
                </el-option>
              </el-select> -->
            </el-form-item>
            <el-form-item label="ES中_source内请求Body名称" prop="requestBody">
              <el-input
                v-model="form.requestBody"
                placeholder="请输入ES中_source内请求体对应的key名称"
              />
            </el-form-item>
            <el-form-item label="ES中_source内请求体名称" prop="header">
              <el-input
                v-model="form.header"
                placeholder="请输入ES中_source内请求体对应的key名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模版关联域名" prop="domainId">
              <el-input v-model="form.domainId" placeholder="请输入模版关联域名" disabled/>
            </el-form-item>
            <el-form-item label="接口正则表达式" prop="apiRegex">
              <el-input v-model="form.apiRegex" placeholder="请输入接口正则表达式" />
            </el-form-item>
            <el-form-item label="域名正则表达式" prop="domainRegex">
              <el-input v-model="form.domainRegex" placeholder="请输入域名正则表达式" />
            </el-form-item>
            <el-form-item label="请求方法的正则表达式" prop="methodRegex">
              <el-input
                v-model="form.methodRegex"
                placeholder="请输入请求方法的正则表达式"
              />
            </el-form-item>
            <el-form-item label="请求体正则表达式" prop="requestBodyRegex">
              <el-input
                v-model="form.requestBodyRegex"
                placeholder="请输入请求体正则表达式"
              />
            </el-form-item>
            <el-form-item label="Header的正则表达式" prop="headerRegex">
              <el-input
                v-model="form.headerRegex"
                placeholder="请输入Header的正则表达式"
              />
            </el-form-item>
          </el-col>
        </el-row>
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
  listEsConditionSetting,
  getEsConditionSetting,
  delEsConditionSetting,
  addEsConditionSetting,
  updateEsConditionSetting,
  exportEsConditionSetting,
} from "@/api/common/esConditionSetting";

export default {
  name: "EsConditionSetting",
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
      // esConditionSetting域名下ES 查询条件配置表格数据
      esConditionSettingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexName: null,
        domainId: this.$route.query.id,
        api: null,
        apiRegex: null,
        domain: null,
        domainRegex: null,
        method: null,
        methodRegex: null,
        requestBody: null,
        requestBodyRegex: null,
        header: null,
        headerRegex: null,
      },
      // 表单参数
      form: {},
      methodOptions: [],
      // 表单校验
      rules: {
        indexName: [
          { required: true, message: "ES对应日志索引名称不能为空", trigger: "blur" },
        ],
        domainId: [{ required: true, message: "模版关联域名不能为空", trigger: "blur" }],
        domain: [
          {
            required: true,
            message: "ES中_source内域名对应的key名称 不能为空",
            trigger: "blur",
          },
        ],
        domainRegex: [
          { required: true, message: "域名正则表达式不能为空", trigger: "blur" },
        ],
        method: [
          {
            required: true,
            message: "ES中_source内请求方法对应的key名称不能为空",
            trigger: "blur",
          },
        ],
        methodRegex: [
          { required: true, message: "请求方法的正则表达式不能为空", trigger: "blur" },
        ],
        requestBody: [
          {
            required: true,
            message: "ES中_source内请求体对应的key名称不能为空",
            trigger: "blur",
          },
        ],
        requestBodyRegex: [
          { required: true, message: "请求体正则表达式不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
      this.getDicts("bss_method_status").then(response => {
        this.methodOptions = response.data;
      });
  },
  methods: {
    /** 查询esConditionSetting域名下ES 查询条件配置列表 */
    getList() {
      this.loading = true;
      listEsConditionSetting(this.queryParams).then((response) => {
        this.esConditionSettingList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      })
       .catch(err => {
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
        esSettingId: null,
        indexName: null,
        domainId:  this.$route.query.id,
        api: null,
        apiRegex: null,
        domain: null,
        domainRegex: null,
        method: null,
        methodRegex: null,
        requestBody: null,
        requestBodyRegex: null,
        header: null,
        headerRegex: null,
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
      this.ids = selection.map((item) => item.esSettingId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加esConditionSetting域名下ES 查询条件配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const esSettingId = row.esSettingId || this.ids;
      getEsConditionSetting(esSettingId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改esConditionSetting域名下ES 查询条件配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.esSettingId != null) {
            updateEsConditionSetting(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEsConditionSetting(this.form).then((response) => {
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
      const esSettingIds = row.esSettingId || this.ids;
      this.$confirm(
        '是否确认删除esConditionSetting域名下ES 查询条件配置编号为"' +
          esSettingIds +
          '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delEsConditionSetting(esSettingIds);
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
        "是否确认导出所有esConditionSetting域名下ES 查询条件配置数据项?",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return exportEsConditionSetting(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
<style lang="scss" scoped>
/deep/ .big .el-form-item__label {
  width: 260px;
}
</style>
