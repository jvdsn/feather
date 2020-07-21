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

import com.jvdsn.feather.shared.media.MediaType;
import com.jvdsn.feather.shared.media.TextMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joachim Vandersmissen
 */
public class ContentTypeTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new ContentType(null));
        Map<String, String> parameters = new HashMap<>(1);
        parameters.put("test", "test");
        MediaType mediaType = new TextMediaType("test", parameters);
        ContentType contentType = new ContentType(mediaType);
        Assertions.assertSame(mediaType, contentType.getMediaType());
    }

    @Test
    public void testDeserialize() {
        ContentType contentType = new ContentType();
        Map<String, String> parameters = new HashMap<>(1);
        parameters.put("test", "test");
        Assertions.assertEquals(new TextMediaType("test", parameters), contentType.deserialize("text/test;test=test").getMediaType());
    }

    @Test
    public void testSerialize() {
        Map<String, String> parameters = new HashMap<>(1);
        parameters.put("test", "test");
        Header header = new ContentType(new TextMediaType("test", parameters));
        Assertions.assertEquals("text/test;test=test", header.serialize());
    }
}
