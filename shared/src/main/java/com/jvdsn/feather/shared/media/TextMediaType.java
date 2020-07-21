/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared.media;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A media type of type text.
 *
 * @author Joachim Vandersmissen
 */
public class TextMediaType extends MediaType {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;

    /**
     * Constructs a new text media type.
     *
     * @param subtype    the subtype of the text media type
     * @param parameters the parameters of the text media type
     */
    public TextMediaType(String subtype, Map<String, String> parameters) {
        super(TYPE_TEXT, subtype, parameters);
    }

    /**
     * Returns the charset defined in the parameters of this media type.
     *
     * @return the charset, or the default charset if no charset was defined in the parameters
     */
    public Charset getCharset() {
        return this.parameters.containsKey("charset") ? Charset.forName(this.parameters.get("charset")) : DEFAULT_CHARSET;
    }

    @Override
    public String toString(byte[] data) {
        return new String(data, this.getCharset());
    }
}
