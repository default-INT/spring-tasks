insert into users(first_name, last_name, password, role, status, username) -- Password "admin"
values ('Evgeniy', 'Trofimov', '$2y$12$GDJdWlOdIC7QJeEE2LZtYeikBGtmBdItwMKfKbmcdbLDWdw9', 'ADMIN', 'ACTIVE', 'admin')
GO

insert into users(first_name, last_name, password, role, status, username) -- Password "user"
values ('Danil', 'Lipsky', '$2y$12$tlEq1zF/HEBCEq1h/chw/eXK5bE2R/RF6Mw1oYeSZQA6pLrBoC7ca', 'USER', 'ACTIVE', 'user')
GO

INSERT INTO roles_permissions(permission, role) VALUES ('ADMIN_PERMISSION', 'ADMIN');
INSERT INTO roles_permissions(permission, role) VALUES ('DEFAULT', 'GUEST');
INSERT INTO roles_permissions(permission, role) VALUES ('READ_ONLY', 'USER');
INSERT INTO roles_permissions(permission, role) VALUES ('COMMENT', 'USER');