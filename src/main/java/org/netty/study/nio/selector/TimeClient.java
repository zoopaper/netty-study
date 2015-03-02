package org.netty.study.nio.selector;

public class TimeClient {
	public static void main(String[] args) {
		int port = 8080;
		if (args.length > 0) {
			port = Integer.valueOf(args[0]);
		}

		new Thread(new TimeClientHandler(8080, "127.0.0.1")).start();

	}
}
