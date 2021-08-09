#!/bin/bash

#打版本出错，回滚
#mvn release:rollback
# 该脚本解释: release:clean(清除) release:prepare 编译 ， release:perform deploy操作 & push到gitlib
#输入的描述，自动去除空格
read -p "请输入描述:" release_description
desc=$(echo "${release_description}" |sed 's/[[:space:]]//g')
mvn -B release:clean release:prepare -DtagPrefix=test -DscmReleaseCommitComment=${desc} release:perform
