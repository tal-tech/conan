// exportExcel
<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['admin:deptRule:add']"
          >获取网关流量</el-button
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
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      :data="deptRuleList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="域名名称" align="center" prop="domain" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin:deptRule:edit']"
            >修改</el-button
          > -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="downLoadBy(scope.row)"
            v-hasPermi="['admin:deptRule:download']"
            >下载</el-button
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
    <el-dialog
      title="获取网关流量"
      :visible.sync="open"
      width="30%"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-form-item label="域名" prop="domain">
          <el-select
            v-model="form.domain"
            filterable
            placeholder="请输入域名名称"
          >
            <el-option
              v-for="item in domainOptions"
              :key="item.id"
              :label="item.name"
              :value="item.name"
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
  excelDeleteFileByDomain,
  excelDownloadFileNoDeleteByDomain,
  excelGetFileNameList,
  excelDownloadByDomain
} from "@/api/admin/interface/excel";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Json from "../../../views/conanApi/Edit-component/Json";
import { listDomain } from "@/api/admin/domain/list";
import axios from "axios";
import { getToken } from "@/utils/auth";
export default {
  name: "exportExcel",
  components: { Treeselect, Json },
  data() {
    return {
      domainOptions: [],
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
      // 部门树选项
      deptOptions: [],
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleJson: null,
        deptId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ruleJson: [{ domain: true, message: "域名不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getTreeselect();
  },
  methods: {
    // 域名id下拉框
    getDomainList() {
      listDomain()
        .then(res => {
          if (res.code == 200 && res.data != null) {
            this.domainOptions = res.data.data;
          } else {
          }
        })
        .catch(err => {
          console.log(err, "err");
        });
    },
    /** 查询部门Schema规则配置列表 */
    getList() {
      this.loading = true;
      let ary = [];
      excelGetFileNameList()
        .then(response => {
          if (response.code == 200 && response.data.length != 0) {
            response.data.forEach(item => {
              ary.push({
                domain: item
              });
            });
            this.deptRuleList = ary;
            this.loading = false;
          } else {
            this.deptRuleList = [];
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
    /** 查询部门下拉树结构 */
    // getTreeselect() {
    //   treeselect().then(response => {
    //     this.deptOptions = response.data;
    //   });
    // },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    downLoadBy(row) {
      this.$confirm("是否确认导出所有部门Schema规则配置数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
        })
        .then(response => {
          this.downLoadZip(row, `网关流量`);
        });
    },
    downLoadZip(row, filename) {
      let reg = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62}\.)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}/;
      axios({
        method: "get",
        url:
          process.env.VUE_APP_BASE_API +
          `/api/1.0/excel/downloadFileNoDeleteByDomain?domain=` +
          row.domain.match(reg)[0],
        responseType: "blob",
        headers: { Authorization: "Bearer " + getToken() }
      }).then(response => {
        this.readFileDownload(response.data, filename);
      });
    },
    // 通过网关下载流量
    readFileDownload(data, msg) {
      var res = data;
      if (
        res.type === "application/json" ||
        res.type === "application/json;charset=UTF-8"
      ) {
        // 失败的时候，注意ie兼容问题
        let fileReader = new FileReader();
        fileReader.onload = function(event) {
          let jsonData = JSON.parse(this.result); // this.result是根据event，读取文件后打印的
          console.log(jsonData, "...............");
          if (jsonData.data === null && jsonData.code === 1) {
            Message({
              message: jsonData.msg || "Error",
              type: "error",
              duration: 5 * 1000
            });
          }
        };
        fileReader.readAsText(res);
      }
      if (
        res.type === "application/octet-stream" ||
        res.type === "application/vnd.ms-excel" ||
        res.type === "application/vnd.ms-excel;charset=UTF-8"
      ) {
        console.log("success..."); // 成功，注意ie兼容问题

        const blob = new Blob([res], {
          type: "application/vnd.ms-excel;charset=utf-8"
        });

        if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(blob, msg);
        } else {
          console.log(blob);
          const url = window.URL.createObjectURL(blob);
          console.log(url);
          const aLink = document.createElement("a");
          aLink.style.display = "none";
          aLink.href = url;
          aLink.setAttribute("download", msg);
          document.body.appendChild(aLink);
          aLink.click();
          document.body.removeChild(aLink);
          window.URL.revokeObjectURL(url);
        }
      }
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        ruleJson: null,
        deptId: null
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.getDomainList();
    },
    /** 修改按钮操作 */
    // handleUpdate(row) {
    //   this.reset();
    //   const id = row.id || this.ids;
    //   getDeptRule(id).then(response => {
    //     this.form = response.data;
    //     this.open = true;
    //     this.title = "修改部门Schema规则配置";
    //   });
    // },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          excelDownloadByDomain(this.form).then(response => {
            this.msgSuccess("获取网关该域名下接口列表时间较长，请稍后刷新查看");
            this.open = false;
            if (response.code==200) {
              this.getList();
            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let reg = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62}\.)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}/;
      const ids = row.domain.match(reg)[0];
      this.$confirm("是否确认删除该数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return excelDeleteFileByDomain(ids);
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
        type: "warning"
      })
        .then(function() {
          return exportDeptRule(queryParams);
        })
        .then(response => {
          this.download(response.msg);
        });
    }
  }
};
</script>
