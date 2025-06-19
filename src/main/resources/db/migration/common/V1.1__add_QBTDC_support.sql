ALTER TABLE qbtdc_quote ADD column wag_quote_item_id BIGINT DEFAULT NULL;
ALTER TABLE qbtdc_quote ADD column rejection_details_id BIGINT DEFAULT NULL;
ALTER TABLE qbtdc_quote ADD column notes VARCHAR(500) DEFAULT NULL;
ALTER TABLE qbtdc_quote ADD column offline_quoted BIT(1) DEFAULT NULL;

ALTER TABLE qbtdc ADD column delay_reason VARCHAR(500) DEFAULT NULL;

ALTER TABLE rejection_details MODIFY COLUMN order_id BIGINT DEFAULT NULL;
ALTER TABLE rejection_details ADD COLUMN quote_item_id BIGINT DEFAULT NULL;
