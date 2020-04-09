/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.ballerinalang.compiler.tree.types;

import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.TableKeySpecifierNode;
import org.ballerinalang.model.tree.TableKeyTypeConstraintNode;
import org.ballerinalang.model.tree.types.TableTypeNode;
import org.ballerinalang.model.tree.types.TypeNode;
import org.wso2.ballerinalang.compiler.tree.BLangNodeVisitor;
import org.wso2.ballerinalang.compiler.tree.BLangTableKeySpecifier;
import org.wso2.ballerinalang.compiler.tree.BLangTableKeyTypeConstraint;

/**
 * @since 1.3.0
 */
public class BLangTableType extends BLangType implements TableTypeNode {

    public BLangType type;
    public BLangType constraint;
    public BLangTableKeySpecifier tableKeySpecifier;
    public BLangTableKeyTypeConstraint tableKeyTypeConstraint;

    @Override
    public TypeNode getConstraint() {
        return this.constraint;
    }

    @Override
    public void setConstraint(TypeNode typeNode) {
        this.constraint = (BLangType) typeNode;
    }

    @Override
    public TableKeySpecifierNode getTableKeySpecifier() {
        return this.tableKeySpecifier;
    }

    @Override
    public void setTableKeySpecifier(TableKeySpecifierNode tableKeySpecifierNode) {
        this.tableKeySpecifier = (BLangTableKeySpecifier) tableKeySpecifierNode;
    }

    @Override
    public TableKeyTypeConstraintNode getTableKeyTypeConstraint() {
        return this.tableKeyTypeConstraint;
    }

    @Override
    public void setTableKeyTypeConstraint(TableKeyTypeConstraintNode tableKeyTypeConstraint) {
        this.tableKeyTypeConstraint = (BLangTableKeyTypeConstraint) tableKeyTypeConstraint;
    }

    @Override
    public void accept(BLangNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public NodeKind getKind() {
        return NodeKind.TABLE_TYPE;
    }

    @Override
    public String toString() {
        return "";
    }
}