/*****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ****************************************************************/

package com.bivgroup.common.orm.interceptors.strategy;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by andreykus on 25.08.2016.
 * Описание Директивы шаблона "chain" для Velocity
 */
public class ChainDirective extends Directive {

    @Override
    /**
     * имя директивы
     * @return - имя директивы
     */
    public String getName() {
        return "chain";
    }

    @Override
    /**
     * тип директивы
     * @return -  тип
     */
    public int getType() {
        return BLOCK;
    }

    @Override
    /**
     * преобразовать
     */
    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws
            IOException,
            ResourceNotFoundException,
            ParseErrorException,
            MethodInvocationException {

        int size = node.jjtGetNumChildren();
        if (size == 0) {
            return true;
        }
        // BLOCK is the last child
        Node block = node.jjtGetChild(node.jjtGetNumChildren() - 1);
        String join = (size > 1) ? (String) node.jjtGetChild(0).value(context) : "";
        String prefix = (size > 2) ? (String) node.jjtGetChild(1).value(context) : "";
        // if there is a conditional prefix, use a separate buffer for children
        StringWriter childWriter = new StringWriter(30);
        int len = block.jjtGetNumChildren();
        int includedChunks = 0;
        for (int i = 0; i < len; i++) {
            Node child = block.jjtGetChild(i);
            // if this is a "chunk", evaluate its expression and prepend join if included...
            if (child instanceof ASTDirective
                    && "chunk".equals(((ASTDirective) child).getDirectiveName())) {
                if (child.jjtGetNumChildren() < 2
                        || child.jjtGetChild(0).value(context) != null) {
                    if (includedChunks > 0) {
                        childWriter.write(join);
                    }
                    includedChunks++;
                }
            }
            child.render(context, childWriter);
        }
        if (includedChunks > 0) {
            childWriter.flush();
            writer.write(prefix);
            writer.write(childWriter.toString());
        }
        return true;
    }
}
