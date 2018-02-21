package com.ming.graph.app;


import ch.qos.logback.classic.Logger;
import com.ming.graph.fmwk.IGraphAnalysis;
import com.ming.graph.impl.DataMining;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ming.graph.config.Constants.GRAPH_NAME_ID;
import static com.ming.graph.config.Constants.graphMetadataMap;

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

    public GraphAnalysis(Graph<Node, Edge> initialGraph, List<Graph<Node, Edge>> evolveGraphList) {
        this.initialGraph = initialGraph;
        this.evolveGraphList = evolveGraphList;
        this.dataMining = new DataMining();
        currentEvolveGraph = evolveGraphList.remove(0);
        edgeList = new ArrayList<>(currentEvolveGraph.getEdges());
    }

    public Graph<Node, Edge> getInitialGraph() {
        return initialGraph;
    }

    public List<Graph<Node, Edge>> getEvolveGraphList() {
        return evolveGraphList;
    }

    @Override
    public void evolveGraph() {
        if (currentEvolveGraph.getEdgeCount() > 0) {
            setCurrentEvGraphName();
            log.info("current graph for evolution: {}, vertices count = {}, edges count = {}, edgeList = {}",
                    currentEvGraphName, currentEvolveGraph.getVertexCount(),
                    currentEvolveGraph.getEdgeCount(), edgeList.size());
            performInfoEvolution();
        } else if (evolveGraphList.size() > 0) {
            currentEvolveGraph = evolveGraphList.remove(0);
            edgeList = new ArrayList<>(currentEvolveGraph.getEdges());
            currentEvGraphName = null;
            setCurrentEvGraphName();
            log.info("Switched to: {}", currentEvGraphName);
            evolveGraph();
        } else log.info("Information Graph evolution finished");
    }

    private void performInfoEvolution() {
        //takes from the current graph for information evolution
        final Edge edge = edgeList.remove(0);
        final Collection<Node> incidentVertices;
        try {
            incidentVertices = currentEvolveGraph.getIncidentVertices(edge);

            /**
             * adds to the existing graph.
             * Join one of the vertices of the new information to the existing graph then join
             * the other vertex of the new information to the already added new vertex
             */
            Edge evolved = new Edge();
            evolved.setName("EvolvedStreet");
            List<Node> vertices = new ArrayList<>(initialGraph.getVertices());
            initialGraph.addEdge(evolved, vertices.get((int) (Math.random() * vertices.size())),
                    incidentVertices.iterator().next());
            initialGraph.addEdge(edge, incidentVertices);

            //removes the added information from the current graph for information evolution
            currentEvolveGraph.removeEdge(edge);
            incidentVertices.forEach(v -> currentEvolveGraph.removeVertex(v));
        } catch (Exception e) {
            //log.error(e.getMessage());
        }
    }

    private void setCurrentEvGraphName() {
        if (StringUtils.isEmpty(currentEvGraphName)) {
            currentEvGraphName = graphMetadataMap.get(currentEvolveGraph).get(GRAPH_NAME_ID)
                    .transformer.apply((DirectedGraph<Node, Edge>) currentEvolveGraph);
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
}
