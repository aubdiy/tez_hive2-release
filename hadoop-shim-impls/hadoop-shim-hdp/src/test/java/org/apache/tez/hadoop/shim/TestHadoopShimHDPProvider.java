/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tez.hadoop.shim;

import org.junit.Assert;
import org.junit.Test;

public class TestHadoopShimHDPProvider {

  @Test
  public void testShimProvider() {
    HadoopShimHDPProvider provider = new HadoopShimHDPProvider();
    Assert.assertNotNull(provider.createHadoopShim("foo", 2, 2));
    Assert.assertNotNull(provider.createHadoopShim("foo", 2, 7));
    Assert.assertNotNull(provider.createHadoopShim("foo", 2, 5));
    Assert.assertNotNull(provider.createHadoopShim("foo", 2, 6));
    Assert.assertNotNull(provider.createHadoopShim("foo", 3, 3));
    Assert.assertNotNull(provider.createHadoopShim("foo", 2, 8));
    Assert.assertNotNull(provider.createHadoopShim("foo", 2, 111));

    Assert.assertEquals(HadoopShimHDP.class,
        provider.createHadoopShim("foo", 2, 9).getClass());
  }

}
