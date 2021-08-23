package com.xzll.test.point.aware;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

/**
 * 时机 实例化bean后 ，bean初始化时候
 * 具体位置见下边
 */
@Component
public class BeanNameAwarePoint implements BeanNameAware {

	@Override
	public void setBeanName(String name) {
		System.out.println("------------BeanNameAwarePoint # setBeanName 开始-------------");
		System.out.println("[BeanNameAwarePoint]  扩展点演示 # setBeanName name: "+name);
		System.out.println("------------BeanNameAwarePoint # setBeanName 结束-------------");
		System.out.println();
	}

/*

AbstractAutowireCapableBeanFactory  类的


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


    private void invokeAwareMethods(String beanName, Object bean) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware)bean).setBeanName(beanName); //TODO 此处回调 BeanNameAware 的 setBeanName方法
            }

            if (bean instanceof BeanClassLoaderAware) {
                ClassLoader bcl = this.getBeanClassLoader();
                if (bcl != null) {
                    ((BeanClassLoaderAware)bean).setBeanClassLoader(bcl);//TODO 此处回调 BeanClassLoaderAware 的 setBeanName方法
                }
            }

            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware)bean).setBeanFactory(this);//TODO 此处回调 BeanFactoryAware 的 setBeanName方法
            }
        }

    }


 */

}
