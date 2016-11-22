/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tez.runtime.library.cartesianproduct;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.tez.dag.api.UserPayload;

import java.nio.ByteBuffer;

import static org.apache.tez.runtime.library.cartesianproduct.CartesianProductUserPayload.*;

class CartesianProductVertexManagerConfig extends CartesianProductConfig {
  private final float minFraction;
  private final float maxFraction;

  public CartesianProductVertexManagerConfig(boolean isPartitioned, String[] sourceVertices,
                                             int[] numPartitions,
                                             float minFraction, float maxFraction,
                                             CartesianProductFilterDescriptor filterDescriptor) {
    super(isPartitioned, numPartitions, sourceVertices, filterDescriptor);
    Preconditions.checkArgument(minFraction <= maxFraction,
      "min fraction(" + minFraction + ") should be less than max fraction(" +
        maxFraction  + ") in cartesian product slow start");
    this.minFraction = minFraction;
    this.maxFraction = maxFraction;
  }

  public float getMinFraction() {
    return minFraction;
  }

  public float getMaxFraction() {
    return maxFraction;
  }

  public static CartesianProductVertexManagerConfig fromUserPayload(UserPayload payload)
    throws InvalidProtocolBufferException {
    CartesianProductConfigProto proto =
      CartesianProductConfigProto.parseFrom(ByteString.copyFrom(payload.getPayload()));

    boolean isPartitioned = proto.getIsPartitioned();
    String[] sourceVertices = new String[proto.getSourceVerticesList().size()];
    proto.getSourceVerticesList().toArray(sourceVertices);
    int[] numPartitions =
      proto.getNumPartitionsCount() == 0 ? null : Ints.toArray(proto.getNumPartitionsList());
    CartesianProductFilterDescriptor filterDescriptor = proto.hasFilterClassName()
      ? new CartesianProductFilterDescriptor(proto.getFilterClassName()) : null;
    if (proto.hasFilterUserPayload()) {
      filterDescriptor.setUserPayload(
        UserPayload.create(ByteBuffer.wrap(proto.getFilterUserPayload().toByteArray())));
    }
    float minFraction = proto.getMinFraction();
    float maxFraction = proto.getMaxFraction();
    return new CartesianProductVertexManagerConfig(isPartitioned, sourceVertices, numPartitions,
      minFraction, maxFraction, filterDescriptor);
  }
}
