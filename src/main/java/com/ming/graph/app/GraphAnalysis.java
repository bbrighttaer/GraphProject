package com.ming.graph.app;


import ch.qos.logback.classic.Logger;
import com.ming.graph.api.IGraphAnalysis;
import com.ming.graph.config.Constants;
import com.ming.graph.impl.DataMining;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ming.graph.config.Constants.GRAPH_METADATA_MAP;
import static com.ming.graph.config.Constants.RAND_GEN_SEED;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 1:48 PM
 * Project: GraphProject
 */
public class GraphAnalysis implements IGraphAnalysis {
    private static Logger log = (Logger) LoggerFactory.getLogger(GraphAnalysis.class);
    private final Graph<Node, Edge> initialGraph;
    private final List<Graph<Node, Edge>> evolveGraphList;
    private final DataMining dataMining;
    private Graph<Node, Edge> currentEvolveGraph;
    private String currentEvGraphName;
    private List<Edge> edgeList;
    private Random rand;
    private int count = 0;

    public GraphAnalysis(List<Graph<Node, Edge>> evolveGraphList) {
        this.initialGraph = new UndirectedSparseGraph<>();
        this.evolveGraphList = evolveGraphList;
        this.dataMining = new DataMining();
        currentEvolveGraph = evolveGraphList.get(count);
        ++count;
        edgeList = new ArrayList<>(currentEvolveGraph.getEdges());
        this.rand = new Random(RAND_GEN_SEED);
    }

    public Graph<Node, Edge> getInitialGraph() {
        return initialGraph;
    }

    public List<Graph<Node, Edge>> getEvolveGraphList() {
        return evolveGraphList;
    }

    @Override
    public void evolveGraph() {
        if (edgeList.size() > 0) {
            setCurrentEvGraphName();
            log.info("current graph for evolution: {}, vertices count = {}, edges count = {}, edgeList = {}",
                    currentEvGraphName, currentEvolveGraph.getVertexCount(),
                    currentEvolveGraph.getEdgeCount(), edgeList.size());
            performInfoEvolution();
        } else if (count < evolveGraphList.size()) {
            currentEvolveGraph = evolveGraphList.get(count);
            ++count;
            edgeList = new ArrayList<>(currentEvolveGraph.getEdges());
            currentEvGraphName = null;
            setCurrentEvGraphName();
            log.info("Switched to: {}", currentEvGraphName);
            evolveGraph();
        } else {
            log.info("Information Graph evolution finished");
            Constants.SIM_OVER = true;
        }
    }

    private void performInfoEvolution() {
        int edge_index;
        Edge e;
        Pair<Node> endpoints;
        boolean flag;
        //selects and edge index
        do {
            edge_index = rand.nextInt(edgeList.size());
            e = edgeList.get(edge_index);
            endpoints = currentEvolveGraph.getEndpoints(e);
            Edge edge = initialGraph.findEdge(endpoints.getFirst(), endpoints.getSecond());
            flag = edge == null;
        } while (!flag);

        if (initialGraph.addEdge(e, endpoints.getFirst(), endpoints.getSecond()))
            edgeList.remove(edge_index);
    }

    private void setCurrentEvGraphName() {
        if (StringUtils.isEmpty(currentEvGraphName)) {
            currentEvGraphName = GRAPH_METADATA_MAP.get(currentEvolveGraph);
            if (currentEvGraphName == null)
                throw new RuntimeException("Graph name ID is not set or does not exist in the graph");
        }
    }

    @Override
    public void printNodeStatistics() {
        dataMining.generalInfo(initialGraph);
    }

    @Override
    public void showUI(String title) {
        dataMining.visualizeGraph(initialGraph, title);
    }

    @Override
    public void updateUI() {
        dataMining.getVisViewer().repaint();
    }

    public DataMining getDataMining() {
        return dataMining;
    }
}
