package com.ming.graph.config;

import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 5:20 PM
 * Project: GraphProject
 */
public class Constants {
    public static final String GRAPH_NAME_ID = "d1";
    public static final String GRAPH_STREETS_PER_NODE_ID = "d2";
    public static final String GRAPH_CRS = "d0";
    public static Map<Graph<Node, Edge>, Map<String, GraphMLMetadata<DirectedGraph<Node, Edge>>>> graphMetadataMap = new HashMap<>();
    public static final String GRAPH_XSD_PKG = "com.ming.graph.xsd";
    public static final Map<String, String> GRAPH_KEYS = new HashMap<>();
    public static final String GRAPH_FOLDER_NAME = "data";
}
