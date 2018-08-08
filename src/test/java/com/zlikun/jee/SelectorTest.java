package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 先启动server，再启动client，然后两个开撕（你一言，我一语，自说自话）
 *
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 16:13
 */
public class SelectorTest {

    private final AtomicLong counter = new AtomicLong();

    @Test
    public void server() throws IOException, InterruptedException {

        // 获取服务端通道
        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            // 绑定监听端口
            channel.bind(new InetSocketAddress(1234));
            // 设置为非阻塞通道
            channel.configureBlocking(false);

            // 打开和个监听器
            try (Selector selector = Selector.open()) {

                // 监听监听事件，这里为等待接收请求
                channel.register(selector, SelectionKey.OP_ACCEPT);

                // 使用选择器选择就绪通道进行处理
                while (selector.select() > 0) {

                    // 遍历选择Key
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();

                        // 根据Key判断相应事件
                        if (key.isAcceptable()) {
                            // 接收处理逻辑
                            SocketChannel sc = channel.accept();
                            // 同样道理，注册读事件（相当于请求）
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            // 读处理逻辑
                            SocketChannel sc = (SocketChannel) key.channel();
                            // 打印读到的数据
                            print(sc);
                            // 读到数据后，回写数据（相当于响应）
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_WRITE);
                        } else if (key.isWritable()) {
                            // 程序休眠500毫秒，表示响应处理过程
                            Thread.sleep(500L);
                            // 响应请求
                            SocketChannel sc = (SocketChannel) key.channel();
                            sc.write(ByteBuffer.wrap((String.format("[%04d] I got it .", counter.incrementAndGet())).getBytes()));
                            // 写完了再注册读事件（相当于接收请求）
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                        } else {
                            System.out.println("暂不处理这种事件!");
                        }

                        // 移除当前Key（已处理过）
                        iter.remove();

                    }

                }

            }


        }

    }

    /**
     * 从通道里读出数据
     *
     * @param channel
     * @throws IOException
     */
    private void print(SocketChannel channel) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(64);
        while (channel.read(buf) > 0) {
            buf.flip();
            System.out.println(new String(buf.array(), buf.position(), buf.remaining()));
            buf.clear();
        }
    }

    @Test
    public void client() throws IOException {

        // 打开一个Socket通道
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(1234));

        // 建立（打开）选择器
        try (Selector selector = Selector.open()) {

            // 与Selector一起使用时，Channel必须处于非阻塞模式下
            // FileChannel不能切换到非阻塞模式，而SocketChannel可以
            channel.configureBlocking(false);

            // 注册选择器事件，可以通过按位或运算符将多个事件连接起来：1 | 4 == 10，10代表什么？
            // channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            // 先注册写，写完了会注册读，然后循环往复
            channel.register(selector, SelectionKey.OP_WRITE);

            // 使用选择器选择就绪通道进行处理
            while (selector.select() > 0) {
                // 遍历选择事件
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isReadable()) {
                        // 如果是读事件，打印通道中的数据（响应）
                        SocketChannel sc = (SocketChannel) key.channel();
                        print(sc);
                        // 读完了，注册一个写事件（再次请求）
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_WRITE);
                    } else if (key.isWritable()) {
                        // 如果是写事件，向通道写入数据（请求）
                        SocketChannel sc = (SocketChannel) key.channel();
                        sc.write(ByteBuffer.wrap((String.format("[%04d] I sent request .", counter.incrementAndGet())).getBytes()));
                        // 写完了，注册一个读事件（响应）
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                    } else {
                        System.out.println("暂不处理这种事件!");
                    }
                    // 移除处理过的事件
                    iter.remove();
                }

            }

        }

    }

}
