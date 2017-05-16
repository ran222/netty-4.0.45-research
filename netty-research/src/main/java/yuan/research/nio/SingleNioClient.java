package yuan.research.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuan.research.nio.util.CharsetUtil;

public class SingleNioClient{
	private static final Logger log = LoggerFactory.getLogger(SingleNioClient.class);
	public static void main(String[] args) throws IOException{
		Selector selector=Selector.open();

		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		socketChannel.connect(new InetSocketAddress("0.0.0.0",8000));

		while (true) {
			selector.select();
			Set<SelectionKey> keySet = selector.selectedKeys();
			ByteBuffer readBuffer=ByteBuffer.allocate(1024);
			for(final SelectionKey key : keySet) {
				if (key.isConnectable()) {
					final SocketChannel channel = (SocketChannel)key.channel();
					if (channel.isConnectionPending()) {
						channel.finishConnect();
						log.info("connect success !");
						channel.write(ByteBuffer.wrap("hi".getBytes()));//发送信息至服务器

						new Thread() {
							@Override
							public void run() {
								ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
								int count=0;
								while (true) {
									try {
										byteBuffer.clear();
										Scanner cin = new Scanner(System.in);
										System.out.println(cin.nextLine());
										//未注册WRITE事件，因为大部分时间channel都是可以写的
										byteBuffer.put(String.valueOf(count++).getBytes("utf-8"));
										byteBuffer.flip();
										channel.write(byteBuffer);
									}catch (IOException e) {
										e.printStackTrace();
										break;
									}
								}
							};
						}.start();
					}
					//注册读事件
					channel.register(selector, SelectionKey.OP_READ);
				}else if (key.isReadable()) {
					SocketChannel keyChannel = (SocketChannel)key.channel();
					readBuffer.clear();
					int count = keyChannel.read(readBuffer);
					if (count > 0) {
						//System.out.println(new String(readBuffer.array(), 0, count));
						//log.info("接收到消息:{}", CharsetUtil.newStringUtf8(readBuffer.array()));
						log.info("接收到消息:{}", new String(readBuffer.array(), 0, count));

						//keyChannel.register(selector, SelectionKey.OP_READ);
					}
				}
			}
			keySet.clear();
		}
	}
}
