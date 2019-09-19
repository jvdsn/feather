/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.server;

import com.joachimvandersmissen.feather.shared.HttpAgent;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;

/**
 * Handles all incoming requests for a single client, forwarding them to the HTTP server.
 *
 * @author Joachim Vandersmissen
 */
public class ClientHandler extends HttpAgent implements Runnable {
    protected final HttpServer httpServer;
    @Getter
    protected boolean stopped;

    /**
     * Constructs a new client handler.
     *
     * @param httpServer The HTTP server constructing this client handler.
     * @param socket     The socket to read from and write to.
     * @throws IOException if an IOException occurs.
     */
    public ClientHandler(@NonNull HttpServer httpServer, Socket socket) throws IOException {
        super(socket);
        this.httpServer = httpServer;
    }

    /**
     * Stops the client handler.
     */
    public void stop() throws IOException {
        this.stopped = true;
        this.disconnect();
    }

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        while (!this.stopped) {
            this.httpServer.handleRequest(this, this.receiveRequest());
        }
    }
}
