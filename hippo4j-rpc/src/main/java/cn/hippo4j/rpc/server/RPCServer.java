/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hippo4j.rpc.server;

import cn.hippo4j.rpc.discovery.ServerPort;
import cn.hippo4j.rpc.exception.ConnectionException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Server Implementation
 */
public class RPCServer implements Server {

    ServerPort port;
    ServerConnection serverConnection;

    public RPCServer(ServerConnection serverConnection, ServerPort port) {
        this.port = port;
        this.serverConnection = serverConnection;
    }

    /**
     * Reference from{@link cn.hippo4j.config.netty.MonitorNettyServer}<br>
     * Start the server side asynchronously
     */
    @Override
    public void bind() {
        CompletableFuture
                .runAsync(() -> serverConnection.bind(port))
                .exceptionally(throwable -> {
                    throw new ConnectionException(throwable);
                });
    }

    @Override
    public boolean isActive() {
        return serverConnection.isActive();
    }

    /**
     * Shut down the server and release the port
     */
    @Override
    public void close() throws IOException {
        serverConnection.close();
    }
}
