package com.ming.graph.impl;

import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.Graph;

import java.util.Comparator;

/**
 * Author: bbrighttaer
 * Date: 3/9/2018
 * Time: 12:01 PM
 * Project: GraphProject
 */
public class NodeComparator implements Comparator<Node> {
    private Graph<Node, Edge> graph;

    public NodeComparator(Graph<Node, Edge> graph) {
        this.graph = graph;
    }

    @Override
    public int compare(Node o1, Node o2) {
        return Integer.compare(graph.getIncidentEdges(o2).size(), graph.getIncidentEdges(o1).size());
    }
}
