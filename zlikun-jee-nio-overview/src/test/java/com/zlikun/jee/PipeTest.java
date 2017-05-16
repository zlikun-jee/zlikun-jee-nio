package com.zlikun.jee;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Pipe;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 19:27
 */
public class PipeTest {

    @Test
    public void test() throws IOException, InterruptedException {
        // 打开管道
        final Pipe pipe = Pipe.open() ;

        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    source(pipe);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } ;
        t1.start();

        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    sink(pipe);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } ;
        t2.start();

        t1.join();
        t2.join();

    }

    private void sink(Pipe pipe) throws IOException {

        // Pipe.SinkChannel
        Pipe.SinkChannel sinkChannel = pipe.sink() ;

        // 向管道写数据
        ByteBuffer sinkBuffer = ByteBuffer.allocate(128) ;
        sinkBuffer.clear() ;
        sinkBuffer.put("Connected to the target VM, address: 'javadebug', transport: 'shared memory'".getBytes()) ;
        sinkBuffer.flip() ;

        while (sinkBuffer.hasRemaining()) {
            sinkChannel.write(sinkBuffer) ;
        }

        sinkBuffer.clear() ;

    }

    private void source(Pipe pipe) throws IOException {

        // 从管道读取数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        ByteBuffer sourceBuffer = ByteBuffer.allocate(128) ;
        System.out.println(sourceChannel.read(sourceBuffer));
        System.out.println(new String(sourceBuffer.array()));

    }

}
