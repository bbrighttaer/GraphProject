package com.ming.graph.impl;

import ch.qos.logback.classic.Logger;
import com.ming.graph.api.IEvolutionManager;
import com.ming.graph.api.IGraphAnalysis;
import com.ming.graph.config.Constants;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.Graph;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

/**
 * Author: bbrighttaer
 * Date: 3/9/2018
 * Time: 12:18 PM
 * Project: GraphProject
 */
public class EvolutionManager implements IEvolutionManager {
    private static Logger log = (Logger) LoggerFactory.getLogger(EvolutionManager.class);

    @Override
    public void afterEvolution(IGraphAnalysis ga) {
        subGraphOp(ga);
        writeMainGraphData(ga);
        new Thread(() -> thenShutdown(1)).start();
    }

    private void printTopKnodes(IGraphAnalysis ga, Graph<Node, Edge> subGraph) {
        log.info("\nTop {} nodes\n---------", Constants.TOP_K_RANKED_NODES);
        final List<Node> nodes = ga.getDataMining().pageRank(subGraph);
        for (int i = 0; i < Constants.TOP_K_RANKED_NODES; i++) log.info(nodes.get(i).getId());
    }

    private void subGraphOp(IGraphAnalysis ga) {
        final Graph<Node, Edge> biggestSubGraph = ga.getDataMining().getBiggestSubGraph(ga.getInitialGraph());
        new DataMining(false).visualizeGraph(biggestSubGraph, "Biggest sub-graph");
        writeSubGraphData(ga, biggestSubGraph);
        printTopKnodes(ga, biggestSubGraph);
    }

    private void writeMainGraphData(IGraphAnalysis ga) {
        ga.getDataMining().writeDegreeDistribution(ga.getInitialGraph(), "main_graph");
        ga.getDataMining().writeDegreeAgainstNodeData(ga.getInitialGraph(), Constants.SORT_DEGREE_VRS_VERTICE_DATA,
                "main_graph");
        ga.getDataMining().writeFeatures(ga.getEvolveGraphList(), "main_graph");
        ga.getDataMining().writePerYearData(ga.getEvolveGraphList(), "main_graph");
        ga.getDataMining().writeAccumulatedPerYearData(ga.getEvolveGraphList(), "main_graph");
    }

    private void writeSubGraphData(IGraphAnalysis ga, Graph<Node, Edge> subGraph) {
        ga.getDataMining().writeDegreeDistribution(subGraph, "sub_graph");
        ga.getDataMining().writeDegreeAgainstNodeData(subGraph, Constants.SORT_DEGREE_VRS_VERTICE_DATA,
                "sub_graph");
        ga.getDataMining().writeFeatures(subGraph, "sub_graph");
    }

    @Override
    public void thenShutdown(int c) {
        Scanner scanner = new Scanner((System.in));
        do {
            log.info("Close application? (Y/N)");
            final String flag = scanner.nextLine();
            if (flag.contains("Y") || flag.contains("y")) {
                log.info("Closing application....");
                System.exit(c);
            }
        } while (true);
    }
}
