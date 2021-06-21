<template>
  <pre @click="clickDom($event)" v-html="syntaxHighlight(jsonString)"></pre>
</template>

<script >
export default {
  name: "ApiJson",
  data() {
    return {};
  },
  props: {
    jsonString: {
      type: String,
      default() {
        return "";
      }
    }
  },
  created() {},

  mounted() {},

  destroyed() {},
  methods: {
    // JSON格式转化
    syntaxHighlight(json) {
      try {
        json = JSON.parse(json);
      } catch (e) {
        console.log("json转化异常");
        return json;
      }
      if (typeof json != "string") {
        json = JSON.stringify(json, undefined, 2);
      }
      json = json
        .replace(/&/g, "&")
        .replace(/</g, "<")
        .replace(/>/g, ">");
      return json.replace(
        /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?|{|}|\[|\])/g,
        function(match) {
          let cls = "number";
          if (/^"/.test(match)) {
            if (/:$/.test(match)) {
              cls = "key";
            } else {
              cls = "string";
            }
          } else if (/true|false/.test(match)) {
            cls = "boolean";
          } else if (/null/.test(match)) {
            cls = "null";
          } else if (/{/.test(match)) {
            return '<i class="open"></i><span class="Object{...}">' + match;
          } else if (/}/.test(match)) {
            return match + "</span>";
          } else if (/\[/.test(match)) {
            return '<i class="open"></i><span class="Array">' + match;
          } else if (/\]/.test(match)) {
            return match + "</span>";
          }
          return '<span class="' + cls + '">' + match + "</span>";
        }
      );
    },

    clickDom(e) {
      let target = e.target;
      if (target.nodeName == "I") {
        if (target.className == "open") {
          target.className = "close";
          let dom = e.target.nextSibling;
          dom.style.display = "none";
          var childNode = document.createElement("em");
          if (dom.className == "Array") {
            let childrenList = dom.children;
            let newList = [];
            for (let i = 0; i < childrenList.length; i++) {
              if (childrenList[i].nodeName == "SPAN") {
                newList.push(childrenList[i]);
              }
            }
            childNode.innerHTML = `${dom.className}[<em style="color:#25aae2;">${newList.length}</em>]`;
          } else {
            childNode.innerHTML = dom.className;
          }
          e.target.parentNode.insertBefore(childNode, e.target.nextSibling);
        } else if (target.className == "close") {
          e.target.parentNode.removeChild(e.target.nextSibling);
          target.className = "open";
          let dom = e.target.nextSibling;
          dom.style.display = "";
        }
      }
    }
  }
};
</script>

<style lang="less" scoped>
.json {
  width: 100%;
  pre {
    width: 100%;
    margin: 0;
    font-weight: 800;
  }
}
/deep/.string {
  color: #3ab54a;
}
/deep/.number {
  color: #25aae2;
}
/deep/.boolean {
  color: #f98280;
}
/deep/.null {
  color: #f1592a;
}
/deep/.key {
  color: #92278f;
}
/deep/.open {
  display: inline-block;
  width: 12px;
  height: 12px;
  background: url("../../../assets/images/close.png") no-repeat;
  background-size: 100% 100%;
  vertical-align: middle;
  cursor: pointer;
}
/deep/.close {
  display: inline-block;
  width: 12px;
  height: 12px;
  background: url("../../../assets/images/open.png") no-repeat;
  background-size: 100% 100%;
  vertical-align: middle;
  cursor: pointer;
}
</style>
