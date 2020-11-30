##### 基于netty实现的分布式IM服务,包含server，route和一个client demo

1. 应用依赖zookeeper，redis
2. server和route为分布式应用，可以根据自己情况自由部署
3. 在Route服务的配置文件中选择不同路由策略
4. 在Route的文档中可以选择对客户端进行消息推送,目前实现了点对点，群发 http://route.../doc.html