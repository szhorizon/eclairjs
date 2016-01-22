/*
 * Copyright 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function EclairJS() {
  var result = require('./SparkContext.js')();

  var kernelP = result[0];

  return {
    SparkContext: result[1],
    SQLContext: require('../../lib/sql/SQLContext.js'),
    StreamingContext: require('../../lib/streaming/StreamingContext.js'),
    KafkaUtils: require('../../lib/streaming/KafkaUtils.js'),
    Duration: require('../../lib/streaming/Duration.js'),

    sql: {
      functions: require('../../lib/sql/functions.js')(kernelP)
    },

    storage: {
      StorageLevel: require('../../lib/storage/StorageLevel.js')(kernelP)
    }
  }
}

module.exports = new EclairJS();
