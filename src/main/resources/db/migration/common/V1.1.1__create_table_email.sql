CREATE TABLE `qbtdc_email_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `email` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_qbtdc_email_details_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
);