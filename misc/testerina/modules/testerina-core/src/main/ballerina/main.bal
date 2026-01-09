// import ballerina/io;
// import ballerina/random;

@Config {
    dataProvider: goldenDataSet
}
@EvalConfig {
    confidence: 1,
    iterations: 3
}
function myAiEvaluation(string i) returns error? {
    // assertEquals(random:createDecimal(), random:createDecimal());
    return error("EERRRRRRROr");
}

int a = 0;

function goldenDataSet() returns map<[string]> {
    map<[string]> dataSet = {
        "my-entry-1": ["input query one"],
        "my-entry-2": ["input query two"],
        "my-entry-3": ["input query three"]
    };
    return dataSet;
}

public function main() {
    string inTargetPath = "/Users/admin/Desktop/AI/revamp-repos/ballerina-lang/misc/testerina/modules/testerina-core/src/main/ballerina/target";
    string inPackageName = "test";
    string inModuleName = "test";
    string inReport = "true";
    string inCoverage = "";
    string inGroups = "";
    string inDisableGroups = "";
    string inTests = "";
    string inRerunFailed = "false";
    string inListGroups = "false";

    setTestOptions(inTargetPath, inPackageName, inModuleName, inReport,
            inCoverage, inGroups, inDisableGroups, inTests, inRerunFailed,
            inListGroups, "true");
    registerTest("myAiEvaluation", myAiEvaluation);

    int startSuiteResult = startSuite();
}