package com.xzll.test.point;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Iterator;

/**
 * ApplicationContextInitializer扩展点演示 时机: spring容器还没被初始化之前
 *
 * <p>
 * 因为这时候spring容器还没被初始化，所以想要自己的扩展的生效，有以下三种方式：
 * <p>
 * 1.在启动类中用springApplication.addInitializers(new ApplicationContextInitializerPoint())语句加入
 * 2.配置文件配置context.initializer.classes=com.xzll.test.ApplicationContextInitializerPoint
 * 3.Spring SPI扩展，在spring.factories中加入org.springframework.context.ApplicationContextInitializer=com.xzll.test.ApplicationContextInitializerPoint
 * <p>
 * //TODO 目前我试了三种方式 只有第二种方式可以输出该类的打印语句 ，1和2都没有输出打印语句 目前不知原因为何。留作以后研究吧
 *
 * 这是整个spring容器在刷新之前初始化ConfigurableApplicationContext的回调接口，简单来说，就是在容器刷新之前调用此类的initialize方法。
 * 这个点允许被用户自己扩展。用户可以在整个spring容器还没被初始化之前做一些事情。
 * 可以想到的场景可能为，在最开始激活一些配置，或者利用这时候class还没被类加载器加载的时机，进行动态字节码注入等操作。
 */
public class ApplicationContextInitializerPoint implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {

		// System.out.println("applicationContext: "+ JSON.toJSONString(applicationContext));
		// 注意这里引入不了FastJson 会报错 AnnotationConfigApplicationContext has not been refreshed yet  ; AnnotationConfigApplicationContext 尚未刷新
		// 详见: https://stackoverflow.com/questions/28404817/annotationconfigapplicationcontext-has-not-been-refreshed-yet-whats-wrong
		System.out.println("------------ApplicationContextInitializerPoint # initialize 开始-------------");
		System.out.println("[ApplicationContextInitializer扩展点演示] # initialize:  " + applicationContext.toString());
		System.out.println("BeanDefinitionCount count: " + applicationContext.getBeanDefinitionCount());
		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		Iterator<String> beanNamesIterator = beanFactory.getBeanNamesIterator();
		beanNamesIterator.forEachRemaining(System.out::println);
		System.out.println("时机: "+ "run 方法中的 this.prepareContext(); 的时候");
		System.out.println("-------------ApplicationContextInitializerPoint # initialize 结束------------");
		System.out.println();

	}

	/*

	private void prepareContext(ConfigurableApplicationContext context, ConfigurableEnvironment environment, SpringApplicationRunListeners listeners, ApplicationArguments applicationArguments, Banner printedBanner) {
        context.setEnvironment(environment);
        this.postProcessApplicationContext(context);
        this.applyInitializers(context);
        listeners.contextPrepared(context);
        if (this.logStartupInfo) {
            this.logStartupInfo(context.getParent() == null);
            this.logStartupProfileInfo(context);
        }

        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton("springApplicationArguments", applicationArguments);
        if (printedBanner != null) {
            beanFactory.registerSingleton("springBootBanner", printedBanner);
        }

        if (beanFactory instanceof DefaultListableBeanFactory) {
            ((DefaultListableBeanFactory)beanFactory).setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
        }

        Set<Object> sources = this.getAllSources();
        Assert.notEmpty(sources, "Sources must not be empty");
        this.load(context, sources.toArray(new Object[0]));
        listeners.contextLoaded(context);
    }


     protected void applyInitializers(ConfigurableApplicationContext context) {
        Iterator var2 = this.getInitializers().iterator();

        while(var2.hasNext()) {
            ApplicationContextInitializer initializer = (ApplicationContextInitializer)var2.next();
            Class<?> requiredType = GenericTypeResolver.resolveTypeArgument(initializer.getClass(), ApplicationContextInitializer.class);
            Assert.isInstanceOf(requiredType, context, "Unable to call initializer.");
            initializer.initialize(context);// todo 此处回调 initialize 方法
        }

    }

	 */
}
