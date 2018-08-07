package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.junit.Assert.assertEquals;

/**
 * 分散（Scatter） / 聚集（Gather）
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/7 21:49
 */
public class GatherAndScatterTest {

    @Test
    public void test() throws IOException {

        ByteBuffer header = ByteBuffer.allocateDirect(64);
        ByteBuffer body = ByteBuffer.allocateDirect(512);

        ByteBuffer[] buffers = {header, body};

        // 写入数据
        try (RandomAccessFile raf = new RandomAccessFile("target/response.log", "rw")) {
            try (FileChannel channel = raf.getChannel()) {
                header.put("GET https://zlikun.com/ HTTP/1.1".getBytes("UTF-8")).put((byte) '\n');
                body.put("<html><head><title>Index</title></head><body>Hello Everyone !</body></html>".getBytes("UTF-8"));
                header.flip();
                body.flip();
                long bytes = channel.write(buffers);
                assertEquals(108, bytes);
                header.clear();
                body.clear();
            }
        }

        // 读取数据
        try (RandomAccessFile raf = new RandomAccessFile("target/response.log", "r")) {
            try (FileChannel channel = raf.getChannel()) {
                channel.read(buffers);

                print(header);
                print(body);

            }
        }

    }

    /**
     * 打印缓冲区里的内容
     *
     * @param buffer
     */
    private void print(ByteBuffer buffer) {
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
    }

}
