# s-contro
一个轻量级的限流工具

# 使用方法

1 下载后编译安装到本地
> mvn install

3 引入jar包
``` xml
<dependency>
    <groupId>com.solantec</groupId>
    <artifactId>s-contro</artifactId>
    <version>1.0.1-RELEASE</version>
</dependency>
```

3 在spring的application.yml中添加配置
``` yml

spring:
  scontro:
    reources:
    - path: /service-decision-manage/demo/*
      limit: 0 //同时访问的最大请求数
      time-out: 1 //排除的请求等待超时时间限制
      is-fair: true //是否公平的分配请求,即先进先出的顺序队列
      fallbackUri: /sContro/default_fallback_path //请求失败的返回url
      watch: true //是否监控此资源,被监控的资源可以调用 http://localhost:7999/sContro/getResourceStates 自带的接口,该接口提供一系列监控指标
    - path: /demo/xxxx
      limit: 2
      time-out: 5
      is-fair: true
```




