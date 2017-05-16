package yuan.research.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleNioServer{
	private static final Logger log = LoggerFactory.getLogger(SingleNioServer.class);

	static void handleKey(Selector selector,SelectionKey key)throws IOException{
		if (key.isAcceptable()) {// 客户端请求连接事件
			ServerSocketChannel keyChannel = (ServerSocketChannel) key.channel();
			// 获得和客户端连接的通道
			SocketChannel channel = keyChannel.accept();
			// 设置成非阻塞
			channel.configureBlocking(false);

			// 在这里可以给客户端发送信息哦
			log.info("新的客户端连接:{}",channel.getRemoteAddress());
			// 在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
			channel.register(selector, SelectionKey.OP_READ);

			//SelectionKey.OP_WRITE 表示底层缓冲区是否空闲
		} else if (key.isReadable()) {// 获得了可读的事件
			// 服务器可读取消息:得到事件发生的Socket通道
			SocketChannel channel = (SocketChannel) key.channel();
			// 创建读取的缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int read=0;
			try{
				read = channel.read(buffer);
			}catch(Exception e){
				channel.close();
				log.error("读取出错",e);
			}
			if(read > 0){
				byte[] data = buffer.array();
				String msg = new String(data).trim();
				log.info("服务端收到来自[{}]的信息:{}", channel.getRemoteAddress(), msg);
				if("exit".equals(msg))key.cancel();

				//回写数据
				ByteBuffer outBuffer = ByteBuffer.wrap("好的".getBytes());
				channel.write(outBuffer);// 将消息回送给客户端
			}else{
				log.info("客户端[{}]的消息为空", channel.getRemoteAddress());
				//key.cancel();//客户端关闭的时候关闭
			}
		}
	}
	public static void main(String[] args) throws IOException{
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// 设置通道为非阻塞
		serverChannel.configureBlocking(false);
		Selector selector=Selector.open();
		serverChannel.bind(new InetSocketAddress("0.0.0.0",8000));
		// 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
		// 当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		log.info("服务器启动");
		// 轮询访问selector
		while (true) {
			// 当注册的事件到达时，方法返回；否则,该方法会一直阻塞
			int size=selector.select();//阻塞;
			//selector.select(1000);//超过1000ms还没有数据过来返回
			//selector.selectNow();//非阻塞
			//selector.wakeup();//非阻塞
			log.info("select到[{}]个事件",size);
			// 获得selector中选中的项的迭代器，选中的项为注册的事件
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				handleKey(selector,key);
				// 删除已选的key,以防重复处理
				it.remove();
			}
		}

	}
}
