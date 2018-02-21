package com.ming.graph.impl;

import com.google.common.base.Supplier;
import com.ming.graph.model.Edge;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 3:08 PM
 * Project: GraphProject
 */
public class EdgeFactory implements Supplier<Edge> {
    @Override
    public Edge get() {
        return new Edge();
    }
}
