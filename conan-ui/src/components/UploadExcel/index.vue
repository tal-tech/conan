<template>
  <div class="component-upload-image">
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
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";

export default {
  components: {},
  data() {
    return {
     upload: {
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/api/1.0/excel"
      },
    };
  },
  props: {
    excelExportValue: {
      type: String,
      default: "",
    },
  },
  methods: {
    
  },
  watch: {},
};
</script>

<style scoped lang="scss">
.avatar {
  width: 100%;
  height: 100%;
}
</style>