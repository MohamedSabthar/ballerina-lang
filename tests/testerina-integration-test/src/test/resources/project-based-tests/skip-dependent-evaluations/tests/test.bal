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

isolated function goldenDataSet() returns map<[string]> {
    map<[string]> dataSet = {
        "my-entry-1": ["input query one"],
        "my-entry-2": ["input query two"],
        "my-entry-3": ["input query three"]
    };
    return dataSet;
}

int value = 0;

@test:Config
@test:EvalConfig {
    confidence: 1,
    iterations: 1
}
function myEval() returns error? {
    return error("eval failed");
}


@test:Config {
    dataProvider: goldenDataSet,
    dependsOn: [myEval]
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalWithDataProviderForBeforeForEach(string query) returns error? {
    println("testIsolatedEvalWithDataProviderForBeforeForEach");
}

@test:Config {
    dependsOn: [testIsolatedEvalWithDataProviderForBeforeForEach]
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalWithoutDataProviderForBeforeForEach() returns error? {
    value += 1;
    println("testNonIsolatedEvalWithoutDataProviderForBeforeForEach");
}


@test:Config {
    dependsOn: [myEval]
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalWithoutDataProviderForBeforeForEach() returns error? {
    println("testIsolatedEvalWithoutDataProviderForBeforeForEach");
}

@test:AfterEach
isolated function afterEach() returns error? {
    println("after each");
}