<template>
  <div class="postman">
    <!-- 请求方式、链接 -->
    <div class="postman-header">
      <div class="postman-header-content">
        <el-input
          class="postman-header-input"
          @input="requestUrlChange"
          placeholder="请输入请求链接"
          :value="requestUrl"
        >
          <el-select
            slot="prepend"
            style="width: 120px"
            v-model="methodValue"
            filterable
            allow-create
            default-first-option
            @change="methodChange"
            placeholder="请求方式"
          >
            <el-option
              v-for="item in methodOPtions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-input>
        <el-button
          :loading="isSending"
          type="primary"
          class="iconfontbtn"
          @click="send"
          >&#xe7c3;&ensp;{{ sendBtnText }}</el-button
        >
        <el-button
          :loading="isSaving"
          @click="clickSave"
          class="iconfontbtn"
          type="primary"
          >&#xe60a;&ensp;{{ saveBtnText }}</el-button
        >
      </div>
    </div>
    <div class="postman-content" :style="{ height: 600 + 'px' }">
      <!-- 请求参数设置 -->
      <div class="postman-params">
        <el-tabs v-model="requestTabsName">
          <el-tab-pane label="Params" name="Params">
            <Params
              :requestUrl="requestUrlTransfer"
              @transferParams="receiveParams"
            ></Params>
          </el-tab-pane>
          <el-tab-pane label="Headers" name="Headers">
            <Headers @transferHeaders="receiveHeaders"></Headers>
          </el-tab-pane>
          <el-tab-pane label="Body" name="Body">
            <RequestBody @transferBody="receiveBody"></RequestBody>
          </el-tab-pane>
        </el-tabs>
      </div>
      <!-- 请求结果 -->
      <div class="postman-response">
        <div class="postman-response-nothing">
          <span>Response</span>
          <el-select
          class="floatRight"
            v-show="responseResult !== ''"
            v-model="responseType"
            @change="responseTypeChange"
          >
            <el-option
              v-for="item in responseOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </div>
        <div class="postman-response-text" v-show="responseResult !== ''">
          <span v-show="responseType == 'Text'">{{ responseResult }}</span>
          <Json
            v-show="responseType == 'JSON'"
            :jsonString="responseResult"
          ></Json>
        </div>
      </div>
    </div>
    <el-dialog
      title="保存用例"
      :visible.sync="formVisible"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-dialog
        width="30%"
        title="是否跳转到用例页面"
        :visible.sync="innerVisible"
        append-to-body
      >
        <div slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">取 消</el-button>
          <el-button type="primary" @click="toTaskPage">确 定</el-button>
        </div>
      </el-dialog>
      <el-form :model="cases_task_form" ref="cases_task_form">
        <el-form-item label="用例名称" prop="cases_name" label-width="100px">
          <el-input
            v-model="cases_task_form.cases_name"
            autocomplete="off"
            placeholder="请输入用例名称(默认为url)"
          ></el-input>
        </el-form-item>
      
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button
          @click="
            formVisible = false;
            isSaving = false;
            this.saveBtnText = '保存';
          "
          >取 消</el-button
        >
        <el-button type="primary" @click="save('cases_task_form')"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Params from "./Params.vue";
import Headers from "./Headers.vue";
import RequestBody from "./RequestBody.vue";
import Json from "./Json.vue";
import { getDebug } from "@/api/conanApi/index"    
export default {
  name: "ApiEdit",
  components: {Headers, RequestBody, Params,Json },// Headers, RequestBody, Json
  props: {
    contentH: {
      type: Number,
      default: function() {
        return 0
      }
    },
    tabsIndex: {
      type: Number,
      default: function() {
        return 0
      }
    }
  },
  data() {
    return {
      currentUserName: JSON.parse(localStorage.getItem("users"))
        ? JSON.parse(localStorage.getItem("users")).username
        : "noname", // 用户名
      currentUserId: 0,
      methodValue: "GET", // 请求方式
      methodOPtions: [
        { label: "GET", value: "GET" },
        { label: "POST", value: "POST" },
        { label: "PUT", value: "PUT" },
        { label: "PATCH", value: "PATCH" },
        { label: "DELETE", value: "DELETE" },
        { label: "COPY", value: "COPY" },
        { label: "HEAD", value: "HEAD" },
        { label: "OPTIONS", value: "OPTIONS" }
      ], // 请求方式下拉选项
      requestUrl: "", // 请求链接
      requestUrlTransfer: "", // 请求链接,传递给子组件
      sendBtnText: "发送", // 发送按钮文案
      isSending: false, // 是否正在发送请求
      saveBtnText: "保存", // 保存按钮文案
      isSaving: false, // 是否正在保存
      requestTabsName: "Params", // 请求参数-tabs默认值
      paramsList: [], // 请求参数列表
      paramsListChecked: [], // 请求参数选中项
      headerList: [], // Header请求参数
      headerListChecked: [], //Header请求参数选中项
      requestBody: {
        body: "none"
      }, // 请求body参数
      responseResult: "", // 请求结果
      responseResultJSON: null, // 请求结果JSON格式
      responseOptions: [
        { label: "Text", value: "Text" },
        { label: "JSON", value: "JSON" }
      ], // 请求结果类型
      responseType: "Text", // 请求结果类型默认值
      innerVisible: false, //内层dialog 视图开关

      formVisible: false, //form  dialog的视图开关
      case_name: "", //用例名称
      cases_task_form: {
        cases_name: "",
        task_name: "",
        department_id: "",
        product_line: ""
      },
      taskList: [], //当前用户添加用例的任务列表选项
      oneCasesInfo: {
        //存储一个用例对象
        name: "",
        department_id: 20, //写死为质量研发部
        product_line_id: 13, //写死为质量研发部
        url: "",
        parms: "",
        exp_result: "",
        method: "",
        exp_result_type: 1, //默认为全量关注
        proto_id: 0, //默认为默认模板
        create_name: this.currentUserName,
        env: 0, //暂时为测试环境
        har_headers: "",
        har_cookie: ""
      },
      taskInfo: {
        name: null,
        type: 1,
        department_id: 20,
        product_line_id: 13,
        notify_list: "",
        department_name: "",
        productLine_name: "",
        cases_list: [],
        create_name: this.currentUserName,
        update_name: this.currentUserName
      }
    };
  },

  created() {},

  mounted() {
    // if (
    //   !this.$store.state.department.departmentList ||
    //   this.$store.state.department.departmentList.length == 0
    // ) {
    //   this.$store.dispatch("department/getDepartmentList");
    // }
  },
  computed: {
      // get departmentList() {
    //   return this.$store.state.department.departmentList
    // }

    // get productLineList() {
    //   return this.$store.state.productLine.productLineList
    // }
  },
  methods: {
    toTaskPage() {
      this.innerVisible = false;
      this.$router.push({ name: "CasesList" });
    },

  
    onDepartmentChange() {
      this.$store.dispatch("productLine/getProductLineList", {
        department_id: this.cases_task_form.department_id
      });
    },

    // 请求方式值改变
    methodChange() {
      // 发给Edit父组件，用来更改tabs名称
      let param = {
        methodValue: this.methodValue,
        tabsIndex: this.tabsIndex,
        requestUrl: this.requestUrl
      };
      this.$emit("requestUrlChange", param);
    },

    // 发送按钮
    send() {
      if (this.requestUrl == "") {
        this.$message({
          message: "请求链接为空！",
          type: "warning"
        });
        return false;
      }
      this.isSending = true;
      this.sendBtnText = "发送中";
      let params = this.paramsDeal();
      console.log("请求参数", params);
      getDebug(params).then(res=>{
         this.isSending = false;
          this.sendBtnText = "发送";
          if (res.code == 200) {
            this.responseResult = res.data.desc;
          }
      })
      // fetch
      //   .post("/api/debug", params)
      //   //.post('http://10.90.72.128:8082/api/1.0/debug', params)
      //   .then((res: any) => {
      //     console.log(res);
      //     this.isSending = false;
      //     this.sendBtnText = "发送";
      //     if (res.code == 200) {
      //       this.responseResult = res.data.desc;
      //     }
      //   });
    },

    // 点击保存按钮
    async clickSave() {
      if (this.requestUrl == "") {
        this.$message({
          message: "请求链接为空！",
          type: "warning"
        });
        return false;
      }
      let params = this.paramsDeal();
      this.oneCasesInfo.name = this.case_name;
      this.oneCasesInfo.url = params.url.toString();
      this.oneCasesInfo.parms = params.parms;
      this.oneCasesInfo.method = params.method.toString();
      this.oneCasesInfo.har_headers = params.header;
      this.oneCasesInfo.har_cookie = params.cookie;
      this.formVisible = true;
      // fetch
      //   .get("/api/task?type=1&create_by=" + this.currentUserId, "")
      //   .then((res: any) => {
      //     if (res.code !== 200) {
      //       this.$message.error("服务器异常");
      //     } else {
      //       this.taskList = res.data.data;
      //       this.taskList = this.taskList.map(item => {
      //         return { value: item.name, label: item.id };
      //       });
      //     }
      //   });
      let res = await this.$store.dispatch("user/getUserListByNameKeyword", {
        name: this.currentUserName
      });
      let href = res[0]._links.self.href;
      let lastIndex = href.lastIndexOf("/");
      this.currentUserId = href.substr(lastIndex + 1, href.length - 1);
    },
    //表单提交 保存用例
    save(caseTaskForm) {
      this.$refs[caseTaskForm].validate(valid => {
        if (valid) {
          //alert('submit!');
        } else {
          //console.log('error submit!!');
          return false;
        }
      });
      console.log(this.cases_task_form);
      this.isSaving = true;
      this.saveBtnText = "保存中";
      this.formVisible = false;
      //保存一个任务并关联当前用例
      this.taskInfo.name = this.cases_task_form.task_name;
      if (
        this.cases_task_form.cases_name != "" &&
        this.cases_task_form.cases_name != null
      ) {
        this.oneCasesInfo.name = this.cases_task_form.cases_name;
      } else {
        let url = this.oneCasesInfo.url.toString();
        url = url.replace(/^http(s)?:/, "");
        url = url.replace("//", "");
        let tag = url.indexOf("/"); //标记/第一次出现的位置，之前为domain,之后为api
        if (tag > 0) {
          url = url.substring(tag, url.length);
        }
        this.oneCasesInfo.name = url;
      }
      this.taskInfo.cases_list[0] = this.oneCasesInfo;
      if (this.taskInfo.name == "" || this.taskInfo.name == null) {
        // fetch.post("/api/cases", this.oneCasesInfo).then((res: any) => {
        //   if (res.code == 200) {
        //     this.$notify({
        //       title: "成功",
        //       message: res.data.desc
        //     });
        //   }
        //   this.isSaving = false;
        //   this.saveBtnText = "保存";
        // });
        // this.innerVisible = true;
      } else {
        // fetch.post("/api/task/taskAndCases", this.taskInfo).then((res: any) => {
        //   if (res.code == 200) {
        //     this.$notify({
        //       title: "成功",
        //       message: res.data.desc
        //     });
        //   }
        //   this.isSaving = false;
        //   this.saveBtnText = "保存";
        // });
        // this.innerVisible = true;
      }
    },

    //保存用例 查询任务列表                }

    async querySearchTask(queryString, cb) {
      let taskList = this.taskList;
      //console.log(taskList);
      let results = queryString
        ? taskList.filter(this.createFilter(queryString))
        : taskList;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return task => {
        return (
          task.value.toLowerCase().indexOf(queryString.toLowerCase()) != -1
        );
      };
    },
    handleSelect(item) {
      console.log(item);
    },
    // 发送请求参数处理
    paramsDeal() {
      console.log(
        this.methodValue,
        this.requestUrl,
        this.paramsListChecked,
        this.headerListChecked,
        this.requestBody
      );
      let url = this.requestUrl;
      let method = this.methodValue;
      let parms = "";
      this.paramsListChecked.forEach(item => {
        parms += `${item.key}=${item.value};`;
      });

      let header = "";
      this.headerListChecked.forEach(item => {
        header += `${item.key}:${item.value};`;
      });

      let cookie = "";
      let mode = this.requestBody.body;
      if (mode == "form-data" || mode == "x-www-form-urlencoded") {
        this.requestBody.paramsListChecked.forEach(item => {
          parms += `${item.key}=${item.value};`;
        });
      } else if (mode == "raw") {
        parms += this.requestBody.rawText.replace(/\s+/g, "");
      }
      let params = {
        url: url,
        method: method,
        parms: parms,
        header: header,
        cookie: cookie
      };
      return params;
    },

    // 处理纯数字字符串
    ParseInt(str) {
      var reg = /^[0-9]+.?[0-9]*$/gi;
      if (reg.test(str)) {
        return parseInt(str);
      } else {
        return str;
      }
    },

    // 请求链接更改
    requestUrlChange(val) {
      this.requestUrl = val.replace(/\s+/g, "");
      this.requestUrlTransfer = this.requestUrl;
    },

    // 接收从Params组件传来的值
    receiveParams(params) {
      console.log("Params", params);
      let url = params.url;
      let paramsList = params.paramsList;
      let paramsListChecked = params.paramsListChecked;
      let val = this.requestUrl;
      let lastStr = val.substr(val.length - 1, 1);
      if (
        lastStr != "=" &&
        lastStr != "&" &&
        !(lastStr == "?" && val.split("?").length == 2)
      ) {
        this.requestUrl = url;
      }
      this.paramsList = paramsList;
      this.paramsListChecked = paramsListChecked;

      // 发给Edit父组件，用来更改tabs名称
      let param = {
        methodValue: this.methodValue,
        tabsIndex: this.tabsIndex,
        requestUrl: this.requestUrl
      };
      this.$emit("requestUrlChange", param);
    },

    // 接收从Headers组件传来的值
    receiveHeaders(params) {
      //console.log("Headers", params);
      let headerList = params.paramsList;
      let headerListChecked = params.paramsListChecked;
      this.headerList = headerList;
      this.headerListChecked = headerListChecked;
    },

    // 接收从RequestBody组件传来的值
    receiveBody(params) {
      //console.log("Body", params);
      this.requestBody = params;
    },

    // 请求结果类型改变
    responseTypeChange(value) {
      //console.log(value);
    }
  }
};
</script>

<style lang="less" scoped>
.el-div {
  display: flex;
}
.postman {
  width: 100%;
  height: 100%;
  position: relative;

  &-header {
    position: absolute;
    top: 0;
    left: 0;
    height: 40px;
    width: 100%;
    background: #ffffff;
    z-index: 99;
    padding-bottom: 10px;
    border-bottom: 1px solid #cccccc;

    &-content {
      display: flex;
    }

    &-input {
      margin-right: 20px;
    }
  }

  &-content {
    width: 100%;
    box-sizing: border-box;
    padding-top: 51px;
    overflow-y: auto;
  }

  &-response {
    width: 100%;
    margin-top: 10px;

    &-nothing {
      width: 100%;
      box-sizing: border-box;
      height: 40px;
      line-height: 40px;
      border: 1px solid #eeeeee;
      color: #c9c9c9;
      background: #fafafa;
      font-weight: 600;

      span {
        margin-left: 10px;
      }

      /deep/.el-input__inner {
        width: 100px;
        height: 30px;
        line-height: 30px;
      }
    }

    &-text {
      width: 100%;
      min-height: 400px;
      max-height: 800px;
      margin-top: 10px;
      padding: 5px;
      box-sizing: border-box;
      border: 2px solid #cccccc;
      border-radius: 5px;
      overflow-y: auto;
    }
  }
}
</style>
