package com.ming.graph.model;

import java.util.HashMap;
import java.util.Map;

import static com.ming.graph.util.GraphUtils.printProps;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 2:51 PM
 * Project: GraphProject
 */
public class Edge {
    private final Map<String, String> edgePropsMap;

    public Edge() {
        this.edgePropsMap = new HashMap<>();
    }

    public Map<String, String> getEdgePropsMap() {
        return edgePropsMap;
    }

    public String printLabel() {
        return printProps(edgePropsMap);
    }

    @Override
    public String toString() {
        return "Edge{properties: {" + printProps(edgePropsMap) + "}}";
    }
}
