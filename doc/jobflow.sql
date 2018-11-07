/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.7.10 : Database - jobflow
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jobflow` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jobflow`;

/*Table structure for table `jf_execution` */

DROP TABLE IF EXISTS `jf_execution`;

CREATE TABLE `jf_execution` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `token` char(10) NOT NULL COMMENT 'token用于区分批处理',
  `job_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '作业ID',
  `job_title` varchar(100) DEFAULT NULL COMMENT '作业标题',
  `node_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '作业流节点ID',
  `rely_node` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '依赖节点',
  `cmd` varchar(255) DEFAULT NULL COMMENT '命令',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `start_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '执行开始时间',
  `end_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '执行结束时间',
  `retry_num` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '重试次数',
  `retry_interval` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '重试时间间隔',
  `exec_num` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '执行次数',
  `host_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '主机ID',
  `group_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '组ID',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '2' COMMENT '执行状态0失败 1成功 2等待 3运行中',
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_job_node` (`token`,`job_id`,`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `jf_execution` */

insert  into `jf_execution`(`id`,`token`,`job_id`,`job_title`,`node_id`,`rely_node`,`cmd`,`create_time`,`start_time`,`end_time`,`retry_num`,`retry_interval`,`exec_num`,`host_id`,`group_id`,`status`) values (1,'MPZVPCFLJL',2,'y2game_koudai_user',0,0,NULL,1531822556,1531822557,1531822561,3,30,1,0,1,1),(2,'YUWIZXRQWY',3,'test_job_stdout',0,0,NULL,1531823512,1531823512,1531823523,3,30,1,0,1,1),(3,'BRBWOSMXGU',2,'y2game_koudai_user',0,0,NULL,1531872568,1531872568,1531872573,3,30,1,0,1,1),(4,'XGFEBLACTK',1,'y2game_koudai_event',0,0,NULL,1531878027,1531878028,1531878039,3,30,1,0,1,1),(5,'SBDHOWMZHK',2,'y2game_koudai_user',0,0,NULL,1531878180,1531878181,1531878185,3,30,1,0,1,1),(6,'RXPXPKXZAB',3,'test_job_stdout',0,0,NULL,1531879409,1531879409,1531879420,3,30,1,0,1,1),(7,'XXDCERCBHE',1,'y2game_koudai_event',0,0,NULL,1531958415,1531958415,1531958427,3,30,1,0,1,1),(8,'KPOFQRYAWY',3,'test_job_stdout',0,0,'/usr/local/php/bin/php /home/open/www/cpzx_stat/backend/yii test/job/stdout',1531991414,1531991414,1531991426,3,30,1,0,1,1),(9,'SBRFTQXLXL',2,'y2game_koudai_user',0,0,'/usr/local/php/bin/php /home/open/www/cpzx_stat/backend/yii etl/y2game/user day=2018-07-18',1531998429,1531998430,1531998434,3,30,1,0,1,1),(10,'XIDLNLKOGY',1,'y2game_koudai_event',0,0,'/usr/local/php/bin/php /home/open/www/cpzx_stat/backend/yii etl/y2game/event day=2018-07-19',1532044804,1532044805,1532044817,3,30,1,0,1,1),(11,'QRUFCXSGPM',2,'y2game_koudai_user',0,0,'/usr/local/php/bin/php /home/open/www/cpzx_stat/backend/yii etl/y2game/user day=2018-07-19',1532045344,1532045344,1532045349,3,30,1,0,1,1),(12,'OBILEWDLBM',3,'test_job_stdout',0,0,'/usr/local/php/bin/php /home/open/www/cpzx_stat/backend/yii test/job/stdout',1532077807,1532077807,1532077818,3,30,1,0,1,1);

/*Table structure for table `jf_group` */

DROP TABLE IF EXISTS `jf_group`;

CREATE TABLE `jf_group` (
  `group_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `title` varchar(60) DEFAULT NULL COMMENT '组标题',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `jf_group` */

insert  into `jf_group`(`group_id`,`title`,`desc`) values (1,'产品中心-数据组','产品中心数据统计组'),(2,'产品中心-发行组','产品中心发行项目组');

/*Table structure for table `jf_host` */

DROP TABLE IF EXISTS `jf_host`;

CREATE TABLE `jf_host` (
  `host_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `host` varchar(60) NOT NULL COMMENT '主机名',
  `type` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '类型1SSH免密码登录',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `username` varchar(60) NOT NULL COMMENT '用户名',
  `password` varchar(60) DEFAULT NULL COMMENT '密码',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `group_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '组ID',
  PRIMARY KEY (`host_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `jf_host` */

/*Table structure for table `jf_job` */

DROP TABLE IF EXISTS `jf_job`;

CREATE TABLE `jf_job` (
  `job_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '作业id',
  `title` varchar(100) NOT NULL COMMENT '作业名称',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '类型',
  `desc` varchar(255) NOT NULL COMMENT '描述',
  `cmd` varchar(255) NOT NULL COMMENT '命令',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `group_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '作业所属组ID',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态1正常0删除',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `jf_job` */

insert  into `jf_job`(`job_id`,`title`,`type`,`desc`,`cmd`,`create_time`,`update_time`,`user_id`,`group_id`,`status`) values (1,'y2game_koudai_event',2,'Y2Game口袋苍穹游戏事件日志处理','/usr/local/php/bin/php /home/open/www/cpzx_stat/backend/yii etl/y2game/event day={yesterday}',1531822493,1531998405,0,1,1),(2,'y2game_koudai_user',2,'Y2Game口袋苍穹游戏用户数据计算','/usr/local/php/bin/php /home/open/www/cpzx_stat/backend/yii etl/y2game/user day={yesterday}',1531822545,1531998421,0,1,1),(3,'test_job_stdout',2,'测试任务输出','/usr/local/php/bin/php /home/open/www/cpzx_stat/backend/yii test/job/stdout',1531823501,1531823501,0,1,1);

/*Table structure for table `jf_jobflow` */

DROP TABLE IF EXISTS `jf_jobflow`;

CREATE TABLE `jf_jobflow` (
  `flow_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '工作流ID',
  `title` varchar(100) NOT NULL COMMENT '名称',
  `desc` varchar(255) NOT NULL COMMENT '描述',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '修改时间',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `group_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '组ID',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态1正常0删除',
  PRIMARY KEY (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `jf_jobflow` */

/*Table structure for table `jf_jobflow_node` */

DROP TABLE IF EXISTS `jf_jobflow_node`;

CREATE TABLE `jf_jobflow_node` (
  `node_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `job_id` int(11) unsigned DEFAULT NULL COMMENT '作业ID',
  `flow_id` int(11) unsigned DEFAULT NULL COMMENT '作业流ID',
  `rely` int(11) unsigned DEFAULT '0' COMMENT '依赖节点ID',
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `jf_jobflow_node` */

/*Table structure for table `jf_schedule` */

DROP TABLE IF EXISTS `jf_schedule`;

CREATE TABLE `jf_schedule` (
  `schedule_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '调度ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `desc` varchar(255) NOT NULL COMMENT '描述',
  `type` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '类型1简单调度2周期调度',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `start_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '开始时间',
  `end_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '结束时间',
  `schedule_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '调度时间',
  `cron` varchar(60) DEFAULT NULL COMMENT 'cron表达式',
  `entity_type` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '实体类型1job 2jobflow',
  `entity_id` int(11) unsigned NOT NULL COMMENT '实体ID',
  `retry_num` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '重试次数',
  `retry_interval` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '重试时间间隔',
  `host_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '脚步宿主机ID',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `group_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '组ID',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态0删除 1正常 2暂停',
  PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `jf_schedule` */

insert  into `jf_schedule`(`schedule_id`,`title`,`desc`,`type`,`create_time`,`update_time`,`start_time`,`end_time`,`schedule_time`,`cron`,`entity_type`,`entity_id`,`retry_num`,`retry_interval`,`host_id`,`user_id`,`group_id`,`status`) values (1,'y2game_koudai_event','Y2Game口袋苍穹游戏事件日志每日处理',2,1531824009,1531824009,0,0,1532044804,'01 8 * * *',1,1,3,30,0,0,1,1),(2,'y2game_koudai_user','Y2Game口袋苍穹游戏用户数据计算',2,1531824079,1531824079,0,0,1532045344,'10 8 * * *',1,2,3,30,0,0,1,1),(3,'test_job_stdout','test_job_stdout',2,1531879179,1531991408,0,0,1532077807,'11 17 * * *',1,3,3,30,0,0,1,1);

/*Table structure for table `jf_user` */

DROP TABLE IF EXISTS `jf_user`;

CREATE TABLE `jf_user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(60) NOT NULL COMMENT '用户名',
  `nickname` varchar(60) NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(60) NOT NULL COMMENT '密码',
  `type` tinyint(1) unsigned NOT NULL DEFAULT '2' COMMENT '类型',
  `group_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属组',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态1正常 0删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `jf_user` */

insert  into `jf_user`(`user_id`,`username`,`nickname`,`password`,`type`,`group_id`,`create_time`,`update_time`,`status`) values (1,'admin','admin','c41609127a22767bc97184edd9e62a0d',1,0,0,0,1),(2,'qiaojiang','qiaojiang','c340f98aa8160175fdc83f2dcb84c33b',1,1,0,0,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
