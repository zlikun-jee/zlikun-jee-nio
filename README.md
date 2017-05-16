# zlikun-jee-nio

Java NIO 知识整理


#### NIO知识点
- Channel
    - `FileChannel` 从文件中读写数据
    - `DatagramChannel` 通过UDP读写网络中的数据
    - `SocketChannel` 通过TCP读写网络中的数据
    - `ServerSocketChannel` 监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel
- Buffer
    - `ByteBuffer`
    - `MappedByteBuffer`
    - `CharBuffer`
    - `DoubleBuffer`
    - `FloatBuffer`
    - `IntBuffer`
    - `LongBuffer`
    - `ShortBuffer`
- Selector  
    Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。
- Scatter/Gather
    - Scattering Reads：是指数据从一个channel读取到多个buffer中
    ![](http://ifeve.com/wp-content/uploads/2013/06/scatter.png)
    - Gathering Writes：是指数据从多个buffer写入到同一个channel
    ![](http://ifeve.com/wp-content/uploads/2013/06/gather.png)


#### NIO相关文章
- [Java NIO系列教程](http://ifeve.com/java-nio-all/)
- [Java NIO系列教程（一） Java NIO 概述](http://ifeve.com/overview/)
- [Java NIO系列教程（二） Channel](http://ifeve.com/channels/)
- [Java NIO系列教程（三） Buffer](http://ifeve.com/buffers/)
- [Java NIO系列教程（四） Scatter/Gather](http://ifeve.com/java-nio-scattergather/)
- [Java NIO系列教程（五） 通道之间的数据传输](http://ifeve.com/java-nio-channel-to-channel/)
- [Java NIO系列教程（六） Selector](http://ifeve.com/selectors/)
- [Java NIO系列教程（七） FileChannel](http://ifeve.com/file-channel/)
- [Java NIO系列教程（八） SocketChannel](http://ifeve.com/socket-channel/)
- [Java NIO系列教程（九） ServerSocketChannel](http://ifeve.com/server-socket-channel/)
- [《Java NIO文档》非阻塞式服务器](http://ifeve.com/non-blocking-server/)
- [Java NIO系列教程（十） Java NIO DatagramChannel](http://ifeve.com/datagram-channel/)
- [Java NIO系列教程（十一） Pipe](http://ifeve.com/pipe/)
- [Java NIO系列教程（十二） Java NIO与IO](http://ifeve.com/java-nio-vs-io/)
- [Java NIO Path](http://tutorials.jenkov.com/java-nio/path.html)
- [Java NIO Files](http://tutorials.jenkov.com/java-nio/files.html)
- [Java NIO AsynchronousFileChannel](http://tutorials.jenkov.com/java-nio/asynchronousfilechannel.html)
