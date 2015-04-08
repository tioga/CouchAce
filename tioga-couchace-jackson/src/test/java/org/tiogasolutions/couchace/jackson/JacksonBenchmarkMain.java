/*
 * Copyright 2012 Harlan Noonkester
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

package org.tiogasolutions.couchace.jackson;

import org.tiogasolutions.couchace.core.api.response.TextDocument;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * User: harlan
 * Date: 3/11/14
 * Time: 11:34 PM
 */
public class JacksonBenchmarkMain {

    private static final int ITERATIONS = 10000;
    //private static final int ITERATIONS = 1;
    private static JacksonCouchJsonStrategy strategy = JacksonTestSetup.strategy;

    public static void main(String[] args) {

        // Warmup
        StringBuffer str = new StringBuffer();
        BigDecimal val = new BigDecimal(0);
        for (int i = 0; i < 100; i++) {
            val = val.add(new BigDecimal(5));
            str = str.append("abcd");
        }

        parseTree();
    }

    public static void parseTree() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<TextDocument> couchDocuments = strategy.readTextDocumentsUsingTree(JacksonTestSetup.viewWithBigDocs);
            assertEquals(couchDocuments.size(), 5);
        }

        long end = System.currentTimeMillis();

        System.out.println("Parse Tree Elapsed: " + (end - start) / 1000);

    }

}
