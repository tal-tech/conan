#使用文档

TBA

#技术文档
https://dengkunnanmayun.gitee.io/conan-docs/#/use/README

#本地开发

* 访问地址：http://localhost:8080/api/1.0
* 接口文档及调试：http://localhost:8080/docs.html
* Druid监控：http://localhost:8080/druid (conan/conan)
* 数据库通过flyway自动建表或迁移
* 日志使用SLF4J+LOGBACK
* 使用Spring Data REST提供各资源的基本CURD的RESTful接口
* 对于返回列表或者分页的接口要加缓存(Caffeine)
* 序列化统一用fastjson

