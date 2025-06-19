CREATE TABLE `orders` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
   `wag_order_id` bigint  NOT NULL,
   `order_number` varchar(32)  NOT NULL,
   `tracking_order_reference` varchar(50) DEFAULT NULL,
   `oao` varchar(32)  NOT NULL,
   `obo` varchar(32) DEFAULT NULL,
   `data_contract_name` varchar(32)  NOT NULL,
   `originator_code` varchar(32)  NOT NULL,
   `transaction_id` varchar(32)  NOT NULL,
   `date_time_stamp` datetime(6)  NOT NULL,
   `application_date` date NULL,
   `due_validation_date` Date NULL DEFAULT NULL,
   `due_completion_date` Date NULL DEFAULT NULL,
   `estimated_completion_date` Date NULL DEFAULT NULL,
   `reforecast_due_date` Date NULL DEFAULT NULL,
   `operator_name` varchar(32) DEFAULT NULL,
   `operator_code` varchar(32) DEFAULT NULL,
   `service_class` varchar(20) DEFAULT NULL,
   `service` varchar(32) DEFAULT NULL,
   `service_code` varchar(20) DEFAULT NULL,
   `supplier_order_number` varchar(20),
   `notification_id` varchar(100),
   `siebel_number` varchar(30),
   `glan_number` varchar(30),
   `account_number` varchar(20) DEFAULT NULL,
   `organisation_name` varchar(50) DEFAULT NULL,
   `ref_quote_item_id` bigint NULL,
   `wasset` varchar(32) DEFAULT NULL,
   `status` varchar(32) DEFAULT NULL,
   `workflow` varchar(32),
   `intervention_flag` bit(1) DEFAULT NULL,
   `paused_on_error_flag` bit(1) DEFAULT NULL,
   `last_not` varchar(8) DEFAULT NULL,
   `notes` varchar(1500),
   `version` int,
   `date_received_by_delivery` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `access` (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
 `circuit_reference` varchar(20),
 `action` int,
 `bandwidth` varchar(5),
 `sla` varchar(8),
 `comms_room_details` varchar(150),
 `comms_room_power_socket_type` int,
 `term_equipment_port_setting` varchar(20),
 `term_equipment_presentation` varchar(6),
 `term_equipment_cable_manager` BIT(1),
 `site_name` varchar(50),
 `site_location_line1` varchar(50),
 `site_location_line2` varchar(50),
 `site_location_line3` varchar(50),
 `site_city` varchar(20),
 `site_county` varchar(20),
 `location_id` varchar(20),
 `location_type` varchar(10),
 `service_class` varchar(20),
 `term_equipment_port` varchar(10),
 `term_equipment_port_speed` varchar(10),
 `appointment_request_received_timestamp` timestamp NULL,
 `full_address` varchar(255) NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `logical_link` (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
 `circuit_reference` varchar(20),
 `vlan` varchar(20),
 `action` int,
 `service_class` varchar(20),
 `bandwidth` varchar(5),
 `ip` varchar(50),
 `voice_channel` int,
  PRIMARY KEY (`id`)
);

CREATE TABLE `access_install` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `access_id` bigint NOT NULL,
  `site_induction` varchar(50),
  `method_insurance_cert` BIT(1),
  `comms_room_ready_for_delivery` BIT(1),
  PRIMARY KEY (`id`),
  CONSTRAINT fk_access_id
  FOREIGN KEY (`access_id`)
  REFERENCES `access` (`id`)
);

CREATE TABLE `contact` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `access_install_id` bigint ,
  `order_id` bigint ,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `number` varchar(20),
  `email` varchar(40),
  `role` varchar(40),
  PRIMARY KEY (`id`),
  CONSTRAINT fk_access_install_id FOREIGN KEY (`access_install_id`) REFERENCES `access_install` (`id`),
  CONSTRAINT fk_order_id FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)

);

CREATE TABLE `rejection_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reject_code` varchar(32) DEFAULT NULL,
  `reject_reason` varchar(255) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `rejection_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
);

CREATE TABLE `quote` (
  `quote_item_id` bigint NOT NULL,
  `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `term` int DEFAULT NULL,
  `non_recurring_price` varchar(32) DEFAULT NULL,
  `recurring_price` varchar(32) DEFAULT NULL,
  `recurring_frequency` varchar(7) DEFAULT NULL,
  `qbtdc_order_number` varchar(32) DEFAULT NULL,
  `qbtdc_project_key` varchar(32) DEFAULT NULL,
  `a_end_target_access_supplier` varchar(7) DEFAULT NULL,
  `ip_range` int DEFAULT NULL,
  `logical_bandwidth` varchar (5),
  `a_end_bandwidth` varchar (3),
  `status` varchar(8),
  PRIMARY KEY (`quote_item_id`)
);

CREATE TABLE `pbtdcs` (
  `order_id` bigint NOT NULL,
  `cust_delay_start_date` datetime,
  `cust_delay_end_date` datetime,
  `cust_delay_reason` varchar(50),
  `customer_access_id` bigint NOT NULL,
  `wholesaler_access_id` bigint ,
  `logical_link_id` bigint ,
  `pbtdc_business_status_id` bigint,
  `quote_item_id` bigint NULL,
  PRIMARY KEY (`order_id`),
  CONSTRAINT fk_pbtdcs_order_id FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT fk_customer_access_id FOREIGN KEY (`customer_access_id`) REFERENCES `access` (`id`),
  CONSTRAINT fk_wholesaler_access_id FOREIGN KEY (`wholesaler_access_id`) REFERENCES `access` (`id`),
  CONSTRAINT fk_logical_link_id FOREIGN KEY (`logical_link_id`) REFERENCES `logical_link` (`id`),
  CONSTRAINT `fk_quote_item_id` FOREIGN KEY  (`quote_item_id`) REFERENCES `quote` (`quote_item_id`)
);


CREATE TABLE `customer_internal_detail` (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `order_id` bigint NOT NULL,
 `name` varchar(50),
 `value` varchar(200),
  PRIMARY KEY (`id`),
  CONSTRAINT fk_cust_internal_det_order_id
  FOREIGN KEY (`order_id`)
  REFERENCES `pbtdcs` (`order_id`)
);

CREATE TABLE pbtdc_business_status (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `last_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    order_entry_and_validation_status VARCHAR(50) DEFAULT NULL,
    planning_status VARCHAR(50) DEFAULT NULL,
    access_installation VARCHAR(50) DEFAULT NULL,
    network_provisioning VARCHAR(50) DEFAULT NULL,
    testing_cpe_installation VARCHAR(50) DEFAULT NULL,
    service_complete_and_operational VARCHAR(50) DEFAULT NULL,
    delivery_on_track BIT(1) DEFAULT NULL,
    `delivery_date` Date NULL DEFAULT NULL
);


CREATE TABLE `intervention_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_timestamp` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `order_id` bigint NOT NULL,
  `wag_order_id` bigint DEFAULT NULL,
  `status` varchar(32) DEFAULT NULL,
  `color` varchar(15) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `workflow` varchar(64) DEFAULT NULL,
  `agent` varchar(32) DEFAULT NULL,
  `closing_agent` varchar(32) DEFAULT NULL,
  `closing_notes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `intervention_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
);

CREATE TABLE `parked_notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `received_timestamp` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `order_id` bigint NOT NULL,
  `supplier_not_id` varchar(100) DEFAULT NULL,
  `notification_type` varchar(32) DEFAULT NULL,
  `processed_status` varchar(1) DEFAULT NULL,
  `supplier_notification` blob NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `parked_notifications_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
);

CREATE TABLE `service_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `last_changed` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `logical_link_id` bigint(20) DEFAULT NULL,
  `action` int(11) DEFAULT NULL,
  `service_name` varchar(50) DEFAULT NULL,
  `service_value` varchar(50) default NULL,
  KEY `service_details_ibfk_1` (`logical_link_id`),
  CONSTRAINT `service_details_ibfk_1`
  FOREIGN KEY (`logical_link_id`)
  REFERENCES `logical_link` (`id`),
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `pbtdc_report` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `report_date` DATE,
   `compressed_report` LONGBLOB,
   `oao` VARCHAR(50),
  PRIMARY KEY (`id`)
);