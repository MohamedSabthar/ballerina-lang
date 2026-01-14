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
    println("run");
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
    println("run");
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
