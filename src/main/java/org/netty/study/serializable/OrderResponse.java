package org.netty.study.serializable;

import java.io.Serializable;

public class OrderResponse implements Serializable {

	private static final long serialVersionUID = 754846646088026444L;

	private long orderId;

	private int respCode;

	private String desc;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString() {
		String s = "OrderResp [orderId=" + this.orderId + ", respCode=" + this.respCode + ", desc=" + this.desc + "]";
		return s;
	}
}
