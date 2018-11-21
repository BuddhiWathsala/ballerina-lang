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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Safe BVM callback implementation.
 *
 * @since 0.985.0
 */
public class SafeStrandCallback extends StrandCallback {

    private Lock dataLock;

    private AtomicBoolean returnValueAvailable;

    private Strand contStrand;

    private BType expType;

    private int retReg;

    public SafeStrandCallback(CallableUnitInfo callableUnitInfo, BType retType) {
        super(callableUnitInfo, retType);
        this.dataLock = new ReentrantLock();
        this.returnValueAvailable = new AtomicBoolean(false);
    }

    @Override
    public void signal() {
        try {
            dataLock.lock();
            this.returnValueAvailable.set(true);
            if (contStrand != null) {
                Strand strand = CallbackReturnHandler.handleReturn(contStrand, expType, retReg, this);
                if (strand != null) {
                    BVMScheduler.schedule(strand);
                }
            }
        } finally {
            dataLock.unlock();
        }
    }

    public void acquireDataLock() {
        dataLock.lock();
    }

    public void releaseDataLock() {
        dataLock.unlock();
    }

    public void setRetData(Strand contStrand, BType expType, int retReg) {
        this.contStrand = contStrand;
        this.expType = expType;
        this.retReg = retReg;
    }

    public boolean returnDataAvailable() {
        return this.returnValueAvailable.get();
    }
}