/*
 * Copyright 2020 Flipkart Internet, pvt ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.gojira.core;

import com.flipkart.gojira.models.TestRequestData;
import java.util.Map;

/**
 * Implementation of  {@link KafkaFilterHandler} in {@link Mode#TEST}.
 */
public class TestKafkaFilterHandler extends KafkaFilterHandler {

  /**
   * Gets test-id from headers and throws {@link RuntimeException} if it is null.
   *
   * <p>Initiates execution by calling {@link DefaultProfileOrTestHandler#start(String,
   * TestRequestData)}
   *
   * <p>Implementation of this is expected to call {@link DefaultProfileOrTestHandler#start(String,
   * TestRequestData)}
   *
   * @param topicName kafka topic name
   * @param key key used for producing message to the topic
   * @param value body used for producing message to the topic
   * @param headersMap headers used for producing message to the topic with key as string and value
   *     as map
   */
  @Override
  protected void handle(
      String topicName, byte[] key, byte[] value, Map<String, byte[]> headersMap) {
    String id = getTestId(headersMap);
    if (id == null) {
      throw new RuntimeException("X-GOJIRA-ID header not present");
    }
    DefaultProfileOrTestHandler.start(id, null);
  }
}
