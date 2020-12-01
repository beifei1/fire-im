package com.fire.im.server.server.initializer;

import com.fire.im.common.protocol.ImMessage;
import com.fire.im.server.server.handler.ServerAuthenticationHandler;
import com.fire.im.server.server.handler.ServerSimpleInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Author: wangzc
 * @Date: 2020/11/23 17:13
 */

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 认证
     */
    private final ServerAuthenticationHandler authenticationHandler = new ServerAuthenticationHandler();
    /**
     * 其他处理
     */
    private final ServerSimpleInboundHandler simpleInboundHandler = new ServerSimpleInboundHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline()
                //12秒没有读取到消息就触发自定义操作
                .addLast("inboundHeartBeat",new IdleStateHandler(12,0,0))
                //入站半包处理
                .addLast("inboundPack", new ProtobufVarint32FrameDecoder())
                //入站protobuf解码器
                .addLast("inboundProtobufDecoder", new ProtobufDecoder(ImMessage.RequestMessage.getDefaultInstance()))
                 //出站在消息头中加入int32标识消息长度
                .addLast("outboundPackAdd", new ProtobufVarint32LengthFieldPrepender())
                //出站protobuf编码器
                .addLast("outboundProtobufEncoder", new ProtobufEncoder())
                //入站身份认证
                .addLast("inboundAuthentication", authenticationHandler)
                //入站其他处理
                .addLast("inboundOther", simpleInboundHandler);
    }
}
