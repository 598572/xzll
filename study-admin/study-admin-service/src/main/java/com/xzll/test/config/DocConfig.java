package com.xzll.test.config;

import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Optional;
import java.util.function.Function;


@Slf4j
@Configuration
public class DocConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("study-test-group")
				.apiInfo(new ApiInfoBuilder().title("个人学习项目--接口文档")
						.contact(new Contact("黄壮壮", "", "h163361631@163.com")).version("1.0").build())
				.select()
				.apis(DocConfig.basePackage("com.xzll.test.controller"))//支持多个controller包，多个逗号分割即可
				.paths(PathSelectors.any())
				.build();
	}

	/**
	 * Predicate that matches RequestHandler with given base package name for the class of the handler method.
	 * This predicate includes all request handlers matching the provided basePackage
	 *
	 * @param basePackage - base package of the classes
	 * @return this
	 */
	public static Predicate<RequestHandler> basePackage(final String basePackage) {
		return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
	}

	/**
	 * 处理包路径配置规则,支持多路径扫描匹配以逗号隔开
	 *
	 * @param basePackage 扫描包路径
	 * @return Function
	 */
	private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
		return input -> {
			for (String strPackage : basePackage.split(",")) {
				boolean isMatch = input.getPackage().getName().startsWith(strPackage);
				if (isMatch) {
					return true;
				}
			}
			return false;
		};
	}

	/**
	 * @param input RequestHandler
	 * @return Optional
	 */
	private static Optional<Class<?>> declaringClass(RequestHandler input) {
		return Optional.ofNullable(input.declaringClass());
	}
}
