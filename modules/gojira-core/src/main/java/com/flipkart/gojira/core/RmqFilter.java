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

import com.flipkart.gojira.models.TestResponseData;
import com.flipkart.gojira.models.rmq.RmqTestResponseData;
import com.rabbitmq.client.AMQP;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter implementation to capture RMQ request and response data. Also responsible for starting and
 * ending the recording of data per request-response capture lifecycle.
 */
public class RmqFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RmqFilter.class);

  public RmqFilter() {}

  /**
   * Initializes a map of {@link Mode} specific filter handlers for RMQ.
   */
  private static final Map<Mode, RmqFilterHandler> filterHashMap =
      Collections.unmodifiableMap(
          new HashMap<Mode, RmqFilterHandler>() {
            {
              put(Mode.NONE, new NoneRmqFilterHandler());
              put(Mode.PROFILE, new ProfileRmqFilterHandler());
              put(Mode.TEST, new TestRmqFilterHandler());
              put(Mode.SERIALIZE, new SerializeRmqFilterHandler());
            }
          });

  /**
   * Integrating application is required to call this method during the start of request-response
   * capture life-cycle. Failure to do so may result in not capturing the request-response data.
   *
   * <p>This method invokes the {@link Mode} specific handler to process the incoming request.
   *
   * @param exchangeName rmq exchange name
   * @param routingKey key used for producing message to the exchange
   * @param data body used for producing message to the exchange
   * @param basicProperties contains headers, transactional reply-to id and meta data for successful
   *     RMQ operation
   * @param mandatory mandatory flag tells RabbitMq that the message must be routable.
   */
  public void start(
      String exchangeName,
      byte[] routingKey,
      byte[] data,
      AMQP.BasicProperties basicProperties,
      boolean mandatory) {

    filterHashMap
        .getOrDefault(ProfileRepository.getMode(), new NoneRmqFilterHandler())
        .handle(exchangeName, routingKey, data, basicProperties, mandatory);
  }

  /**
   * Integrating application is required to call this method during the end of request-response
   * capture life-cycle. Failure to do so may result in not capturing the request-response data and
   * also potential memory leak.
   *
   * <p>This method calls {@link DefaultProfileOrTestHandler#end(TestResponseData)}.
   *
   * @param bytes response data if any. In most cases this may be null
   */
  public void end(byte[] bytes) {
    RmqTestResponseData rmqTestResponseData =
        RmqTestResponseData.builder().setRespondData(bytes).build();
    DefaultProfileOrTestHandler.end(rmqTestResponseData);
  }
}
