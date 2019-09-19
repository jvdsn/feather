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

import com.joachimvandersmissen.feather.shared.transfercoding.TransferCoding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The Transfer-Encoding header.
 *
 * @author Joachim Vandersmissen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferEncoding implements Header {
    protected @NonNull List<TransferCoding> transferCodings;

    public TransferEncoding(@NonNull TransferCoding... transferCodings) {
        this.transferCodings = new ArrayList<>(Arrays.asList(transferCodings));
    }

    /**
     * Returns the first transfer coding if possible.
     *
     * @return An optional containing the first transfer coding, or an empty optional.
     */
    public Optional<TransferCoding> getFirst() {
        return this.transferCodings.stream().findFirst();
    }

    @Override
    public TransferEncoding deserialize(String s) {
        this.transferCodings = Header.stringToList(s, TransferCoding::getByIdentifier);
        return this;
    }

    @Override
    public String serialize() {
        return Header.listToString(this.transferCodings, TransferCoding::getIdentifier);
    }
}
