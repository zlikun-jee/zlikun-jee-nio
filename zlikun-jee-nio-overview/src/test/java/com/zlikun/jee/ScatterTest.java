package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 15:44
 */
public class ScatterTest {

    @Test
    public void test() throws IOException {

        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);

        RandomAccessFile raf = new RandomAccessFile("target/response.log", "r");
        FileChannel channel = raf.getChannel();

        ByteBuffer[] buffers = {header, body};

        // Scattering Reads
        // 从Channel中读取数据，分别写入多个Buffer，按Buffer顺序，一个Buffer写满后后入下一个Buffer
        // 意味着不适用于大小不固定的消息，通常使用时，不满足的部分应使用填充解决
        channel.read(buffers);

        header.flip();
        body.flip();

        print(header) ;
        print(body);

        raf.close();

    }

    private void print(ByteBuffer buffer) {
        System.out.println(new String(buffer.array()));
    }

}
