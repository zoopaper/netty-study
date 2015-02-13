package org.netty.study.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	HttpMethod GET = new HttpMethod("GET");

	private String url;

	private final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

	private final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

	public HttpFileServerHandler(String url) {
		this.url = url;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

		if (!request.getDecoderResult().isSuccess()) {
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}

		if (request.getMethod() != GET) {
			//
			return;
		}
		final String uri = request.getUri();
		final String path = sanitizeUri(uri);
		if (path == null) {
			return;
		}

		File file = new File(path);

		if (file.isHidden() || !file.exists()) {
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}

		if (file.isDirectory()) {
			if (uri.endsWith("/")) {
				sendList(ctx, file);
			} else {
				sendRedirect(ctx, uri + '/');
			}
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {

	}

	public String sanitizeUri(String uri) {
		try {
			uri = URLDecoder.decode(url, "UTF-8");

		} catch (UnsupportedEncodingException e) {

			try {
				uri = URLDecoder.decode(uri, "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) {
				throw new Error();
			}

		}
		if (!uri.startsWith(url)) {
			return null;
		}

		if (!uri.startsWith("/")) {
			return null;
		}

		uri = uri.replace('/', File.separatorChar);
		if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.startsWith(".")
				|| uri.endsWith(".")) {
			return null;
		}

		return System.getProperty("user.dir") + File.separator + uri;

	}

	public final void sendList(ChannelHandlerContext ctx, File file) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");
		StringBuilder sb = new StringBuilder();

		String dirPath = file.getPath();

		sb.append("<!DOCTYPE html>\r\n");
		sb.append("<html><head><title>");
		sb.append(dirPath);
		sb.append("目录：");
		sb.append("</title></head><body>\r\n");
		sb.append("<h3>");
		sb.append(dirPath).append("目录：");
		sb.append("</h3>\r\n");

		sb.append("<ul>");
		sb.append("<li>链接：<a href=\"../\">..</a></li>\r\n");

		for (File f : file.listFiles()) {
			if (f.isHidden() || !f.canRead()) {
				continue;
			}
			String name = f.getName();
			if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
				continue;
			}

			sb.append("<li>链接：<a href=\"");
			sb.append(name);
			sb.append("\"");
			sb.append("</a></li>\r\n");

		}

		sb.append("</ul></body></html>\r\n");

		ByteBuf buf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
		response.content().writeBytes(buf);
		buf.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public void sendRedirect(ChannelHandlerContext ctx, String newUri) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		response.headers().set(HttpHeaders.Names.LOCATION, newUri);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

	}

}
