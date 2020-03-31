# yogurtcat-server-system
yogurtcat-server-system项目是一个基于spring boot搭建的管理后台脚手架，目的是帮助开发人员聚焦业务开发快速完成业务交付。
## 特性列表
- 基于Flyway的数据库版本管理  
- 支持构建deb安装包 ，通过安装包部署服务器

## 功能列表
- 用户管理：提供用户的相关配置，新增用户后，默认密码为123456
- 角色管理：对权限与菜单进行分配，可根据部门设置角色的数据权限
- 菜单管理：已实现菜单动态路由，后端可配置化，支持多级菜单
- 操作日志：记录用户操作的日志
- 异常日志：记录异常日志，方便开发人员定位错误

### 配置文件（开发指导临时存放）
- spring启动配置文件  
spring启动配置文件提供了三种场景，具体说明如下
    - application-dev.yml  
    开发阶段数据库采用内存数据库H2的mysql模式，开发人员可以启动后就可以调试程序没有额外的数据库依赖
    - application-test.yml
    - application-prod.yml

## 十年期ssl证书生成
keytool -genkey -alias yogurtcat -keyalg RSA -validity 3650 -keystore yogurtcat.keystore

##  IDE
### mapstruct插件安装
https://mapstruct.org/documentation/ide-support/


##  MAVEN
### gpg配置
windows使用https://www.gpg4win.org/download.html软件配置，下面以Linux环境为例说明
执行gpg --gen-key生成密钥

gpg --gen-key这里是采集密匙，输入用户名和邮箱，其他的值可以使用默认值，然后输入Passphrase的值，这个值需要记住，这个相当于密钥的密码，发布过程中进行签名操作的时候会用到。

上传GPG公钥

将公钥上传到公共的密钥服务器，这样其他人才可以通过公钥来验证jar包的完整性。

gpg --keyserver hkp://keyserver.ubuntu.com:11371 --send-keys CAB4165C69B699D989D2A62BD74A11D3F9F41243其中CAB4165C69B699D989D2A62BD74A11D3F9F41243为密钥的ID，可以通过gpg –list-keys命令查看



