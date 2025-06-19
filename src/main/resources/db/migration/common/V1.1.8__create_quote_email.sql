CREATE TABLE `quote_email` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quote_group_id` bigint NOT NULL,
  `email` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_quote_email_quote_group_id` FOREIGN KEY (`quote_group_id`) REFERENCES `qbtdc_order_map` (`quote_group_id`)
);