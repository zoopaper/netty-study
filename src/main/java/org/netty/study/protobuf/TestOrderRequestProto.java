package org.netty.study.protobuf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

public class TestOrderRequestProto {
	
	private static byte[] encode(OrderRequestProto.OrderRequest req) {
		return req.toByteArray();
	}
	
	private static OrderRequestProto.OrderRequest decode(byte[] body) throws InvalidProtocolBufferException{
		
		return OrderRequestProto.OrderRequest.parseFrom(body);
	}
	
	private static OrderRequestProto.OrderRequest createOrderRequest(){
		OrderRequestProto.OrderRequest.Builder builder = OrderRequestProto.OrderRequest.newBuilder();
		builder.setOrderId(1001);
		builder.setUserName("krisjin");
		builder.setProductName("netty in action");
		List<String> address= new ArrayList<String>();
		address.add("Beijing of china");
		address.add("Shanghai of china");
		address.add("Guangzhou of china");
		builder.addAllAddress(address);
		
		return builder.build();
	}
	
	public static void main(String[] args) throws InvalidProtocolBufferException, UnsupportedEncodingException {
		OrderRequestProto.OrderRequest req = createOrderRequest();
		System.out.println("Before encode: \n"+req.toString());
		
		byte[] st=encode(req);
		
		
		OrderRequestProto.OrderRequest req2=decode(encode(req));
		System.out.println("after decode : \n"+req2.toString());
		
		
		
		
	}
	
}
