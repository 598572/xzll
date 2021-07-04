# 种一颗树最好的时间是十年前，其次是现在。加油💪🏻💪🏻💪🏻

### 该项目目的
1.本项目是个人的学习记录，以及实际情况中遇到的一些问题 </br>
4.知识分享

### 杂谈
2.我实在想不出一个好的项目名 所以姑且就叫 study吧 </br>
3.你看着项目他是不是比裤兜都干净？没错 就是这样 干净 后续所有的都需一点点 AOF </br>

### 阶段目标1 redis整理与几个经典使用场景练习  end： 2021-05-31;

### 阶段目标2 rocketmq整理与源码阅读 （标准为主流程方法读加上注释） end： 暂定1个月 6.01-6.30

### 整合了个nacos 过程及其不顺利 各种改bootstrap文件 一直加载不上bootstrap文件  
```
有说是 引入
spring-cloud-context jar包的
有说是需要引入
<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency> 实时证明哪种都没有效果 ps: 可能这两种方式对更高版本有用
另外我以为是版本问题 但是经过确认没问题 版本对应关系参见 ： https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E
springboot和springcloud的对应  https://start.spring.io/actuator/info

```

```
最后解决方式 ： 我发现他居然没把bootstarpt.properties文件打进target目录中去 于是一番查找 最终 定位到 
<!--    <packaging>pom</packaging>--> 把他去掉 因为这玩意只打class文件 默认是jar打成jar就好了
```

```
增加了maven profile 配置 根据不同环境 指定不同配置文件 有了它不用 application_dev.properties application_test.properties 了
```

