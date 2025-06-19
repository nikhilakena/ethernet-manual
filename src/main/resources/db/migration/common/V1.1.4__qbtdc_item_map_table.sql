DROP TABLE `qbtdc_map`;

CREATE TABLE `qbtdc_order_map` (
   `quote_group_id` bigint NOT NULL AUTO_INCREMENT,
   `operator_code` varchar(5)  NOT NULL,
   `operator_name` varchar(32) NOT NULL,
   `order_number` varchar(32) ,
   `sync_flag` BIT(1) NOT NULL,
    PRIMARY KEY (`quote_group_id`)
);

CREATE TABLE `qbtdc_item_map` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `quote_id` bigint NOT NULL,
   `wag_quote_id` bigint NOT NULL,
   `quote_group_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk__quote_item_map__quote_group_id` foreign Key (`quote_group_id`) REFERENCES `qbtdc_order_map`(`quote_group_id`)
);