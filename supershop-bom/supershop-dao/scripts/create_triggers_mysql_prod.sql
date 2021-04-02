DELIMITER $$

CREATE TRIGGER product_lock
BEFORE DELETE ON product
FOR EACH ROW
BEGIN

  IF OLD.date_updated > CURDATE()-2 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Attempt to delete existing products';
  END IF;

END $$


DELIMITER ;