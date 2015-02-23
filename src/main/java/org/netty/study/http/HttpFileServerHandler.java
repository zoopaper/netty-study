package org.netty.study.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

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

		if (request.getMethod() != HttpMethod.GET) {
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
			return;
		}
		final String uri = request.getUri();
		final String path = sanitizeUri(uri);
		if (path == null) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
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
			return;
		}

		if (!file.isFile()) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}

		RandomAccessFile randomAccessFile = null;

		try {
			randomAccessFile = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			sendError(ctx, HttpResponseStatus.NO_CONTENT);
			return;
		}
		long fileLength = randomAccessFile.length();
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		HttpHeaders.setContentLength(response, fileLength);
		setContentTypeHeader(response, file);

		if (HttpHeaders.isKeepAlive(request)) {
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		ctx.write(response);
		ChannelFuture sendFileFuture;

		sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
		sendFileFuture.addListener(new ChannelProgressiveFutureListener() {

			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {

				System.err.println(future.channel() + " transfer complete");

			}

			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
					throws Exception {
				if (total < 0) {
					System.err.println(future.channel() + " transfer progress : " + progress);
				} else {
					System.err.println(future.channel() + " transfer progress : " + progress + " / " + total);
				}
			}
		});

		ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if (!HttpHeaders.isKeepAlive(request)) {
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		cause.printStackTrace();
		if (ctx.channel().isActive()) {
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(
				"Failure :" + status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public String sanitizeUri(String uri) {

		// Decode the path.
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}

		if (uri.isEmpty() || uri.charAt(0) != '/') {
			return null;
		}

		// Convert file separators.
		uri = uri.replace('/', File.separatorChar);

		// Simplistic dumb security check.
		// You will have to do something serious in the production environment.
		if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.charAt(0) == '.'
				|| uri.charAt(uri.length() - 1) == '.' || INSECURE_URI.matcher(uri).matches()) {
			return null;
		}

		// Convert to absolute path.
		return System.getProperty("user.dir") + File.separator + uri;
	}

	public final void sendList(ChannelHandlerContext ctx, File file) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
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
		sb.append("<li><a href=\"../\">..</a></li>\r\n");

		for (File f : file.listFiles()) {
			if (f.isHidden() || !f.canRead()) {
				continue;
			}
			String name = f.getName();
			if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
				continue;
			}

			sb.append("<li><a href=\"");
			sb.append(name);
			sb.append("\">");
			sb.append(name);
			sb.append("</a></li>\r\n");

		}

		sb.append("</ul></body></html>\r\n");

		ByteBuf buffer = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
		response.content().writeBytes(buffer);
		buffer.release();

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public void sendRedirect(ChannelHandlerContext ctx, String newUri) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		response.headers().set(HttpHeaders.Names.LOCATION, newUri);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

	}

	private void setContentTypeHeader(HttpResponse response, File file) {

		MimetypesFileTypeMap mimeTypeMap = new MimetypesFileTypeMap();
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, mimeTypeMap.getContentType(file.getPath()));
	}

}
