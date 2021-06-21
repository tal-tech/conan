<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="Schema规则" prop="ruleJson">
        <el-input
          v-model="queryParams.ruleJson"
          placeholder="请输入Schema规则"
          clearable
          type="textarea"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门Id" prop="sysDeptId">
        <treeselect
          class="w10"
          :normalizer="normalizer"
          v-model="queryParams.sysDeptId"
          :options="deptOptions"
          :show-count="true"
          placeholder="请选择所属部门id"
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
          v-hasPermi="['admin:deptRule:add']"
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
          v-hasPermi="['admin:deptRule:edit']"
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
          v-hasPermi="['admin:deptRule:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin:deptRule:export']"
          >导出</el-button
        >
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :data="deptRuleList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Id" align="center" prop="id" />
      <el-table-column label="Schema规则" align="center" prop="ruleJson" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin:deptRule:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:deptRule:remove']"
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

    <!-- 添加或修改部门Schema规则配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-form-item label="部门Id" prop="sysDeptId">
          <treeselect
            v-model="form.sysDeptId"
            :options="deptOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="请选择归属部门"
          />
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
  listDeptRule,
  getDeptRule,
  delDeptRule,
  addDeptRule,
  updateDeptRule,
  exportDeptRule,
} from "@/api/admin/deptRule/index";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Json from "../../../views/conanApi/Edit-component/Json";

export default {
  name: "deptRule",
  components: { Treeselect, Json },
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
      // 部门Schema规则配置表格数据
      deptRuleList: [],
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: [],
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleJson: null,
        deptId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ruleJson: [{ required: true, message: "Schema规则不能为空", trigger: "blur" }],
        deptId: [{ required: true, message: "部门Id不能为空", trigger: "blur" }],
      },
      // 展示
      showType: 1,
    };
  },
  created() {
    this.getList();
    this.getTreeselect();
  },
  methods: {
    /** 查询部门Schema规则配置列表 */
    getList() {
      this.loading = true;
      listDeptRule(this.queryParams).then((response) => {
        if (response.data.data.length != 0 && response.code == 200) {
          this.deptRuleList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        } else {
          this.deptRuleList = [];
          this.loading = false;
        }
      }) .catch(err => {
          this.loading = false;
        });
    },
    // 页面跳转新标签
    jumpUrl(url) {
      window.open(url, "_blank");
    },
    /** 转换接口管理数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      if (node.children == null || node.children == "null") {
        delete node.children;
      }
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
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
        id: null,
        ruleJson: null,
        deptId: null,
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
      this.title = "添加部门Schema规则配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getDeptRule(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改部门Schema规则配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateDeptRule(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDeptRule(this.form).then((response) => {
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
      this.$confirm(
        '是否确认删除部门Schema规则配置编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delDeptRule(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有部门Schema规则配置数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportDeptRule(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
