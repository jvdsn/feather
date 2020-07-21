/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared.header;

import com.jvdsn.feather.shared.transfercoding.ChunkedTransferCoding;
import com.jvdsn.feather.shared.transfercoding.DefaultTransferCoding;
import com.jvdsn.feather.shared.transfercoding.TransferCoding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Joachim Vandersmissen
 */
public class TransferEncodingTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new TransferEncoding(null));
        List<TransferCoding> transferCodings = Arrays.asList(new ChunkedTransferCoding(new DefaultTransferCoding()), new DefaultTransferCoding());
        TransferEncoding transferEncoding = new TransferEncoding(transferCodings);
        Assertions.assertSame(transferCodings, transferEncoding.getTransferCodings());
    }

    @Test
    public void testGetFirst() {
        List<TransferCoding> transferCodings = Arrays.asList(new ChunkedTransferCoding(new DefaultTransferCoding()), new DefaultTransferCoding());
        TransferEncoding transferEncoding = new TransferEncoding(transferCodings);
        Assertions.assertEquals(2, transferCodings.size());
        Assertions.assertTrue(transferEncoding.getFirst().isPresent());
        Assertions.assertEquals(transferCodings.get(0), transferEncoding.getFirst().get());
        Assertions.assertFalse(new TransferEncoding(Collections.emptyList()).getFirst().isPresent());
    }

    @Test
    public void testDeserialize() {
        List<TransferCoding> transferCodings = Arrays.asList(new ChunkedTransferCoding(new DefaultTransferCoding()), new DefaultTransferCoding());
        TransferEncoding transferEncoding = new TransferEncoding();
        Assertions.assertEquals(transferCodings, transferEncoding.deserialize("chunked,default").getTransferCodings());
    }

    @Test
    public void testSerialize() {
        List<TransferCoding> transferCodings = Arrays.asList(new ChunkedTransferCoding(new DefaultTransferCoding()), new DefaultTransferCoding());
        Header header = new TransferEncoding(transferCodings);
        Assertions.assertEquals("chunked,default", header.serialize());
    }
}
