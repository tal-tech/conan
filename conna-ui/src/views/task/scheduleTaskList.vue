<template>
  <div class="app-container">
    <div>
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="关键字">
          <el-input placeholder="支持模糊查询" v-model="queryParams.keyword"></el-input>
        </el-form-item>
        <!-- <el-form-item label="创建人">
          <el-input placeholder="支持模糊查询" v-model="queryParams.create_by"></el-input>
           <el-select
            :loading="loading"
            :remote-method="getUserListByKeyword"
            clearable
            filterable
            placeholder="支持模糊查询"
            remote
            v-model="queryParams.create_by"
          >
            <el-option
              :key="item.id"
              :label="item.name"
              :value="item.id"
              v-for="item in userList"
            ></el-option>
          </el-select> 
        </el-form-item>-->

        <!-- <el-form-item label="时间范围">
          <el-date-picker
          @change="timeChange"
            :picker-options="timeRangeOptions"
            align="right"
            end-placeholder="结束日期"
            range-separator="至"
            start-placeholder="开始日期"
            type="datetimerange"
            v-model="queryParams.data"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item> -->
        <el-form-item>
          <el-button
            @click="searchTask"
            class="el-button el-button--cyan el-button--mini"
            icon="el-icon-search"
            >查询任务</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="dataList-Btn">
      <el-button
        @click="createScheduleTask"
        type="primary"
        size="mini"
        icon="el-icon-plus"
        >新建任务</el-button
      >
    </div>
    <div class="dataList-Table">
      <!--  data.name.toLowerCase().includes(search.toLowerCase()) || -->
      <el-table
        v-loading="loading"
        element-loading-spinner="el-icon-loading"
        element-loading-text="拼命加载中"
        :data="
          tableData.filter(
            (data) =>
              !search ||
              data.task_name.toLowerCase().includes(search.toLowerCase()) ||
              data.create_by.toLowerCase().includes(search.toLowerCase()) ||
              data.task_name.toLowerCase().includes(search.toLowerCase()) ||
              data.update_by.toLowerCase().includes(search.toLowerCase())
          )
        "
        :header-cell-style="{ background: '#F3F4F7', color: '#555' }"
        style="width: 100%"
        fit
        tooltip-effect="light"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form class="task-table-expand" inline label-position="center">
              <el-form-item label="关联任务">
                <span>{{ props.row.task_name }}</span>
              </el-form-item>
              <el-form-item label="关联任务ID">
                <span>{{ props.row.task_id }}</span>
              </el-form-item>
              <el-form-item label="更新人">
                <span>{{ props.row.update_by }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.update_at }}</span>
              </el-form-item>
              <el-form-item label="创建人">
                <span>{{ props.row.create_by }}</span>
              </el-form-item>
              <el-form-item label="创建时间">
                <span>{{ props.row.create_at }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="#" type="index" align="center"> </el-table-column>
        <el-table-column label="名称" show-overflow-tooltip prop="name" align="center">
        </el-table-column>
        <el-table-column label="类型" prop="type" align="center">
          <template slot-scope="scope">
            <span class="bigIcon" v-if="scope.row.type == '录制'"
              ><i
                class="iconfont icon-luzhi"
                style="color: rgb(18, 150, 219); margin-right: 5px"
              ></i
              >{{ scope.row.type }}</span
            >
            <span class="bigIcon" v-else
              ><i
                class="iconfont icon-huifang"
                style="color: #1296db; margin-right: 5px"
              ></i
              >{{ scope.row.type }}</span
            >
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="250px" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="changeStatus(scope.row)"
              v-model="scope.row.status == '运行' ? true : false"
              active-text="运行"
              inactive-text="暂停"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="cron表达式" prop="cron" align="center"> </el-table-column>
        <el-table-column label="定时策略" prop="strategy" align="center">
        </el-table-column>
        <el-table-column align="center" label="操作" width="250px">
          <template slot="header">
            <el-input v-model="search" size="mini" placeholder="可输入关键字搜索" />
          </template>
          <template slot-scope="scope">
            <el-tooltip content="编辑" placement="top-start" effect="light">
              <el-button
                @click="editScheduleTask(scope.$index, scope.row)"
                class="el-button el-button--lvse el-button--mini"
                circle
                icon="el-icon-edit"
                size="mini"
              >
              </el-button>
            </el-tooltip>
            <el-tooltip content="运行记录" placement="top-start" effect="light">
              <el-button
                @click="executionDetail(scope.row)"
                type="primary"
                circle
                icon="el-icon-document"
                size="mini"
              >
              </el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top-start" effect="light">
              <el-button
                @click="deleteScheduleTask(scope.row)"
                size="mini"
                circle
                type="danger"
                icon="el-icon-delete"
              >
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.page"
        :limit.sync="queryParams.size"
        @pagination="getTaskTable"
      />
    </div>
    <!-- 定时任务弹框 -->
    <el-dialog
      :title="operateType"
      :visible.sync="scheduleTaskFormVisible"
      :close-on-click-modal="false"
      width="50%"
    >
      <el-form
        class="addCases"
        :model="scheduleTaskInfo"
        label-position="left"
        ref="createCasesForm"
        label-width="290px"
        :rules="createCasesFormRules"
      >
        <el-form-item label="定时任务名称" prop="name">
          <el-input
            v-model="scheduleTaskInfo.name"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="关联任务" prop="task_id">
          <div style="margin-top: 15px">
            <el-input
              placeholder="任务ID"
              v-model="scheduleTaskInfo.task_id"
              auto-complete="off"
            >
              <template slot="append">
                <i
                  class="el-icon-plus"
                  type="primary"
                  slot="suffix"
                  @click="addTaskList"
                  circle
                >
                </i
              ></template>
            </el-input>
          </div>
        </el-form-item>
        <el-form-item label="Cron表达式(0 0 9 * * ? * 每天9点执行)" prop="cron">
          <div style="margin-top: 15px">
            <el-input v-model="scheduleTaskInfo.cron" auto-complete="off">
              <template slot="append"
                ><el-button size="mini">
                  <a :href="`https://qqe2.com/cron`" target="_blank"
                    >Cron在线工具</a
                  ></el-button
                ></template
              >
            </el-input>
          </div>
        </el-form-item>
        <el-form-item label="执行策略" prop="strategy">
          <el-select clearable v-model="scheduleTaskInfo.strategy" placeholder="请选择">
            <el-option
              :key="item1.value"
              :label="item1.label"
              :value="item1.value"
              v-for="item1 in strategyList"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="scheduleTaskFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 关联任务id -->
    <el-dialog
      title="关联任务查询"
      ref="multipleTable"
      tooltip-effect="dark"
      :visible.sync="taskListTableVisible"
      width="80%"
    >
      <el-table
        :data="taskList.filter((data) => !search || data.name.includes(search))"
        style="width: 95%"
        height="500"
        @selection-change="handleTaskSelectionChange"
      >
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column
          property="name"
          show-overflow-tooltip
          fixed
          label="任务名称"
          width="200"
        ></el-table-column>
        <el-table-column property="taskId" label="任务ID" width="100"></el-table-column>
        <el-table-column property="createByName" label="创建人"></el-table-column>
        <el-table-column height="20px" width="200">
          <template slot="header">
            <el-input v-model="search" size="small" placeholder="提供任务关键字搜索" />
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="addRow(scope.row)"
              >ADD</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
import pagination from "../../components/Pagination";
import {
  GetTaskSchedule,
  CreateTaskSchedule,
  TaskScheduleByTaskId,
  TaskScheduleByCronExpression,
  deleteTaskById,
  execTaskById,
  updateTaskById,
} from "../../api/task/scheduleTaskList";
import { listTask } from "../../api/task/index";

export default {
  name: "ScheduleTaskList",
  components: {
    pagination,
  },
  data() {
    return {
      showNextTime: "",
      operateType: "定时任务",
      scheduleTaskFormVisible: false,
      taskListTableVisible: false,
      scheduleTaskInfo: {
        name:null,
        task_name:null,
        task_id:null,
        type:null,
        cron:null,
        status:null,
        strategy:null,
        operate_by:null,
      },
      strategyList: [
        {
          value: 0,
          label: "默认",
        },
        {
          value: 1,
          label: "执行一次",
        },
      ],
      userList: [], //创建人列表
      loading: false,
      search: "",
      timeRangeOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      total: 0, //总页数
      tableData: [],
      taskList: [],
      multipleCasesSelection: [],
      queryParams: {
        page: 1,
        size: 10,
        date_from: null,
        date_to: null,
        create_by: null,
        keyword: null,
      },
      createCasesFormRules: {
        scheduleName: [
          { required: true, message: "请输入任务名称", trigger: "blur" },
          { min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur" },
        ],
        cronExpression: [
          {
            required: true,
            message: "请输入正确的Cron表达式",
            validator: this.apiValidator,
            trigger: "blur",
          },
        ],
        taskId: [{ required: true, message: "关联的任务ID", trigger: "blur" }],
        jenkinsUrl: [
          {
            required: false,
            message: "请输入正确的job",
            validator: this.jpbValidator,
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {},
  computed: {},
  mounted() {
    this.getTaskTable();
  },
  methods: {
    getTaskTable() {
      this.loading = true;
      GetTaskSchedule(this.queryParams)
        .then((res) => {
          if (res.code == 200 && JSON.stringify(res.data) !== "{}") {
            this.tableData = res.data.data;
            this.total = res.data.total;
            setTimeout(() => {
              this.loading = false;
            }, 500);
          } else {
            this.loading = false;
            return false;
          }
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    // 改变
    changeStatus(row) {
      this.$confirm("是否要修改当前状态", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return execTaskById(row.id);
        })
        .then(() => {
          this.getTaskTable();
          this.msgSuccess("修改成功");
        });
    },
    // time change
    timeChange(value) {
      if (!value) return;
      this.queryParams.date_from = value[0];
      this.queryParams.date_to = value[1];
    },
    addTaskList() {
      this.taskListTableVisible = true;
      this.findTaskId();
    },
    handleTaskSelectionChange(val) {
      this.multipleCasesSelection = val;
    },
    // 创建人模糊查询
    getUserListByKeyword() {},
    // 部门下拉框查询
    onDepartmentChange() {},
    // 查询任务
    searchTask() {
      this.queryParams.page = 1;
      this.getTaskTable();
    },
    // 创建任务
    createScheduleTask() {
      this.operateType = "新建定时任务";
      this.scheduleTaskInfo ={};
      this.scheduleTaskFormVisible = true;
    },
    // 编辑任务
    editScheduleTask(index, row) {
      this.operateType = "修改定时任务";
      this.scheduleTaskInfo = row;
      this.scheduleTaskFormVisible = true;
    },
    // 查找关联任务id
    findTaskId(){
      let obj={
        pageSize:10,
        pageNum:1
      }
      listTask(obj)
        .then((res) => {
          if (res.code == 200 && JSON.stringify(res.data) !== "{}") {
            this.taskList = res.data.data;
            this.total = res.data.total;
          } else {
            return false;
          }
        })
        .catch((err) => {
          console.log(err, "err");
        });
    },
    // 添加add
    addRow(row){
      this.scheduleTaskInfo.task_id=row.taskId;
      this.taskListTableVisible=false;
    },
    // 任务提交
    save() {
      this.$refs.createCasesForm.validate((valid) => {
        if (!valid) {
          return false;
        }
        let obj={
          operate_by : "admin",
          type:this.scheduleTaskInfo.type == "录制" ? 1 : 2,
          strategy:this.scheduleTaskInfo.strategy == "默认" ? 0 : 1,
          cron:this.scheduleTaskInfo.cron,
          task_id:this.scheduleTaskInfo.task_id,
          name:this.scheduleTaskInfo.name
        };
        (this.operateType === "新建定时任务" ? CreateTaskSchedule : updateTaskById)(
          obj,
         this.scheduleTaskInfo.id
        )
          .then((res) => {
            if (res.code == 200) {
              this.msgSuccess(
                this.operateType === "新建定时任务" ? "新建成功" : "修改成功"
              );
              this.scheduleTaskFormVisible = false;
              this.getTaskTable();
            } else {
              this.msgSuccess(
                this.operateType === "新建定时任务" ? "新建失败" : "修改失败"
              );
            }
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    // 运行记录
    executionDetail(row) {
      this.$router.push({name:"DetailSchedule",query:{id:row.id}})
    },
    // 删除
    deleteScheduleTask(row) {
      const ids = row.id;
      this.$confirm('是否确认删除任务管理名称为"' + row.name + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return deleteTaskById(ids);
        })
        .then(() => {
          this.getTaskTable();
          this.msgSuccess("删除成功");
        });
    },
  },
};
</script>
<style lang="scss" scoped></style>
