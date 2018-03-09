package com.ming.graph.api;

import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.Graph;

import java.util.List;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 1:54 PM
 * Project: GraphProject
 * Key graph operations for every time step here
 */
public interface IGraphAnalysis {
    void evolveGraph();

    void printNodeStatistics();

    void showUI(String title);

    void updateUI();

    Graph<Node, Edge> getInitialGraph();

    List<Graph<Node, Edge>> getEvolveGraphList();

    IDataMining getDataMining();
}
