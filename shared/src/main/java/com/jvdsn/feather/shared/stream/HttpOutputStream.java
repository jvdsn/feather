/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared.stream;

import com.jvdsn.feather.shared.Headers;
import com.jvdsn.feather.shared.header.Header;
import com.jvdsn.feather.shared.request.HttpRequest;
import com.jvdsn.feather.shared.response.HttpResponse;
import lombok.NonNull;
import lombok.Value;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;

/**
 * An HTTP output stream.
 *
 * @author Joachim Vandersmissen
 */
@Value
public class HttpOutputStream extends OutputStream {
    public static final String LINE_END = "\r\n";
    @NonNull OutputStream outputStream;

    /**
     * Writes a line to the output.
     * Lines are ended by CRLF.
     *
     * @param line the line
     */
    public void writeLine(String line) throws IOException {
        this.outputStream.write((line + LINE_END).getBytes());
    }

    /**
     * Writes the headers to the output.
     *
     * @param headers the headers
     */
    public void writeHeaders(Headers headers) throws IOException {
        for (Entry<String, Header> header : headers.getHeaders().entrySet()) {
            this.writeLine(header.getKey() + Header.SEPARATOR + header.getValue().serialize());
        }

        this.writeLine("");
    }

    /**
     * Writes an HTTP request to the output.
     *
     * @param httpRequest the HTTP request
     */
    public void writeRequest(HttpRequest httpRequest) throws IOException {
        this.writeLine(httpRequest.getRequestMethod().toString() + ' ' + httpRequest.getResource() + ' ' + httpRequest.getHttpVersion());
        this.writeHeaders(httpRequest.getHeaders());
    }

    /**
     * Writes an HTTP response to the output.
     *
     * @param httpResponse the HTTP response
     */
    public void writeResponse(HttpResponse httpResponse) throws IOException {
        this.writeLine(httpResponse.getHttpVersion() + ' ' + httpResponse.getStatusCode() + ' ' + httpResponse.getStatusMessage());
        this.writeHeaders(httpResponse.getHeaders());
    }

    @Override
    public void write(int i) throws IOException {
        this.outputStream.write(i);
    }
}
