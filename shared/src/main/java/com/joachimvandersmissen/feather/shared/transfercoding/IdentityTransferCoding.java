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

import java.io.IOException;
import java.math.BigInteger;

/**
 * An 'identity' transfer coding.
 *
 * @author Joachim Vandersmissen
 */
public class IdentityTransferCoding extends TransferCoding {
    /**
     * Constructs a new identity transfer coding.
     */
    public IdentityTransferCoding() {
        super("identity");
    }

    @Override
    public byte[] readBody(HttpInputStream inputStream, BigInteger contentLength) throws IOException {
        byte[] body = new byte[contentLength.intValue()];
        inputStream.read(body);
        return body;
    }

    @Override
    public void writeBody(HttpOutputStream outputStream, BigInteger contentLength, byte... body) throws IOException {
        outputStream.write(body, 0, contentLength.intValue());
    }
}
