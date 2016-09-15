-- Este script está com os valores default para popular o banco de dados quando o HrStatus é iniciado pela primeira vez
-- default user setup.
insert into USER (username, enabled, firstLogin, mail, nome, password, failedLogins) VALUES ('root',true,false,'changeme@example.com','Administrador', 'sD3fPKLnFKZUjnSV4qA/XoJOqsmDfNfxWcZ7kPtLc0I=',0);
insert into ROLE (id, role, username) values (1, 'ROLE_ADMIN', 'root');
insert into SETUP (id, mailJndi, mailFrom) values (1, 'java:jboss/mail/HrStatus','hrstatus@hrstatus.com.br');