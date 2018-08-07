package com.zlikun.jee;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 13:24
 */
public class ByteBufferTest {

    @Test
    public void test() throws IOException {

        int capacity = 64 ;
        // 分配缓冲区空间，本例：64byte，此时缓冲区是写模式，返回一个ByteBuffer实例，使用简单工厂模式
        // 写模式下，position值为0，每写入一个字节，position将自增一次，其最大值为capacity - 1
        // 写模式下，表示你最多能往Buffer里写多少数据(limit等于capacity)
        ByteBuffer buffer = ByteBuffer.allocate(capacity) ;

        Assert.assertEquals(0 ,buffer.position());
        Assert.assertEquals(capacity ,buffer.limit());
        Assert.assertEquals(capacity ,buffer.capacity());

        // 可以通过put()方法写入数据
        byte [] data = "# FileChannel\n".getBytes() ;
        // pub()方法返回ByteBuffer实例，所以可以使用链式写法
        buffer.put(data).put((byte) 'A').put((byte) '\n') ;

        // 缓冲区剩余可写长度 = 总容量 - 当前可写位置
        Assert.assertEquals(data.length + 2 ,buffer.position());
        Assert.assertEquals(capacity ,buffer.limit());

        // flip() 将缓冲区切换到读模式，此时position将被重置为0，limit则与position重置前的值相同(即实际写入缓冲区的数据字节数)
        buffer.flip() ;

        Assert.assertEquals(0 ,buffer.position());
        Assert.assertEquals(data.length + 2 ,buffer.limit());

        // hasRemaining() 判断是否包含剩余数据
        byte [] bytes = new byte[buffer.limit()] ;
        int i = 0 ;
        while (buffer.hasRemaining()) {
            // get() 读取一个字节，并自增position值(移动下一次读取的位置)
            bytes[i ++] = buffer.get() ;
        }
        System.out.print(new String(bytes));

        // array()返回缓冲区全部数据(不移动position，包含未填充的部分)
        System.out.println(new String(buffer.array()));
        Assert.assertEquals(capacity ,buffer.array().length);

        // rewind() 将position重置为0，limit不变，即允许重新读取数据
        buffer.rewind() ;

        // mark() 和 reset() 分别表示标记一个位置和重置到标记的位置
        Assert.assertEquals("# F" ,new String(new byte [] {buffer.get() ,buffer.get() ,buffer.get()}));
        buffer.mark() ;                     // 读3个字节后打一个标记
        buffer.get() ;
        buffer.get() ;
        buffer.get() ;
        buffer.reset() ;                    // 再次读3个字节后，重置
        // remaining() 剩余未读数据
        bytes = new byte[buffer.remaining()] ;
        i = 0 ;
        while (buffer.hasRemaining()) {
            // get() 读取一个字节，并自增position值(移动下一次读取的位置)
            bytes[i ++] = buffer.get() ;
        }
        Assert.assertEquals("ileChannel\nA\n" ,new String(bytes));

        // 清空缓冲区，使其可以被再次写入
        // clear() 清空整个缓冲区
        // compact() 只清除已读过的数据，未读过的数据被移到缓冲区起始处，新写入的数据将放在未读数据后面
        buffer.clear() ;

    }

}
