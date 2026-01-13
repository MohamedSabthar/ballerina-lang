import ballerina/jballerina.java;
import ballerina/test;

final handle outStreamObj = outStream();

isolated function print(handle printStream, any|error obj) = @java:Method {
    name: "print",
    'class: "java.io.PrintStream",
    paramTypes: ["java.lang.Object"]
} external;

isolated function println(anydata|error... objs) {
    lock {
        foreach var obj in objs.clone() {
            print(outStreamObj, obj);
        }
        print(outStreamObj, "\n");
    }
}

isolated function outStream() returns handle = @java:FieldGet {
    name: "out",
    'class: "java.lang.System"
} external;

@test:Config
@test:EvalConfig {
    confidence: 2,
    iterations: 1
}
function evalWithInvalidConfidenceConfig() returns error? {
}

@test:Config
@test:EvalConfig {
    confidence: 0.4,
    iterations: 0
}
isolated function evalWithInvalidIterationConfig() returns error? {
}

@test:Config
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalExecutionWithoutDataProvider() returns error? {
}

int value = 0;

@test:Config
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalExecutionWithoutDataProvider() returns error? {
    value += 1;
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalExecutionWithDataProvider(string query) returns error? {
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalExecutionWithDataProvider(string query) returns error? {
    value += 1;
}

@test:Config
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvaluationFailsWhenConfidenceIsLowWithoutDataProvider() returns error? {
    test:assertEquals(1, 2);
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvaluationFailsWhenConfidenceIsLowWithDataProvider(string query) returns error? {
    test:assertEquals(1, 2);
}

isolated function goldenDataSet() returns map<[string]> {
    map<[string]> dataSet = {
        "my-entry-1": ["input query one"],
        "my-entry-2": ["input query two"],
        "my-entry-3": ["input query three"]
    };
    return dataSet;
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider(int query) returns error? {
    value += 1;
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithDataProvider(int query) returns error? {
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider(int query) returns error? {
}

@test:Config
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvaluationFailsForInvalidInputDataEntryTypeWithoutDataProvider(int query) returns error? {
    value += 1;
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalWithDataProviderReturningErrorForEntry(string query) returns error? {
    return error("inavalid response returned from the model");
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalWithDataProviderReturningErrorForEntry(string query) returns error? {
    value += 1;
    return error("inavalid response returned from the model");
}

@test:Config
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalWithoutDataProviderReturningErrorForIteration() returns error? {
    value += 1;
    return error("inavalid response returned from the model");
}

@test:Config
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalWithoutDataProviderReturningErrorForIteration() returns error? {
    return error("inavalid response returned from the model");
}

@test:Config {
    before: beforeFunction
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider() returns error? {
    value += 1;
    println("run");
}

@test:Config {
    after: afterFunction
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider() returns error? {
    value += 1;
    println("run");
}

@test:Config {
    before: beforeFunction
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedBeforeFunctionExecutionBeforeEachIterationWithoutDataProvider() returns error? {
    println("run");
}

@test:Config {
    before: afterFunction
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedAfterFunctionExecutionBeforeEachIterationWithoutDataProvider() returns error? {
    println("run");
}

isolated function afterFunction() {
    println("after function executed");
}

isolated function beforeFunction() {
    println("before function executed");
}

@test:Config {
    dataProvider: goldenDataSet,
    before: beforeFunction
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider(string query) returns error? {
    println("run: ", query);
}

@test:Config {
    dataProvider: goldenDataSet,
    after: afterFunction
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider(string query) returns error? {
    println("run: ", query);
}

@test:Config {
    dataProvider: goldenDataSet,
    before: beforeFunction
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedBeforeFunctionExecutionBeforeEachIterationWithDataProvider(string query) returns error? {
    value += 1;
    println("run: ", query);
}

@test:Config {
    dataProvider: goldenDataSet,
    after: afterFunction
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedAfterFunctionExecutionBeforeEachIterationWithDataProvider(string query) returns error? {
    value += 1;
    println("run: ", query);
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testFailureOfInvalidArgumentInNonIsolatedEvalWithDataProvider(int a) returns error? {
    value += 1;
}

@test:Config {
    dataProvider: goldenDataSet
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testFailureOfInvalidArgumentInIsolatedEvalWithDataProvider(int a) returns error? {
}

@test:Config {
    dataProvider: nonReadonlyDataset
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalWithDataProviderFailingForNonReadOnly() returns error? {
    value += 1;
}

@test:Config {
    dataProvider: nonReadonlyDataset
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalWithDataProviderFailingForNonReadOnly() returns error? {

}

class MyClass {
    private int data = 0;

    function setData(int data) {
        self.data = data;
    }

    function getData(int data) returns int {
        return self.data;
    }
}

isolated function nonReadonlyDataset() returns map<[MyClass]> {
    return {"first": [new]};
}

@test:Config {
    dataProvider: function() returns map<[string]> => {}
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalWithDataProviderWithNoData(string query) returns error? {
    value += 1;
}


@test:Config {
    dataProvider: isolated function() returns map<[string]> => {}
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalWithDataProviderWithNoData(string query) returns error? {

}



@test:Config {
    before:  function() returns error? => error("before function failed")
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalFailIfBeforeFunctionFails() returns error? {

}

@test:Config {
    before:  function() returns error? => error("before function failed")
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalFailIfBeforeFunctionFails() returns error? {
    value+=1;
}



@test:Config {
    after:  function() returns error? => error("after function failed")
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalAfterFunctionFails() returns error? {

}

@test:Config {
    after:  function() returns error? => error("after function failed")
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalAfterFunctionFails() returns error? {
    value+=1;
}