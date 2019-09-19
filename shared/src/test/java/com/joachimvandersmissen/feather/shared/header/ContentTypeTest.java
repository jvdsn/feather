/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. 
 */

package com.joachimvandersmissen.feather.shared.header;

import com.joachimvandersmissen.feather.shared.media.MediaType;
import com.joachimvandersmissen.feather.shared.media.TextMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author Joachim Vandersmissen
 */
public class ContentTypeTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new ContentType(null));
        MediaType mediaType = new TextMediaType("test", Map.of("test", "test"));
        ContentType contentType = new ContentType(mediaType);
        Assertions.assertSame(mediaType, contentType.getMediaType());
    }

    @Test
    public void testDeserialize() {
        ContentType contentType = new ContentType();
        Assertions.assertEquals(new TextMediaType("test", Map.of("test", "test")), contentType.deserialize(" text / test ; test = test ").getMediaType());
    }

    @Test
    public void testSerialize() {
        Header header = new ContentType(new TextMediaType("test", Map.of("test", "test")));
        Assertions.assertEquals("text/test;test=test", header.serialize());
    }
}
