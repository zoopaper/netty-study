package org.netty.study.http.xml;

/**
 * @author krisjin
 *
 */
public class Order {
	private long orderNumber;

	private Customer customer;

	private Address billTo;

	private Shipping shipping;

	private Address shipTo;

	private float total;

}
