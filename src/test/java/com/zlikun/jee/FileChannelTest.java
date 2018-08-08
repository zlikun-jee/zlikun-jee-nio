package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 11:42
 */
public class FileChannelTest {

    @Test
    public void test() throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile("pom.xml", "r")) {
            // 打开文件通道
            try (FileChannel channel = raf.getChannel()) {
                // 分配一个字节缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(64);
                // 从通道中读取数据写入缓冲区，返回实际写入数据长度
                while (channel.read(buffer) != -1) {
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        System.out.print((char) buffer.get());
                    }
                    buffer.clear();
                }

            }
        }

    }

}
