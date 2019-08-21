// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import ballerina/grpc;

// Enable when you need to test locally.
//public function main() {
//    string resp = testClientSocketTimeout();
//    io:println(resp);
//}

public function testClientSocketTimeout() returns string {
    // Client endpoint configuration
    HelloWorld14BlockingClient helloWorldBlockingEp = new("http://localhost:9104", {
                                        timeoutInMillis : 1000});

    // Executes unary blocking call.
    var unionResp = helloWorldBlockingEp->hello("WSO2");

    // Reads message from response.
    if (unionResp is grpc:Error) {
        return "Error from Connector: " + unionResp.reason() + " - " + <string> unionResp.detail()["message"];
    } else {
        string result;
        [result, _] = unionResp;
        return result;
    }
}

public type HelloWorld14BlockingClient client object {

    *grpc:AbstractClientEndpoint;

    private grpc:Client grpcClient;

    public function __init(string url, grpc:ClientEndpointConfig? config = ()) {
        // initialize client endpoint.
        grpc:Client c = new(url, config);
        grpc:Error? result = c.initStub(self, "blocking", ROOT_DESCRIPTOR, getDescriptorMap());
        if (result is grpc:Error) {
            error err = result;
            panic err;
        } else {
            self.grpcClient = c;
        }
    }

    public remote function hello(string req, grpc:Headers? headers = ()) returns ([string, grpc:Headers]|grpc:Error) {

        var payload = check self.grpcClient->blockingExecute("HelloWorld14/hello", req, headers);
        grpc:Headers resHeaders = new;
        any result = ();
        [result, resHeaders] = payload;
        return [result.toString(), resHeaders];
    }

};

public type HelloWorld14Client client object {

    *grpc:AbstractClientEndpoint;

    private grpc:Client grpcClient;

    public function __init(string url, grpc:ClientEndpointConfig? config = ()) {
        // initialize client endpoint.
        grpc:Client c = new(url, config);
        grpc:Error? result = c.initStub(self, "non-blocking", ROOT_DESCRIPTOR, getDescriptorMap());
        if (result is grpc:Error) {
            error err = result;
            panic err;
        } else {
            self.grpcClient = c;
        }
    }

    public remote function hello(string req, service msgListener, grpc:Headers? headers = ()) returns (grpc:Error?) {

        return self.grpcClient->nonBlockingExecute("HelloWorld14/hello", req, msgListener, headers);
    }

};

const string ROOT_DESCRIPTOR = "0A1E677270635F756E6172795F626C6F636B696E675F636F6E742E70726F746F1A1E676F6F676C652F70726F746F6275662F77726170706572732E70726F746F32530A0C48656C6C6F576F726C64313412430A0568656C6C6F121C2E676F6F676C652E70726F746F6275662E537472696E6756616C75651A1C2E676F6F676C652E70726F746F6275662E537472696E6756616C7565620670726F746F33";
function getDescriptorMap() returns map<string> {
    return {
        "grpc_unary_blocking_cont.proto":"0A1E677270635F756E6172795F626C6F636B696E675F636F6E742E70726F746F1A1E676F6F676C652F70726F746F6275662F77726170706572732E70726F746F32530A0C48656C6C6F576F726C64313412430A0568656C6C6F121C2E676F6F676C652E70726F746F6275662E537472696E6756616C75651A1C2E676F6F676C652E70726F746F6275662E537472696E6756616C7565620670726F746F33",
        "google/protobuf/wrappers.proto":"0A1E676F6F676C652F70726F746F6275662F77726170706572732E70726F746F120F676F6F676C652E70726F746F62756622230A0B446F75626C6556616C756512140A0576616C7565180120012801520576616C756522220A0A466C6F617456616C756512140A0576616C7565180120012802520576616C756522220A0A496E74363456616C756512140A0576616C7565180120012803520576616C756522230A0B55496E74363456616C756512140A0576616C7565180120012804520576616C756522220A0A496E74333256616C756512140A0576616C7565180120012805520576616C756522230A0B55496E74333256616C756512140A0576616C756518012001280D520576616C756522210A09426F6F6C56616C756512140A0576616C7565180120012808520576616C756522230A0B537472696E6756616C756512140A0576616C7565180120012809520576616C756522220A0A427974657356616C756512140A0576616C756518012001280C520576616C756542570A13636F6D2E676F6F676C652E70726F746F627566420D577261707065727350726F746F50015A057479706573F80101A20203475042AA021E476F6F676C652E50726F746F6275662E57656C6C4B6E6F776E5479706573620670726F746F33"

    };
}
