/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/11/5 20:56:19                           */
/*==============================================================*/


DROP TABLE IF EXISTS JHI_PERSISTENT_TOKEN;

DROP TABLE IF EXISTS PLATFORM_MESSAGE_INFO;

DROP TABLE IF EXISTS PLATFORM_MESSAGE_INFO_FILES;

DROP TABLE IF EXISTS PLATFORM_SECURITY_ACCOUNT;

DROP TABLE IF EXISTS PLATFORM_SYS_CLASSIFY;

DROP TABLE IF EXISTS PLATFORM_SYS_USER;

DROP TABLE IF EXISTS FILE_DATA_SOURCE;

/*==============================================================*/
/* Table: JHI_PERSISTENT_TOKEN                                  */
/*==============================================================*/
CREATE TABLE JHI_PERSISTENT_TOKEN
(
   ID                   VARCHAR(50) NOT NULL,
   TOKEN_VALUE          VARCHAR(200),
   TOKEN_DATE           DATE,
   IP_ADDRESS           VARCHAR(39),
   USER_AGENT           VARCHAR(200),
   SECURITY_ACCOUNT_ID  VARCHAR(50),
   PRIMARY KEY (ID)
);

ALTER TABLE JHI_PERSISTENT_TOKEN COMMENT 'JHI_PERSISTENT_TOKEN';

/*==============================================================*/
/* Table: PLATFORM_MESSAGE_INFO                                 */
/*==============================================================*/
CREATE TABLE PLATFORM_MESSAGE_INFO
(
   ID                   VARCHAR(50) NOT NULL,
   MMESSAGE_INFO        VARCHAR(2000) COMMENT '消息体',
   PUBLISH_DATE         DATETIME COMMENT '发布时间',
   PLATFORM_SYS_USER_ID VARCHAR(50) COMMENT '创建人',
   PUBLISH_TOOL         VARCHAR(500) COMMENT '发布的工具',
   BROWSE_COUNT         bigint(12) COMMENT '浏览次数',
   FAVORITY_COUNT       bigint(12) COMMENT '此条消息收藏的次数',
   ENJOY_COUNT          bigint(12) COMMENT '此条消息赞的次数',
   COMMENT_COUNT        bigint(12) COMMENT '此条记录评论次数',
   BROWSE_USER_IDS      TEXT COMMENT '消息浏览人',
   ENJOY_USER_IDS       TEXT COMMENT '赞过本条说说的人的id',
   FAVORITY_USER_IDS    TEXT COMMENT '收藏过本条说说的人的ID',
   PRIMARY KEY (ID)
);

ALTER TABLE PLATFORM_MESSAGE_INFO COMMENT '短消息';

/*==============================================================*/
/* Table: PLATFORM_MESSAGE_INFO_FILES                           */
/*==============================================================*/
CREATE TABLE PLATFORM_MESSAGE_INFO_FILES
(
   ID                   VARCHAR(50) NOT NULL,
   FILE_INFO            VARCHAR(50) NOT NULL COMMENT '文件保存的ID',
   FILE_NAME            VARCHAR(100) COMMENT '文件名字',
   PLATFORM_MESSAGE_INFO_ID VARCHAR(50) COMMENT '简讯ID',
   PRIMARY KEY (ID)
);

ALTER TABLE PLATFORM_MESSAGE_INFO_FILES COMMENT '消息体附件';

/*==============================================================*/
/* Table: PLATFORM_SECURITY_ACCOUNT                             */
/*==============================================================*/
CREATE TABLE PLATFORM_SECURITY_ACCOUNT
(
   ID                   VARCHAR(50) NOT NULL,
   ACCOUNT              VARCHAR(200) COMMENT '账号',
   PASSWORD             VARCHAR(200) COMMENT '密码',
   TOKEN                VARCHAR(255) COMMENT '编码',
   IS_EXPIRED           TINYINT(1) COMMENT '是否过期 0 没有过期 1 过期',
   IS_LOCK              TINYINT(1) COMMENT '是否锁定 0 没有锁定 1 锁定',
   CREATE_TIME          DATETIME COMMENT '创建时间',
   STATUS               TINYINT(2) COMMENT '状态 1激活，0冻结，-1注销',
   LAST_LOGIN_TIME      DATETIME COMMENT '上次登录时间',
   IS_DEL               tinyint(1) COMMENT '是否删除 0 没有删除 1 删除',
   LOGIN_TIME           INT(10) COMMENT '登陆次数',
   PLATFORM_SYS_USER_ID VARCHAR(50) COMMENT '用户信息',
   PRIMARY KEY (ID)
);

ALTER TABLE PLATFORM_SECURITY_ACCOUNT COMMENT '用户登录账号';

/*==============================================================*/
/* Table: PLATFORM_SYS_CLASSIFY                                 */
/*==============================================================*/
CREATE TABLE PLATFORM_SYS_CLASSIFY
(
   ID                   VARCHAR(50) NOT NULL,
   NAME                 VARCHAR(200) COMMENT '名称',
   CLASS_DESCRIBE       VARCHAR(500) COMMENT '描述',
   PLATFORM_SYS_USER_ID VARCHAR(50) COMMENT '创建人',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   IS_DEL               tinyint(1) COMMENT '是否被删除 0 没有被删除 1 删除',
   NUMBER               BIGINT(11) COMMENT '人数',
   PRIMARY KEY (ID)
);

ALTER TABLE PLATFORM_SYS_CLASSIFY COMMENT '类型群组';

/*==============================================================*/
/* Table: PLATFORM_SYS_USER                                     */
/*==============================================================*/
CREATE TABLE PLATFORM_SYS_USER
(
   ID                   VARCHAR(50) NOT NULL,
   NAME                 VARCHAR(50) COMMENT '用户姓名',
   GENDER               TINYINT(2) COMMENT '用户性别 0 女  1 男',
   NUMBER               VARCHAR(50) COMMENT '用户身份证号',
   ALIEN                VARCHAR(50) COMMENT '用户网名',
   EMAIL                VARCHAR(100) COMMENT 'e-mail',
   HEAD_URL             VARCHAR(50) COMMENT '用户头像',
   CREATE_DATE_TIME     DATETIME COMMENT '创建时间',
   MODIFY_DATE_TIME     DATETIME COMMENT '修改时间',
   IS_DEL               TINYINT(1) COMMENT '是否删除 1 没有删除 0 删除',
   PRIMARY KEY (ID)
);

ALTER TABLE PLATFORM_SYS_USER COMMENT '用户表';


INSERT INTO `PLATFORM_SYS_USER` (`ID`, `NAME`, `GENDER`, `NUMBER`, `ALIEN`, `EMAIL`, `CREATE_DATE_TIME`, `MODIFY_DATE_TIME`, `IS_DEL`) VALUES ('1', 'admin', '1', 'admin', 'admin', 'xue_2013@sina.com', '2016-11-05 15:44:53', '2016-11-05 15:44:56', '0');
INSERT INTO `PLATFORM_SECURITY_ACCOUNT` (`ID`, `ACCOUNT`, `PASSWORD`, `IS_EXPIRED`, `IS_LOCK`, `CREATE_TIME`, `STATUS`, `LAST_LOGIN_TIME`, `IS_DEL`, `LOGIN_TIME`, `PLATFORM_SYS_USER_ID`) VALUES ('1', 'admin', '123456', '0', '0', '2016-11-05 21:04:57', '1', '2016-11-05 21:05:04', '0', '0', '1');
INSERT INTO `PLATFORM_SYS_CLASSIFY` (`ID`, `NAME`, `CLASS_DESCRIBE`, `PLATFORM_SYS_USER_ID`, `CREATE_DATE`, `IS_DEL`, `NUMBER`) VALUES ('1', '我', '我就是我不一样的烟火', '1', '2016-11-05 21:00:23', '0', '0');


-- ----------------------------
-- Table structure for FILE_DATA_SOURCE
-- ----------------------------

CREATE TABLE `FILE_DATA_SOURCE` (
  `ID` char(36) NOT NULL  COMMENT 'ID',
  `BASE_PATH` varchar(255) DEFAULT NULL COMMENT '文件的绝对路径',
  `FASTDFS_ID` varchar(255) DEFAULT NULL COMMENT '文件的FasfDfsId',
  `FILE_NAME` varchar(255) DEFAULT NULL COMMENT '文件的名称',
  `FILE_SIZE` bigint(20) DEFAULT NULL COMMENT '文件的大小',
  `GROUP_NAME` varchar(255) DEFAULT NULL COMMENT '文件的分组ID',
  `RELATIVE_PATH` varchar(255) DEFAULT NULL COMMENT '文件的相对路径',
  `RESOURCE` varchar(255) DEFAULT NULL COMMENT '资源的信息',
  PRIMARY KEY (`ID`)
) ;

ALTER TABLE PLATFORM_SYS_USER COMMENT '文件信息记录';


-- alter col to user info table
ALTER TABLE `PLATFORM_SYS_USER` ADD COLUMN `DATE_OF_BIRTH`  datetime NULL  COMMENT '出生日期';