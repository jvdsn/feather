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
import com.jvdsn.feather.shared.request.RequestMethod;
import com.jvdsn.feather.shared.response.HttpResponse;
import lombok.NonNull;
import lombok.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * An HTTP input stream.
 *
 * @author Joachim Vandersmissen
 */
@Value
public class HttpInputStream extends InputStream {
    public static final String LINE_END = "\r\n";
    @NonNull InputStream inputStream;

    /**
     * Reads a line from the input.
     * Lines are ended by CRLF.
     *
     * @return the line
     */
    public String readLine() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        while ((i = this.inputStream.read()) != -1 && i != LINE_END.charAt(0)) {
            stringBuilder.append((char) i);
        }

        for (int j = 1; j < LINE_END.length(); j++) {
            if ((i = this.inputStream.read()) != LINE_END.charAt(j)) {
                throw new IOException("Expected " + (int) LINE_END.charAt(j) + " but got " + i + " for line end");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Reads the headers from the input.
     *
     * @return the headers
     */
    public Headers readHeaders() throws IOException {
        NavigableMap<String, Header> headers = new TreeMap<>();
        String line;
        while (!(line = this.readLine()).isEmpty()) {
            int i = line.indexOf(Header.SEPARATOR);
            String fieldName = line.substring(0, i).trim();
            Header header = Header.getByFieldName(fieldName);
            header.deserialize(line.substring(i + 1).trim());
            headers.put(fieldName, header);
        }

        return new Headers(headers);
    }

    /**
     * Reads an HTTP request from the input.
     *
     * @return the HTTP request
     */
    public HttpRequest readHttpRequest() throws IOException {
        String requestLine = this.readLine();
        int i = requestLine.indexOf(' ');
        RequestMethod requestMethod = RequestMethod.valueOf(requestLine.substring(0, i));
        String request = requestLine.substring(i + 1);
        int i1 = request.indexOf(' ');
        return new HttpRequest(requestMethod, request.substring(0, i1), request.substring(i1 + 1), this.readHeaders());
    }

    /**
     * Reads an HTTP response from the input.
     *
     * @return the HTTP response
     */
    public HttpResponse readHttpResponse() throws IOException {
        String statusLine = this.readLine();
        int i = statusLine.indexOf(' ');
        String httpVersion = statusLine.substring(0, i);
        String status = statusLine.substring(i + 1);
        int i1 = status.indexOf(' ');
        return new HttpResponse(httpVersion, Integer.parseInt(status.substring(0, i1)), status.substring(i1 + 1), this.readHeaders());
    }

    @Override
    public int read() throws IOException {
        return this.inputStream.read();
    }
}
