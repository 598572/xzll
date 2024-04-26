package com.xzll.agent.config.po;


import com.xzll.agent.config.advice.ForkJoinPoolMonitorAdvice;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @since 2.11.0
 */
public class ClassInfo {
    private final String className;
    private final byte[] classFileBuffer;
    private final ClassLoader loader;

    public ClassInfo( String className,
                       byte[] classFileBuffer, ClassLoader loader) {
        this.className = className;
        this.classFileBuffer = classFileBuffer;
        this.loader = loader;
    }

    public String getClassName() {
        return className;
    }

    private CtClass ctClass;



    public CtClass getCtClass() throws IOException {
        if (ctClass != null) return ctClass;

        final ClassPool classPool = new ClassPool(true);
        if (loader == null) {
        	//类加载器不变 还是使用应用类加载器 即 appClassLoader 当时classpath需要指定为 study-agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar
			//这个jar，否则植入从是成功的，但是运行时找不到植入的类
            classPool.appendClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
        } else {
            classPool.appendClassPath(new LoaderClassPath(loader));
        }
        final CtClass clazz = classPool.makeClass(new ByteArrayInputStream(classFileBuffer), false);
        clazz.defrost();
		classPool.insertClassPath(new ClassClassPath(ForkJoinPoolMonitorAdvice.class));
        this.ctClass = clazz;
        return clazz;
    }

    private boolean modified = false;

    public boolean isModified() {
        return modified;
    }

    public void setModified() {
        this.modified = true;
    }
}
