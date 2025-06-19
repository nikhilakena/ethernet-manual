CREATE TABLE `qbtdc_map` (
   `quote_group_id` bigint NOT NULL AUTO_INCREMENT,
   `operator_code` varchar(5)  NOT NULL,
   `operator_name` varchar(32) NOT NULL,
   `order_number` varchar(32) ,
   `sync_flag` BIT(1) NOT NULL,
    PRIMARY KEY (`quote_group_id`)
);