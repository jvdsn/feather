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

import com.jvdsn.feather.shared.Headers;
import com.jvdsn.feather.shared.stream.HttpInputStream;
import com.jvdsn.feather.shared.stream.HttpOutputStream;
import lombok.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A 'chunked' transfer coding.
 *
 * @author Joachim Vandersmissen
 */
public class ChunkedTransferCoding extends TransferCoding {
    public static final char EXTENSION_SEPARATOR = ';';
    public static final char EXTENSION_VALUE_SEPARATOR = '=';
    public static final BigInteger DEFAULT_CHUNK_SIZE = BigInteger.valueOf(8192);

    protected final TransferCoding transferCoding;
    protected final BigInteger chunkSize;

    /**
     * Constructs a new chunked transfer coding.
     *
     * @param transferCoding the underlying transfer coding to use
     * @param chunkSize      the chunk size to use
     */
    public ChunkedTransferCoding(@NonNull TransferCoding transferCoding, @NonNull BigInteger chunkSize) {
        super(TransferCoding.CHUNKED);
        this.transferCoding = transferCoding;
        this.chunkSize = chunkSize;
    }

    /**
     * Constructs a new chunked transfer coding.
     *
     * @param transferCoding the underlying transfer coding to use
     */
    public ChunkedTransferCoding(@NonNull TransferCoding transferCoding) {
        this(transferCoding, DEFAULT_CHUNK_SIZE);
    }

    /**
     * Subclasses can override this method to use the extensions in a chunk.
     *
     * @param outputStream the output stream to which the full payload (all chunks) is written
     * @param chunk        the chunk
     * @param extensions   the extensions on the chunk
     */
    protected void useExtensions(ByteArrayOutputStream outputStream, byte[] chunk, Map<String, String> extensions) {
    }

    /**
     * Subclasses can override this method to use the trailer.
     *
     * @param outputStream the output stream to which the full payload (all chunks) is written
     * @param trailer      the trailer
     */
    protected void useTrailer(ByteArrayOutputStream outputStream, Headers trailer) {
    }

    /**
     * Subclasses can override this method to provide the extensions on a chunk.
     *
     * @param inputStream the input stream from which the full payload (all chunks) is read
     * @param chunk       the chunk
     * @return the extensions
     */
    protected Map<String, String> getExtensions(ByteArrayInputStream inputStream, byte[] chunk) {
        return Collections.emptyMap();
    }

    /**
     * Subclasses can override this method to provide the trailer.
     *
     * @param inputStream the input stream from which the full payload (all chunks) is read
     * @return the trailer
     */
    protected Headers getTrailer(ByteArrayInputStream inputStream) {
        return new Headers();
    }

    @Override
    public byte[] readBody(HttpInputStream inputStream, BigInteger contentLength) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            while (true) {
                String line = inputStream.readLine();
                String[] splitted = line.split(String.valueOf(EXTENSION_SEPARATOR));
                BigInteger chunkSize = new BigInteger(splitted[0], 16);
                byte[] chunk = this.transferCoding.readBody(inputStream, chunkSize);

                Map<String, String> extensions = new HashMap<>();
                for (int i = 1; i < splitted.length; i++) {
                    int j = splitted[i].indexOf(EXTENSION_VALUE_SEPARATOR);
                    extensions.put(splitted[i].substring(0, j), splitted[i].substring(j + 1));
                }

                this.useExtensions(outputStream, chunk, extensions);
                outputStream.write(chunk);
                inputStream.readLine();
                if (chunkSize.equals(BigInteger.ZERO)) {
                    break;
                }
            }

            this.useTrailer(outputStream, inputStream.readHeaders());
            return outputStream.toByteArray();
        }
    }

    @Override
    public void writeBody(HttpOutputStream outputStream, BigInteger contentLength, byte[] body) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(body)) {
            while (true) {
                int available = inputStream.available();
                StringBuilder line = new StringBuilder();
                BigInteger chunkSize = this.chunkSize.min(BigInteger.valueOf(available));
                line.append(chunkSize.toString(16));
                byte[] chunk = new byte[chunkSize.intValue()];
                inputStream.read(chunk);

                Map<String, String> extensions = this.getExtensions(inputStream, chunk);
                for (Entry<String, String> extension : extensions.entrySet()) {
                    line.append(EXTENSION_SEPARATOR).append(extension.getKey()).append(EXTENSION_VALUE_SEPARATOR).append(extension.getValue());
                }

                outputStream.writeLine(line.toString());
                this.transferCoding.writeBody(outputStream, chunkSize, chunk);
                outputStream.writeLine("");
                if (available == 0) {
                    break;
                }
            }

            outputStream.writeHeaders(this.getTrailer(inputStream));
        }
    }
}
