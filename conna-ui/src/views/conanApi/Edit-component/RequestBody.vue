<template>
  <div class="postman">
    <!-- 请求参数设置 -->
    <div>
      <div class="postman-radio">
        <el-radio-group v-model="radio" @change="radioChange">
          <el-radio label="none">none</el-radio>
          <el-radio label="form-data">form-data</el-radio>
          <el-radio label="x-www-form-urlencoded"
            >x-www-form-urlencoded</el-radio
          >
          <el-radio label="raw">raw</el-radio>
        </el-radio-group>
      </div>
      <div class="postman-none" v-show="radio == 'none'">
        This request does not have a body
      </div>
      <div v-show="radio == 'form-data'">
        <el-table
          v-show="keyValueEdit"
          class="table"
          ref="multipleTable"
          :data="paramsList"
          tooltip-effect="dark"
          border
          style="width: 100%"
        >
          <!-- <el-table-column
                    align="center"
                    type="selection"
                    width="55">
          </el-table-column>-->
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
      <div v-show="radio == 'x-www-form-urlencoded'">
        <el-table
          v-show="urlencodedKeyValueEdit"
          class="table"
          :data="urlencodedList"
          tooltip-effect="dark"
          border
          style="width: 100%"
        >
          <!-- <el-table-column
                    align="center"
                    type="selection"
                    width="55">
          </el-table-column>-->
          <el-table-column align="center" width="55">
            <template slot-scope="scope">
              <el-checkbox
                v-if="scope.$index != urlencodedList.length - 1"
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
                <div class="desc-btn" @click="urlencodedKeyValueEdit = false">
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
                    v-show="scope.$index != urlencodedList.length - 1"
                    @click="deleteUrlencoded(scope.$index)"
                    class="el-icon-close"
                  ></i>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <div v-show="!urlencodedKeyValueEdit">
          <div class="keyvalue">
            <span @click="urlencodedKeyValueEdit = true">Key-Value Edit</span>
          </div>
          <el-input
            :placeholder="placeholder"
            type="textarea"
            :rows="10"
            @change="urlencodedTextChange"
            v-model="urlencodedText"
          ></el-input>
        </div>
      </div>
      <div v-show="radio == 'raw'">
        <el-input
          type="textarea"
          :rows="10"
          @input="rawTextChange"
          v-model="rawText"
        ></el-input>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ApiRequestBody",
  data() {
    return {
      radio: "none",
      paramsList: [
        {
          checked: false,
          key: "",
          value: "",
          description: ""
        }
      ], // 请求参数列表
      paramsListWatch:null, //this.paramsList[this.paramsList.length - 1], // 请求参数最后一个元素
      paramsListChecked: [], // 请求参数选中项
      keyValueEdit: true, // 是否是key-value模式
      paramsText: "", //
      urlencodedList: [
        {
          checked: false,
          key: "",
          value: "",
          description: ""
        }
      ], // 请求参数列表
      urlencodedListWatch: null, // 请求参数最后一个元素this.urlencodedList[this.urlencodedList.length - 1]
      urlencodedListChecked: [], // 请求参数选中项
      urlencodedKeyValueEdit: true, // 是否是key-value模式
      urlencodedText: "", 
      rawText: "raw", 
      placeholder:
        "Rows are separated by new lines\nKeys and values are separated by : \nPrepend // to any row you want to add but keep disabled"
    };
  },

  created() {},

  mounted() {},

  destroyed() {},
  methods: {
    // 请求链接格式化
    requestUrlFormat() {
      let paramsList = this.paramsList;
      let paramsListChecked = [];
      paramsList.forEach((item, index) => {
        if (item.checked) {
          if (item.key != "" && item.value != "") {
            paramsListChecked.push(item);
          } else if (item.key != "") {
            paramsListChecked.push(item);
          }
        }
      });
      this.paramsListChecked = paramsListChecked;
      let params = {
        body: "form-data",
        paramsListChecked: this.paramsListChecked,
        paramsList: this.paramsList
      };
      this.$emit("transferBody", params);
    },

    // 请求链接格式化
    urlencodedFormat() {
      let urlencodedList = this.urlencodedList;
      let urlencodedListChecked = [];
      urlencodedList.forEach((item, index) => {
        if (item.checked) {
          if (item.key != "" && item.value != "") {
            urlencodedListChecked.push(item);
          } else if (item.key != "") {
            urlencodedListChecked.push(item);
          }
        }
      });
      this.urlencodedListChecked = urlencodedListChecked;
      let params = {
        body: "x-www-form-urlencoded",
        paramsListChecked: this.urlencodedListChecked,
        paramsList: this.urlencodedList
      };
      this.$emit("transferBody", params);
    },

    // 参数删除操作
    deleteParams(index) {
      let paramsList = this.paramsList;
      paramsList.splice(index, 1);
      this.paramsList = paramsList;
    },

    // urlencoded参数删除操作
    deleteUrlencoded(index) {
      let urlencodedList = this.urlencodedList;
      urlencodedList.splice(index, 1);
      this.urlencodedList = urlencodedList;
    },

    // raw输入框值改变
    rawTextChange() {
      let rawText = this.rawText.replace(/\s+/g, "");
      let params = {
        body: "raw",
        rawText: rawText
      };
      this.$emit("transferBody", params);
    },

    // 单选按钮改变事件
    radioChange(value) {
      console.log(value);
      let params = {};
      if (value == "none") {
        params = {
          body: "none"
        };
      }
      if (value == "form-data") {
        params = {
          body: "form-data",
          paramsListChecked: this.paramsListChecked,
          paramsList: this.paramsList
        };
      }
      if (value == "x-www-form-urlencoded") {
        params = {
          body: "x-www-form-urlencoded",
          paramsListChecked: this.urlencodedListChecked,
          paramsList: this.urlencodedList
        };
      }
      if (value == "raw") {
        params = {
          body: "raw",
          rawText: this.rawText
        };
      }
      this.$emit("transferBody", params);
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
    },

    urlencodedTextChange() {
      let paramsText = this.urlencodedText;
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

        let oldParamsList = this.urlencodedList;
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
      this.urlencodedList = paramsList;
      this.urlencodedListWatch = this.urlencodedList[
        this.urlencodedList.length - 1
      ];
    }
  },
  watch: {
    paramsListWatch: {
      handler: function(newValue, oldValue) {
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
    urlencodedListWatch: {
      handler: function(newValue, oldValue) {
        if (
          newValue.key == "" &&
          newValue.value == "" &&
          newValue.description == ""
        ) {
          return false;
        } else {
          this.urlencodedList[this.urlencodedList.length - 1].checked = true;
          let obj = {
            checked: false,
            key: "",
            value: "",
            description: ""
          };
          this.urlencodedList.push(obj);
          this.urlencodedListWatch = this.urlencodedList[
            this.urlencodedList.length - 1
          ];
        }
      },
      deep: true
    },
    urlencodedList: {
      handler: function(newValue, oldValue) {
        this.urlencodedFormat();

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
        this.urlencodedText = str;
      },
      deep: true
    }
  }
};
</script>

<style lang="less" scoped>
.postman {
  width: 100%;

  &-radio {
    margin-bottom: 10px;
  }

  &-none {
    width: 100%;
    height: 50px;
    line-height: 50px;
    text-align: center;
    color: #cccccc;
    border-top: 1px solid #cccccc;
    border-bottom: 1px solid #cccccc;
  }

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
