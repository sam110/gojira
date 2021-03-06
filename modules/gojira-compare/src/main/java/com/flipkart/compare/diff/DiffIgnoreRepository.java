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

package com.flipkart.compare.diff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * DiffIgnoreRepository is the interface to hold DiffDetails that are to be ignored. Current
 * interface only takes care {@link DiffDetail#getDiffType()} and {@link DiffDetail#getDiffPath()}
 * based ignore patterns. {@link DiffDetail#getExpectedValue()} and {@link
 * DiffDetail#getActualValue()} based ignore patterns are not supported as part of this interface.
 */
public abstract class DiffIgnoreRepository {

  /**
   * This variable holds the list of {@link DiffDetail#getDiffPath()} values as Pattern for
   * optimized matching during comparison per {@link DiffDetail#getDiffType()}.
   */
  static final Map<DiffType, List<Pattern>> diffIgnorePatterns = new HashMap<>();

  /**
   * This method sets up {@link DiffIgnoreRepository#diffIgnorePatterns}.
   *
   * @param diffIgnoreMap map of diffType to list of diffPath Validation of key to DiffType and
   *                      converting list of diffPath to list of Pattern is the responsibility of
   *                      this method's implementation
   */
  public abstract void setupDiffIgnorePatterns(Map<String, List<String>> diffIgnoreMap);

  /**
   * Gets the list of Pattern to be ignored.
   *
   * @return {@link DiffIgnoreRepository#diffIgnorePatterns}
   */
  public abstract Map<DiffType, List<Pattern>> getDiffIgnorePatterns();

}
