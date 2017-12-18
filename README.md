# Java 服务端种子工程结构约定和示例

> 1. [如何使用模型验证](docs/how-to-use-model-validate.markdown)
> 2. [spring正确使用事务](docs/how-to-use-transactional.markdown)
> 3. [如何正确使用http码](docs/how-to-use-http-status-code.markdown)
> 4. [分布式事务处理](docs/distributed-transactional.markdown)
> 5. [集成OData](docs/handler-odata.markdown)

## 一、介绍
这个过程结构主要是解决在规模较大的软件项目中目录结构、工程结构等等。

## 二、maven的groupid、artifactid命名
1. xx系统中所有的业务模块组件的groupid都命名为：com.numsg.xsystem
* artifactid命名规则为：numsg-xsystem-[modulename]-services 或 numsg-xsystem-[modulename]-webapi  

## 三、版本号规则
1. 采用语义化命名规则，初始版本号为:1.0.0
* Releases版本规则为：1.0.0-RELEASES
* Snapshot版本规则为：1.0.0-SNAPSHOT

## 四、种子工程应用  
### 种子工程打开 
* 先将种子工程代码clone到本地
* 打开IDEA，使用“import project”  
* 下一步,选择Import project from external model,选择Greadle
* 下一步,选择以下三项
    * Use auto-import  
    * Create directories for ....  
    * Use default gradle wrapper(recommended)  
    
### 业务工程包名修改 
1. 关闭IDEA种子工程 （或使用目录浏览方式打开工程进行修改） 
2. 修改工程根文件夹名numsg-system1-workspace  
    如修改为system1-module1-workspace
3. 修改根下面的setting.gradle里面的rootProject.name = 'numsg-system1-workspace'
改为rootProject.name = 'system1-module1-workspace'  
4. 修改所有（包含子模块里面）build.gradle里面的group 'com.numsg.system1'
改为group 'com.numsg.system1.module1'  
5. 修改所有的模块名  
    *  如numsg-system1-backend，右键修改文件夹名为system1-module1-backend
    *  修改子模块内的build.gradle里面的archivesBaseName
      如archivesBaseName = 'numsg-system1-backend'
       改为archivesBaseName = 'system1-module1-backend'  
       
6. 修改根下面的setting.gradle，include里面的所以模块名修改为对应的修改后模块名称，如'system1-module1-backend' 
7. 其他如contract、service、webapi、common都做类似修改    
8. 编译并发布common   
gradle面板内选择Tasks->build->build进行编译
编译成功后，执行发布Tasks->publishing->publish
9. 参照步骤8，分别编译并发布contract、service、webapi
10. 修改backend->build.gradle
将其中的compile修改为具体引用的模块包  
11. 修改backend里面的java->xxx->configs->Application里面的扫描包名  
    修改backend里面的java->xxx->configs->JpaConfiguration里面的repository包名
    修改backend里面的java->xxx->aspectj->ControllerLogAspectJ里面的controller包名  
    修改backend里面的java->xxx->aspectj->LimitIPRequestAspect里面的controller包名

12. 常用gradle命令  
gradle clean  或gradlew  clean          -- 清理  
gradle build  或gradlew  build          -- 编译  
gradle build --refresh-dependencies     --编译并下载最新依赖包  
