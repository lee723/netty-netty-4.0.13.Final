/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(
            EchoClientHandler.class.getName());

    private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler(int firstMessageSize) {
        if (firstMessageSize <= 0) {
            throw new IllegalArgumentException("firstMessageSize: " + firstMessageSize);
        }
        firstMessage = Unpooled.buffer(firstMessageSize);
        for (int i = 0; i < firstMessage.capacity(); i ++) {
            firstMessage.writeByte((byte) i);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端接收消息：" + msg.toString());
        // 加上这句又会写回到服务端，然后来回写
//        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
        ctx.close();
    }
}