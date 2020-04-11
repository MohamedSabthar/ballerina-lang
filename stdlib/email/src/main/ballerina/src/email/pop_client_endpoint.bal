// Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/java;

# Represents a POP Client, which interacts with a POP Server.
public type PopClient client object {

    # Gets invoked during the `email:PopClient` initialization.
    #
    # + host - Host of the POP Client
    # + username - Username of the POP Client
    # + password - Password of the POP Client
    # + clientConfig - Configurations for the POP Client
    # + return - An `email:Error` if failed while creating the client or else ()
    public function __init(@untainted string host, @untainted string username, @untainted string password,
            PopConfig clientConfig = {}) returns Error? {
        return initPopClientEndpoint(self, java:fromString(host), java:fromString(username),
            java:fromString(password), clientConfig);
    }

# Reads a message.
# ```ballerina
# email:Email|email:Error? emailResponse = popClient->read();
# ```
#
# + folder - Folder to read emails. Default value is `INBOX`
# + return - An`email:Email` if read message is successful, () if there is no emails in the specified folder,
#            or else an `email:Error` if failed to receive the message to the recipient
    public remote function read(string folder = DEFAULT_FOLDER) returns Email|Error? {
        return popRead(self, java:fromString(folder));
    }

};

function initPopClientEndpoint(PopClient clientEndpoint, handle host, handle username, handle password,
        PopConfig config) returns Error? = @java:Method {
    name : "initPopClientEndpoint",
    class : "org.ballerinalang.stdlib.email.client.EmailAccessClient"
} external;

function popRead(PopClient clientEndpoint, handle folder) returns Email|Error? = @java:Method {
    name : "readMessage",
    class : "org.ballerinalang.stdlib.email.client.EmailAccessClient"
} external;

# Configuration of the POP Endpoint.
#
# + port - Port number of the POP server
# + enableSsl - If set to true, use SSL to connect and use the SSL port by default.
#               Defaults to true for the "pop3s" protocol and false for the "pop3" protocol.
public type PopConfig record {|
    int port = 995;
    boolean enableSsl = true;
|};
