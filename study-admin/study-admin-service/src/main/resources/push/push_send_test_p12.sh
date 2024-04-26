#!/bin/bash
#推送p12证书的路径
CERT_FILE=${cert_filename.p12}
#推送证书的密码
CERT_PASSWORD=${cert_password}
#true是开发环境，false是生产环境
SANDBOX=true
#安装包的bundleId
BUNDLE_ID=${bundle_id}
#安装包获取的APNs的Token
DEVICE_TOKEN=${deviceToken}

if [ "${SANDBOX}" = true ]; then
  HOST=api.sandbox.push.apple.com
else
  HOST=api.push.apple.com
fi
curl -v \
  -d '{"aps":{"alert":"Test Push Message! --EMAS"}}' \
  --cert-type P12 \
  --cert ${CERT_FILE}:${CERT_PASSWORD} \
  -H "apns-topic:${BUNDLE_ID}" \
  https://${HOST}/3/device/${DEVICE_TOKEN}
