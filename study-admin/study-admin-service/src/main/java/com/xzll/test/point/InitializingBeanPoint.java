package com.xzll.test.point;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * bean实例化的时候 AbstractAutowireCapableBeanFactory 类的 initializeBean方法 中的 invokeInitMethods(beanName, wrappedBean, mbd);
 *
 */
@Component
public class InitializingBeanPoint implements InitializingBean {
	public InitializingBeanPoint() {
		System.out.println();
		System.out.println("################## InitializingBeanPoint 的构造方法 ################## ");
		System.out.println();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("-----------------------[InitializingBeanPoint]  扩展点演示 # afterPropertiesSet  开始--------------------------------------");
		System.out.println("[InitializingBean] # afterPropertiesSet");
		System.out.println("时机: bean实例化后 AbstractAutowireCapableBeanFactory 类的 initializeBean方法 中的 invokeInitMethods(beanName, wrappedBean, mbd);");
		System.out.println("-----------------------[InitializingBeanPoint]  扩展点演示 # afterPropertiesSet  结束--------------------------------------");
		System.out.println();
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


    protected void invokeInitMethods(String beanName, Object bean, @Nullable RootBeanDefinition mbd) throws Throwable {
        boolean isInitializingBean = bean instanceof InitializingBean;
        if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
            }

            if (System.getSecurityManager() != null) {
                try {
                    AccessController.doPrivileged(() -> {
                        ((InitializingBean)bean).afterPropertiesSet();
                        return null;
                    }, this.getAccessControlContext());
                } catch (PrivilegedActionException var6) {
                    throw var6.getException();
                }
            } else {
                ((InitializingBean)bean).afterPropertiesSet();//TODO 回调在此处
            }
        }

        if (mbd != null && bean.getClass() != NullBean.class) {
            String initMethodName = mbd.getInitMethodName();
            if (StringUtils.hasLength(initMethodName) && (!isInitializingBean || !"afterPropertiesSet".equals(initMethodName)) && !mbd.isExternallyManagedInitMethod(initMethodName)) {
                this.invokeCustomInitMethod(beanName, bean, mbd);
            }
        }

    }
	 */
}
