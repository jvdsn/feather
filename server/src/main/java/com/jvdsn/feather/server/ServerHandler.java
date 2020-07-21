/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.server;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Handles all incoming connections, forwarding them to the HTTP server.
 *
 * @author Joachim Vandersmissen
 */
@RequiredArgsConstructor
public class ServerHandler implements Runnable {
    protected final @NonNull HttpServer httpServer;
    protected final @NonNull ServerSocket serverSocket;
    @Getter
    protected boolean stopped;

    /**
     * Stops the server handler.
     */
    public void stop() throws IOException {
        this.stopped = true;
        this.serverSocket.close();
    }

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        while (!this.stopped) {
            this.httpServer.acceptConnection(this.serverSocket.accept());
        }
    }
}
