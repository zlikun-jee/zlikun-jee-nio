package com.zlikun.jee.java.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通道测试，通道可以是单向的也可以是双向的
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/7 21:15
 */
public class ChannelTest {

    @Test
    public void test() throws IOException {

        // FileInputStream总是以read-only的权限打开文件，所以虽然通道是双向的，但write方法却是禁用的
        try (FileInputStream input = new FileInputStream("LICENSE")) {

            // 打开一个文件通道
            try (FileChannel channel = input.getChannel()) {

                // 尝试写入数据，发抛出 NonWritableChannelException 异常
                // java.nio.channels.NonWritableChannelException
                // channel.write(ByteBuffer.wrap("Hello".getBytes()));

                ByteBuffer buffer = ByteBuffer.allocate(64);
                // 从通道中读取数据到缓冲区
                while (channel.read(buffer) != -1) {
                    buffer.flip();
                    System.out.print(new String(buffer.array(), buffer.position(), buffer.remaining()));
                    buffer.clear(); // 即使不清空，在本例中也不影响（是按remaining读取，后面会覆盖前面的，没有覆盖的地方不会被读出）
                }

            }

        }


    }

}
