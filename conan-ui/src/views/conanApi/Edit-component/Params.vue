<template>
  <div class="postman">
    <!-- 请求参数设置 -->
    <div class="postman-params">
      <el-table
        v-show="keyValueEdit"
        class="table"
        ref="multipleTable"
        :data="paramsList"
        tooltip-effect="dark"
        border
        style="width: 100%"
      >
     
        <el-table-column align="center" width="55">
          <template slot-scope="scope">
            <el-checkbox
              v-if="scope.$index != paramsList.length - 1"
              v-model="scope.row.checked"
            ></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column label="KEY">
          <template slot-scope="scope">
            <el-input
              style="height: 25px;"
              v-model.trim="scope.row.key"
              placeholder="Key"
            ></el-input>
          </template>
        </el-table-column>
        <el-table-column label="VALUE">
          <template slot-scope="scope">
            <el-input
              style="height: 25px;"
              v-model.trim="scope.row.value"
              placeholder="Value"
            ></el-input>
          </template>
        </el-table-column>
        <el-table-column label="DESCRIPTION" show-overflow-tooltip>
          <template slot="header">
            <div class="desc">
              <div>DESCRIPTION</div>
              <div class="desc-btn" @click="keyValueEdit = false">
                Bulk Edit
              </div>
            </div>
          </template>
          <template slot-scope="scope">
            <div style="display:flex;">
              <el-input
                style="height: 25px;"
                v-model.trim="scope.row.description"
                placeholder="Description"
              ></el-input>
              <div class="delete">
                <i
                  v-show="scope.$index != paramsList.length - 1"
                  @click="deleteParams(scope.$index)"
                  class="el-icon-close"
                ></i>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div v-show="!keyValueEdit">
        <div class="keyvalue">
          <span @click="keyValueEdit = true">Key-Value Edit</span>
        </div>
        <el-input
          :placeholder="placeholder"
          type="textarea"
          :rows="10"
          @change="paramsTextChange"
          v-model="paramsText"
        ></el-input>
      </div>
    </div>
  </div>
</template>

<script >
export default {
  name:'ApiParams',
  data() {
    return {
      paramsList: [
        {
          checked: false,
          key: "",
          value: "",
          description: ""
        }
      ], // 请求参数列表
      paramsListWatch:null , // 请求参数最后一个元素this.paramsList[this.paramsList.length - 1]
      paramsListChecked: [], // 请求参数选中项
      keyValueEdit: true, // 是否是key-value模式
      paramsText: "", // Bulk-Edit模式数据
      placeholder:
        "Rows are separated by new lines\nKeys and values are separated by : \nPrepend // to any row you want to add but keep disabled"
    };
  },
  props: {
    requestUrl: {
      type: String,
      default: function() {
        return "";
      }
    }
  },

  created() {},

  mounted() {},

  destroyed() {},

  methods: {
    // 请求链接格式化
    requestUrlFormat() {
      let url = this.requestUrl.split("?")[0];
      let paramsList = this.paramsList;
      let params = "";
      let paramsListChecked = [];
      paramsList.forEach((item, index) => {
        if (item.checked) {
          if (item.key != "" && item.value != "") {
            params += `&${item.key}=${item.value}`;
            paramsListChecked.push(item);
          } else if (item.key != "") {
            params += `&${item.key}`;
            paramsListChecked.push(item);
          }
        }
      });
      if (params) {
        url += "?" + params.slice(1, params.length);
      }
      let param = {
        url: url,
        paramsListChecked: paramsListChecked,
        paramsList: paramsList
      };
      this.$emit("transferParams", param);
      this.paramsListChecked = paramsListChecked;
    },

    // 参数格式化
    paramsFormat(url) {
      let index = url.indexOf("?");
      let checkedList = [];
      if (index != -1) {
        let params = url.slice(index + 1, url.length);
        if (params != "") {
          let arr = params.split("&");
          arr.forEach(item => {
            if (item) {
              let keyValue = item.split("=");
              let obj = {
                checked: true,
                key: keyValue[0] || "",
                value: keyValue[1] || "",
                description: ""
              };
              checkedList.push(obj);
            }
          });
        }
      }
      let paramsList = this.paramsList;
      let noCheckedList = [];
      paramsList.forEach(item => {
        if (!item.checked) {
          noCheckedList.push(item);
        }
      });
      this.paramsList = checkedList.concat(noCheckedList);
    },

    // 参数删除操作
    deleteParams(index) {
      let paramsList = this.paramsList;
      paramsList.splice(index, 1);
      this.paramsList = paramsList;
    },

    // Bulk-Edit模式输入框更改
    paramsTextChange() {
      let paramsText = this.paramsText;
      paramsText = paramsText
        .replace(/(^\s*)|(\s*$)/g, "")
        .replace(/\n+/g, "&NL")
        .replace(/\s+/g, "");
      let arr = [];
      if (paramsText != "") {
        arr = paramsText.split("&NL");
      }
      // 拆分key-value
      let paramsList = [];
      arr.forEach(item => {
        let obj = {
          checked: true,
          key: "",
          value: "",
          description: ""
        };
        if (/^(\/\/)$/.test(item)) {
          return false;
        }
        if (/^(\/\/)/.test(item)) {
          obj.checked = false;
          item = item.replace(/^\/\//, "");
        } else {
          obj.checked = true;
        }
        if (/^:$/.test(item)) {
          return false;
        }

        let index = item.indexOf(":");
        if (index == -1) {
          obj.key = item;
        } else {
          obj.key = item.slice(0, index);
          obj.value = item.slice(index + 1, item.length);
        }

        let oldParamsList = this.paramsList;
        for (let i = 0; i < oldParamsList.length - 1; i++) {
          if (obj.key == oldParamsList[i].key) {
            obj.description = oldParamsList[i].description;
            break;
          }
        }
        paramsList.push(obj);
      });
      paramsList.push({
        checked: false,
        key: "",
        value: "",
        description: ""
      });
      this.paramsList = paramsList;
      this.paramsListWatch = this.paramsList[this.paramsList.length - 1];
    }
  },
  watch: {
    paramsListWatch: {
      handler: function(newValue, oldValue) {
        console.log(newValue);
        if (
          newValue.key == "" &&
          newValue.value == "" &&
          newValue.description == ""
        ) {
          return false;
        } else {
          this.paramsList[this.paramsList.length - 1].checked = true;
          let obj = {
            checked: false,
            key: "",
            value: "",
            description: ""
          };
          this.paramsList.push(obj);
          this.paramsListWatch = this.paramsList[this.paramsList.length - 1];
        }
      },
      deep: true
    },
    paramsList: {
      handler: function(newValue, oldValue) {
        this.requestUrlFormat();
        // Bulk-Edit
        let str = "";
        newValue.forEach(item => {
          if (item.key != "" || item.value != "") {
            if (item.checked) {
              str += `${item.key}:${item.value}\n`;
            } else {
              str += `//${item.key}:${item.value}\n`;
            }
          }
        });
        this.paramsText = str;
      },
      deep: true
    },
    requestUrl: {
      handler: function(newValue, oldValue) {
        let url = this.requestUrl;
        this.paramsFormat(url);
      },
      deep: true
    }
  }
};
</script>

<style lang="less" scoped>
.postman {
  width: 100%;

  .delete {
    width: 30px;
    height: 28px;
    text-align: center;
    margin-left: 5px;
    cursor: pointer;

    i {
      line-height: 28px;
      font-size: 20px;
    }
  }

  /deep/.el-table td,
  /deep/.el-table th {
    padding: 5px 0 !important;
  }
  /deep/.el-table th > .cell,
  /deep/.el-table .cell {
    line-height: 28px;
    padding: 0 5px;
  }

  .table /deep/.el-input__inner {
    height: 28px;
  }
}
.keyvalue {
  height: 30px;
  line-height: 30px;
  text-align: right;
  color: #ff6600;

  span {
    cursor: pointer;
    margin-right: 10px;
    font-size: 12px;
    font-weight: 900;
    padding-left: 10px;
    border-left: 1px solid #cccccc;
  }
}
.desc {
  display: flex;
  justify-content: space-between;

  &-btn {
    color: #ff6600;
    cursor: pointer;
    margin-right: 10px;
  }
}
</style>
