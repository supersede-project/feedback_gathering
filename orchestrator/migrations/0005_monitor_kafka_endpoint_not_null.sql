alter table `monitor_configuration` modify column `kafka_endpoint` varchar(255) default null;
alter table `monitor_configuration` modify column `state` varchar(255) default null;
