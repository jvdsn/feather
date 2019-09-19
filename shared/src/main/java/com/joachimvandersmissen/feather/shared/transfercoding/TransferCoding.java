/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.shared.transfercoding;

import com.joachimvandersmissen.feather.shared.stream.HttpInputStream;
import com.joachimvandersmissen.feather.shared.stream.HttpOutputStream;
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
    public static final String IDENTITY = "identity";

    protected final @NonNull String identifier;

    /**
     * Returns a transfer coding by the identifier, or the identity transfer coding if the identifier is unknown.
     *
     * @param identifier The identifier.
     * @return The header.
     */
    public static TransferCoding getByIdentifier(String identifier) {
        switch (identifier) {
            case CHUNKED:
                return new ChunkedTransferCoding(new IdentityTransferCoding());
            default:
                return new IdentityTransferCoding();
        }
    }

    /**
     * Reads the message body according to the transfer coding.
     *
     * @param inputStream   The input stream to read from.
     * @param contentLength The length of the message body.
     * @return The message body.
     */
    public abstract byte[] readBody(HttpInputStream inputStream, BigInteger contentLength) throws IOException;

    /**
     * Writes the message body according to the transfer coding.
     *
     * @param outputStream  The output stream to write to.
     * @param contentLength The length of the message body.
     * @param body          The message body.
     */
    public abstract void writeBody(HttpOutputStream outputStream, BigInteger contentLength, byte... body) throws IOException;
}
