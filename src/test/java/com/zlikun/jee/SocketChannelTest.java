package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 16:57
 */
public class SocketChannelTest {

    @Test
    public void test() throws IOException, InterruptedException {

        // 打开SocketChannel
        SocketChannel channel = SocketChannel.open() ;
//        channel.configureBlocking(false) ;
        // 非阻塞模式下，connect()方法可能在连接建立之前就返回了，为了确认连接是否建立，可以调用finishConnect()方法
        channel.connect(new InetSocketAddress("192.168.9.206" ,80)) ;

        // 从SocketChannel中读取数据
        ByteBuffer buffer = ByteBuffer.allocate(1024) ;
        while (channel.read(buffer) != -1) {
            buffer.flip() ;
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            buffer.clear() ;
        }

        // 关闭SocketChannel
        channel.close();

    }

}
