package org.netty.study.nio;

/**
 * @author krisjin
 * 
 */
public class TimeServer {
	public static void main(String[] args) {
		int port = 8080;
		if (args.length > 0) {
			port = Integer.valueOf(args[0]);
		}

		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);

		new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
	}
}
