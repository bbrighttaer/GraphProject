package com.ming.graph.api;

import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.Graph;

import java.util.List;
import java.util.Set;

public interface IDataMining {
    void generalInfo(Graph<Node, Edge> graph);

    void rankingAnalysis(Graph<Node, Edge> graph);

    void clustering(Graph<Node, Edge> graph);

    void topologyAnalysis(Graph<Node, Edge> graph);

    void randomlyGeneratedGraphs(Graph<Node, Edge> graph);

    void visualizeGraph(Graph<Node, Edge> graph, String title);

    void computeFeatures(List<Graph<Node, Edge>> graphList);

    void computeDegreeDistribution(Graph<Node, Edge> graph);

    Set weaklyConnectedComponents(Graph<Node, Edge> graph);

    Graph<Node, Edge> getBiggestSubGraph(Graph<Node, Edge> graph);

    void edgeBetweeness(Graph<Node, Edge> graph);
}
