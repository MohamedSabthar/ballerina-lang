import ballerina/io;
import ballerina/random;

@Config {
    dataProvider: mapDataProvider
}
@EvalConfig {
    confidence: 1,
    iterations: 10
}
isolated function dataProviderTest(int aa, int b, string c) returns error? {
    // a = 1;
    io:println("hit");
    assertTrue(random:createDecimal() > 0.5);
}

int a = 0;

function mapDataProvider() returns map<[int, int, string]> {
    map<[int, int, string]> dataSet = {
        "banana": [10, 10, "banana"],
        "cherry": [5, 5, "cherry"],
        "apple": [5, 5, "apple"],
        "orange": [5, 5, "orange"],
        "carrot": [5, 5, "carrot"],
        "lemon": [5, 5, "lemon"],
        "tomatto": [5, 5, "tomatto"],
        "papaya": [5, 5, "papaya"],
        "grapes": [5, 5, "grapes"],
        "mango": [5, 5, "mango"],
        "pineapple": [5, 5, "pineapple"],
        "watermelon": [5, 5, "watermelon"],
        "strawberry": [5, 5, "strawberry"],
        "melon": [5, 5, "melon"],
        "guava": [5, 5, "guava"],
        "pomegranate": [5, 5, "pomegranate"],
        "jackfruit": [5, 5, "jackfruit"],
        "coconut": [5, 5, "coconut"],
        "peach": [5, 5, "peach"],
        "pear": [5, 5, "pear"],
        "plum": [5, 5, "plum"],
        "blueberry": [5, 5, "blueberry"],
        "raspberry": [5, 5, "raspberry"],
        "kiwi": [5, 5, "kiwi"],
        "avocado": [5, 5, "avocado"],
        "cucumber": [5, 5, "cucumber"],
        "pepper": [5, 5, "pepper"],
        "onion": [5, 5, "onion"],
        "potato": [5, 5, "potato"],
        "tomato": [5, 5, "tomato"],
        "garlic": [5, 5, "garlic"],
        "ginger": [5, 5, "ginger"],
        "spinach": [5, 5, "spinach"],
        "broccoli": [5, 5, "broccoli"],
        "cauliflower": [5, 5, "cauliflower"],
        "cabbage": [5, 5, "cabbage"],
        "beetroot": [5, 5, "beetroot"],
        "celery": [5, 5, "celery"],
        "corn": [5, 5, "corn"],
        "mushroom": [5, 5, "mushroom"]

    };
    return dataSet;
}

public function main() {
    string inTargetPath = "/Users/luheerathan/luhee/Ballerina-Project-Files/Test/testerina-parallelization/ballerina/target";
    string inPackageName = "test";
    string inModuleName = "test_";
    string inReport = "false";
    string inCoverage = "false";
    string inGroups = "";
    string inDisableGroups = "";
    string inTests = "";
    string inRerunFailed = "false";
    string inListGroups = "false";

    setTestOptions(inTargetPath, inPackageName, inModuleName, inReport,
            inCoverage, inGroups, inDisableGroups, inTests, inRerunFailed,
            inListGroups, "true");
    registerTest("dataProviderTest", dataProviderTest);

    int startSuiteResult = startSuite();
}