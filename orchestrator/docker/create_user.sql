CREATE USER supersede_orch@localhost IDENTIFIED BY 'supersede_orchestrator';

GRANT ALL PRIVILEGES ON supersede_orchestrator_spring.* TO 'supersede_orch'@'localhost';

FLUSH PRIVILEGES;