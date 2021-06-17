<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width=""
    >
      <el-form-item label="请求URL" prop="curlUrl">
        <el-input
          v-model="queryParams.curlUrl"
          placeholder="请输入请求URL"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item
        label="获取cookie的header头的key,Cookie\Token"
        prop="responseGetCookieKey"
      >
        <el-input
          v-model="queryParams.responseGetCookieKey"
          placeholder="请输入获取cookie的header头的key,Cookie\Token"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="获取cookie的类型" prop="keyType">
        <el-select
          v-model="queryParams.keyType"
          placeholder="请选择获取cookie的类型"
          clearable
          size="small"
        >
          <el-option
            v-for="(dict, index) in keyTypeOptions"
            :key="index"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="cyan"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['common:domainAuth:add']"
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
          v-hasPermi="['common:domainAuth:edit']"
        >修改</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['common:domainAuth:remove']"
        >删除</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['common:domainAuth:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="domainAuthList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="获取cookie的类型" align="center" prop="authId" />
      <el-table-column label="请求URL" align="center" prop="curlUrl" />
      <el-table-column
        label="获取cookie的header头的key,Cookie\Token"
        align="center"
        prop="responseGetCookieKey"
      />
      <el-table-column
        label="获取cookie的类型"
        align="center"
        prop="keyType"
        :formatter="keyTypeFormat"
      />
      <el-table-column label="关联的domain_id" align="center" prop="domainId" />
      <el-table-column label="cookie信息" align="center" prop="cookie" />
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
            @click="handleUpdate(scope.row)"
            v-hasPermi="['common:domainAuth:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['common:domainAuth:remove']"
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

    <!-- 添加或修改域名鉴权对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="">
        <el-form-item label="请求URL" prop="curlUrl">
          <el-input v-model="form.curlUrl" placeholder="请输入请求URL" />
        </el-form-item>
        <el-form-item
          label="获取cookie的header头的key,Cookie\Token"
          prop="responseGetCookieKey"
        >
          <el-input
            v-model="form.responseGetCookieKey"
            placeholder="请输入获取cookie的header头的key,Cookie\Token"
          />
        </el-form-item>
        <el-form-item label="获取cookie的类型" prop="keyType">
          <el-select
            v-model="form.keyType"
            placeholder="请选择获取cookie的类型"
            class="w100"
          >
            <el-option
              v-for="(dict, index) in keyTypeOptions"
              :key="index"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关联的domain_id" prop="domainId">
          <el-input
            v-model="form.domainId"
            placeholder="请输入关联的domain_id"
            disabled
          />
        </el-form-item>
        <el-form-item label="cookie信息" prop="cookie">
          <el-input v-model="form.cookie" placeholder="请输入cookie信息" />
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
  listDomainAuth,
  getDomainAuth,
  delDomainAuth,
  addDomainAuth,
  updateDomainAuth,
  exportDomainAuth
} from "@/api/common/domainAuth";

export default {
  name: "DomainAuth",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 域名鉴权表格数据
      domainAuthList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 获取cookie的类型字典
      keyTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        curlUrl: null,
        responseGetCookieKey: null,
        keyType: null,
        domainId:this.$route.query.id
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        curlUrl: [
          { required: true, message: "请求URL不能为空", trigger: "blur" }
        ],
        keyType: [
          {
            required: true,
            message: "获取cookie的类型不能为空",
            trigger: "change"
          }
        ],
        domainId: [
          {
            required: true,
            message: "关联的domain_id不能为空",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("bss_cookie_info").then(response => {
      this.keyTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询域名鉴权列表 */
    getList() {
      this.loading = true;
      listDomainAuth(this.queryParams)
        .then(response => {
          if (response.code == 200) {
            this.domainAuthList = response.data.data;
            this.total = response.data.total;
            this.loading = false;
          }
        })
        .catch(err => {
          this.loading = false;
        });
    },
    // 获取cookie的类型字典翻译
    keyTypeFormat(row, column) {
      return this.selectDictLabel(this.keyTypeOptions, row.keyType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        authId: null,
        curlUrl: null,
        responseGetCookieKey: null,
        keyType: null,
        domainId: this.$route.query.id,
        updateTime: null,
        cookie: null
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
      this.ids = selection.map(item => item.authId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加域名鉴权";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const authId = row.authId || this.ids;
      getDomainAuth(authId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改域名鉴权";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.authId != null) {
            updateDomainAuth(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDomainAuth(this.form).then(response => {
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
      const authIds = row.authId || this.ids;
      this.$confirm(
        '是否确认删除域名鉴权编号为"' + authIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return delDomainAuth(authIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有域名鉴权数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return exportDomainAuth(queryParams);
        })
        .then(response => {
          this.download(response.msg);
        });
    }
  }
};
</script>
