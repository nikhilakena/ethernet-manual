CREATE TABLE `qbtdc` (
  `order_id` bigint NOT NULL,
  `recurring_frequency` varchar(7) DEFAULT NULL,
  `sync_flag` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  CONSTRAINT fk_qbtdcs_order_id FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
);

CREATE TABLE `qbtdc_quote` (
  `quote_item_id` bigint NOT NULL AUTO_INCREMENT,
  `ref_quote_item_id` bigint DEFAULT NULL,
  `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `term` int DEFAULT NULL,
  `non_recurring_price` varchar(32) DEFAULT NULL,
  `recurring_price` varchar(32) DEFAULT NULL,
  `status` varchar(8) DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `logical_link_id` bigint DEFAULT NULL,
  `customer_access_id` bigint DEFAULT NULL,
  `wholesaler_access_id` bigint DEFAULT NULL,
  PRIMARY KEY (`quote_item_id`),
  CONSTRAINT fk_qbtdc_quote_order_id FOREIGN KEY (`order_id`) REFERENCES `qbtdc` (`order_id`),
  CONSTRAINT fk_qbtdc_quote_logical_link_id FOREIGN KEY (`logical_link_id`) REFERENCES `logical_link` (`id`),
  CONSTRAINT fk_qbtdc_quote_customer_access_id FOREIGN KEY (`customer_access_id`) REFERENCES `access` (`id`),
  CONSTRAINT fk_qbtdc_quote_wholesaler_access_id FOREIGN KEY (`wholesaler_access_id`) REFERENCES `access` (`id`)
);

ALTER TABLE orders add column project_key varchar(32) DEFAULT NULL;
ALTER TABLE logical_link add column ip_range int DEFAULT NULL;
ALTER TABLE access add column supplier varchar(7) DEFAULT NULL;
ALTER TABLE access add column target_access_supplier varchar(7) DEFAULT NULL;
ALTER TABLE access add column network_status varchar(7) DEFAULT NULL;
ALTER TABLE access add column multi_eircode varchar(1) DEFAULT NULL ;

