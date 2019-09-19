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

import com.joachimvandersmissen.feather.shared.HostAndPort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * The Host header.
 *
 * @author Joachim Vandersmissen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Host implements Header {
    protected @NonNull HostAndPort hostAndPort;

    @Override
    public Host deserialize(String s) {
        this.hostAndPort = HostAndPort.parse(s);
        return this;
    }

    @Override
    public String serialize() {
        return this.hostAndPort.toString();
    }
}