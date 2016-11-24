ALTER TABLE `feedback_orchestrator`.`applications1_history` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3);
ALTER TABLE `feedback_orchestrator`.`configurations_history` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ;
ALTER TABLE `feedback_orchestrator`.`configurations_mechanisms_history`
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ;
ALTER TABLE `feedback_orchestrator`.`general_configurations_history` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ;
ALTER TABLE `feedback_orchestrator`.`mechanisms_history` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ;
ALTER TABLE `feedback_orchestrator`.`parameters_history` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ;
ALTER TABLE `feedback_orchestrator`.`user_groups_history` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ;
ALTER TABLE `feedback_orchestrator`.`users_history` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ;

