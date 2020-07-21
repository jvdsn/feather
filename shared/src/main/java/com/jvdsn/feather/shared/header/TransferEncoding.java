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

import com.jvdsn.feather.shared.transfercoding.TransferCoding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
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

    /**
     * Returns the first transfer coding if possible.
     *
     * @return an optional containing the first transfer coding, or an empty optional
     */
    public Optional<TransferCoding> getFirst() {
        for (TransferCoding transferCoding : this.transferCodings) {
            return Optional.of(transferCoding);
        }

        return Optional.empty();
    }

    @Override
    public TransferEncoding deserialize(String s) {
        String[] splitted = s.split(",");
        List<TransferCoding> transferCodings = new ArrayList<>(splitted.length);
        for (String identifier : splitted) {
            identifier = identifier.trim();
            if (!identifier.isEmpty()) {
                transferCodings.add(TransferCoding.getByIdentifier(identifier));
            }
        }

        return new TransferEncoding(transferCodings);
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.transferCodings.size() - 1; i++) {
            sb.append(this.transferCodings.get(i).getIdentifier()).append(',');
        }

        sb.append(this.transferCodings.get(this.transferCodings.size() - 1).getIdentifier());
        return sb.toString();
    }
}
