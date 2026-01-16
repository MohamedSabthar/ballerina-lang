import ballerina/test;

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
isolated function testIsolatedEval(string query) returns error? {
    println("testIsolatedEvalWithDataProviderForBeforeForEach");
}

@test:Config {
    dependsOn: [testIsolatedEval]
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedEval() returns error? {
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
isolated function testIsolatedEvalWithoutDataProvider() returns error? {
    println("testIsolatedEvalWithoutDataProviderForBeforeForEach");
}

