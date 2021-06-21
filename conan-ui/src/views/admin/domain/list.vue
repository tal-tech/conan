<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="域名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入域名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上线状态" prop="online">
        <el-select v-model="queryParams.online" clearable placeholder="是否已上线">
          <el-option
            v-for="item in onlineOptions"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="鉴权" prop="isAuth">
        <el-select v-model="queryParams.isAuth" clearable placeholder="是否需要鉴权">
          <el-option
            v-for="item in isAuthOptions"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
          </el-option>
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
          v-hasPermi="['admin:domain:add']"
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
          v-hasPermi="['admin:domain:edit']"
          >修改</el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['admin:domain:remove']"
          >删除</el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin:domain:export']"
          >导出</el-button
        >
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :data="domainList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" />
      <el-table-column label="域名" align="center" prop="name">
        <template slot-scope="scope">
          <router-link
            :to="{ name: 'EsConditionSetting', query: { id: scope.row.id } }"
            class="link-type"
          >
            <el-tooltip
              class="item"
              effect="light"
              content="点击可查看ES数据源相关配置"
              placement="top"
            >
              <span>{{ scope.row.name }}</span>
            </el-tooltip>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="是否已上线" align="center" prop="online">
        <template slot-scope="scope">
          <el-tag effect="plain" :type="scope.row.online === 1 ? 'success' : 'danger'">
            {{ scope.row.online | valueFormat }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否需要鉴权" align="center" prop="isAuth">
        <template slot-scope="scope">
          <el-tag effect="plain" :type="scope.row.isAuth === 1 ? 'success' : 'danger'">
            {{ scope.row.isAuth | valueFormat }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="ES数据源相关配置" align="center" prop="">
        <template slot-scope="scope">
           <router-link
            :to="{ name: 'EsConditionSetting', query: { id: scope.row.id } }"
            class="link-type"
          >
            <el-tooltip
              class="item"
              effect="light"
              content="点击可查看ES数据源相关配置"
              placement="top"
            >
            <el-button icon="el-icon-s-tools" type="success" size="mini" round></el-button>
            </el-tooltip>
          </router-link>
          <router-link
            :to="{ name: 'DomainAuth', query: { id: scope.row.id } }"
            class="link-type"
            style="margin-left:10px"
          >
            <el-tooltip
              class="item"
              effect="light"
              content="点击可查看鉴权配置"
              placement="top"
            >
              <el-button icon="el-icon-setting" type="warning"  size="mini" round></el-button>
            </el-tooltip>
          </router-link>
        </template>
      </el-table-column>
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
            q
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

    <!-- 添加或修改域名信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="域名" prop="name">
          <el-input v-model="form.name" placeholder="请输入域名" />
        </el-form-item>
        <el-form-item label="数据源ID" prop="esSourceId">
          <el-select
            v-model="form.esSourceId"
            clearable
            placeholder="数据源ID"
            class="w100"
          >
            <el-option
              v-for="item in esSourceOptions"
              :key="item.esSourceId"
              :label="item.esName"
              :value="parseInt(item.esSourceId)"
            >
              <span style="float: left">{{ item.esName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{
                item.esSourceId
              }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门ID" prop="sysDeptId">
          <treeselect
            v-model="form.sysDeptId"
            :options="deptOptions"
            :show-count="true"
            placeholder="请选择归属部门"
          />
        </el-form-item>
        <el-form-item label="是否已上线" prop="online">
          <el-select
            v-model="form.online"
            clearable
            placeholder="是否已上线"
            class="w100"
          >
            <el-option
              v-for="item in onlineOptions"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否需要鉴权" prop="isAuth">
          <el-select
            v-model="form.isAuth"
            clearable
            placeholder="是否需要鉴权"
            class="w100"
          >
            <el-option
              v-for="item in isAuthOptions"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            >
            </el-option>
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
  listDomain,
  getDomain,
  delDomain,
  addDomain,
  updateDomain,
  exportDomain,
} from "@/api/admin/domain/list";
import { listEsDataSource } from "@/api/common/esDataSource";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "DoMainList",
  components: { Treeselect },
  data() {
    return {
      onlineOptions: [],
      esSourceOptions: [],
      isAuthOptions: [],
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
      // 域名信息表格数据
      domainList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        online: null,
        isAuth: null,
      },
      // 部门树选项
      deptOptions: undefined,
      // 表单参数
      form: {
        name: null,
        online: null,
        isAuth: null,
        createBy: "",
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: "域名不能为空", trigger: "blur" }],
        online: [
          {
            required: true,
            message: "是否已上线(0-否，1-是)不能为空",
            trigger: "blur",
          },
        ],
        updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
        updateBy: [{ required: true, message: "更新人不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      this.getList();
      this.getTreeselect();
      this.getesSourceIdList();
      this.getDicts("bss_online_status").then((response) => {
        this.onlineOptions = response.data;
      });
      this.getDicts("bss_authentication_status").then((response) => {
        this.isAuthOptions = response.data;
      });
    },
    // 获取数据源id下拉框
    getesSourceIdList() {
      listEsDataSource().then((res) => {
        this.esSourceOptions = res.data.data;
      });
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
      });
    },
    /** 查询域名信息列表 */
    getList() {
      this.loading = true;
      listDomain(this.queryParams)
        .then((res) => {
          if (res.code == 200 && res.data != null) {
            this.domainList = res.data.data;
            this.total = res.data.total;
            this.loading = false;
          } else {
            this.loading = false;
          }
        })
        .catch((err) => {
          console.log(err, "err");
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
        name: null,
        online: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        isAuth: null,
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
      this.title = "添加域名信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getDomain(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改域名信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateDomain(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            console.log(this.form, "this.form");
            addDomain(this.form).then((response) => {
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
      this.$confirm('是否确认删除域名信息编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delDomain(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有域名信息数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportDomain(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
