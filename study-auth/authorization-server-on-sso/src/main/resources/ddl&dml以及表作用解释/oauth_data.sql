
-- 注意:
-- 这条记录的 web_server_redirect_uri 字段，我们设置为 http://127.0.0.1:9090/login，
-- 这是稍后我们搭建的 xx业务系统的回调地址。
--
-- 统一登录系统采用 OAuth 2.0 的授权码模式进行授权。
-- 授权成功后，浏览器会跳转 http://127.0.0.1:9090/login 回调地址，然后 XXX业务 系统会通过授权码向统一登录系统获取访问令牌。
-- 通过这样的方式，完成一次单点登录的过程。

INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('clientapp', '888888', 'read_userinfo,read_contacts',
	'password,authorization_code,refresh_token', 'http://127.0.0.1:9090/login', null, 3600, 864000, null, true);
