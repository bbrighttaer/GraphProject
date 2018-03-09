package com.ming.graph.impl;

import com.ming.graph.api.IEvolutionManager;
import com.ming.graph.app.GraphAnalysis;
import com.ming.graph.config.Constants;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.Graph;

/**
 * Author: bbrighttaer
 * Date: 3/9/2018
 * Time: 12:18 PM
 * Project: GraphProject
 */
public class EvolutionManager implements IEvolutionManager {
    @Override
    public void afterEvolution(GraphAnalysis ga) {
        ga.getDataMining().computeDegreeDistribution(ga.getInitialGraph());
        ga.getDataMining().writeDegreeAgnstNodeData(ga.getInitialGraph(), Constants.SORT_DEGREE_VRS_VERTICE_DATA);
        final Graph<Node, Edge> biggestSubGraph = ga.getDataMining().getBiggestSubGraph(ga.getInitialGraph());
        new DataMining(false).visualizeGraph(biggestSubGraph, "Biggest sub-graph");
    }
}
