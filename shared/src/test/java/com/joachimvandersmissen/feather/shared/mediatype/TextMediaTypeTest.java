/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.shared.mediatype;

import com.joachimvandersmissen.feather.shared.media.MediaType;
import com.joachimvandersmissen.feather.shared.media.TextMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @author Joachim Vandersmissen
 */
public class TextMediaTypeTest {
    @Test
    public void testGetCharset() {
        TextMediaType textMediaType = new TextMediaType("test", Map.of("charset", "US-ASCII"));
        Assertions.assertEquals(StandardCharsets.US_ASCII, textMediaType.getCharset());
        Assertions.assertEquals(StandardCharsets.ISO_8859_1, new TextMediaType("test", Collections.emptyMap()).getCharset());
    }

    @Test
    public void testToString() {
        MediaType mediaType = new TextMediaType("test", Map.of("charset", "US-ASCII"));
        Assertions.assertEquals("abc", mediaType.toString(new byte[]{'a', 'b', 'c'}));
    }
}
