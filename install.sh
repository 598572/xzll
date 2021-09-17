#!/bin/bash
# 安装钉钉jar包到本地 jar下载地址 https://developers.dingtalk.com/document/app/download-the-server-side-sdk
mvn install:install-file -Dfile=/Users/hzz/Downloads/dingtalk-sdk-java/taobao-sdk-java-auto_1479188381469-20210910.jar -DgroupId=com.taobao -DartifactId=ding -Dversion=1 -Dpackaging=jar
