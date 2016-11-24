ALTER TABLE `feedback_repository`.`feedbacks` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) ;
ALTER TABLE `feedback_repository`.`feedback_comments` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) ;


