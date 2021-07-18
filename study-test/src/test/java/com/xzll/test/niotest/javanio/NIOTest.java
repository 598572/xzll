package com.xzll.test.niotest.javanio;

import org.junit.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 13:01
 * @Description:
 */
public class NIOTest {

    @Test
    public void nioCreate() {
        //这三种都是在 HeapByteBuffer 即堆上申请空间

        //方式1：allocate方式直接分配，将会在堆内存申请buffer空间
        ByteBuffer allocate = ByteBuffer.allocate(20);
        //方式2：通过wrap根据一个已有的数组创建
        byte[] bytes = new byte[10];
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        //方式3：通过wrap根据一个已有的数组 *** 指定区间创建 ***
        ByteBuffer wrapoffset = ByteBuffer.wrap(bytes, 2, 5);//从指针为2的位置创建，长度为5

        //打印出刚刚创建的缓冲区的相关信息
        print(allocate, wrap, wrapoffset);
    }

    private static void print(Buffer... buffers) {
        /*
         一: Buffer 抽像类

            //JDK1.4时，引入的api
            public final int capacity( )//返回此缓冲区的容量
            public final int position( )//返回此缓冲区的位置
            public final Buffer position (int newPositio)//设置此缓冲区的位置
            public final int limit( )//返回此缓冲区的限制
            public final Buffer limit (int newLimit)//设置此缓冲区的限制
            public final Buffer mark( )//在此缓冲区的位置设置标记
            public final Buffer reset( )//将此缓冲区的位置重置为以前标记的位置
            public final Buffer clear( )//清除此缓冲区
            public final Buffer flip( )//反转此缓冲区
            public final int remaining( )//返回当前位置与限制之间的元素数
            public final boolean hasRemaining( )//告知在当前位置和限制之间是否有元素
            public abstract boolean isReadOnly( );//告知此缓冲区是否为只读缓冲区

            //JDK1.6时引入的api
            public abstract boolean hasArray();//告知此缓冲区是否具有可访问的底层实现数组
            public abstract Object array();//返回此缓冲区的底层实现数组
            public abstract int arrayOffset();//返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量
            public abstract boolean isDirect();//告知此缓冲区是否为直接缓冲区


            总结: 0 <= mark <= position <= limit <= capacity

         二: ByteBuffer 抽像类 继承 Buffer抽象类
            //缓冲区创建相关api
            public static ByteBuffer allocateDirect(int capacity)
            public static ByteBuffer allocate(int capacity)
            public static ByteBuffer wrap(byte[] array)
            public static ByteBuffer wrap(byte[] array,int offset, int length)

            //缓存区存取相关API
            public abstract byte get( );//从当前位置position上get，get之后，position会自动+1
            public abstract byte get (int index);//从绝对位置get
            public abstract ByteBuffer put (byte b);//从当前位置上普通，put之后，position会自动+1
            public abstract ByteBuffer put (int index, byte b);//从绝对位置上put


         三: clear()与compact()方法区别:

            一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。
            如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
            如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。
            如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数据，那么使用compact()方法。
            compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。

         */
        for (Buffer buffer : buffers) {
            System.out.println(
                    "capacity=" + buffer.capacity()
                            + ",limit=" + buffer.limit()
                            + ",position=" + buffer.position()
                            + ",hasRemaining:" + buffer.hasArray()
                            + ",remaining=" + buffer.remaining()
                            + ",hasArray=" + buffer.hasArray()
                            + ",isReadOnly=" + buffer.isReadOnly()
                            + ",arrayOffset=" + buffer.arrayOffset());
        }
    }

    /**
     * nio常用方法演示
     */
    @Test
    public void nioCreatePutGetFlipClearRemainingCapacityHasRemaining() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        print(buffer);
        buffer.put("helloword".getBytes());
        //put进去后 指针随元素位移
        print(buffer);

        System.out.println();
        System.out.println("---------使用flip翻转缓冲区，其实是把position移到0的位置，limit移到元素的最后一位-1 这样才能读取到数据 如果不翻转的话，直接get()会从当前position位置读取(当前position是put后最后元素的下一位置即hello后一位 position为5)，没数据的--------");
        //flip 翻转内存区域
        buffer.flip();


        //第一种读取方式
        byte[] b = new byte[buffer.capacity()];
        for (int i = 0; buffer.hasRemaining(); i++) {
            System.out.print(buffer + " 当前position: " + buffer.position());
            byte currentValue = buffer.get();
            System.out.print(" ; 当前position上的元素为: " + new String(new byte[]{currentValue}));
            System.out.println();
            //每次get position都会+1
            b[i] = currentValue;
        }
        System.out.println("第一种方式读取到的内容" + new String(b));

        System.out.println();
        System.out.println("----------------------------------------------上边第一种方式 下边第二种方式 两种读取方式演示 --------------------------------------------------------");
        System.out.println();

        //第二种读取方式
        System.out.println("注意: 上边是通过hasRemaining循环判断是否有元素 这个是直接给get()方法传入一个byte数组即可一次性读取buffer里的所有元素");
        //造点数据
        buffer.clear();
        buffer.put("helloword2way".getBytes());
        buffer.flip();

        int count = buffer.remaining();
        System.out.println("翻转后剩余可读元素: " + count);
        byte[] content = new byte[count];//构造一个与剩余可读元素大小相同的数组
        buffer.get(content);
        System.out.println("第二种方式读取到的内容" + new String(content));

        System.out.println();
        System.out.println("-----------------");


    }
}
