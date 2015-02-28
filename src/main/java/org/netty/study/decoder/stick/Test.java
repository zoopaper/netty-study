package org.netty.study.decoder.stick;

public class Test {

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			new Thread() {

				public void run() {
					new TimeClient().connect(8888, "127.0.0.1");
				}

			}.start();
		}

	}

}
