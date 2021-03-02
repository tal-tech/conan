<template>
  <div class="app-container">
    <div class="el-row">
      <div class="el-col el-col-6">
        <el-form :model="queryParams" ref="queryForm" :inline="true">
          <el-form-item label="Task任务名称：" prop="taskName">
            <h4 style="margin: 0">{{ queryParams.taskName }}</h4>
          </el-form-item>
        </el-form>
      </div>
      <div class="el-col el-col-18" align="right" style="margin-top: 20px">
        <el-button-group>
          <el-button type="success" size="mini" @click="handleTask('dept')"
            >按部门添加</el-button
          >
          <el-button type="warning" size="mini" @click="handleTask('api')"
            >按域名添加</el-button
          >
          <el-button type="primary" size="mini" @click="handleTask('domain')"
            >按接口名添加</el-button
          >
        </el-button-group>
      </div>
    </div>

    <el-table v-loading="loading" element-loading-spinner="el-icon-loading"
      element-loading-text="拼命加载中" :data="taskApiRelationList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="部门名称" align="center" prop="deptName" />
      <el-table-column label="域名名称" align="center" prop="domainName" />
      <el-table-column label="接口名称" align="center" prop="apiName" />
      <el-table-column label="最大录制数" align="center" prop="recordCount" />
      <el-table-column label="执行顺序" align="center" prop="position" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row,scope.$index,taskApiRelationList)"
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
    <div class="mybtn" >
       <router-link
            :to="{ name: 'TaskList' }"
            class="link-type"
          >
          <el-button size="mini" type="primary" >返回上一页</el-button>
       </router-link>
      <el-button size="mini" type="success" class="ML10" @click="onSubmit" v-show="submitShow">保存任务</el-button>
    </div>

    <!-- 添加或修改Task任务与接口关联关系对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="30%" append-to-body>
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="最大录制数" prop="recordCount">
            <el-input-number v-model="form.recordCount" controls-position="right" :min="0"  placeholder="请输入最大录制数" />
        </el-form-item>
        <el-form-item label="执行顺序" prop="position">
            <el-input-number v-model="form.position" controls-position="right" :min="0"  placeholder="请输入执行顺序" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 按条件新增任务对话框 -->
    <el-dialog
      :title="visibleTitle"
      :visible.sync="dialogFormVisible"
      width="400px"
      append-to-body
    >
      <el-form :model="ruleform" label-width="70px">
        <el-form-item
          label="部门"
          prop="deptId"
          v-if="visibleTitle == '按部门添加所有接口'"
        >
          <treeselect
            v-model="ruleform.deptId"
            :options="deptOptions"
            :show-count="true"
            placeholder="请选择归属部门"
          />
        </el-form-item>
        <el-form-item
          label="域名"
          prop="domainId"
          v-if="visibleTitle == '按域名添加所有接口'"
        >
          <el-select v-model="ruleform.domainId" placeholder="请选择域名" class="w100">
            <el-option
              v-for="dict in domainIdOptions"
              :key="dict.id"
              :label="dict.name"
              :value="parseInt(dict.id)"
            ></el-option>
          </el-select>
        </el-form-item>

        <div v-if="visibleTitle == '按接口名添加所有接口'">
          <el-form-item label="域名" prop="domainName">
            <el-select v-model="ruleform.domainName" placeholder="请选择域名" class="w100">
              <el-option
                v-for="dict in domainIdOptions"
                :key="dict.id"
                :label="dict.name"
                :value="dict.name"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="接口名" prop="apiName">
            <el-input v-model="ruleform.apiName" placeholder="请输入接口名"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getInfoByDeptIdTaskApiRelation,
  getInfoByDomainIdTaskApiRelation,
  getInfoByApiNameAndDomainNameTaskApiRelation,
  getTaskApiRelation,
  addTaskApiRelation,
} from "@/api/task/executeDetails";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { listDomain } from "@/api/admin/domain/list";
export default {
  name: "ExecuteTheTaskDetails",
  components: { Treeselect },
  data() {
    return {
      // 保存按钮
      submitShow:false,
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
      // Task任务与接口关联关系表格数据
      taskApiRelationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 按条件
      dialogFormVisible: false,
      visibleTitle: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskId: null,
        apiId: null,
        recordCount: null,
        diffType: null,
        position: null,
        taskName: null,
      },
      // 表单参数
      form: {},
      ruleform: {},
      deptOptions: [],
      domainIdOptions: [],
      // 表单校验
      rules: {
        taskId: [{ required: true, message: "Task任务ID不能为空", trigger: "blur" }],
        apiId: [{ required: true, message: "接口ID不能为空", trigger: "blur" }],
      },
    };
  },
  mounted() {
    this.queryParams.taskName = this.$route.query.taskName;
    this.initData();
  },
  methods: {
    initData(){
      this.getList();
      this.getTreeselect();
      this.getApiList();
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
      });
    },
    /** 查询Task任务与接口关联关系列表 */
    getList() {
      this.submitShow=false;
      this.loading = true;
      getTaskApiRelation(this.$route.query.taskId).then((response) => {
        if (response.code==200&&response.data.length!=0) {
          this.taskApiRelationList = response.data;
        }
        this.loading = false;
      }).catch(err=>{
        this.loading = false;
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    onSubmit() {
      let news = [];
      this.taskApiRelationList.forEach((item) => {
        let obj = {
          apiId: item.apiId,
          diffType: item.diffType,
          id: item.id?item.id:0,
          position: item.position,
          recordCount: item.recordCount,
          taskId: this.$route.query.taskId,
        };
        news.push(obj);
      });
      addTaskApiRelation(news).then((res) => {
        if (res.code == 200 ) {
            this.getList();
        }
      })
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        taskId: this.$route.query.taskId,
        apiId: null,
        recordCount: null,
        diffType: null,
        position: null,
      };
      this.resetForm("form");
    },
    /** 查找域名数据 */
    getApiList() {
      listDomain().then((res) => {
        if (res.code == 200) {
          this.domainIdOptions = res.data.data;
        } else {
          this.domainIdOptions = [];
        }
      });
    },

    /** 按条件新增接口 */
    handleTask(type) {
      this.ruleform = {};
      if (type == "dept") {
        this.visibleTitle = "按部门添加所有接口";
      } else if (type == "api") {
        this.visibleTitle = "按域名添加所有接口";
      } else {
        this.visibleTitle = "按接口名添加所有接口";
      }
      this.dialogFormVisible = true;
    },
    /** 确认按钮 */
    confirm() {
       let newsList= [];
      if (this.visibleTitle == "按部门添加所有接口") {
        getInfoByDeptIdTaskApiRelation(this.ruleform.deptId).then((res) => {
          if (res.code == 200 && res.data.length != 0) {
            newsList = this.taskApiRelationList.concat(res.data);
            this.taskApiRelationList=newsList;
          }
        });
      } else if (this.visibleTitle == "按域名添加所有接口") {
        getInfoByDomainIdTaskApiRelation(this.ruleform.domainId).then((res) => {
          if (res.code == 200 && res.data.length != 0) {
            newsList = this.taskApiRelationList.concat(res.data);
            this.taskApiRelationList=newsList;
          }
        });
      } else {
        let obj = {
          apiName: this.ruleform.apiName,
          domainName: this.ruleform.domainName,
        };
        getInfoByApiNameAndDomainNameTaskApiRelation(obj).then((res) => {
          if (res.code == 200 && res.data.length != 0) {
            newsList = this.taskApiRelationList.concat(res.data);
            this.taskApiRelationList=newsList;
          }
        });
      }
      this.dialogFormVisible = false;
      this.submitShow=true;

    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加Task任务与接口关联关系";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
        this.form = row;
        this.open = true;
        this.title = "修改接口Schema规则";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
            this.msgSuccess("修改成功");
            this.open = false;
            this.submitShow=true;
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row,index,rows) {
      console.log(index,'index');
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除Task任务与接口关联关系编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then( ()=> {
          rows.splice(index, 1);
          this.submitShow=true;
        })
        .then(() => {
          this.msgSuccess("删除成功");
        });
    },
  },
};
</script>
<style lang="scss">
.mybtn {
  text-align: center;
  margin-top: 20px;
}
.ML10{
  margin-left: 10px;
}
</style>
