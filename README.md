README
数据库性能分析地址 http://127.0.0.1/blog/druid/index.html

*****************************************************************************
I think it is very instesting

https://jhipster.github.io/
jhipster to build Spring boot + angular project
*****************************************************************************


需要看一下 security 里面的一些实现
AbstractDirectUrlResolver  是对基础路径的管理
AuthoritiesConstants 权限名称的管理
CustomPersistentRememberMeServices 登录服务管理
DirectUrlResolver 路径管理
LoginSuccessAuthenticationHandler 登录成功后的操作
LogoutSuccessHandler 退出以后的操作
PlatformSecurityAccountRepository 登录查询的管理
SourceSecurityAuthenticationEntry 资源信息权限拦截管理
WebSecurityConfigurer 权限的实现类，权限从这儿开始实现工作

--------------------------------------------
TokenAuthenticationProvider 用户的登录授权管理
--------------------------------------------

********************************************************************************

MYSQL 数据库修改
C:\ProgramData\MySQL\MySQL Server 5.6
# 大小写铭感
lower_case_table_names=0
# group_concat长度限制
group_concat_max_len=-1

********************************************************************************


********************************************************************************

MYSQL 数据库修改
google地址分享
C:\Windows\System32\drivers\etc\hosts

********************************************************************************



********************************************************************************
GET POST request
业务请求统一在请求加上 business
********************************************************************************


http://www.tuicool.com/articles/BzUJZbi  這裏面介紹的很不錯，很詳細
1、package.json文件是在安裝了nodeJs后生成。
    cmd -> 項目目錄 -> npm init 然後輸入相應信息就可以自動生成
2、項目文件node_modules 是項目自動生成,裏面還有相應的批處理命令.bin。感覺這個文件怎麽有一些多餘呢？
    cmd -> 項目目錄 -> npm install grunt --save-dev
3、npm是拷貝過來的
4、npm install 安裝其他的一些插件，比如“grunt-contrib-concat” 出現錯誤的話，這樣來一炮就好了npm config set registry http://registry.cnpmjs.org
5、安裝bower ，npm install bower -g


********************************************************************************
Maven属性
①内置属性：
    ${basedir}表示项目更目录，既包含pom.xml文件的目录；${version}表示项目版本
②pom属性：
    用具可以使用该属性应用POM文件中的元素的值：
        * ${project.build.scoreDirectory}项目的主源码目录.默认为src/main/java
        * ${project.build.testSourceDirectory} 测试源码目录.默认/src/test/java
        * ${project.build.directory} 项目构建输出目录，默认/target
        * ${project.outputDirectory} 项目主代码编译输出目录 ，默认target/classes/
        * ${project.testOutDirectory} 项目测试代码编译输出目录，默认target/test
        * ${project.groupId} 项目groupId
        * ${project.artifactId} 项目的artificialId
        * ${project.version} 项目的version,和${version}等价
        * ${project.build.finalName} 项目打包输出的文件的名称。默认${project.artifactId}-${project.version}
 ③自定义属性：
    * 用户在pom的<properties>元素下定义的Maven属性
 ④Setting属性:
    与POM属性相同，可以在setting.开头的属性应用到setting.xml文件的xml元素的值
 ⑤Java系统属性
    所有Java系统属性都可以使用Maven属性应用，可以使用mvn help:system查看这些属性
 ⑥环境变量：
    所有的环境变量可以使用env.开头的Maven属性应用。可以使用mvn help:system查看所有的环境变量
********************************************************************************