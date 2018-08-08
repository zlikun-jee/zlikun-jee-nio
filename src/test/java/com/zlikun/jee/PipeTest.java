package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 19:27
 */
public class PipeTest {

    @Test
    public void test() throws IOException, InterruptedException {
        // 打开管道
        final Pipe pipe = Pipe.open();

        // 线程计数器
        final CountDownLatch latch = new CountDownLatch(2);

        // 启动生产者线程（源）
        new Thread(() -> {
            try {
                source(pipe);
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }).start();

        // 启动消费者线程（目标）
        new Thread(() -> {
            try {
                sink(pipe);
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }).start();

        latch.await();

    }

    /**
     * 向管道写数据（感觉跟Flume的工作机制很像）
     *
     * @param pipe
     * @throws IOException
     */
    private void sink(Pipe pipe) throws IOException {

        try (Pipe.SinkChannel channel = pipe.sink()) {

            ByteBuffer buffer = ByteBuffer.allocate(128);
            buffer.put("Hello NIO !".getBytes());
            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            buffer.clear();
        }

    }

    /**
     * 从通道中读取数据
     *
     * @param pipe
     * @throws IOException
     */
    private void source(Pipe pipe) throws IOException {

        try (Pipe.SourceChannel channel = pipe.source()) {
            ByteBuffer buffer = ByteBuffer.allocate(128);
            // 从通道中读出数据（写入缓冲区）
            assertEquals(11, channel.read(buffer));
            // 准备开始读取缓冲区数据
            buffer.flip();
            // 从缓冲区中清空数据（打印）
            assertEquals("Hello NIO !", new String(buffer.array(), buffer.position(), buffer.remaining()));
        }

    }

}
