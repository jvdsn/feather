/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.shared.media;

import java.util.Map;

/**
 * A media type with unknown type.
 *
 * @author Joachim Vandersmissen
 */
public class UnknownMediaType extends MediaType {
    /**
     * Constructs a new unknown media type.
     *
     * @param type The type of the media type.
     * @param subtype The subtype of the media type.
     * @param parameters The parameters of the media type.
     */
    public UnknownMediaType(String type, String subtype, Map<String, String> parameters) {
        super(type, subtype, parameters);
    }

    @Override
    public String toString(byte... data) {
        throw new UnsupportedOperationException("Unsupported toString for " + this.type + '/' + this.subtype);
    }
}
