## 平台简介
yogurtcat平台的目标是建立一个企业级的快速开发平台，帮助研发,测试以及运维人员高效的完成工作。
为了这个目标，yogurtcat平台需要提供以下几大特性
- 开箱即用  
平台代码下载后可以直接运行，不需要额外配置
- 集群部署  
平台采用前后端分离架构，各个部件都是无状态，可以根据业务规模动态调整集群规模。
- 多副本  
多副本能力是其中使用的数据库，很多分布式存储提供的能力，提升平台可靠性。
- 故障恢复  
平台采用的数据库，缓存，本地化存储都支持多台部署，避免单点故障。
- 状态监控  
平台基于ELK方案的beat，支持多种纬度的服务器监控包括日志，系统，网络等。
## 项目视图
![输入图片说明](https://images.gitee.com/uploads/images/2019/0619/180107_032f95ca_56595.png "thumb-yogurtcat-parent (1).png")
- yogurtcat-parent  
顶级项目
- yogurtcat-front-parent  
二级项目，前端顶级项目，所有的前端项目放置在此项目下
- yogurtcat-front-system  
三级项目，系统管理前台项目
- yogurtcat-generator-parent  
二级项目，代码生成项目
- yogurtcat-server-parent  
二级项目，服务端顶级项目，所有的服务端项目放置于此项目下
- yogurtcat-server-system  
三级项目，系统管理服务端项目
## 物理视图
![输入图片说明](https://images.gitee.com/uploads/images/2019/0620/232613_927e678c_56595.png "yogurtcat部署图.png")
- NGNIX集群  
客户端与服务集群之间需要提供负载均衡能力，将客户端的请求相对均衡的分发给集群中的服务器。NGINX在这里承担了这个角色，将客户的请求均匀的分发。负载方式支持hash，IP段等。
由于平台采取前后端分离的方式进行部署，所以负载均衡包括前端请求的负载和服务端请求的负载。
- 前端服务器集群  
前端服务器提供页面访问服务，前端服务器支持独立部署，并可以根据业务规模横向拓展。  
- 服务端服务器集群  
服务端服务器提供认证，鉴权，业务编排等服务。不同于传统有状态服务端，平台服务端将状态数据全部抽离以达到服务端无状态的目的。服务端无状态的优势在于允许请求可以在集群中完全均衡负载。
- 本地文件服务器集群  
平台使用seaweedfs文件服务器提供文件服务，业务中使用的各种文件例如音频，视频，文档等都需要保存于seaweedfs文件服务器。来自客户端的seaweedfs的查询请求需要通过Nginx鉴权完成才允许访问。
- 缓存服务器集群  
对于访问比较频繁的数据，平台提供缓存服务器能力来减少对数据库的读取压力。
- 数据库服务器集群  
对于要求高并发场景，可以通过数据库集群来提供，但是大部分情况是主备组网提供高可靠。
- ELK集群  
ELK由elasticsearch，logstash以及kibana组成，由于目前只需要日志收集能力，实际使用的是elasticsearch，filebear以及kibana组成。其中elasticsearch提供存储能力，filebeat提供日志收集能力，kibana提供日志查看。使用其他收集器后会使用logstash作为收集管道。
## 开发规约
- 【强制】前端工程提交前清理完所有lint错误
- 【强制】服务端工程提交前清理完所有[java代码检查插件](https://github.com/alibaba/p3c/tree/master/eclipse-plugin)错误
- 【强制】开发交付件包括业务代码，单体测试代码
- 【推荐】开发代码不允许出现强环境依赖，保持项目开箱即用
## 开发指导
http://freshconnect.gitee.io/yogurtcat-monolithic-doc/
