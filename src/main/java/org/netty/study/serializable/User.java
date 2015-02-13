package org.netty.study.serializable;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class User implements Serializable {

	private static final long serialVersionUID = 1875526722384216011L;

	public int id;
	private String userName;
	private String password;
	private String mobilePhone;
	private int sex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public byte[] codeC() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.id);
		buffer.flip();
		value = null;
		byte[] result = new byte[buffer.remaining()];

		buffer.get(result);
		return result;

	}
}
