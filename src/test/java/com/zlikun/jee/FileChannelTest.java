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

        RandomAccessFile raf = new RandomAccessFile("src/test/resources/hello.txt" ,"rw") ;
        // 获取文件通道
        FileChannel channel = raf.getChannel() ;

        ByteBuffer buffer = ByteBuffer.allocate(64) ;

        // 从通道中读取数据写入缓冲区，返回实际写入数据长度
        int size = channel.read(buffer) ;
        while (size != -1) {
            buffer.flip() ;

            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }

            buffer.clear() ;
            size = channel.read(buffer) ;

        }

        raf.close();

    }

}
