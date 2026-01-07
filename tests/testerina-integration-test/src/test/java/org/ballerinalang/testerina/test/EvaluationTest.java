/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.testerina.test;

import org.ballerinalang.test.context.BMainInstance;
import org.ballerinalang.test.context.BallerinaTestException;
import org.ballerinalang.testerina.test.utils.AssertionUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Test class to test evaluation related implementation.
 */
public class EvaluationTest extends BaseTestCase {

    private BMainInstance balClient;
    private String projectPath;

    @BeforeClass()
    public void setup() {
        balClient = new BMainInstance(balServer);
        projectPath = projectBasedTestsPath.toString();
    }

    private static final Path commandOutputsDir = Path.of("src", "test", "resources", "command-outputs");

    private void writeTestOutToFile(String fileName, String output) throws IOException {
        Files.writeString(commandOutputsDir.resolve("windows").resolve(fileName), output);
        Files.writeString(commandOutputsDir.resolve("unix").resolve(fileName), output);
    }

    @Test
    public void testEvalFailureForInvalidConfidence() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{"--tests", "evalWithInvalidConfidenceConfig", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testEvalFailureForInvalidConfidence.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testEvalFailureForInvalidConfidence.txt", output);
    }

    @Test
    public void testEvalFailureForInvalidIteration() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{"--tests", "evalWithInvalidIterationConfig", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testEvalFailureForInvalidIteration.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testEvalFailureForInvalidIteration.txt", output);
    }

    @Test
    public void testIsolatedEvalExecutionWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{"--tests", "testIsolatedEvalExecutionWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalExecutionWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalExecutionWithoutDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvalExecutionWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{"--tests", "testNonIsolatedEvalExecutionWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalExecutionWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalExecutionWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvalExecutionWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{"--tests", "testIsolatedEvalExecutionWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalExecutionWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalExecutionWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvalExecutionWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{"--tests", "testNonIsolatedEvalExecutionWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalExecutionWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalExecutionWithDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{"--tests", "testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{"--tests", "testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider.txt", output);
    }

    // data provider test isolate and non isolate
    // failure case: without data provider isolated and non isolated
    // console report output validation
}
