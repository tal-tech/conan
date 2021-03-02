<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
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
        <el-select v-model="queryParams.method" clearable placeholder="http方法">
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
        <el-select v-model="queryParams.isOnline" clearable placeholder="是否已上线">
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
        <el-select v-model="queryParams.isRead" clearable placeholder="是否读接口">
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
        <el-select v-model="queryParams.isEnable" clearable placeholder="是否启用">
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
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :data="apiList"
    >
      <el-table-column label="#" type="index" align="center"></el-table-column>
      <el-table-column label="接口名" align="center" prop="name">
        <template slot-scope="scope">
          <router-link
            :to="{
              name: 'ApiSchemaRule',
              query: { apiId: scope.row.apiId, name: scope.row.name },
            }"
            class="link-type"
          >
            <el-tooltip
              class="item"
              effect="light"
              content="点击可查看Schema配置规则"
              placement="top"
            >
              <span>{{ scope.row.name }}</span>
            </el-tooltip>
          </router-link>
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
          <el-tag :type="scoped.row.isOnline == 1 ? 'success' : 'danger'" effect="plain">
            {{ scoped.row.isOnline | valueFormat }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否读接口" align="center" prop="isRead">
        <template slot-scope="scoped">
          <el-tag :type="scoped.row.isRead == 1 ? 'success' : 'danger'" effect="plain">
            {{ scoped.row.isRead | valueFormat }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否启用" align="center" prop="isEnable">
        <template slot-scope="scoped">
          <el-tag :type="scoped.row.isEnable == 1 ? 'success' : 'danger'" effect="dark">
            {{ scoped.row.isEnable | isEnableFormat }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="可录制请求条数" align="center" prop="recordableCount">
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    </div>
</template>

<script>
import {
  listApi,
  getApi,
  delApi,
  addApi,
  updateApi,
  exportApi,
} from "@/api/admin/interface/index";
import { listDomain } from "@/api/admin/domain/list";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "ApiRule",
  components: { Treeselect },
  data() {
    return {
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
        recordableCount: null,
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
        sysDeptId: [{ required: true, message: "所属部门id不能为空", trigger: "blur" }],
        domainId: [{ required: true, message: "所属域名id不能为空", trigger: "blur" }],
        name: [{ required: true, message: "接口名不能为空", trigger: "blur" }],
        method: [
          {
            required: true,
            message: `http方法不能为空`,
            trigger: "blur",
          },
        ],
        isOnline: [
          {
            required: true,
            message: "是否已上线不能为空",
            trigger: "blur",
          },
        ],
        updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
        updateBy: [{ required: true, message: "更新人不能为空", trigger: "blur" }],
      },
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
      this.getDicts("bss_method_status").then((response) => {
        this.methodOptions = response.data;
      });
      this.getDicts("bss_online_status").then((response) => {
        console.log(response, "response");
        this.isOnlineOptions = response.data;
      });
      this.getDicts("bss_authentication_status").then((response) => {
        this.isReadOptions = response.data;
      });
      this.getDicts("bss_isEnable_status").then((response) => {
        this.isEnableOptions = response.data;
      });
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions, "ssss");
      });
    },

    // 域名id下拉框
    getDomainList() {
      listDomain()
        .then((res) => {
          if (res.code == 200 && res.data != null) {
            console.log(res.data);
            this.domainOptions = res.data.data;
          } else {
          }
        })
        .catch((err) => {
          console.log(err, "err");
        });
    },
    /** 查询接口管理列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams, "this.queryParams");
      listApi(this.queryParams)
        .then((response) => {
          if (response.code == 200) {
            this.apiList = response.data.data;
            this.total = response.data.total;
            this.loading = false;
          } else {
            this.loading = false;
          }
        })
        .catch((err) => {
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
        id: null,
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
        updateBy: null,
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
  }
};
</script>
<style lang="scss"></style>
