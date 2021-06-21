package com.xzll.test.niotest.三种IO代码实现;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 08:29
 * @Description: mmap 演示
 * <p>
 * MappedByteBuffer mmap的java实现 使用堆外 (直接内存)  引用 https://www.cnblogs.com/lsgxeva/p/11619464.html 大佬的博文
 * <p>
 * <p>
 * 1.MappedByteBuffer特点:
 * <p>
 * MappedByteBuffer 使用是堆外的虚拟内存，因此分配（map）的内存大小不受 JVM 的 -Xmx 参数限制，但是也是有大小限制的。
 * 如果当文件超出 Integer.MAX_VALUE 字节限制时，可以通过 position 参数重新 map 文件后面的内容。
 * MappedByteBuffer 在处理大文件时性能的确很高，但也存内存占用、文件关闭不确定等问题，被其打开的文件只有在垃圾回收的才会被关闭，而且这个时间点是不确定的。
 * MappedByteBuffer 提供了文件映射内存的 mmap() 方法，也提供了释放映射内存的 unmap() 方法。然而 unmap() 是 FileChannelImpl 中的私有方法，无法直接显示调用。因此，用户程序需要通过 Java 反射的调用 sun.misc.Cleaner 类的 clean() 方法手动释放映射占用的内存区域。
 * <p>
 * <p>
 * 2.堆内内存与堆外内存区别:
 * <p>
 * 堆外内存（DirectBuffer）在使用后需要应用程序手动回收，而堆内存（HeapBuffer）的数据在 GC 时可能会被自动回收。
 * 因此，在使用 HeapBuffer 读写数据时，为了避免缓冲区数据因为 GC 而丢失，NIO 会先把 HeapBuffer 内部的数据拷贝到一个临时的 DirectBuffer 中的本地内存（native memory），
 * 这个拷贝涉及到 sun.misc.Unsafe.copyMemory() 的调用，背后的实现原理与 memcpy() 类似。
 * 最后，将临时生成的 DirectBuffer 内部的数据的内存地址传给 I/O 调用函数，这样就避免了再去访问 Java 对象处理 I/O 读写
 */
public class MmapTest extends IOCommonTest {

    public static final String MMAP_TARGET = "/Users/admin/hzz_main/xzll/mmap拷贝的目标文件.txt";
    private static String MMAP_SOURCE = "/Users/admin/hzz_main/xzll/mmap源文件.txt";


    //    @Before()
    public void createFileNotExist() {
        Path source = Paths.get(MMAP_SOURCE);
        try {
            if (Files.exists(source)) {
                Files.delete(source);
            }
            if (!Files.exists(source))
                Files.createFile(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copy(File source, File destination) throws IOException {
        FileChannel input = null, output = null;
        try {
            //FileInputStream获取到的channel只能读
            //FileOutputStream 获取到的channel (读写都可以)
            input = new FileInputStream(source).getChannel();
            output = new FileOutputStream(destination).getChannel();

            long size = input.size();
            MappedByteBuffer buffer = input.map(FileChannel.MapMode.READ_ONLY, 0, size);

            output.write(buffer);

        } finally {
            if (input != null)
                input.close();
            if (output != null)
                output.close();
        }
    }

    //@Test
    public void testCopy() throws IOException {
        File file = new File(MMAP_SOURCE);
        File file2 = new File(MMAP_TARGET);

        long l = System.currentTimeMillis();
        System.out.println("拷贝前: 文件大小 : " + file2.length() );
        copy(file,file2);
        System.out.println("mmap 拷贝文件用时: "
                + (System.currentTimeMillis() - l) + " ms " + "文件大小 " + file2.length() + " byte ");
    }

    /**
     * 使用  MappedByteBuffer 读 文件内容
     */
    @Test
    public void readFromFileByMappedByteBuffer() {
        Path path = Paths.get(MMAP_SOURCE);
        int length = CONTENT.getBytes(Charset.forName(CHARSET)).length;
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, length);
            if (mappedByteBuffer != null) {
                byte[] bytes = new byte[length];
                mappedByteBuffer.get(bytes);
                String content = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("通过MappedByteBuffer读取文件 读到的内容: " + content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用  MappedByteBuffer 写 内容到文件 (底层是mmap 见图片 study-test/src/test/java/com/xzll/test/niotest/零拷贝理论相关/mmap.png )
     */
//    @Test
    public void writeTest() {
        long l1 = System.currentTimeMillis();
        Path path = Paths.get(MMAP_SOURCE);
        byte[] bytes = CONTENT.getBytes();
        System.out.println("mmap writeTest 获取文件用时 :"+(System.currentTimeMillis()-l1)+"  ms  不算入性能之内");
        //使用FileChannel.open获取FileChannel 使用 fileChannel.map()方法
        long openBefore = System.currentTimeMillis();
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            System.out.println("mmap 获取FileChannel用时 :" + (System.currentTimeMillis() - openBefore) + " ms ");
            /*
            map() 方法
            1.构造DirectByteBuffer对象 申请内存空间(在堆外的直接内存上 )
            2.通过本地方法 map0() 为文件分配一块虚拟内存，作为它的内存映射区域，然后返回这块内存映射区域的起始地址。
            文件映射需要在 Java 堆中创建一个 MappedByteBuffer 的实例。如果第一次文件映射导致 OOM，则手动触发垃圾回收，休眠 100ms 后再尝试映射，如果失败则抛出异常。
            通过 Util 的 newMappedByteBuffer （可读可写）方法或者 newMappedByteBufferR（仅读） 方法方法反射创建一个 DirectByteBuffer 实例，其中 DirectByteBuffer 是 MappedByteBuffer 的子类。
             */
            long mapBefore = System.currentTimeMillis();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
            System.out.println("mmap 申请内存映射区域 用时 :" + (System.currentTimeMillis() - mapBefore) + " ms ");
            long l = System.currentTimeMillis();
            if (mappedByteBuffer != null) {
                //写入到缓存 但不会立马到磁盘 操作系统会在某个时刻刷到磁盘
                mappedByteBuffer.put(bytes);
                //强制刷新到磁盘
//                long forceBefore = System.currentTimeMillis();
//                mappedByteBuffer.force();
//                System.out.println("force用时: " + (System.currentTimeMillis() - forceBefore) + " ms ");
                System.out.println("mmap MappedByteBuffer 写入数据用时: "
                        + (System.currentTimeMillis() - l) + " ms " + "文件大小 " + path.toFile().length() + " byte " +
                        "写入进文件---> " + MMAP_SOURCE
                );
            }
            /*
            使用MappedByteBuffer写文件并强制刷新到磁盘 完成 用时:2194 ms 文件大小 360000000 byte
             */

        } catch (IOException e) {
            System.out.println("使用MappedByteBuffer写文件并强制刷新到磁盘 失败");
            e.printStackTrace();
        }

        /*
        map() 方法原码注释:

        将此通道文件的一个区域直接映射到内存中。
        文件的一个区域可以通过以下三种模式之一映射到内存中：
        只读：任何修改结果缓冲区的尝试都将导致抛出java.nio.ReadOnlyBufferException 。 ( MapMode.READ_ONLY )
        读/写：对结果缓冲区所做的更改最终将传播到文件； 它们可能对映射相同文件的其他程序可见，也可能不可见。 ( MapMode.READ_WRITE )
        私有：对结果缓冲区所做的更改不会传播到文件，并且对映射相同文件的其他程序不可见； 相反，它们将导致创建缓冲区修改部分的私有副本。 ( MapMode.PRIVATE )
        对于只读映射，此通道必须已打开进行读取； 对于读/写或私有映射，必须已打开此通道进行读和写。
        此方法返回的mapped byte buffer的位置为零，限制和容量为size ； 它的标记将是未定义的。 缓冲区及其表示的映射将保持有效，直到缓冲区本身被垃圾收集。
        映射一旦建立，就不再依赖于用于创建它的文件通道。 特别是关闭通道对映射的有效性没有影响。
        内存映射文件的许多细节本质上取决于底层操作系统，因此未指定。 当请求的区域未完全包含在此频道的文件中时，此方法的行为未指定。 未指定此程序或其他程序对基础文件的内容或大小所做的更改是否传播到缓冲区。 未指定缓冲区更改传播到文件的速率。
        对于大多数操作系统，文件映射到内存比读，或通过一般书写的几十数据的千字节的更昂贵的read和write的方法。 从性能的角度来看，通常只值得将相对较大的文件映射到内存中。
        参数：
        mode – FileChannel.MapMode类中定义的常量READ_ONLY 、 READ_WRITE或PRIVATE之一，分别取决于文件是要映射为只读、读/写还是私有（写时复制）
        position – 文件中映射区域开始的位置； 必须是非负数
        size – 要映射的区域的大小； 必须为非负且不大于Integer.MAX_VALUE
        返回：
        映射字节缓冲区
        抛出：
        NonReadableChannelException – 如果模式为READ_ONLY但未打开此通道进行读取
        NonWritableChannelException – 如果模式是READ_WRITE或PRIVATE但此通道未打开用于读取和写入
        IllegalArgumentException – 如果参数的前提条件不成立
        IOException – 如果发生其他一些 I/O 错误
        也可以看看：
        FileChannel.MapMode , MappedByteBuffer

        public abstract MappedByteBuffer map(MapMode mode,
                                         long position, long size) throws IOException;
         */


    }

    /*---------------------------------------FileChannelImpl # map() 方法源码 ---------------------------------------------------*/
    /*

    //map源码
    public MappedByteBuffer map(MapMode var1, long var2, long var4) throws IOException {
        this.ensureOpen();
        //一些校验
        if (var1 == null) {
            throw new NullPointerException("Mode is null");
        } else if (var2 < 0L) {
            throw new IllegalArgumentException("Negative position");
        } else if (var4 < 0L) {
            throw new IllegalArgumentException("Negative size");
        } else if (var2 + var4 < 0L) {
            throw new IllegalArgumentException("Position + size overflow");
        } else if (var4 > 2147483647L) {
            throw new IllegalArgumentException("Size exceeds Integer.MAX_VALUE");
        } else {
            byte var6 = -1;
            if (var1 == MapMode.READ_ONLY) {
                var6 = 0;
            } else if (var1 == MapMode.READ_WRITE) {
                var6 = 1;
            } else if (var1 == MapMode.PRIVATE) {
                var6 = 2;
            }

            assert var6 >= 0;

            if (var1 != MapMode.READ_ONLY && !this.writable) {
                throw new NonWritableChannelException();
            } else if (!this.readable) {
                throw new NonReadableChannelException();
            } else {
                long var7 = -1L;
                int var9 = -1;

                Object var10;
                try {
                    this.begin();
                    var9 = this.threads.add();
                    if (this.isOpen()) {
                        long var34;
                        MappedByteBuffer var37;
                        int var12;
                        //同步操作
                        synchronized (this.positionLock) {
                            long var14;
                            do {
                                var14 = this.nd.size(this.fd);
                            } while (var14 == -3L && this.isOpen());

                            if (!this.isOpen()) {
                                var37 = null;
                                return var37;
                            }

                            MappedByteBuffer var17;
                            if (var14 < var2 + var4) {
                                if (!this.writable) {
                                    throw new IOException("Channel not open for writing - cannot extend file to required size");
                                }

                                int var16;
                                do {
                                    var16 = this.nd.truncate(this.fd, var2 + var4);
                                } while (var16 == -3 && this.isOpen());

                                if (!this.isOpen()) {
                                    var17 = null;
                                    return var17;
                                }
                            }

                            if (var4 == 0L) {
                                var7 = 0L;
                                FileDescriptor var38 = new FileDescriptor();
                                //如果是写模式
                                if (this.writable && var6 != 0) {
                                    //使用反射 构建 MappedByteBuffer对象 这其中 (DirectByteBuffer#构造方法 DirectByteBuffer(int cap)  使用unsafe类进行内存分配 )
                                    var17 = Util.newMappedByteBuffer(0, 0L, var38, (Runnable) null);
                                    return var17;
                                }
                                //否则构建读的 MappedByteBufferR 对象
                                var17 = Util.newMappedByteBufferR(0, 0L, var38, (Runnable) null);
                                return var17;
                            }

                            var12 = (int) (var2 % allocationGranularity);
                            long var36 = var2 - (long) var12;
                            var34 = var4 + (long) var12;

                            try {
                                //(调用native方法 map0 (c实现) 为文件分配一块虚拟内存，作为它的内存映射区域，然后返回这块内存映射区域的起始地址。)
                                var7 = this.map0(var6, var36, var34);
                            } catch (OutOfMemoryError var31) {
                                //oom后 手动触发GC 并sleep 100ms 等待GC完成 如果把手动触发GC关闭 那这个肯定不会执行
                                System.gc();

                                try {
                                    Thread.sleep(100L);
                                } catch (InterruptedException var30) {
                                    Thread.currentThread().interrupt();
                                }
                                //再次申请 内存映射区域 如果还是OOM 那么抛出异常
                                try {
                                    var7 = this.map0(var6, var36, var34);
                                } catch (OutOfMemoryError var29) {
                                    throw new IOException("Map failed", var29);
                                }
                            }
                        }

                        FileDescriptor var13;//文件指令
                        try {
                            var13 = this.nd.duplicateForMapping(this.fd);
                        } catch (IOException var28) {
                            unmap0(var7, var34);
                            throw var28;
                        }

                        assert IOStatus.checkAll(var7);

                        assert var7 % allocationGranularity == 0L;

                        int var35 = (int) var4;
                        FileChannelImpl.Unmapper var15 = new FileChannelImpl.Unmapper(var7, var34, var35, var13);
                        if (this.writable && var6 != 0) {
                            var37 = Util.newMappedByteBuffer(var35, var7 + (long) var12, var13, var15);
                            return var37;
                        }

                        var37 = Util.newMappedByteBufferR(var35, var7 + (long) var12, var13, var15);
                        return var37;
                    }

                    var10 = null;
                } finally {
                    this.threads.remove(var9);
                    this.end(IOStatus.checkAll(var7));
                }

                return (MappedByteBuffer) var10;
            }
        }
    }
    */
    /*--------------------------------------- DirectByteBuffer 构造器源码(申请映射内存 堆外  ) ---------------------------------------------------*/
    /**/
    /*

    // Primary constructor
    //
    DirectByteBuffer(int cap) {                   // package-private

        super(-1, 0, cap, cap);
        boolean pa = VM.isDirectMemoryPageAligned();
        int ps = Bits.pageSize();
        long size = Math.max(1L, (long) cap + (pa ? ps : 0));
        Bits.reserveMemory(size, cap);

        long base = 0;
        try {
            //使用unsafe分配内存 注意这个不是在堆上分配的 而是在直接内存中 不受GC影响 so  可能会oom? 如何回收这片区域？看看netty的实现就知道了 具体不再展开 google就好
            base = unsafe.allocateMemory(size);
        } catch (OutOfMemoryError x) {
            //异常的话 释放该区域
            Bits.unreserveMemory(size, cap);
            throw x;
        }
        unsafe.setMemory(base, size, (byte) 0);
        if (pa && (base % ps != 0)) {
            // Round up to page boundary
            address = base + ps - (base & (ps - 1));
        } else {
            address = base;
        }
        //初始化 DirectByteBuffer 时还会创建一个 Deallocator 线程，并通过 Cleaner 的 freeMemory()
        //方法来对直接内存进行回收操作，freeMemory() 底层调用的是操作系统的 free() 函数。
        cleaner = Cleaner.create(this, new DirectByteBuffer.Deallocator(base, size, cap));
        att = null;

    }
     */
    /*-----------------------------------------------使用  MappedByteBuffer 写内容到文件--------------------------------------------------------------------*/


}
