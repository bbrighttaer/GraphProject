package com.ming.graph.fmwk;

import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.Graph;

public interface IDataMining {
    void generalInfo(Graph<Node, Edge> graph);

    void rankingAnalysis(Graph<Node, Edge>graph);

    void clustering(Graph<Node, Edge> graph);

    void topologyAnalysis(Graph<Node, Edge> graph);

    void randomlyGeneratedGraphs(Graph<Node, Edge> graph);

    void visualizeGraph(Graph<Node, Edge> graph, String title);
}
