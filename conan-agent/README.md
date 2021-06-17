[![](https://echo.100tal.com/api/badge/wangxiao_qa_ep/conan-agent?branch=online&type=reliability_rating)](https://echo.100tal.com/wangxiao_qa_ep/conan-agent/online/settings)  </br>

#使用文档

TBA

#技术文档

* [原型图](https://org.modao.cc/app/4e0d2060afbbbbfcc6ccbaebf7c1ace6#screen=s8A340F089B1557914187952)
* [流程图]()
* [架构图](http://wiki.xesv5.com/pages/viewpage.action?pageId=18573890)
* [数据库](http://wiki.xesv5.com/pages/viewpage.action?pageId=18573883)
* [接口文档]()

#本地开发 owner

*[代码规范](http://wiki.xesv5.com/pages/viewpage.action?pageId=17701300)*

* 访问地址：http://localhost:8080/api/1.0
* 接口文档及调试：http://localhost:8080/docs.html
* Druid监控：http://localhost:8080/druid (conan/conan)
* 数据库通过flyway自动建表或迁移
* 日志使用SLF4J+LOGBACK
* 使用Spring Data REST提供各资源的基本CURD的RESTful接口
* 对于返回列表或者分页的接口要加缓存(Caffeine)
* 序列化统一用fastjson

