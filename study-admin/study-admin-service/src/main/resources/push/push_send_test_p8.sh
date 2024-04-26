#!/usr/bin/env zsh

set -e

TEAM_ID=mmm # team id
TOKEN_KEY_FILE_NAME="/Users/hzz/work/ios最新证书/dev-AuthKey_001.p8"
AUTH_KEY_ID=xxx # key_id
TOPIC=com.yyy.zzz # topic 一般是包名
DEVICE_TOKEN=ttt # device_token
APNS_HOST_NAME=api.sandbox.push.apple.com # 使用沙盒环境

# openssl s_client -connect "${APNS_HOST_NAME}":443

JWT_ISSUE_TIME=$(date +%s)
JWT_HEADER=$(printf '{ "alg": "ES256", "kid": "%s" }' "${AUTH_KEY_ID}" | openssl base64 -e -A | tr -- '+/' '-_' | tr -d =)
JWT_CLAIMS=$(printf '{ "iss": "%s", "iat": %d }' "${TEAM_ID}" "${JWT_ISSUE_TIME}" | openssl base64 -e -A | tr -- '+/' '-_' | tr -d =)
JWT_HEADER_CLAIMS="${JWT_HEADER}.${JWT_CLAIMS}"
JWT_SIGNED_HEADER_CLAIMS=$(printf "${JWT_HEADER_CLAIMS}" | openssl dgst -binary -sha256 -sign "${TOKEN_KEY_FILE_NAME}" | openssl base64 -e -A | tr -- '+/' '-_' | tr -d =)
AUTHENTICATION_TOKEN="${JWT_HEADER}.${JWT_CLAIMS}.${JWT_SIGNED_HEADER_CLAIMS}"

/usr/bin/curl -v \
              --header "apns-topic: $TOPIC" \
              --header "apns-push-type: alert" \
              --header "authorization: bearer $AUTHENTICATION_TOKEN" \
              --data '{"aps":{"alert":"test-测试push消息通过脚本001"}}' \
              --http2 https://${APNS_HOST_NAME}/3/device/${DEVICE_TOKEN}
