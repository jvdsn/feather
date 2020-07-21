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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * The Connection header.
 *
 * @author Joachim Vandersmissen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Connection implements Header {
    protected @NonNull String tokens;

    @Override
    public Connection deserialize(String s) {
        this.tokens = s;
        return this;
    }

    @Override
    public String serialize() {
        return this.tokens;
    }
}