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

import org.ballerinalang.test.context.BMainInstance;
import org.ballerinalang.test.context.BallerinaTestException;
import org.ballerinalang.testerina.test.BaseTestCase;
import org.ballerinalang.testerina.test.utils.AssertionUtils;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Base class for evaluation feature tests.
 * Provides common utilities and setup for all evaluation test classes.
 */
public abstract class BaseEvaluationTest extends BaseTestCase {

    protected BMainInstance balClient;
    protected String projectPath;
    protected static final String PARALLEL_FLAG = "--parallel";
    protected static final Path COMMAND_OUTPUTS_DIR = Path.of("src", "test", "resources", "evaluation-test-outputs");

    @BeforeClass()
    public void setup() {
        balClient = new BMainInstance(balServer);
        projectPath = projectBasedTestsPath.toString();
    }

    /**
     * Writes test output to both Windows and Unix output directories.
     *
     * @param fileName The name of the output file
     * @param output   The test output to write
     * @throws IOException If writing fails
     */
    protected void writeTestOutToFile(String fileName, String output) throws IOException {
        Files.writeString(COMMAND_OUTPUTS_DIR.resolve("windows").resolve(fileName), output);
        Files.writeString(COMMAND_OUTPUTS_DIR.resolve("unix").resolve(fileName), output);
    }

    /**
     * Runs a specific test and verifies its output.
     *
     * @param testName    The name of the test to run
     * @param packageName The package containing the test
     * @throws BallerinaTestException If test execution fails
     * @throws IOException            If output verification fails
     */
    protected void runTestAndVerify(String testName, String packageName) throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, "--tests", testName, packageName});
        String output = balClient.runMainAndReadStdOut("test", args, new HashMap<>(), projectPath, false);
        String fileName = getOutputFileName(testName);
        writeTestOutToFile(fileName, output);
        AssertionUtils.assertOutput(fileName, output);
    }

    /**
     * Runs all tests in a package and verifies output.
     *
     * @param packageName The package to test
     * @throws BallerinaTestException If test execution fails
     * @throws IOException            If output verification fails
     */
    protected void runPackageTestAndVerify(String packageName) throws BallerinaTestException, IOException {
        String[] args = mergeCoverageArgs(new String[]{PARALLEL_FLAG, packageName});
        String output = balClient.runMainAndReadStdOut("test", args, new HashMap<>(), projectPath, false);
        String fileName = getOutputFileName(packageName);
        writeTestOutToFile(fileName, output);
        AssertionUtils.assertOutput(fileName, output);
    }

    /**
     * Generates the output file name for a test.
     *
     * @param testName The name of the test
     * @return The output file name
     */
    protected String getOutputFileName(String testName) {
        return getClass().getSimpleName() + "-" + testName + ".txt";
    }
}