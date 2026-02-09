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
