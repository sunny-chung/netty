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

public class InspectedHttp2HeadersDecoder extends DefaultHttp2HeadersDecoder {

    public InspectedHttp2HeadersDecoder() {
    }

    public InspectedHttp2HeadersDecoder(boolean validateHeaders) {
        super(validateHeaders);
    }

    public InspectedHttp2HeadersDecoder(boolean validateHeaders, boolean validateHeaderValues) {
        super(validateHeaders, validateHeaderValues);
    }

    public InspectedHttp2HeadersDecoder(boolean validateHeaders, long maxHeaderListSize) {
        super(validateHeaders, maxHeaderListSize);
    }

    public InspectedHttp2HeadersDecoder(boolean validateHeaders, boolean validateHeaderValues, long maxHeaderListSize) {
        super(validateHeaders, validateHeaderValues, maxHeaderListSize);
    }

    public InspectedHttp2HeadersDecoder(boolean validateHeaders, long maxHeaderListSize,
                                        int initialHuffmanDecodeCapacity) {
        super(validateHeaders, maxHeaderListSize, initialHuffmanDecodeCapacity);
    }

    InspectedHttp2HeadersDecoder(boolean validateHeaders, boolean validateHeaderValues, HpackDecoder hpackDecoder) {
        super(validateHeaders, validateHeaderValues, hpackDecoder);
    }

    @Override
    protected Http2Headers newHeaders() {
        return new InspectedHttp2Headers(validateHeaders(), validateHeaderValues(), numberOfHeadersGuess());
    }
}
