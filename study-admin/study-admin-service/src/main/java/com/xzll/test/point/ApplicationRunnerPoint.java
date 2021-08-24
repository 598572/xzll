package com.xzll.test.point;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerPoint implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("-----------------------[ApplicationRunnerPoint]  扩展点演示 # run  开始--------------------------------------");
		System.out.println("[ApplicationRunnerPoint] # run ; "+"时机：此时已经刷新容器处于run方法的后半部分了 接下来run方法将发布running事件");
		System.out.println("-----------------------[ApplicationRunnerPoint]  扩展点演示 # run  结束--------------------------------------");
		System.out.println();
	}
	/*

	SpringApplication 类的 run方法中的 callRunners


	private void callRunners(ApplicationContext context, ApplicationArguments args) {
        List<Object> runners = new ArrayList();
        runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
        runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
        AnnotationAwareOrderComparator.sort(runners);
        Iterator var4 = (new LinkedHashSet(runners)).iterator();

        while(var4.hasNext()) {
            Object runner = var4.next();
            if (runner instanceof ApplicationRunner) {
                this.callRunner((ApplicationRunner)runner, args);
            }

            if (runner instanceof CommandLineRunner) {
                this.callRunner((CommandLineRunner)runner, args);
            }
        }

    }

	 */
}
