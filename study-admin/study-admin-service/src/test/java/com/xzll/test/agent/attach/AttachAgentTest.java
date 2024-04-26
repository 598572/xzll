package com.xzll.test.agent.attach;

import com.sun.tools.attach.VirtualMachine;

/**
 * @Author: 黄壮壮
 * @Date: 2023/3/3 09:15:21
 * @Description:
 */
public class AttachAgentTest {

	public static void main(String[] args)throws Exception {
		//1. 根据进程id 与目标jvm程序建立 socket连接
		VirtualMachine vm = VirtualMachine.attach("57846");
		try {
			//2. 加载指定的 agent jar，本质是发送请求
//			vm.loadAgent("/usr/local/src/agent/attach/study-agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar");
			vm.loadAgent("/Users/hzz/myself_project/xzll/study-agent/target/study-agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar");

		} finally {
			//程序结束时 卸载agent jar
//			vm.detach();
		}
		Thread.sleep(20000);
	}
}
