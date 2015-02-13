package org.netty.study.serializable;

import java.io.Serializable;

public class OrderRequest implements Serializable {

	private static final long serialVersionUID = 7717830451484890784L;

	private long orderId;

	private String userName;

	private String productName;

	private String address;

	private String mobilePhone;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String toString() {

		String s = "Order Request[orderId=" + this.orderId + ", userName=" + this.userName + ", address="
				+ this.address + ", productName=" + this.productName + "]";

		return s;
	}

}
