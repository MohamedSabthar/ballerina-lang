import ballerina/test;

@test:BeforeEach
isolated function beforeEach() returns error? {
    println("before each");
}

int value = 0;

@test:Config
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEval() returns error? {
    value += 1;
    println("testNonIsolatedEval");
}

@test:Config {
    dependsOn: [testNonIsolatedEval]
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEval() returns error? {
    println("testIsolatedEval");
}

@test:Config {
    dataProvider: goldenDataSet,
    dependsOn: [testIsolatedEval]
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEvalWithDataProvider(string query) returns error? {
    value += 1;
    println("testNonIsolatedEvalWithDataProvider");
}

@test:Config {
    dataProvider: goldenDataSet,
    dependsOn: [testNonIsolatedEvalWithDataProvider]
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
isolated function testIsolatedEvalWithDataProvider(string query) returns error? {
    println("testIsolatedEvalWithDataProvider");
}
