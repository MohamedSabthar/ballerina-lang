import ballerina/jballerina.java;

isolated function outStream() returns handle = @java:FieldGet {
    name: "out",
    'class: "java.lang.System"
} external;

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

function goldenDataSet() returns map<[string]> {
    map<[string]> dataSet = {
        "my-entry-1": ["input query one"],
        "my-entry-2": ["input query two"],
        "my-entry-3": ["input query three"]
    };
    return dataSet;
}
