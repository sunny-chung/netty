/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.netty.handler.codec.http2;

import java.util.Map;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

public class InspectedHttp2Headers extends DefaultHttp2Headers {
    public static final String PSEUDO_HEADER_KEY_DYNAMIC_TABLE_SIZE_UPDATE = ":::update-dynamic-table-size";

    public InspectedHttp2Headers() {
    }

    public InspectedHttp2Headers(boolean validate) {
        super(validate);
    }

    public InspectedHttp2Headers(boolean validate, int arraySizeHint) {
        super(validate, arraySizeHint);
    }

    public InspectedHttp2Headers(boolean validate, boolean validateValues, int arraySizeHint) {
        super(validate, validateValues, arraySizeHint);
    }

    public InspectedHttp2Headers(Http2Headers headers) {
        super(false, false, headers.size());
        for (Map.Entry<CharSequence, CharSequence> header : headers) {
            add(header.getKey(), header.getValue(), BinaryFormat.LITERAL_NEVER_INDEXED, null);
        }
    }

    public Http2Headers add(CharSequence name, CharSequence value, BinaryFormat format, Integer headerIndex) {
        validateName(nameValidator(), true, name);
        validateValue(valueValidator(), name, value);
        checkNotNull(value, "value");
        int h = hashingStrategy.hashCode(name);
        int i = index(h);
        add0(h, i, name, value, format, headerIndex);
        return this;
    }

    private void add0(int h, int i, CharSequence name, CharSequence value, BinaryFormat format, Integer headerIndex) {
        // Update the hash table.
        entries[i] = newHeaderEntry(h, name, value, format, headerIndex, entries[i]);
        ++size;
    }

    protected HeaderEntry<CharSequence, CharSequence> newHeaderEntry(int h, CharSequence name, CharSequence value,
                                                                     BinaryFormat format, Integer headerIndex,
                                                                     HeaderEntry<CharSequence, CharSequence> next) {
        return new InspectedHttp2HeaderEntry(h, name, value, format, headerIndex, next);
    }

    public class InspectedHttp2HeaderEntry extends Http2HeaderEntry {
        private BinaryFormat format;
        private Integer index;

        InspectedHttp2HeaderEntry(int hash, CharSequence key, CharSequence value, BinaryFormat format,
                                  Integer headerIndex, HeaderEntry<CharSequence, CharSequence> next) {
            super(hash, key, value, next);
            this.format = format;
            this.index = headerIndex;
        }

        public BinaryFormat getFormat() {
            return format;
        }

        public Integer getIndex() {
            return index;
        }

        void setFormat(BinaryFormat format) {
            this.format = format;
        }

        void setIndex(Integer index) {
            this.index = index;
        }
    }

    public enum BinaryFormat {
        INDEXED, LITERAL_WITH_INDEXING, LITERAL_WITHOUT_INDEXING, LITERAL_NEVER_INDEXED,

        /**
         * Pseudo header.
         */
        OTHER;
    }
}
