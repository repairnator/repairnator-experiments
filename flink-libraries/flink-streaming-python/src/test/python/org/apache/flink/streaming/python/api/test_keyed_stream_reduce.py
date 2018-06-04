################################################################################
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
################################################################################
from org.apache.flink.api.common.functions import ReduceFunction, FlatMapFunction
from org.apache.flink.api.java.functions import KeySelector

from utils import constants


class Tokenizer(FlatMapFunction):
    def flatMap(self, value, collector):
        for v in value:
            collector.collect((1, v))


class Selector(KeySelector):
    def getKey(self, input):
        return input[1]


class Sum(ReduceFunction):
    def reduce(self, input1, input2):
        count1, val1 = input1
        count2, val2 = input2
        return (count1 + count2, val1)


class Main:
    def run(self, flink):
        elements = [(1, 222 if x % 2 == 0 else 333) for x in range(constants.NUM_ELEMENTS_IN_TEST)]

        env = flink.get_execution_environment()
        env.set_parallelism(2) \
            .from_elements(elements) \
            .flat_map(Tokenizer()) \
            .key_by(Selector()) \
            .reduce(Sum()) \
            .output()

        result = env.execute()


def main(flink):
    Main().run(flink)
