package com.zlikun.jee;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @auther zlikun <zlikun-dev@hotmail.com>
 * @date 2017/5/16 15:51
 */
public class FileChannelTransferTest {

    @Test
    public void transferFrom() throws IOException {

        RandomAccessFile fromFile = new RandomAccessFile("src/test/resources/hello.txt" ,"r") ;
        FileChannel fromChannel = fromFile.getChannel() ;

        RandomAccessFile toFile = new RandomAccessFile("target/hello.log" ,"rw") ;
        FileChannel toChannel = toFile.getChannel() ;

        // 将数据从源通道传输到FileChannel中
        toChannel.transferFrom(fromChannel ,0 ,fromChannel.size()) ;

        toFile.close();
        fromFile.close();
    }

    @Test
    public void transferTo() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("src/test/resources/hello.txt" ,"r") ;
        FileChannel fromChannel = fromFile.getChannel() ;

        RandomAccessFile toFile = new RandomAccessFile("target/hello.log" ,"rw") ;
        FileChannel toChannel = toFile.getChannel() ;

        // 将数据从FileChannel传输到其他的channel中
        fromChannel.transferTo(0 ,fromChannel.size() ,toChannel) ;

        toFile.close();
        fromFile.close();
    }

}
