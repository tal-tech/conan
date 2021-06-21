<template>
  <div class="app-container">
    <!-- <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="api id" prop="apiId">
        <el-input v-model="queryParams.apiId" placeholder="请输入api_id" />
      </el-form-item>
      <el-form-item label="接口结构规则" prop="ruleJson">
        <el-input
          v-model="queryParams.ruleJson"
          placeholder="请输入接口结构规则"
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
    </el-form> -->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['admin/api:rule:add']"
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
          v-hasPermi="['admin/api:rule:edit']"
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
          v-hasPermi="['admin/api:rule:remove']"
          >删除</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin/api:rule:export']"
          >导出</el-button
        >
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :data="ruleList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="api_id" align="center" prop="apiId" />
      <el-table-column label="接口结构规则" align="center" prop="ruleJson" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin/api:rule:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin/api:rule:remove']"
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

    <!-- 添加或修改接口Schema规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="68px">
        <el-form-item label="api_id" prop="apiId" >
          <el-input v-model="form.apiId" placeholder="请输入api_id" disabled />
        </el-form-item>
        <el-form-item style="width: 100%" label="规则" prop="ruleJson">
          <el-radio-group v-model="showType" size="mini" class="MR">
            <el-radio-button label="0">JSON格式</el-radio-button>
            <el-radio-button label="1">编辑</el-radio-button>
          </el-radio-group>
          <el-button
            type="success"
            size="mini"
            @click="
              jumpUrl(
                'https://www.liquid-technologies.com/online-json-to-schema-converter'
              )
            "
          >
            Schema 模板生成
          </el-button>
          <el-button
            style="margin: 0"
            type="success"
            size="mini"
            @click="jumpUrl('https://www.jsonschemavalidator.net/')"
          >
            模板编辑及验证
          </el-button>
          <el-input
            v-show="showType == 1"
            type="textarea"
            :autosize="{ minRows: 10, maxRows: 10 }"
            v-model.trim="form.ruleJson"
          ></el-input>
          <div v-show="showType == 0" class="json">
            <Json :jsonString="form.ruleJson"></Json>
          </div>
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
  listRule,
  getRule,
  delRule,
  addRule,
  updateRule,
  exportRule,
} from "@/api/conanConfig/rule";
import Json from "../conanApi/Edit-component/Json.vue";

export default {
  name: "ApiSchemaRule",
  components: { Json },
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
      // 接口Schema规则表格数据
      ruleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        apiId: this.$route.query.apiId,
        ruleJson: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        apiId: [{ required: true, message: "api_id不能为空", trigger: "blur" }],
        ruleJson: [{ required: true, message: "接口结构规则不能为空", trigger: "blur" }],
      },
      // 展示
      showType: 1,
    };
  },
  created() {
    console.log(this.$route.query, "sssss");
    this.getList();
  },
  methods: {
    // 页面跳转新标签
    jumpUrl(url) {
      window.open(url, "_blank");
    },
    /** 查询接口Schema规则列表 */
    getList() {
      this.loading = true;
      listRule(this.$route.query.apiId).then((response) => {
        if (response.code == 200 && response.data != null) {
          this.ruleList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        } else {
          this.ruleList = [];
          this.loading = false;
        }
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
        apiId: this.$route.query.apiId,
        ruleJson: null,
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
      this.form.apiId = this.$route.query.apiId;
      this.title = "添加接口Schema规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getRule(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改接口Schema规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateRule(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRule(this.form).then((response) => {
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
      this.$confirm('是否确认删除接口Schema规则编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delRule(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有接口Schema规则数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportRule(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
<style lang="scss"></style>
