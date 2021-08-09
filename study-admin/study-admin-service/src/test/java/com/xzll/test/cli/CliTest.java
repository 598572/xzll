package com.xzll.test.cli;

import org.apache.commons.cli.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 12:07
 * @Description: 读取命令行参数示例  rocketmq中大量使用了apache的cli工具读取命令行参数 所以在这里做个小示例
 */
public class CliTest {
    //args 的设置见 study-test/src/test/java/com/xzll/test/cli/cli示例.png
    public static void main(String[] args) {

        //这里不使用写死的 直接在idea启动时候的命令行设置
//        String[] arg = {"-h", "-c", "config.xml", "-n", "我是参数"};

        testOptions(args);
    }


    public static void testOptions(String[] args) {
        Options options = new Options();
        Option opt = new Option("h", "help", true, "Print help");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("c", "configFile", true, "Name server config properties file");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("p", "printConfigItem", false, "Print all config item");
        opt.setRequired(false);
        options.addOption(opt);

        //当 hasArg=true时候 可以设置value ************
        opt = new Option("n", "nameServerAdd", true, "Print nameServerAdd");
        opt.setRequired(false);
        options.addOption(opt);

        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        CommandLine commandLine = null;
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                // 打印使用帮助
                hf.printHelp("testApp", options, true);
            }

            //打印opts的名称和值
            System.out.println("--------------------------------------");
            Option[] opts = commandLine.getOptions();
            if (opts != null) {
                for (Option opt1 : opts) {
                    String name = opt1.getLongOpt();
                    String value = commandLine.getOptionValue(name);
                    System.out.println(name + "=>" + value);
                }
            }
            //打印命令行设置的参数
            List<String> argList = commandLine.getArgList();
            if (CollectionUtils.isNotEmpty(argList)) {
                argList.forEach(x -> {
                    System.out.printf("命令行的参数：%s", x);
                });
            }
        } catch (ParseException e) {
            hf.printHelp("testApp", options, true);
        }

        /*
        多个
        usage: testApp [-c <arg>] [-h <arg>] [-n <arg>] [-p]
         -c,--configFile <arg>      Name server config properties file
         -h,--help <arg>            Print help
         -n,--nameServerAdd <arg>   Print nameServerAdd
         -p,--printConfigItem       Print all config item
        --------------------------------------
        nameServerAdd=>12340999
        help=>我是h
        命令行的参数：auto=true命令行的参数：h_key=k_value

         */

        // 关闭钩子，在关闭前处理一些操作
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private volatile boolean hasShutdown = false;
            private AtomicInteger shutdownTimes = new AtomicInteger(0);

            @Override
            public void run() {
                synchronized (this) {
                    if (!this.hasShutdown) {
                        System.out.println();
                        System.out.println("jvm关闭前的钩子");
                    }
                }
            }
        }, "ShutdownHook"));

    }
}
