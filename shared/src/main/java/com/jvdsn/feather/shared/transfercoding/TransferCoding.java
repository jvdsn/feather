/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared.transfercoding;

import com.jvdsn.feather.shared.stream.HttpInputStream;
import com.jvdsn.feather.shared.stream.HttpOutputStream;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigInteger;

/**
 * A transfer coding.
 *
 * @author Joachim Vandersmissen
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public abstract class TransferCoding {
    public static final String CHUNKED = "chunked";
    public static final String DEFAULT = "default";

    protected final @NonNull String identifier;

    /**
     * Returns a transfer coding by the identifier, or the default transfer coding if the identifier is unknown.
     *
     * @param identifier the identifier
     * @return the header
     */
    public static TransferCoding getByIdentifier(String identifier) {
        switch (identifier) {
            case CHUNKED:
                return new ChunkedTransferCoding(new DefaultTransferCoding());
            default:
                return new DefaultTransferCoding();
        }
    }

    /**
     * Reads the message body according to the transfer coding.
     *
     * @param inputStream   the input stream to read from
     * @param contentLength the length of the message body
     * @return the message body
     */
    public abstract byte[] readBody(HttpInputStream inputStream, BigInteger contentLength) throws IOException;

    /**
     * Writes the message body according to the transfer coding.
     *
     * @param outputStream  the output stream to write to
     * @param contentLength the length of the message body
     * @param body          the message body
     */
    public abstract void writeBody(HttpOutputStream outputStream, BigInteger contentLength, byte[] body) throws IOException;
}
