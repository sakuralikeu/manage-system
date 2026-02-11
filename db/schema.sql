CREATE DATABASE IF NOT EXISTS performance_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE performance_db;

CREATE TABLE IF NOT EXISTS roles (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  role_name VARCHAR(64) NOT NULL,
  description VARCHAR(255) NOT NULL,
  permissions JSON NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_roles_role_name (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL,
  password VARCHAR(255) NOT NULL,
  real_name VARCHAR(64) NOT NULL,
  role_id BIGINT UNSIGNED NOT NULL,
  department VARCHAR(128),
  position_type VARCHAR(32),
  status TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_username (username),
  KEY idx_users_role_id (role_id),
  CONSTRAINT fk_users_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS system_config (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  config_key VARCHAR(128) NOT NULL,
  config_value VARCHAR(512) NOT NULL,
  description VARCHAR(255),
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_system_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS audit_logs (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id BIGINT UNSIGNED,
  action VARCHAR(128) NOT NULL,
  resource VARCHAR(128),
  resource_id BIGINT,
  details JSON,
  ip_address VARCHAR(45),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_audit_logs_user_id (user_id),
  KEY idx_audit_logs_action (action),
  CONSTRAINT fk_audit_logs_users_user_id FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS projects (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  project_name VARCHAR(200) NOT NULL,
  project_code VARCHAR(50) NOT NULL,
  project_type VARCHAR(32) NOT NULL,
  allocation_method VARCHAR(32) NOT NULL,
  presale_ratio DECIMAL(5,4),
  rd_ratio DECIMAL(5,4),
  manager_id BIGINT UNSIGNED NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'IN_PROGRESS',
  total_virtual_hours DECIMAL(10,2),
  supervisor_approved TINYINT(1) NOT NULL DEFAULT 0,
  supervisor_id BIGINT UNSIGNED,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  finished_at DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uk_projects_project_code (project_code),
  KEY idx_projects_manager_id (manager_id),
  KEY idx_projects_supervisor_id (supervisor_id),
  CONSTRAINT fk_projects_users_manager_id FOREIGN KEY (manager_id) REFERENCES users (id),
  CONSTRAINT fk_projects_users_supervisor_id FOREIGN KEY (supervisor_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS project_nodes (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  project_id BIGINT UNSIGNED NOT NULL,
  node_name VARCHAR(50) NOT NULL,
  node_order INT NOT NULL,
  weight DECIMAL(5,4) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_project_nodes_project_id (project_id),
  CONSTRAINT fk_project_nodes_projects_project_id FOREIGN KEY (project_id) REFERENCES projects (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS project_participation (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  project_id BIGINT UNSIGNED NOT NULL,
  node_id BIGINT UNSIGNED NOT NULL,
  user_id BIGINT UNSIGNED NOT NULL,
  estimated_hours DECIMAL(8,2),
  actual_hours DECIMAL(8,2),
  contribution_ratio DECIMAL(5,4),
  user_confirmed TINYINT(1) NOT NULL DEFAULT 0,
  confirmed_at DATETIME,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_project_participation_project_id (project_id),
  KEY idx_project_participation_node_id (node_id),
  KEY idx_project_participation_user_id (user_id),
  CONSTRAINT fk_project_participation_projects_project_id FOREIGN KEY (project_id) REFERENCES projects (id),
  CONSTRAINT fk_project_participation_project_nodes_node_id FOREIGN KEY (node_id) REFERENCES project_nodes (id),
  CONSTRAINT fk_project_participation_users_user_id FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (id, role_name, description, permissions)
VALUES
  (1, 'DIRECTOR', '总监/副总监', '{
    "modules": {
      "project": ["create", "update", "delete", "view", "workhour_record", "contribution_calculate", "progress_manage"],
      "key_task": ["create", "update", "delete", "view", "complete", "settle"],
      "penalty": ["create", "update", "view", "confirm", "settle"],
      "settlement": ["generate_monthly", "view", "export", "confirm"],
      "user": ["view", "manage_roles"],
      "system_config": ["view", "update"]
    },
    "data_scope": "all"
  }'),
  (2, 'PROJECT_LEAD', '项目负责人', '{
    "modules": {
      "project": ["create", "update", "view", "workhour_record", "contribution_calculate", "progress_manage"],
      "key_task": ["create", "update", "view"],
      "penalty": ["view"],
      "settlement": ["view"]
    },
    "data_scope": "department"
  }'),
  (3, 'MEMBER_PRESALE', '参与人员（售前）', '{
    "modules": {
      "project": ["view", "workhour_record"],
      "key_task": ["view", "complete"],
      "settlement": ["view"]
    },
    "data_scope": "own"
  }'),
  (4, 'MEMBER_RD', '参与人员（研发）', '{
    "modules": {
      "project": ["view", "workhour_record"],
      "key_task": ["view", "complete"],
      "settlement": ["view"]
    },
    "data_scope": "own"
  }'),
  (5, 'HR_FINANCE', '指定人员（HR/财务）', '{
    "modules": {
      "settlement": ["view", "export", "confirm"],
      "user": ["view"],
      "project": ["view"],
      "key_task": ["view"],
      "penalty": ["view"]
    },
    "data_scope": "all"
  }')
ON DUPLICATE KEY UPDATE
  description = VALUES(description),
  permissions = VALUES(permissions);

INSERT INTO system_config (config_key, config_value, description)
VALUES (
  'default_node_weights',
  '[{"name":"商机","weight":0.02},{"name":"项目建议书","weight":0.03},{"name":"可研","weight":0.05},{"name":"招投标","weight":0.05},{"name":"前向签约","weight":0.05},{"name":"系统研发","weight":0.50},{"name":"后向采购","weight":0.05},{"name":"后向签约","weight":0.05},{"name":"项目试运行","weight":0.10},{"name":"初验","weight":0.05},{"name":"终验","weight":0.05}]',
  '默认项目节点权重'
)
ON DUPLICATE KEY UPDATE
  config_value = VALUES(config_value),
  description = VALUES(description);
