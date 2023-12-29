package com.xzll.test.javajuc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/26 10:56
 * @Description: 看到掘进上有个很好的记忆方法 总结的确实比较不错 在此引用一下 哈哈
 * <p>
 * 1. CompletableFuture 记忆规律: 来源: https://juejin.cn/post/6844904195162636295  另外个不错的文章  https://www.programcreek.com/java-api-examples/?class=java.util.concurrent.CompletableFuture&method=complete
 * <p>
 * 以Async结尾的方法，都是异步方法，对应的没有Async则是同步方法，一般都是一个异步方法对应一个同步方法。
 * 以Async后缀结尾的方法，都有两个重载的方法，一个是使用内容的forkjoin线程池，一种是使用自定义线程池
 * 以run开头的方法，其入口参数一定是无参的，并且没有返回值，类似于执行Runnable方法。
 * 以supply开头的方法，入口也是没有参数的，但是有返回值
 * 以Accept开头或者结尾的方法，入口参数是有参数，但是没有返回值
 * 以Apply开头或者结尾的方法，入口有参数，有返回值
 * 带有either后缀的方法，表示谁先完成就消费谁
 * <p>
 * <p>
 * 2. API方法分类与记忆规律
 * CompletableFuture提供了方法大约有50多个，单纯一个个记忆，是很麻烦的，因此将其划分为以下几类：
 * <p>
 * 2.1  : 创建类
 * <p>
 * completeFuture 可以用于创建默认返回值
 * runAsync 异步执行，无返回值
 * supplyAsync 异步执行，有返回值
 * anyOf 任意一个执行完成，就可以进行下一步动作
 * allOf 全部完成所有任务，才可以进行下一步任务
 * <p>
 * 2.2  : 状态取值类
 * <p>
 * join 合并结果，等待
 * get 合并等待结果，可以增加超时时间;get和join区别，join只会抛出unchecked异常，get会返回具体的异常
 * getNow 如果结果计算完成或者异常了，则返回结果或异常；否则，返回valueIfAbsent的值
 * isCancelled
 * isCompletedExceptionally
 * isDone
 * <p>
 * 2.3  : 控制类 用于主动控制CompletableFuture的完成行为
 * <p>
 * complete
 * completeExceptionally
 * cancel
 * <p>
 * 2.4 : 接续类 CompletableFuture 最重要的特性，没有这个的话，CompletableFuture就没意义了，用于注入回调行为。
 * <p>
 * thenApply, thenApplyAsync
 * thenAccept, thenAcceptAsync
 * thenRun, thenRunAsync
 * thenCombine, thenCombineAsync
 * thenAcceptBoth, thenAcceptBothAsync
 * runAfterBoth, runAfterBothAsync
 * applyToEither, applyToEitherAsync
 * acceptEither, acceptEitherAsync
 * runAfterEither, runAfterEitherAsync
 * thenCompose, thenComposeAsync
 * whenComplete, whenCompleteAsync
 * handle, handleAsync
 * exceptionally
 */
@Slf4j
public class CompletableFutureTest extends JucCommonTest {

    /*
    在学习这个之前 先了解几个函数式接口
    (最后总结完 CompletableFuture后 发现理解CompletableFutureTest这些方法的关键 就是理解这几个函数式接口 其实只有知道每个函数式接口的用途就很好理解 CompletableFuture了 )
    (暂时只是概念了解后面会写代码演示如何使用 以及如何自定义函数式接口)  参考: https://juejin.cn/post/6854573213108666381?share_token=c92d7a3c-c4c8-400f-a1c0-942ef892afe5

    0. Runnable 不传参数 也没返回值

    1. Function<T, R> 接受一个参数，并且有返回值
    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t);
    }

    2. Consumer 接受一个参数，没有返回值
    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t);
    }

    3. Supplier 没有参数，有一个返回值
    @FunctionalInterface
    public interface Supplier<T> {
        T get();
    }

    4. BiConsumer<T, U> 接受两个参数（Bi， 英文单词词根，代表两个的意思），没有返回值
    @FunctionalInterface
    public interface BiConsumer<T, U> {
        void accept(T t, U u);

    5. 还有很多 BiFunction ....等等各种函数式接口 后续会对其进行总结和代码演示 革命尚未成功 同志任须努力啊!!!!!!!!

     */

    /*
    在演示之前 我们先看关于CompletableFuture常用这些方法的总结 (先总结 后演示哈!!!!!)  此总结在自我理解的基础上参考了 https://juejin.cn/post/6854573213108666381
    另外为了突出高亮的总结 全部使用 TODO 来突出总结语句
    1.串行关系:
        then 直译【然后】，也就是表示下一步，所以通常是一种串行关系体现, then 后面的单词（比如 run /apply/accept）就是上面说的函数式接口中的抽象方法名称了，它的作用和那几个函数式接口的作用是一样一样滴

        TODO 前一步只要完成了 ，那么我就执行， 且不需要前一步的返回结果
        CompletableFuture<Void> thenRun(Runnable action)
        TODO (异步) ps:下边的同理 只要带Async结尾且  都是这个意思
        CompletableFuture<Void> thenRunAsync(Runnable action)
        TODO (异步使用自己配置的线程池) ps:下边的同理 只要带Async结尾且支持Executor入参的 都是这个意思
        CompletableFuture<Void> thenRunAsync(Runnable action, Executor executor)

        TODO 前一步只要完成了 ，那么我就执行， 且需要传入上一步的返回值 (如果上一步阻塞或者抛出异常 那么我不执行) ，有返回值
        <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
        <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
        <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)

        TODO 前一步只要完成了 ，那么我就执行， 且需要传入上一步的返回值 (如果上一步阻塞或者抛出异常 那么我不执行) 无返回值 纯消费
        CompletableFuture<Void> thenAccept(Consumer<? super T> action)
        CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action)
        CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor)

        TODO 方法允许你对两个 CompletableFuture 进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作。
         而且返回两个CompletableFuture的结果 相当于打平, 与lambda的 flatMqp有相似的道理       (和 thenCombine 有点类似)     >>>>>>>>>>>>>>>> 可以理解为 串联
        <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn)
        <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn)
        <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor)

    2.聚合 And 关系
        (combine... with... 和 both...and... 都是要求两者都满足，也就是 and 的关系了)

        TODO 合并 两个 CompletableFuture， 多个 CompletableFuture的合并使用 anyOf或者 allOf (和 thenCompose 有点类似)    >>>>>>>>>>>>>>>> 可以理解为 并联
        <U,V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)
        <U,V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)
        <U,V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn, Executor executor)

        TODO 消费两个CompletableFuture的结果 适用于合并任务并消费任务结果 和thenCombine有点像 但是thenCombine有返回值 thenAcceptBoth没返回值
        <U> CompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action)
        <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action)
        <U> CompletableFuture<Void> thenAcceptBothAsync( CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action, Executor executor)

        TODO runAfterBoth是当两个 CompletableFuture 都正常完成计算的时候,执行一个Runnable，这个Runnable并不使用计算的结果
        CompletableFuture<Void> runAfterBoth(CompletionStage<?> other, Runnable action)
        CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action)
        CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor)

    3. 聚合 Or 关系
        Either...or... 表示两者中的一个，自然也就是 Or 的体现了

        TODO 方法是当任意一个CompletionStage完成的时候，fn会被执行，它的返回值会当作新的CompletableFuture<U>的计算结果。
        <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn)
        <U> CompletableFuture<U> applyToEitherAsync(、CompletionStage<? extends T> other, Function<? super T, U> fn)
        <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn, Executor executor)

        TODO 当任意一个CompletionStage完成的时候，action这个消费者就会被执行。这个方法返回 CompletableFuture<Void>
        CompletableFuture<Void> acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action)
        CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action)
        CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor)

        TODO 任意一个 CompletableFuture完成 就执行 传入一个 Runnable
        CompletableFuture<Void> runAfterEither(CompletionStage<?> other, Runnable action)
        CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action)
        CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor)

    4. 异常处理

        TODO 可以理解为 catch块 会捕获异常 并有返回值 因为是 Function
        CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn)
        CompletableFuture<T> exceptionallyAsync(Function<Throwable, ? extends T> fn)
        CompletableFuture<T> exceptionallyAsync(Function<Throwable, ? extends T> fn, Executor executor)

        TODO 可以理解为 catch块 会捕获异常 但是不会有返回值(因为是BiConsumer) 只返回上一个调用他的 CompletableFuture
        CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action)
        CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action)
        CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor)


        TODO 可以简单理解为 finally 块 会捕获异常 且有返回值( BiFunction 说明传入一对参数 )，可以根据异常进行业务处理，如 重试/通知/抛出等等 所以这个个人感觉比较常用哦!!!!!!!
        <U> CompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> fn)
        <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn)
        <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn, Executor executor)

    5. 校验状态
        isDone 用来返回 future 对象是否已经完成
        isCancelled 用来返回 future 对象是否已经被取消
        isCompletedExceptionally 用来返回 future 是否出现了异常

     */


    /**
     * CompletableFuture.runAsync
     * <p>
     * 使用ForkJoinPool.commonPool()作为它的线程池执行异步代码。 无返回值
     */
    @Test
    public void runAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

            System.out.println(getCurrentThreadName() + "Hello");
        });
        try {
            Void unused = future.get();
            System.out.println("证明 runAsync 方法是无返回值的 unused : " + unused);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(getCurrentThreadName() + "CompletableFuture");
    }

    /**
     * CompletableFuture.supplyAsync
     * <p>
     * 使用ForkJoinPool.commonPool()作为它的线程池执行异步代码，异步操作有返回值
     */
    @Test
    public void supplyAsync() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " <--- supplyAsync当前线程");
            return "Hello";
        });
        try {
            //调用  future.get()  ，主线程将等待异步线程的返回结果
            System.out.println(getCurrentThreadName() + "supplyAsync的返回值: " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(getCurrentThreadName() + "CompletableFuture");

    }

    /**
     * 注意: complete()方法特点 (源码注释翻译 ： 如果尚未完成，则将get()和相关方法返回的值设置为给定值 即 complete方法的入参)
     * <p>
     * 如果吧 sleep去掉 那么将打印 World (也就说明 complete是立即执行的 不等待supplyAsync的返回结果 )
     * 加上sleep后 将打印 Hello 说明 future如果执行完毕能够返回结果，此时再调用complete(T t)则会无效。
     *
     * @throws InterruptedException
     */
    @Test
    public void complete() throws InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
        Thread.sleep(2000);
        future.complete("World");

        try {
            System.out.println(getCurrentThreadName() + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * completeExceptionally源码注释翻译 ： (如果尚未完成，则导致调用get()和相关方法抛出给定的异常 )
     * <p>
     * 如果使用completeExceptionally(Throwable ex)则抛出一个异常，而不是一个成功的结果。
     *
     * @throws InterruptedException
     */
    @Test
    public void completeExceptionally() throws InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
        Thread.sleep(5000); //如果这里不等待 那么future.get();时候将抛出异常 ， 如果等待 那么将正常打印 Hello
        future.completeExceptionally(new Exception());

        try {
            //future.get() 时候将抛出异常
            System.out.println(getCurrentThreadName() + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化。(串行的后续操作并不一定会和前序操作使用同一个线程)
     * <p>
     * 返回一个新的 CompletionStage，当这个阶段正常完成时，将这个阶段的结果作为提供函数的参数执行。
     * <p>
     * 下一个thenApply依赖上一个的返回结果 且由(main) 主线程执行 thenApply ; 而 thenApplyAsync 是由默认线程池执行
     * <p>
     * 有关涵盖异常完成的规则，请参阅 CompletionStage 文档。
     * <p>
     * 入参 :
     * 出参 :
     *
     * @throws InterruptedException
     */
    @Test
    public void thenApply() {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {

//                    注意: 如果将此sleep打开 那么后边两个thenApply的执行线程将变为    ForkJoinPool.commonPool-worker-1 思考 为什么???
//                    答案: TODO  目前我还不好给出解释 呵呵呵 以后研究 总之得出结论: (串行的后续操作并不一定会和前序操作使用同一个线程)
//                    try {
//                        Thread.sleep(5);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    System.out.println("supplyAsync方法的当前线程: " + Thread.currentThread().getName());
                    return "---我是supplyAsync方法的返回结果---; ";
                })
                .thenApply(s -> {
                    System.out.println("第一个thenApply的当前线程: " + Thread.currentThread().getName());
                    return s + "---我是第一个thenApply方法的返回结果---; ";
                })
                .thenApply(r -> {
                    System.out.println("第二个thenApply的当前线程: " + Thread.currentThread().getName());
                    return r + "---我是第二个thenApply方法的返回结果---; ";
                });

        System.out.println("我是主线程!!!!!!!!!!");
        try {
            System.out.print("主线程阻塞等待结果... : ");
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * thenApplyAsync 可以 异步执行 supplyAsync返回的结果 并且还可以传递给下一个 thenApplyAsync
     * 某个流程出现异常 后续将不会执行 注意: 但是主线程任然后执行 线程间互不影响哦 !!!!!!!!!!
     */
    @Test
    public void thenApplyAsync() {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("supplyAsync方法的当前线程: " + Thread.currentThread().getName());
                    return "---我是supplyAsync方法的返回结果---; ";
                })
                .thenApplyAsync(s -> {
                    System.out.println("第一个thenApplyAsync的当前线程: " + Thread.currentThread().getName());
                    // int i=1/0; 可以看到 如果某个 thenApplyAsync执行时候出异常 那么后续的thenApplyAsync(或者其他的处理)将不会执行
                    // 这是thenApplyAsync 或者 thenApply 与handle最大的区别 !!!!!!!!!!!!!!!
                    return s + "---我是第一个thenApplyAsync方法的返回结果---; ";
                })
                .thenApplyAsync(r -> {
                    System.out.println("第二个thenApplyAsync的当前线程: " + Thread.currentThread().getName());
                    return r + "---我是第二个thenApplyAsync方法的返回结果---; ";
                });

        System.out.println("我是主线程!!!!!!!!!!");
        try {
            System.out.print("主线程阻塞等待结果... : ");
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    /**
     * 原生方式演示 不使用 lambda
     */
    @Test
    public void nativityWay() {
        CompletableFuture<Long> future2 = CompletableFuture
                .supplyAsync(new Supplier<Long>() {
                    //Supplier  结果的提供者。 get();返回一个结果
                    @Override
                    public Long get() {
                        long result = new Random().nextInt(10);
                        System.out.println("原生的方式 (不使用lambda) " + result);
                        return result;
                    }
                }).thenApply(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long t) {
                        long result = t * 5;
                        System.out.println("原生的方式 (不使用lambda) " + result);
                        return result;
                    }
                });
        try {
            System.out.println(future2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * thenAccept  接收任务的处理结果，并消费处理，无返回结果。
     * 可以看出，该方法只是消费执行完成的任务，并可以根据上面的任务返回的结果进行处理。并没有后续的输错操作。
     */
    @Test
    public void thenAccept() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "测试 thenAccept 这个方法";
            }
        }).thenAccept(str -> {
            System.out.println("我(thenAccept)是消费方法(负责消费上一个阶段的返回结果):  " + str);
        });
        try {
            System.out.println("future.get();返回的结果 : " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * thenRun 纯消费 ，且不依赖上个流程的结果，适用于不依赖上个结果的(线程)串行调用
     */
    @Test
    public void thenRun() {

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "测试 thenRun 这个方法";
            }
        }).thenRun(() -> {
            System.out.println("我(thenRun)是消费方法(我不依赖上个任务的结果，不需要传递给我上个任务的结果 ，" +
                    "我就是纯碎的 消费 无返回值，且不依赖上个任务的结果 yes 就是这样):  ");
        });
        try {
            System.out.println("future.get();返回的结果 : " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并 两个 CompletableFuture， 多个 CompletableFuture的合并使用 anyOf或者 allOf
     */
    @Test
    public void thenCombine() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "我是 future1";
            }
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "我是 future2";
            }
        });
        CompletableFuture<String> result = future1.thenCombine(future2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String future1Result, String future2Result) {
                return "我是thenCombine我将合并多个future ; " + future1Result + " ; " + future2Result;
            }
        });
        try {
            System.out.println("" + result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     * 消费两个CompletableFuture的结果 适用于合并任务并消费任务结果
     */
    @Test
    public void thenAcceptBoth() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("我是future1");
            return "我是future1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("我是future2");
            return "我是future2";
        });
        //future1和future2都执行完后 调用 thenAcceptBoth一块消费二者的返回结果
        future1.thenAcceptBoth(future2, new BiConsumer<String, String>() {
            @Override
            public void accept(String t, String u) {
                System.out.println("我是thenAcceptBoth，我消费你俩===>>> future1: " + t + " ; future2: " + u + ";");
            }
        });
    }

    /**
     * applyToEither 任意一个完成(谁快就用谁的) 那么就执行 Function， applyEither是 Consumer 消费 不需要参数
     */
    @Test
    public void applyToEither() throws ExecutionException, InterruptedException {
        Random rand = new Random();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        });
        CompletableFuture<String> f = future.applyToEither(future2, i -> i.toString());
        System.out.println("任意一个执行完我就执行 谁执行快我就用谁的结果 : " + f.get());//结果 有时候会100 有时候会是200 ，谁返回的快就用谁的结果
    }

    /**
     * thenCompose 方法允许你对两个 CompletableFuture 进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作。 而且返回两个
     * CompletableFuture的结果 相当于 打平 与lambda的 flatMqp有相似的道理
     */
    @Test
    public void thenCompose() {
        CompletableFuture<String> f = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "我是supplyAsync方法的返回结果; ";
            }
        }).thenCompose(param -> CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "我是thenCompose 我将上个阶段的结果汇总: " + param + "我是thenCompose方法的返回结果; ";
            }
        }));
        try {
            System.out.println("thenCompose result : " + f.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注意: 该方法 (thenComposeVariant ) 是 上边thenCompose方法的    变种!!!!!!
     * <p>
     * thenCompose合并两个CompletableFuture thenApply可以处理合并后的结果 ， 但是如果有一个CompletableFuture 阻塞 那么thenApply不会执行，
     * 此时 anyOf就派上用场了 比如有n个线程去抓取n个网站的页面 ， 然后只要有一个返回结果 那么就去分析 ，此时可以用 anyOf 似乎也可以使用 applyToEither (谁先返回用谁的结果)
     */
    @Test
    public void thenComposeVariant() throws ExecutionException, InterruptedException {
        CompletableFuture<String> ff1 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "我是ff1 我负责去京东抓取页面; ";
            }
        });

//        CompletableFuture<String> ff2 = CompletableFuture.supplyAsync(new Supplier<String>() {
//            @Override
//            public String get() {
//                return "我是ff1 我负责去抖音抓取页面; ";
//            }
//        });
//        演示: 合并两个 CompletableFuture结果 如果使用 thenApply 会形成嵌套 所以并不适用 要使用 thenCompose(打平 CompletableFuture) 才是解决办法
//        CompletableFuture<CompletableFuture<String>> completableFutureCompletableFuture = ff1.thenApply(param -> ff2);

        //使用thenCompose合并二者结果 在进行下一步操作
        CompletableFuture<String> future = ff1.
                thenCompose(param -> CompletableFuture
                        .supplyAsync(new Supplier<String>() {
                            @Override
                            public String get() {
                                //如果在此处sleep一万年 , 那么后续的 thenApply将等1万年 哈哈
//                                try {
//                                    Thread.sleep(500000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                                return param + "  我是ff2 我负责去抖音抓取页面";
                            }
                        }))
                .thenApply(param -> {
                    //thenApply只是在上边操作都完成的时候才执行 ， 如果想要任意一个执行完就执行后边的逻辑 就需要用 anyOf方法了
                    System.out.println("使用抓取到的数据进行分析,抓取的数据 : " + param);
                    return "分析完后返回";
                });
        System.out.println("thenCompose result : " + future.get());

    }

    //当然这里多数时处理两个 Future 的关系，如果超过两个Future，如何处理他们的一些聚合关系呢？


    /**
     * anyOf 只要有一个(肯定是最先完成的)完成，则完成，(如果最先完成这个抛出异常)，则携带异常
     * <p>
     * allOf  等待所有的CompletableFuture都执行完成
     */
    @Test
    public void anyOfAndAllOf() throws ExecutionException, InterruptedException {
        Random rand = new Random();
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(rand.nextInt(100));//不能写成一样的和future2 那样的话 总是future1先抢到cpu 总是future1先执行 anyOf就看不出来不同了
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 放开这个时候 ， anyOf将抛出异常(如果正好是future1先执行完的话，而如果是future2先执行完，那么anyOf将正常打印)
            // 而allOf则只要其中一个future有异常 在get结果时候就会抛出!!!!!
            //int i = 3 / 0;
            return 9999;
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(rand.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "我是future2";
        });

        //anyOf 只要有一个完成，则完成，如果最先完成这个有异常则抛出异常(无异常，则future.get(); 正常执行)
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2);
        System.out.println("我是anyOf,只要有一个future完成 我就执行，如果最先完成这个抛出异常， 那么我也会抛出异常 f.get(): " + anyOf.get());

        //allOf  等待所有的CompletableFuture都执行完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2);
        System.out.println("我是allOf,我负责等待所有传入的future完成(如果有一个抛出异常 那么我在get时候也会抛出异常) 然后才继续下一步 f.get(): " + allOf.get());

    }

    /**
     * exceptionally 对异常进行捕获处理 相当于 catch 块 ，这回理解了吧
     */
    @Test
    public void exceptionally() throws ExecutionException, InterruptedException {
        int age = 901;
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            if (age < 0) {
                throw new IllegalArgumentException("何方神圣？");
            }
            if (age > 18) {
                return "大家都是成年人";
            } else {
                return "未成年禁止入内";
            }
        }).thenApply((str) -> {
            log.info("游戏开始");
            int i = 9 / 0;
            return str;
        }).exceptionally(ex -> {
            log.info("我是 exceptionally (相当于catch块)用来捕获异常的异常被我捕获: errorMsg: " , ex);
            return null;
        });
        System.out.println("如果出现异常 ，exceptionally不会抛出异常 而会返回异常时候我们指定的返回值. " + future1.get());
    }

	@Test
	public void exceptionallyTest2() throws ExecutionException, InterruptedException {
		int age = 901;
		CompletableFuture.runAsync(() -> {
			throw  new NullPointerException();
		}).exceptionally(ex -> {
			log.info("我是 exceptionally (相当于catch块)用来捕获异常的异常被我捕获: errorMsg: " , ex);
			return null;
		});
	}

    /**
     * 当上边几个流程出现异常时候 可处理异常 但是不会没返回值 且如果调用future1.get();方法 会抛出异常!! 而 exceptionally不会抛出这是很重要的区别
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            int i1 = 1 / 0;
            return "我是supplyAsync返回值";
        }).thenApply((str) -> {
            int i = 9 / 0;
            return str;
        }).whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {

                if (throwable != null) {
                    System.out.println("出现异常!: " + throwable.getMessage());
                }
                System.out.println("当完成时候");
            }
        });
        //注意 如果在这里调用 future1.get()方法 那么将会 抛出异常
        //System.out.println(future1.get());
    }

	public static void main(String[] args) {
		String s="{\n" +
				"    \\\"chatId\\\": \\\"200-4-2766-1-64\\\"," +
				"    \\\"fromId\\\": \\\"10086\\\"," +
				"    \\\"fromUserType\\\": 1," +
				"    \\\"toId\\\": \\\"100\\\"," +
				"    \\\"toUserType\\\": 4," +
				"    \\\"chatType\\\": 4," +
				"    \\\"op\\\":\\\"getMember\\\"" +
				"}";
		System.out.println(s);
	}
    /**
     * 这个handle(); 一般用的比较多哦!!!!!!!!!!!!
     *
     * handle 是执行任务完成时对结果的处理。!!!!!!!!!!!!!!!!!!!!
     * handle 方法和 thenApply 方法处理方式基本一样。
     * !!!! (重要区别) :  不同的是 handle 是在任务完成后再执行，还可以处理异常的任务。thenApply 只可以执行正常的任务，任务出现异常则不执行后续的 thenApply 方法。
     */
    @Test
    public void handle() throws ExecutionException, InterruptedException {

    	CompletableFuture.runAsync(()->{
			System.out.println(1/0);
//			System.out.println(1/9);
		}).handle((unused, throwable) -> {
			if (throwable!=null){
				log.error("出错啦:",throwable);
			}
			return null;
		});

//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
//            @Override
//            public Integer get() {
//                int i = 10 / 0;
//                return new Random().nextInt(10);
//            }
//        }).handle(new BiFunction<Integer, Throwable, Integer>() {
//            //handle 可以处理 出异常和正常运行这两种情况 而thenApply就不行 出异常后不执行后边的
//            @Override
//            public Integer apply(Integer param, Throwable throwable) {
//                int result = -1;
//                if (throwable == null) {
//                    System.out.println(getCurrentThreadName() + "上个任务没出异常 ");
//                    result = param * 2;
//                } else {
//                    System.out.println(getCurrentThreadName() + "上个任务出异常啦! " + throwable.getMessage() + " ; 这里可以执行出异常后的逻辑! ");
//                }
//                return result;
//            }
//        }).thenApply((Integer s) -> {
//            System.out.println("上个流程出异常了! 但是我任然可以获取到出异常后的返回值! 上个流程的返回值是: " + s);
//            return s;
//        });
//        System.out.println("我是主线程获取到的future结果是(当出现异常时候 调用get方法将获取到异常时候我们设定的返回值) : " + future.get());
    }

    /**
     * 演示 get();方法超时时候的异常处理
     */
    @Test
    public void getTimeOut() {
        //获取当前方法
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        System.out.println(methodName + "  00000 ");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("睡他个一万年");
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "睡他个一万年";
        });

        try {
            System.out.println("我是主线程 我先获取future的返回结果 不过我只等待3000ms 超时后我就不等了 mmp 不给拉倒: " + future.get(3000, TimeUnit.MILLISECONDS));
        } catch (TimeoutException e) {
            System.out.println("我是主线程 我先获取future的返回结果 不过我只等待3000ms 超时后我就不等了," +
                    " mmp 不给拉倒 出现异常了 我捕获住，继续处理其他事情 (记住不能不捕获呀 不捕获会中断程序的哦 )，errorMsg : " + e);
            //e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    /**
     * isDone 用来返回 future 对象是否已经完成
     */
    @Test
    public void isDone() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "我是supplyAsync方法返回的");
        System.out.println("done?" + future.isDone() + " cancel?" + future.isCancelled() + " exp?" + future.isCompletedExceptionally());
        future.get();
        future.cancel(true);
        System.out.println("done?" + future.isDone() + " cancel?" + future.isCancelled() + " exp?" + future.isCompletedExceptionally());
        future.get();
    }



    /**
     * join VS  get (都是等待一个或者多个(当在allOf后边调用时候)的future得返回结果 get可以设置超时时间 join没有时间设置  )
     * 区别很小 参考这个博文 https://stackoverflow.com/questions/45490316/completablefuture-join-vs-get
     *
     * 总的来说就一句话 get();会抛出未检查异常，你可以处理或者抛出异常 但是 join();并不会 他只是在运行时候抛出异常
     *
     * 比如在构建多任务集 执行任务集结果后 需要获取结果，可以使用如下:
     *
     * List<ResultObject> futureResultList = completableFutureList.stream()
     *      .map(completableFuture -> completableFuture.join()).flatMap(List::stream).collect(Collectors.toList());
     * CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
     *
     */
    @Test
    public void joinVsGet() {
        List<String> messages = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8",
                "9", "10", "11");
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<String> mapResult = new ArrayList<>();

        List<CompletableFuture<String>> fanoutRequestList = new ArrayList<>(messages.size());
        for (String msg : messages) {
            CompletableFuture<String> future = CompletableFuture
                    .supplyAsync(() -> String.valueOf(10 / Integer.valueOf(msg))).exceptionally(ex -> "Error")
                    .handle((p1, throwable) -> {
                        if (throwable != null) {
                            return "出现异常了";
                        } else {
                            return p1;
                        }
                    });
            fanoutRequestList.add(future);
        }

//        try {
        //全部等待(个人理解) !!!!!!!!!!
//            CompletableFuture.allOf(fanoutRequestList).get();
//
//        } catch (InterruptedException | ExecutionException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        //第一种方式: 等待任务集完成 使用join();或者可以使用get();也行 (get可以设置超时时间)   //全部等待(个人理解) !!!!!!!!!! 一个个等待
        List<String> collect = fanoutRequestList.stream().map(future -> future.join()).collect(Collectors.toList());
        //等待所有任务集完成。
        CompletableFuture.allOf(fanoutRequestList.toArray(new CompletableFuture[fanoutRequestList.size()]));
        //获取到任务集结果后 (进行下一步处理 这里只做个打印就完事)
        collect.stream().forEach(x -> {
            System.out.println("(way1) , 这是任务集结果: " + x);
        });

        //第二种方式:   或者也可以这样 先等待所有任务集都完成 然后再获取结果
        CompletableFuture.allOf(fanoutRequestList.toArray(new CompletableFuture[0])).join();
        fanoutRequestList.forEach(y -> {
            try {
                String result = y.get();
                System.out.println("(way2) , 这是任务集结果: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

}
