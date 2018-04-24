// The Ballerina WebSub Publisher brings up the internal Ballerina Hub, registers a topic at the hub, and publishes
// updates to the topic.
import ballerina/log;
import ballerina/runtime;
import ballerina/websub;

function main(string... args) {

    // Start up the internal Ballerina Hub.
    log:printInfo("Starting up the Ballerina Hub Service");
    websub:WebSubHub webSubHub = websub:startUpBallerinaHub();

    // Register a topic at the hub.
    var registrationResponse = webSubHub.registerTopic("http://www.websubpubtopic.com");
    match (registrationResponse) {
        error webSubError => log:printError("Error occurred registering topic: " + webSubError.message);
        () => log:printInfo("Topic registration successful!");
    }

    // Make the publisher wait until the subscriber subscribes at the hub.
    runtime:sleep(15000);

    // Publish directly to the internal Ballerina Hub.
    var publishResponse = webSubHub.publishUpdate("http://www.websubpubtopic.com",
        {
            "action": "publish",
            "mode": "internal-hub"
        }
    );
    match (publishResponse) {
        error webSubError => log:printError("Error notifying hub: " + webSubError.message);
        () => log:printInfo("Update notification successful!");
    }

    // Make the publisher wait until the subscriber unsubscribes at the hub.
    runtime:sleep(15000);

    // Publish directly to the internal Ballerina Hub.
    publishResponse = webSubHub.publishUpdate("http://www.websubpubtopic.com",
        {"action": "publish", "mode": "internal-hub"});
    match (publishResponse) {
        error webSubError => log:printError("Error notifying hub: " + webSubError.message);
        () => log:printInfo("Update notification successful!");
    }

    // Make the publisher wait until notification is done to subscribers, if any.
    runtime:sleep(15000);
}
