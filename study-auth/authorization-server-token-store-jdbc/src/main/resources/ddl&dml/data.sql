INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('clientapp', '888888', 'read_userinfo,read_contacts',
	'password,refresh_token', null, null, 3600, 864000, null, true);
