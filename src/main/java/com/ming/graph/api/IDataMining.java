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

    Set weaklyConnectedComponents(Graph<Node, Edge> graph);

    Graph<Node, Edge> getBiggestSubGraph(Graph<Node, Edge> graph);

    List<Node> pageRank(Graph<Node, Edge> graph);

    void edgeBetweeness(Graph<Node, Edge> graph);

    void writeFeatures(List<Graph<Node, Edge>> graphList, String fileNameSuffix);

    void writeFeatures(Graph<Node, Edge> graph, String fileNameSuffix);

    void writeDegreeDistribution(Graph<Node, Edge> graph, String fileNameSuffix);

    void writeAccumulatedPerYearData(List<Graph<Node, Edge>> graphList, String fileNameSuffix);

    void writePerYearData(List<Graph<Node, Edge>> graphList, String fileNameSuffix);

    void writeDegreeAgainstNodeData(Graph<Node, Edge> graph, boolean sorted, String fileNameSuffix);
}
