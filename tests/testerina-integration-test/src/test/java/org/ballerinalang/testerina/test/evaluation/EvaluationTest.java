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
package org.ballerinalang.testerina.test.evaluation;

import org.ballerinalang.test.context.BMainInstance;
import org.ballerinalang.test.context.BallerinaTestException;
import org.ballerinalang.testerina.test.BaseTestCase;
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
    public static final String PARALLEL_FLAG = "--parallel";

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
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "evalWithInvalidConfidenceConfig", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testEvalFailureForInvalidConfidence.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testEvalFailureForInvalidConfidence.txt", output);
    }

    @Test
    public void testEvalFailureForInvalidIteration() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "evalWithInvalidIterationConfig", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testEvalFailureForInvalidIteration.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testEvalFailureForInvalidIteration.txt", output);
    }

    @Test
    public void testIsolatedEvalExecutionWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalExecutionWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalExecutionWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalExecutionWithoutDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvalExecutionWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalExecutionWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalExecutionWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalExecutionWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvalExecutionWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalExecutionWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalExecutionWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalExecutionWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvalExecutionWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalExecutionWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalExecutionWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalExecutionWithDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvalWithDataProviderReturningErrorForEntry() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalWithDataProviderReturningErrorForEntry", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalWithDataProviderReturningErrorForEntry.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalWithDataProviderReturningErrorForEntry.txt", output);
    }

    @Test
    public void testNonIsolatedEvalWithDataProviderReturningErrorForEntry() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalWithDataProviderReturningErrorForEntry", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalWithDataProviderReturningErrorForEntry.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalWithDataProviderReturningErrorForEntry.txt", output);
    }

    @Test
    public void testIsolatedEvalWithoutDataProviderReturningErrorForIteration() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalWithoutDataProviderReturningErrorForIteration", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalWithoutDataProviderReturningErrorForIteration.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalWithoutDataProviderReturningErrorForIteration.txt", output);
    }

    @Test
    public void testNonIsolatedEvalWithoutDataProviderReturningErrorForIteration() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalWithoutDataProviderReturningErrorForIteration", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalWithoutDataProviderReturningErrorForIteration.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalWithoutDataProviderReturningErrorForIteration.txt", output);
    }

    @Test
    public void testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider.txt", output);
    }

    @Test
    public void testIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider.txt", output);
    }

    @Test
    public void testIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider.txt", output);
    }

    @Test
    public void testFailureOfInvalidArgumentInNonIsolatedEvalWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testFailureOfInvalidArgumentInNonIsolatedEvalWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testFailureOfInvalidArgumentInNonIsolatedEvalWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testFailureOfInvalidArgumentInNonIsolatedEvalWithDataProvider.txt", output);
    }

    @Test
    public void testFailureOfInvalidArgumentInIsolatedEvalWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testFailureOfInvalidArgumentInIsolatedEvalWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testFailureOfInvalidArgumentInIsolatedEvalWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testFailureOfInvalidArgumentInIsolatedEvalWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvalWithDataProviderFailingForNonReadOnly() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalWithDataProviderFailingForNonReadOnly", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalWithDataProviderFailingForNonReadOnly.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalWithDataProviderFailingForNonReadOnly.txt", output);
    }

    @Test
    public void testIsolatedEvalWithDataProviderFailingForNonReadOnly() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalWithDataProviderFailingForNonReadOnly", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalWithDataProviderFailingForNonReadOnly.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalWithDataProviderFailingForNonReadOnly.txt", output);
    }

    @Test
    public void testNonIsolatedEvalWithDataProviderWithNoData() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalWithDataProviderWithNoData", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalWithDataProviderWithNoData.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalWithDataProviderWithNoData.txt", output);
    }

    @Test
    public void testIsolatedEvalWithDataProviderWithNoData() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalWithDataProviderWithNoData", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalWithDataProviderWithNoData.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalWithDataProviderWithNoData.txt", output);
    }

    @Test
    public void testIsolatedEvalFailIfBeforeFunctionFails() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalFailIfBeforeFunctionFails", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalFailIfBeforeFunctionFails.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalFailIfBeforeFunctionFails.txt", output);
    }

    @Test
    public void testNonIsolatedEvalFailIfBeforeFunctionFails() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalFailIfBeforeFunctionFails", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalFailIfBeforeFunctionFails.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalFailIfBeforeFunctionFails.txt", output);
    }

    @Test
    public void testIsolatedEvalAfterFunctionFails() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalAfterFunctionFails", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalAfterFunctionFails.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalAfterFunctionFails.txt", output);
    }

    @Test
    public void testNonIsolatedEvalAfterFunctionFails() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalAfterFunctionFails", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalAfterFunctionFails.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalAfterFunctionFails.txt", output);
    }

    @Test
    public void testIsolatedEvalFailIfBeforeFunctionFailsWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalFailIfBeforeFunctionFailsWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalFailIfBeforeFunctionFailsWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalFailIfBeforeFunctionFailsWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvalFailIfBeforeFunctionFailsWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalFailIfBeforeFunctionFailsWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalFailIfBeforeFunctionFailsWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalFailIfBeforeFunctionFailsWithDataProvider.txt", output);
    }

    @Test
    public void testIsolatedEvalAfterFunctionFailsWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testIsolatedEvalAfterFunctionFailsWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testIsolatedEvalAfterFunctionFailsWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testIsolatedEvalAfterFunctionFailsWithDataProvider.txt", output);
    }

    @Test
    public void testNonIsolatedEvalAfterFunctionFailsWithDataProvider() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", "testNonIsolatedEvalAfterFunctionFailsWithDataProvider", "evaluation"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testNonIsolatedEvalAfterFunctionFailsWithDataProvider.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testNonIsolatedEvalAfterFunctionFailsWithDataProvider.txt", output);
    }

    @Test
    public void testEvalWithBeforeForEach() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "evaluation-before-each"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testEvalWithBeforeForEach.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testEvalWithBeforeForEach.txt", output);
    }

    @Test
    public void testEvalWithAfterForEach() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "evaluation-after-each"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testEvalWithAfterForEach.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testEvalWithAfterForEach.txt", output);
    }

    @Test
    public void testSkippingDependentEval() throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "skip-dependent-evaluations"});
        String output = balClient.runMainAndReadStdOut("test", args,
                new HashMap<>(), projectPath, false);
        writeTestOutToFile("EvaluationTest-testSkippingDependentEval.txt", output);
        AssertionUtils.assertOutput("EvaluationTest-testSkippingDependentEval.txt", output);
    }
}
