package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 16:13
 */
public class SelectorTest {

    @Test
    public void test() throws IOException {

        SocketChannel channel = null ;

        // 创建一个Selector
        Selector selector = Selector.open() ;

        // 向Selector注册Channel
        // 与Selector一起使用时，Channel必须处于非阻塞模式下
        // FileChannel不能切换到非阻塞模式，而SocketChannel可以
        channel.configureBlocking(false) ;
        // 第二个参数，通过Selector监听Channel时对什么事件感兴趣：Connect / Accept / Read / Write
//        SelectionKey skey = channel.register(selector ,SelectionKey.OP_READ) ;
        // 如果对多个事件感兴趣，可以用"位或"操作符将常量连接起来
        SelectionKey skey = channel.register(selector ,SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE) ;

//      // 从SelectionKey中获取Channel和Selector
//        skey.channel() ;
//        skey.selector() ;

        while(true) {
            int readyChannels = selector.select();
            if(readyChannels == 0) continue;
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = (SelectionKey) keyIterator.next();
                if(key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }
                keyIterator.remove();
            }
        }

//        selector.close();     // 关闭选择器
    }

}
