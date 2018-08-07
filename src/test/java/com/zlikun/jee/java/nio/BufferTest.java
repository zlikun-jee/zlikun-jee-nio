package com.zlikun.jee.java.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/7 18:04
 */
public class BufferTest {

    @Test
    public void test() {

        // capacity     缓冲区容量，初始化后不能修改
        // limit        上界标记，缓冲区第一个不能读写的元素
        // position     下一个要读或写的元素的索引，位置会被put()和read()更新
        // mark         一个标记，与reset()配合使用，调用reset()会将position重置为mark的值
        // 0 <= mark <= position <= limit <= capacity

        // 构造一个64字节的字节缓冲区对象
        ByteBuffer buffer = ByteBuffer.allocate(64);

        // 1. 存取API
        // 写入一个字节
        buffer.put((byte) 'A');
        // 这时position的值被更新
        assertEquals(1, buffer.position());
        // 写入数据时，还可以指定位置，这种方式不会更新position的值（更准备的描述它相当于是替换的概念）
        buffer.put(3, (byte) 'X');
        assertEquals(1, buffer.position());
        buffer.put((byte) 'B');
        buffer.put((byte) 'C');
        buffer.put((byte) 'D'); // 覆盖掉了之前的X
        assertEquals(4, buffer.position());
        // 此时无法读取数据，需要先将position重置
        buffer.position(0);
        assertEquals(0, buffer.position());
        // 开始读取字节数据
        assertEquals('A', buffer.get());
        assertEquals('B', buffer.get());
        assertEquals(2, buffer.position());
        // 也可以指定索引，与put方法类似，也不改变position值
        assertEquals('A', buffer.get(0));
        assertEquals(2, buffer.position());
        // 查找剩余可读元素（limit为初始值，等于缓冲区最容量值）
        assertEquals(buffer.limit() - 2, buffer.remaining());

        assertEquals(2, buffer.position());
        assertEquals(64, buffer.limit());

        // 2. 翻转，假设我们数据已经写完（缓冲区可能满也可能不满），此时我们准备清空缓冲区
        buffer = ByteBuffer.allocate(64);
        buffer.put("Hello".getBytes());
        // 我们现在要做的是将limit设置为当前position值，然后重置position为0
        buffer.limit(buffer.position()).position(0);
        assertEquals(0, buffer.position());
        assertEquals(5, buffer.limit());

        // 实际上述代码已由API设计者预想到了，提供了flip()方法
        buffer = ByteBuffer.allocate(64);
        buffer.put("Hello".getBytes());
        // limit = position;
        // position = 0;
        // mark = -1;
        buffer.flip();
        assertEquals(0, buffer.position());
        assertEquals(5, buffer.limit());
        // 此时可以查看剩余可读数据即为已写入的数据
        assertEquals(5, buffer.remaining());

        // 还有一个与flip()类似的方法，其只修改position=0，不修改limit的值
        // 典型应用场景是读过一遍后（此时position==limit），回退重新读（position==0）
        buffer = ByteBuffer.allocate(64);
        buffer.put("Hello".getBytes());
        // position = 0;
        // mark = -1;
        buffer.rewind();
        assertEquals(0, buffer.position());
        assertEquals(64, buffer.limit());

        // 3. 释放，现在写完了数据，开始读取
        buffer = ByteBuffer.allocate(64);
        buffer.put("Hello".getBytes());
        buffer.flip();
        assertEquals("Hello", new String(buffer.array(), buffer.position(), buffer.remaining()));
        // 使用遍历方法取
        while (buffer.hasRemaining()) {
            System.out.println((char) buffer.get());
        }
        // 此时如果需要重新读取可以使用rewind()
        buffer.rewind();
        // 也可以直接用for循环遍历（好处是不用每次都检查hasRemaining()，实际意义不大，该方法只是比较position与limit的大小）
        for (int i = buffer.position(), len = buffer.remaining(); i < len; i++) {
            System.out.println((char) buffer.get());
        }


        // 4. 压缩，如果在填充过程中，需要释放一部分数据，之后继续填充，可以使用compact()方法
        buffer = ByteBuffer.allocate(32);
        // 填充数据
        for (int i = 0; i < 26; i++) {
            buffer.put((byte) ('A' + i));
        }
        assertEquals(26, buffer.position());
        assertEquals(buffer.capacity(), buffer.limit());
        // 读取5个字节数据
        buffer.flip();
        for (int i = 0; i < 5; i++) {
            buffer.get();
        }
        assertEquals(21, buffer.remaining());
        assertEquals(26, buffer.limit());
        // 执行压缩：将未读取的数据复制从0位开始填充（相当于向左平移，把读过的数据覆盖掉）
        // 此时减少了5个字节，所以剩余已写字节为21，复制后position应为21（可以继续写的位置）
        // System.arraycopy(hb, ix(position()), hb, ix(0), remaining());
        // position(remaining());
        // limit(capacity());
        buffer.compact();
        // 压缩之后，position=剩余已写字节数，limit=容量
        assertEquals(21, buffer.position());
        assertEquals(buffer.capacity(), buffer.limit());
        // 此时可以继续写数据（remaining为可写数据量）
        assertEquals(buffer.capacity() - buffer.position(), buffer.remaining());
        // 全部使用'A'填充
        while (buffer.hasRemaining()) {
            buffer.put((byte) 'A');
        }
        // 开始读取
        System.out.println("------------");
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println((char) buffer.get());
        }


        // 5. 标记，mark用于配合reset()使用
        buffer = ByteBuffer.allocate(16);
        buffer.put("ABCDEFG".getBytes());
        buffer.flip();
        assertEquals('A', buffer.get());
        assertEquals('B', buffer.get());
        buffer.mark();      // 标记，mark=position
        assertEquals('C', buffer.get());
        assertEquals('D', buffer.get());
        buffer.reset();     // 重置，position=mark
        assertEquals('C', buffer.get());
        assertEquals('D', buffer.get());
        assertEquals('E', buffer.get());
        buffer.reset();     // 重置，重置并不会修改mark标记，所以可以重复使用
        assertEquals('C', buffer.get());

        // 6. 清空（单纯丢弃数据，并非将数据读出）
        buffer.clear();

        // 7. 比较缓冲区
        buffer = ByteBuffer.allocate(16);
        buffer.put("Hello".getBytes());
        buffer.flip();  // 表示已缓冲区已写完，否则比较时remaining()不相等，导致比较不成功
        ByteBuffer buffer2 = ByteBuffer.wrap("Hello".getBytes());
        assertEquals(buffer.remaining(), buffer2.remaining());
        assertTrue(buffer.equals(buffer2));

        // 8. 批量移动


        // 9. 复制缓冲区（数据、标记等都一致，但是不同的两个对象）
        ByteBuffer buffer3 = buffer.duplicate();
        assertTrue(buffer.equals(buffer3));
        buffer.clear();
        assertEquals(5, buffer3.remaining());
        // 也可以创建一个只读视图（将不能修改）
        ByteBuffer buffer4 = buffer.asReadOnlyBuffer();
        assertTrue(buffer.equals(buffer4));


    }

}
