package com.ming.graph.model;

import java.util.HashMap;
import java.util.Map;

import static com.ming.graph.util.GraphUtils.printProps;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 2:48 PM
 * Project: GraphProject
 */
public class Node {
    private final Map<String, String> nodePropsMap;
    private String id;

    public Node() {
        this.nodePropsMap = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getNodePropsMap() {
        return nodePropsMap;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id = '" + id + '\'' +
                ", properties : {" + printProps(nodePropsMap) + "}}";
    }
}
