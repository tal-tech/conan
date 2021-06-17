<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="所属域名id" prop="domainId">
        <el-select
          v-model="queryParams.domainId"
          filterable
          placeholder="请输入所属域名id"
        >
          <el-option
            v-for="item in domainOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所属部门id" prop="sysDeptId">
        <treeselect
          class="w10"
          v-model="queryParams.sysDeptId"
          :options="deptOptions"
          :normalizer="normalizer"
          :show-count="true"
          placeholder="请选择所属部门id"
        />
      </el-form-item>
      <el-form-item label="http方法" prop="method">
        <el-select
          v-model="queryParams.method"
          clearable
          placeholder="http方法"
        >
          <el-option
            v-for="(item, index) in methodOptions"
            :key="index"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="上线状态" prop="isOnline">
        <el-select
          v-model="queryParams.isOnline"
          clearable
          placeholder="是否已上线"
        >
          <el-option
            v-for="item in isOnlineOptions"
            :key="item.dictCode"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否读接口" prop="isRead">
        <el-select
          v-model="queryParams.isRead"
          clearable
          placeholder="是否读接口"
        >
          <el-option
            v-for="item in isReadOptions"
            :key="item.dictCode"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否启用" prop="isEnable">
        <el-select
          v-model="queryParams.isEnable"
          clearable
          placeholder="是否启用"
        >
          <el-option
            v-for="item in isEnableOptions"
            :key="item.dictCode"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button
          type="cyan"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          v-hasPermi="['admin:api:list']"
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
          v-hasPermi="['admin:api:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-plus"
          size="mini"
          @click="upload.open2 = true"
          v-hasPermi="['admin:api:export']"
          >excel批量导入</el-button
        >
      
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-plus"
          size="mini"
          @click="moreExcel"
          v-hasPermi="['admin:api:export']"
          >快速获取网关接口Excel(导出网关域名接口Excel)</el-button
        >
      </el-col>
      <right-toolbar
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :data="apiList"
    >
      <el-table-column label="#" type="index" align="center"></el-table-column>
      <el-table-column label="接口名" align="center" prop="name">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
          <!-- <router-link
            :to="{
              name: 'ApiSchemaRule',
              query: { id: scope.row.id, name: scope.row.name }
            }"
            class="link-type"
            v-else
          >
            <el-tooltip
              class="item"
              effect="light"
              content="点击可查看Schema配置规则"
              placement="top"
            >
              <span>{{ scope.row.name }}</span>
            </el-tooltip>
          </router-link> -->
        </template>
      </el-table-column>
      <el-table-column label="http方法" align="center" prop="method">
        <template slot-scope="scoped">
          <div>
            {{ scoped.row.method | methodFormat }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="是否已上线" align="center" prop="isOnline">
        <template slot-scope="scoped">
          <el-tag
            :type="scoped.row.isOnline == 1 ? 'success' : 'danger'"
            effect="plain"
          >
            {{ scoped.row.isOnline | valueFormat }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否读接口" align="center" prop="isRead">
        <template slot-scope="scoped">
          <el-tag
            :type="scoped.row.isRead == 1 ? 'success' : 'danger'"
            effect="plain"
          >
            {{ scoped.row.isRead | valueFormat }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否启用" align="center" prop="isEnable">
        <template slot-scope="scoped">
          <el-tag
            :type="scoped.row.isEnable == 1 ? 'success' : 'danger'"
            effect="dark"
          >
            {{ scoped.row.isEnable | isEnableFormat }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="可录制请求条数"
        align="center"
        prop="recordableCount"
      >
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
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin:api:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:api:remove']"
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
    <!-- 添加或修改接口管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="所属部门id" prop="sysDeptId">
          <treeselect
            v-model="form.sysDeptId"
            :options="deptOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="请选择所属部门id"
          />
        </el-form-item>
        <el-form-item label="所属域名id" prop="domainId">
          <el-select
            v-model="form.domainId"
            filterable
            placeholder="请输入所属域名id"
          >
            <el-option
              v-for="item in domainOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接口名" prop="name">
          <el-input
            v-model="form.name"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-form-item label="http方法" prop="method">
          <el-select
            v-model="form.method"
            clearable
            placeholder="http方法"
            class="w100"
          >
            <el-option
              v-for="item in methodOptions"
              :key="item.dictCode"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否已上线" prop="isOnline">
          <el-select
            v-model="form.isOnline"
            clearable
            placeholder="是否已上线"
            class="w100"
          >
            <el-option
              v-for="item in isOnlineOptions"
              :key="item.dictCode"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否读接口" prop="isRead">
          <el-select
            v-model="form.isRead"
            clearable
            placeholder="是否读接口"
            class="w100"
          >
            <el-option
              v-for="item in isReadOptions"
              :key="item.dictCode"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnable">
          <el-select
            v-model="form.isEnable"
            clearable
            placeholder="是否启用"
            class="w100"
          >
            <el-option
              v-for="item in isEnableOptions"
              :key="item.dictCode"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="可录制请求条数" prop="recordableCount">
          <el-input-number
            v-model="form.recordableCount"
            placeholder="请输入可录制请求条数"
            controls-position="right"
            :min="0"
            class="w100"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="上传文件" :visible.sync="upload.open2" width="500px" append-to-body>
       <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :http-request="excelExport"
        :auto-upload="true"
        drag
      >
     <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import {
  excelExportList,
} from "@/api/admin/interface/excel";
import {
  listApi,
  getApi,
  delApi,
  addApi,
  updateApi,
  exportApi
} from "@/api/admin/interface/index";
import { listDomain } from "@/api/admin/domain/list";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { getToken } from "@/utils/auth";

export default {
  name: "Interface",
  components: { Treeselect },
  data() {
    return {
        upload: {
        // // 是否显示弹出层（用户导入）
        open2: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        domain:'',
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/api/1.0/excel"
      },
      fileList:[],
      // 遮罩层
      loading: false,
      // 显示搜索条件
      showSearch: true,
      // 接口管理表格数据
      apiList: [],
      // 部门树选项
      deptOptions: [],
      // 接口管理树选项
      apiOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sysDeptId: null,
        domainId: null,
        name: null,
        method: null,
        isOnline: null,
        isRead: null,
        isEnable: null,
        recordableCount: null
      },
      total: 0,
      // 表单参数
      form: {},
      isReadOptions: [],
      isOnlineOptions: [],
      methodOptions: [],
      isEnableOptions: [],
      domainOptions: [],
      // 表单校验
      rules: {
        sysDeptId: [
          { required: true, message: "所属部门id不能为空", trigger: "blur" }
        ],
        domainId: [
          { required: true, message: "所属域名id不能为空", trigger: "blur" }
        ],
        name: [{ required: true, message: "接口名不能为空", trigger: "blur" }],
        method: [
          {
            required: true,
            message: `http方法不能为空`,
            trigger: "blur"
          }
        ],
        isOnline: [
          {
            required: true,
            message: "是否已上线不能为空",
            trigger: "blur"
          }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        updateBy: [
          { required: true, message: "更新人不能为空", trigger: "blur" }
        ]
      }
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    // 初始化数据
    initData() {
      this.getList();
      this.getDomainList();
      this.getTreeselect();
      this.getDicts("bss_method_status").then(response => {
        this.methodOptions = response.data;
      });
      this.getDicts("bss_online_status").then(response => {
        console.log(response, "response");
        this.isOnlineOptions = response.data;
      });
      this.getDicts("bss_authentication_status").then(response => {
        this.isReadOptions = response.data;
      });
      this.getDicts("bss_isEnable_status").then(response => {
        this.isEnableOptions = response.data;
      });
    },
    //  导入excel
    excelExport(file) {
      var formData = new FormData();
      formData.append("excel", file.file);
      excelExportList(formData).then(response => {
        this.upload.open2 = false;
        this.$refs.upload.clearFiles();
        this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
        this.getList();
      });
    },
    // 获取更多接口excel
    moreExcel() {
      this.$router.push({
        name:'exportExcel',
        query:{name:'exportExcel'}
      })
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data;
        console.log(this.deptOptions, "ssss");
      });
    },

    // 域名id下拉框
    getDomainList() {
      listDomain()
        .then(res => {
          if (res.code == 200 && res.data != null) {
            console.log(res.data);
            this.domainOptions = res.data.data;
          } else {
          }
        })
        .catch(err => {
          console.log(err, "err");
        });
    },
    /** 查询接口管理列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams, "this.queryParams");
      listApi(this.queryParams)
        .then(response => {
          if (response.code == 200) {
            this.apiList = response.data.data;
            this.total = response.data.total;
            this.loading = false;
          } else {
            this.loading = false;
          }
        })
        .catch(err => {
          this.loading = false;
        });
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

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        apiId: null,
        sysDeptId: null,
        domainId: null,
        name: null,
        method: null,
        isOnline: null,
        isRead: null,
        isEnable: null,
        recordableCount: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加接口管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getApi(row.apiId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改接口管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          (this.form.apiId ? updateApi : addApi)(this.form)
            .then(data => {
              if (data.code == 200) {
                //   新增成功的话，就弹出提示
                this.$confirm(
                  (this.form.apiId ? "更新" : "添加") + "成功！",
                  "提示",
                  {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "success"
                  }
                ).then(
                  () => {
                    this.open = false;
                    this.getList();
                  },
                  () => {
                    this.open = false;
                    this.getList();
                  }
                );
              } else {
                this.$message.error("抱歉，操作失败，请重试！");
              }
            })
            .catch(err => {
              this.open = false;
            });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm(
        '是否确认删除接口管理接口名为" ' + row.name + ' "的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return delApi(row.apiId);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
  },
};
</script>
<style lang="scss"></style>
