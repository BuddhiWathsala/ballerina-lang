/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.bre.vm;

import org.ballerinalang.model.types.BType;
import org.ballerinalang.util.codegen.CallableUnitInfo;

import java.util.concurrent.Semaphore;

/**
 * VM callback implementation which can be used to wait for a return value.
 *
 * @since 0.985.0
 */
public class StrandWaitCallback extends StrandCallback {

    private Semaphore check;

    public StrandWaitCallback(CallableUnitInfo callableUnitInfo, BType retType) {
        super(callableUnitInfo, retType);
        this.check = new Semaphore(0);
    }

    @Override
    public void signal() {
        this.check.release();
    }

    public void waitForResponse() {
        try {
            this.check.acquire();
        } catch (InterruptedException ignore) { /* ignore */ }
    }
}