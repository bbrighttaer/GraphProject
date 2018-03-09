package com.ming.graph.impl;

import ch.qos.logback.classic.Logger;
import com.ming.graph.api.IEvolutionManager;
import com.ming.graph.app.GraphAnalysis;
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
    public void afterEvolution(GraphAnalysis ga) {
        ga.getDataMining().computeDegreeDistribution(ga.getInitialGraph());
        ga.getDataMining().writeDegreeAgnstNodeData(ga.getInitialGraph(), Constants.SORT_DEGREE_VRS_VERTICE_DATA);
        final Graph<Node, Edge> biggestSubGraph = ga.getDataMining().getBiggestSubGraph(ga.getInitialGraph());
        new DataMining(false).visualizeGraph(biggestSubGraph, "Biggest sub-graph");
        log.info("\nTop {} nodes\n---------", Constants.TOP_K_RANKED_NODES);
        final List<Node> nodes = ga.getDataMining().pageRank(ga.getInitialGraph());
        for (int i = 0; i < Constants.TOP_K_RANKED_NODES; i++) log.info(nodes.get(i).getId());
        new Thread(() -> thenShutdown(1)).start();
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
