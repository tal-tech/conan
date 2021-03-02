// 时间格式化
let dateServer = value => {
  return value.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3')
}
// 过滤是否
let valueFormat = value => {
  if (value == 0) {
    return "否";
  } else {
    return "是";
  }
}
// http请求方法

let methodFormat = value => {
  switch (value) {
    case 0:
      return "GET";
    case 1:
      return "POST";
    case 2:
      return "PUT";
    case 3:
      return "DELETE";
    case 4:
      return "HEAD";
    case 5:
      return "CONNECT";
    case 6:
      return "OPTIONS";
    default:
      return "TRACE"
  }
}
//启用禁用状态
var isEnableFormat = value => {
  if (value == 0) {
    return "禁用";
  } else {
    return "启用";
  }
}

// 任务列表状态
let taskFormat = value => {
  switch (value) {
    case 0:
      return "可执行";
    case 1:
      return "录制中";
    case 2:
      return "录制成功";
    case 3:
      return "录制失败";
    case 4:
      return "回放中";
    case 5:
      return "回放成功";
    case 6:
      return "回放失败";
    case 7:
      return "比对中";
    case 8:
      return "比对成功";
    case 9:
      return "比对失败";
  }
  
}

// 任务列表状态
let typeFormat = value => {
  if (value == 0) {
      return "普通任务";
  } else {
      return "带场次任务";
  }
}
// 管理员锁定任务
let isForceLockFormat = value => {
  if (value == 0) {
      return "正常任务";
  } else {
      return "锁定任务";
  }
}

export { dateServer, valueFormat, methodFormat, isEnableFormat, taskFormat ,typeFormat,isForceLockFormat}
