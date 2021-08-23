package com.xzll.test.point;

import com.xzll.test.controller.TestController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * 时机 bean实例化时候
 */
@Configuration
public class BeanPostProcessPoint implements BeanPostProcessor {


	public BeanPostProcessPoint() {
		System.out.println();
		System.out.println("################## BeanPostProcessPoint 的构造方法 ##################");
		System.out.println();
	}

	/**
	 * bean实例化之前
	 *
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		/**
		 * 可以根据需要在这里进行某个bean的扩展
		 */
		if (bean instanceof TestController) {
			System.out.println("-----------------------[BeanPostProcessPoint]  扩展点演示 # postProcessBeforeInitialization  开始--------------------------------------");
			System.out.println("[BeanPostProcessPoint]  扩展点演示 # postProcessBeforeInitialization , crurrentBeanName: " + beanName);
			System.out.println("这里只有当bean是TestController时候才打印 否则的话控制台要爆满了 根本看不清 ");
			System.out.println("时机 bean实例化后，初始化之前");
			System.out.println("-----------------------[BeanPostProcessPoint]  扩展点演示 # postProcessBeforeInitialization  结束--------------------------------------");
			System.out.println();
		}
		return bean;
	}

	/**
	 * bean实例化之后
	 *
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		/**
		 * 可以根据需要在这里进行某个bean的扩展
		 */
		if (bean instanceof TestController) {
			System.out.println("-----------------------[BeanPostProcessPoint]  扩展点演示 # postProcessAfterInitialization  开始--------------------------------------");
			System.out.println("[BeanPostProcessPoint]  扩展点演示 # postProcessAfterInitialization , crurrentBeanName: " + beanName);
			System.out.println("这里只有当bean是TestController时候才打印 否则的话控制台要爆满了 根本看不清 ");
			System.out.println("时机 bean实例化时候");
			System.out.println("-----------------------[BeanPostProcessPoint]  扩展点演示 # postProcessAfterInitialization  结束--------------------------------------");
			System.out.println();
		}
		return bean;
	}



	/*

	AbstractAutowireCapableBeanFactory的这个方法


	protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
        if (System.getSecurityManager() != null) {
            AccessController.doPrivileged(() -> {
                this.invokeAwareMethods(beanName, bean);
                return null;
            }, this.getAccessControlContext());
        } else {
            this.invokeAwareMethods(beanName, bean);
        }

        Object wrappedBean = bean;
        if (mbd == null || !mbd.isSynthetic()) {
            wrappedBean = this.applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        }

        try {
            this.invokeInitMethods(beanName, wrappedBean, mbd);
        } catch (Throwable var6) {
            throw new BeanCreationException(mbd != null ? mbd.getResourceDescription() : null, beanName, "Invocation of init method failed", var6);
        }

        if (mbd == null || !mbd.isSynthetic()) {
            wrappedBean = this.applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        }

        return wrappedBean;
    }


     public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;

        Object current;
        for(Iterator var4 = this.getBeanPostProcessors().iterator(); var4.hasNext(); result = current) {
            BeanPostProcessor processor = (BeanPostProcessor)var4.next();
            current = processor.postProcessBeforeInitialization(result, beanName); //TODO 回调postProcessBeforeInitialization方法 在此处  postProcessAfterInitialization方法就不看了 一样的逻辑
            if (current == null) {
                return result;
            }
        }

        return result;
    }

	 */
}
