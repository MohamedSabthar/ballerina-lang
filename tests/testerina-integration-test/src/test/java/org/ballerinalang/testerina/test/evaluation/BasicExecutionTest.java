/*
 *  Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.ballerinalang.testerina.test.evaluation;

import org.ballerinalang.test.context.BallerinaTestException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Test class for basic evaluation execution.
 * Tests isolated and non-isolated evaluations with and without data providers.
 */
public class BasicExecutionTest extends BaseEvaluationTest {

    private static final String PACKAGE_NAME = "evaluation-basic";

    /**
     * Provides test data for basic execution scenarios.
     * Format: {testName, description}
     */
    @DataProvider(name = "basicExecutionTests")
    public Object[][] basicExecutionTestsDataProvider() {
        return new Object[][]{
                {"testIsolatedEvalWithoutDataProvider",
                        "Isolated evaluation without data provider executes successfully"},
                {"testNonIsolatedEvalWithoutDataProvider",
                        "Non-isolated evaluation without data provider executes successfully"},
                {"testIsolatedEvalWithDataProvider",
                        "Isolated evaluation with data provider executes successfully"},
                {"testNonIsolatedEvalWithDataProvider",
                        "Non-isolated evaluation with data provider executes successfully"}
        };
    }

    @Test(dataProvider = "basicExecutionTests",
            description = "Test basic execution for isolated and non-isolated evaluations")
    public void testBasicExecution(String testName, String description)
            throws BallerinaTestException, IOException {
        Reporter.log(description);
        runTestAndVerify(testName, PACKAGE_NAME);
    }
}