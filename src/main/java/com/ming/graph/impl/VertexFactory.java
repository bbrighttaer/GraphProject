package com.ming.graph.impl;

import com.google.common.base.Supplier;
import com.ming.graph.model.Node;


/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 3:04 PM
 * Project: GraphProject
 */
public class VertexFactory implements Supplier<Node> {
    @Override
    public Node get() {
        return new Node();
    }
}
