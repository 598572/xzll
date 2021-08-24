package com.xzll.test.point;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class PostConstructPoint {

	public PostConstructPoint() {
		System.out.println();
		System.out.println("################## PostConstructPoint的构造方法 ################## ");
		System.out.println();
	}

	@PostConstruct
	public void init(){
		System.out.println();
		System.out.println("-----------------------[PostConstructPoint]  扩展点演示 @PostConstruct 开始--------------------------------------");
		System.out.println("[PostConstructPoint执行时机演示]");
		System.out.println("-----------------------[PostConstructPoint]  扩展点演示 @PostConstruct 结束--------------------------------------");
		System.out.println();
	}

	@PreDestroy
	public void destroy(){
		System.out.println();
		System.out.println("-----------------------[PostConstructPoint]  扩展点演示 @PreDestroy 开始--------------------------------------");
		System.out.println("[PostConstructPoint执行时机演示]");
		System.out.println("-----------------------[PostConstructPoint]  扩展点演示 @PreDestroy 结束--------------------------------------");
		System.out.println();
	}
}
