package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 15:11
 */
public class GatherTest {

    @Test
    public void test() throws IOException {

        ByteBuffer header = ByteBuffer.allocate(128) ;
        ByteBuffer body = ByteBuffer.allocate(1024) ;

        RandomAccessFile raf = new RandomAccessFile("target/response.log" ,"rw") ;
        // 获取文件通道
        FileChannel channel = raf.getChannel() ;

        ByteBuffer [] buffers = {header ,body} ;

        // 为buffer填充数据
        header.put(getBytes(127 ,'H')).put((byte) '\n') ;
        body.put(getBytes(256 ,'A')) ;

        header.flip() ;
        body.flip() ;

        // Gathering Writes
        // 向channel中写入数据(多个Buffer向同一Channel写数据)，按Buffer顺序写入
        channel.write(buffers) ;

        raf.close();
    }

    private byte [] getBytes(int length ,char letter) {
        byte [] bytes = new byte[length] ;
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) letter;
        }
        return bytes ;
    }

}
