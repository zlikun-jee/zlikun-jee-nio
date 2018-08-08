package com.zlikun.jee;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * channel-to-channel，transferFrom与transferTo功能一致，方向相反
 * 采用这种方式性能极好，如果能使用推荐之
 *
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 15:51
 */
public class FileChannelTransferTest {

    @Test
    public void transferFrom() throws IOException {

        // 打列随机访问文件对象（源对象、目标对象）
        try (RandomAccessFile fromFile = new RandomAccessFile("LICENSE", "r");
             RandomAccessFile toFile = new RandomAccessFile("target/LICENSE.txt", "rw")) {

            // 打开两个文件通道
            try (FileChannel fromChannel = fromFile.getChannel();
                 FileChannel toChannel = toFile.getChannel()) {

                // 从一个通道读取数据写入另一个通道（文件复制）
                toChannel.transferFrom(fromChannel, 0, fromChannel.size());

            }
        }

    }

    @Test
    public void transferTo() throws IOException {

        // 打列随机访问文件对象（源对象、目标对象）
        try (RandomAccessFile fromFile = new RandomAccessFile("LICENSE", "r");
             RandomAccessFile toFile = new RandomAccessFile("target/LICENSE.txt", "rw")) {

            // 打开两个文件通道
            try (FileChannel fromChannel = fromFile.getChannel();
                 FileChannel toChannel = toFile.getChannel()) {

                // 从一个通道读取数据写入另一个通道（文件复制）
                fromChannel.transferTo(0, fromChannel.size(), toChannel);

            }
        }

    }

}
