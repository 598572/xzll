package com.xzll.test.niotest.netty.另一个netty的示例基于国外一篇文档;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 18:48
 * @Description: 基于 https://programmer.help/blogs/netty-basic-to-entry-2-netty-core-functions-and-thread-model.html 的netty代码和释义
 */
public class NettyByteBuf {

	public static void main(String[] args) {
		// Create a byteBuf object that contains an array of bytes [10]
		// Through readerindex, writerIndex and capacity, the buffer is divided into three areas
		// Read area: [0,readerindex)
		// Readable region: [readerindex,writerIndex)
		// Writable area: [writerIndex,capacity)
		ByteBuf byteBuf = Unpooled.buffer(10);
		System.out.println("byteBuf=" + byteBuf);
		for (int i = 0; i < 8; i++) {
			byteBuf.writeByte(i);
		}
		System.out.println("byteBuf=" + byteBuf);
		for (int i = 0; i < 5; i++) {
			System.out.println(byteBuf.getByte(i));
		}
		System.out.println("byteBuf=" + byteBuf);
		for (int i = 0; i < 5; i++) {
			System.out.println(byteBuf.readByte());
		}
		System.out.println("byteBuf=" + byteBuf);
		//Creating ByteBuf with Unpooled tool class
		ByteBuf byteBuf2 = Unpooled.copiedBuffer("hello,zhuge!", CharsetUtil.UTF_8);
		//Use relevant methods
		if (byteBuf2.hasArray()) {
			byte[] content = byteBuf2.array();
			//Convert content to string

			System.out.println(new String(content, CharsetUtil.UTF_8));
			System.out.println("byteBuf=" + byteBuf2);
			System.out.println(byteBuf2.readerIndex()); // 0
			System.out.println(byteBuf2.writerIndex()); // 12
			System.out.println(byteBuf2.capacity()); // 36
			System.out.println(byteBuf2.getByte(0)); // Get ascii code of character h at array 0, h=104 41
			int len = byteBuf2.readableBytes(); //Number of bytes read 12
			System.out.println("len=" + len);
			//Use for to fetch bytes
			for (int i = 0; i < len; i++) {
				System.out.println((char) byteBuf2.getByte(i));
			}
			//Scope read
			System.out.println(byteBuf2.getCharSequence(0, 6, CharsetUtil.UTF_8));
			System.out.println(byteBuf2.getCharSequence(6, 6, CharsetUtil.UTF_8));
		}
	}
}
