
CREATE TABLE app_role (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  role_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE employee (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  username varchar(255) NOT NULL,
  registered_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  active_status varchar(10) NOT NULL DEFAULT 'active',
  deleted_date timestamp DEFAULT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE user_role (
  user_id bigint(20) NOT NULL,
  role_id bigint(20) NOT NULL,
  CONSTRAINT FK859n2jvi8ivhui0rl0esws6o FOREIGN KEY (user_id) REFERENCES employee (id),
  CONSTRAINT FKa68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES app_role (id)
);