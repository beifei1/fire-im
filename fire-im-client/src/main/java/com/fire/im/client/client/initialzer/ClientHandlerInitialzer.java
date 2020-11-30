package com.fire.im.client.client.initialzer;

import com.fire.im.client.client.handler.ImClientSimpleInboundHandler;
import com.fire.im.common.protocol.ImMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 16:39
 */

public class ClientHandlerInitialzer extends ChannelInitializer<Channel> {

    private ImClientSimpleInboundHandler clientHandler = new ImClientSimpleInboundHandler();

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                //10 秒没发送消息 将IdleStateHandler 添加到 ChannelPipeline 中
                .addLast(new IdleStateHandler(0, 10, 0))
                //拆包解码
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(ImMessage.RequestMessage.getDefaultInstance()))
                //拆包编码
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(clientHandler);
    }
}
