/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared;

import com.jvdsn.feather.shared.header.ContentLength;
import com.jvdsn.feather.shared.header.Header;
import com.jvdsn.feather.shared.header.TransferEncoding;
import com.jvdsn.feather.shared.request.HttpRequest;
import com.jvdsn.feather.shared.response.HttpResponse;
import com.jvdsn.feather.shared.stream.HttpInputStream;
import com.jvdsn.feather.shared.stream.HttpOutputStream;
import com.jvdsn.feather.shared.transfercoding.DefaultTransferCoding;
import com.jvdsn.feather.shared.transfercoding.TransferCoding;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Optional;

/**
 * A general purpose HTTP agent.
 *
 * @author Joachim Vandersmissen
 */
@RequiredArgsConstructor
public class HttpAgent {
    protected final TransferCoding defaultTransferCoding;
    protected final Socket socket;
    protected final HttpInputStream inputStream;
    protected final HttpOutputStream outputStream;

    /**
     * Constructs a new HTTP agent.
     *
     * @param socket The socket to read from and write to.
     */
    public HttpAgent(@NonNull Socket socket) throws IOException {
        this.defaultTransferCoding = new DefaultTransferCoding();
        this.socket = socket;
        this.inputStream = new HttpInputStream(socket.getInputStream());
        this.outputStream = new HttpOutputStream(socket.getOutputStream());
    }

    /**
     * Disconnects from the HTTP agent.
     */
    public void disconnect() throws IOException {
        this.socket.close();
    }

    /**
     * Receives an HTTP request from the agent.
     *
     * @return the HTTP request
     */
    public HttpRequest receiveRequest() throws IOException {
        return this.inputStream.readHttpRequest();
    }

    /**
     * Receives an HTTP response from the agent.
     *
     * @return the HTTP response
     */
    public HttpResponse receiveResponse() throws IOException {
        return this.inputStream.readHttpResponse();
    }

    /**
     * Receives a message body from the agent.
     *
     * @param headers the headers to use
     * @return the message body
     */
    public byte[] receiveMessageBody(Headers headers) throws IOException {
        Optional<TransferCoding> transferCoding = headers.get(Header.TRANSFER_ENCODING, TransferEncoding.class).flatMap(TransferEncoding::getFirst);
        BigInteger contentLength = headers.get(Header.CONTENT_LENGTH, ContentLength.class).map(ContentLength::getLength).orElse(BigInteger.ZERO);
        return transferCoding.orElse(this.defaultTransferCoding).readBody(this.inputStream, contentLength);
    }

    /**
     * Sends an HTTP request to the agent.
     *
     * @param request the HTTP request
     */
    public void sendRequest(HttpRequest request) throws IOException {
        this.outputStream.writeRequest(request);
    }

    /**
     * Sends an HTTP response to the agent.
     *
     * @param response the HTTP response
     */
    public void sendResponse(HttpResponse response) throws IOException {
        this.outputStream.writeResponse(response);
    }

    /**
     * Sends a message body to the agent.
     *
     * @param headers the headers to use
     * @param body    the message body
     */
    public void sendMessageBody(Headers headers, byte[] body) throws IOException {
        Optional<TransferCoding> transferCoding = headers.get(Header.TRANSFER_ENCODING, TransferEncoding.class).flatMap(TransferEncoding::getFirst);
        BigInteger contentLength = headers.get(Header.CONTENT_LENGTH, ContentLength.class).map(ContentLength::getLength).orElse(BigInteger.ZERO);
        transferCoding.orElse(this.defaultTransferCoding).writeBody(this.outputStream, contentLength, body);
    }
}
