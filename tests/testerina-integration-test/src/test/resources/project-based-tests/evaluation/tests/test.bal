import ballerina/test;

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
} function testNonIsolatedEvalWithDataProviderReturningErrorForEntry(string query) returns error? {
    value+=1;
    return error("inavalid response returned from the model");
}

@test:Config 
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
 function testNonIsolatedEvalWithoutDataProviderReturningErrorForIteration() returns error? {
    value+=1;
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

int beforeCount = 0;
int iterCount1 = 0;


function increamentBefore() {
    beforeCount += 1;
}

function increamentAfter() {
    afterCount += 1;
}

@test:Config {
    before:  increamentBefore
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedBeforeFunctionExecutionBeforeEachIteraionWithoutDataProvider() returns error? {
    iterCount1 += 1;
    test:assertEquals(beforeCount, iterCount1);
}


int iterCount2 = 0;
int afterCount = 0;

@test:Config {
    after:  increamentAfter
}
@test:EvalConfig {
    confidence: 1,
    iterations: 3
}
function testNonIsolatedAfterFunctionExecutionBeforeEachIteraionWithoutDataProvider() returns error? {
    test:assertEquals(afterCount, iterCount2);
    iterCount2+=1;
}