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
package io.ballerinalang.compiler.internal.parser.tree;

import io.ballerinalang.compiler.syntax.tree.DefaultableParameterNode;
import io.ballerinalang.compiler.syntax.tree.Node;
import io.ballerinalang.compiler.syntax.tree.NonTerminalNode;
import io.ballerinalang.compiler.syntax.tree.SyntaxKind;

import java.util.Collection;
import java.util.Collections;

/**
 * This is a generated internal syntax tree node.
 *
 * @since 2.0.0
 */
public class STDefaultableParameterNode extends STParameterNode {
    public final STNode annotations;
    public final STNode visibilityQualifier;
    public final STNode typeName;
    public final STNode paramName;
    public final STNode equalsToken;
    public final STNode expression;

    STDefaultableParameterNode(
            STNode annotations,
            STNode visibilityQualifier,
            STNode typeName,
            STNode paramName,
            STNode equalsToken,
            STNode expression) {
        this(
                annotations,
                visibilityQualifier,
                typeName,
                paramName,
                equalsToken,
                expression,
                Collections.emptyList());
    }

    STDefaultableParameterNode(
            STNode annotations,
            STNode visibilityQualifier,
            STNode typeName,
            STNode paramName,
            STNode equalsToken,
            STNode expression,
            Collection<STNodeDiagnostic> diagnostics) {
        super(SyntaxKind.DEFAULTABLE_PARAM, diagnostics);
        this.annotations = annotations;
        this.visibilityQualifier = visibilityQualifier;
        this.typeName = typeName;
        this.paramName = paramName;
        this.equalsToken = equalsToken;
        this.expression = expression;

        addChildren(
                annotations,
                visibilityQualifier,
                typeName,
                paramName,
                equalsToken,
                expression);
    }

    public STNode modifyWith(Collection<STNodeDiagnostic> diagnostics) {
        return new STDefaultableParameterNode(
                this.annotations,
                this.visibilityQualifier,
                this.typeName,
                this.paramName,
                this.equalsToken,
                this.expression,
                diagnostics);
    }

    public STDefaultableParameterNode modify(
            STNode annotations,
            STNode visibilityQualifier,
            STNode typeName,
            STNode paramName,
            STNode equalsToken,
            STNode expression) {
        if (checkForReferenceEquality(
                annotations,
                visibilityQualifier,
                typeName,
                paramName,
                equalsToken,
                expression)) {
            return this;
        }

        return new STDefaultableParameterNode(
                annotations,
                visibilityQualifier,
                typeName,
                paramName,
                equalsToken,
                expression,
                diagnostics);
    }

    public Node createFacade(int position, NonTerminalNode parent) {
        return new DefaultableParameterNode(this, position, parent);
    }

    @Override
    public void accept(STNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T apply(STNodeTransformer<T> transformer) {
        return transformer.transform(this);
    }
}
