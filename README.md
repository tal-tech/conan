
# conan

## 柯南流量回放平台
English | [简体中文](readmeCN.md)
<p align="left">
    <a href="https://github.com/1042970366/">
        <img src="https://img.shields.io/badge/license-MIT-green" alt="MIT License" />
    </a>
    <a href="https://java.org/">
        <img src="https://img.shields.io/badge/java-1.8.1-green" alt="Vue2.0">
    </a>
    <a href="https://vuejs.org/">
        <img src="https://img.shields.io/badge/vue.js-2.0-green" alt="Vue2.0">
    </a>
    <a href="https://github.com/1042970366/">
        <img src="https://img.shields.io/badge/author-TALconan-blueviolet" alt="Author">
    </a>
    <a href="https://github.com/1042970366/">
        <img src="https://img.shields.io/badge/🚀-open--in--browser-blueviolet" alt="Live Demo">
    </a>
</p>

After continuous polishing and iteration in online education business, Conan platform is finally open source, aiming to provide more professional and stable quality-effectiveness assurance scheme for more quality-effectiveness assurance teams in the industry. With the continuous changes of business and technology architecture, the quality assurance work of the service end becomes more and more complex. In recent years, the traffic playback has taken root in the industry, but most of them are tools based and the cost of use and secondary development are relatively high. Conan platform emerged.



## Objectives

Based on the recording and playback capability and result verification capability of online real user traffic, the solution is provided for smoke test, integrated regression test, online verification and online inspection.



## Core functions

**Flow collection**



The traffic recording and acquisition based on ES log source, platform configuration access, low cost of use, and detailed traffic collection data are provided.




**Traffic playback**



The distributed back-end architecture improves the execution efficiency for traffic playback, supports service authentication configuration, and the playback based on HTTP protocol conforms to the real business scenario.



**Result verification**



The routine verification mode of traffic playback is mainly diff of traffic results, but a large number of traffic noise (time stamp, self increasing data...) has always affected the accuracy of the results. Conan platform performs the first layer verification based on the configured jsonschema in playback, and then carries out the second layer verification of traffic diff based on the self-developed noise reduction comparison service, thus ensuring the accuracy of the result verification , which greatly improves the reliability of the traffic playback results.




## Platform advantages and application scenarios

**Advantages**

-Solve the problem of low coverage and high maintenance cost of traditional automation

-Multi rule traffic result Assertion Verification

-Support for multi rule traffic result comparison

-Flow data can be used for automated testing and performance testing

-Simple interaction, configuration access

-Open source co construction, continuous optimization



**Application scenarios**

-Lift and measure the quality card point

-CI/CD pipeline quality card point

-Service line monitoring and inspection




**Platform business architecture**

![后端业务架构.png](http://ttc-tal.oss-cn-beijing.aliyuncs.com/1605259627/%E5%90%8E%E7%AB%AF%E4%B8%9A%E5%8A%A1%E6%9E%B6%E6%9E%84.png)

<center>

Business architecture

</center>



**Platform technical architecture**

![后端技术架构.png](http://ttc-tal.oss-cn-beijing.aliyuncs.com/1605255935/%E5%90%8E%E7%AB%AF%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84.png)
<center>

Server architecture

</center>

<br>



**Platform capability and function**


![柯南能力图.png](http://ttc-tal.oss-cn-beijing.aliyuncs.com/1605261800/%E6%9F%AF%E5%8D%97%E8%83%BD%E5%8A%9B%E5%9B%BE.png)


**Instructions for use**

-Traffic collection: ES log;

-Playback protocol: http protocol;

-For specific environment, please refer to the open source detailed technical documents



##Write at the end

The improvement of quality and efficiency may not be achieved through a single platform, and the combination of technology and human can bring more breakthroughs. Being good at using technological innovation, we can calmly face more and more frequent demands and more complex business. The technical solution of Conan platform is produced by the class business of online school and gradually popularized. The platform is now open source, and hope more excellent people or teams will participate in it to provide more solutions for quality and efficiency assurance.



**Detailed use of documents**

https://dengkunnanmayun.gitee.io/conan-docs/#/use/README



**More**

https://mp.weixin.qq.com/s/1Cvi5kkqfF9y1rBi97qLwg



</br>

</br>


**Project leader** - Li Ning



<img src=" http://ttc-tal.oss-cn-beijing.aliyuncs.com/1606904630/image.png " width="100" height="100" align="middle" />

</br>

</br>



**Project member** - Liu Jinsong huyaoguo dengkunnan Ji Ying



<img src=" http://ttc-tal.oss-cn-beijing.aliyuncs.com/1605698754/image.png " width="100" height="100"  />

<img src=" http://ttc-tal.oss-cn-beijing.aliyuncs.com/1605702320/image.png " width="100" height="100" />

<img src=" http://ttc-tal.oss-cn-beijing.aliyuncs.com/1605702371/image.png " width="100" height="100"  />

<img src=" http://ttc-tal.oss-cn-beijing.aliyuncs.com/1605698855/image.png " width="100" height="100"  />

</br>

</br>



**Official QQ group of Conan**

<img src="http://ttc-tal.oss-cn-beijing.aliyuncs.com/1614485571/image.png" width="150" height="230" align="middle" />

<br>
