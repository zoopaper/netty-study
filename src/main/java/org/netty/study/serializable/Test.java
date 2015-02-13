package org.netty.study.serializable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Test {

	public static void main(String[] args) throws IOException {
		User user = new User();
		user.setId(1001);
		user.setUserName("krisjin");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);

		os.writeObject(user);
		os.flush();
		os.close();

		byte[] b = bos.toByteArray();
		System.out.println("The jdk serializable length is :" + b.length);
		bos.close();

		System.out.println("The byte array serializable length is :" + user.codeC().length);
	}

}
