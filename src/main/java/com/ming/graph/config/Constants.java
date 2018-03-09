package com.ming.graph.config;

import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.Graph;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 5:20 PM
 * Project: GraphProject
 */
public class Constants {
    public static final int GROWING_RATE = 50;
    public static final boolean SORT_DEGREE_VRS_VERTICE_DATA = true;
    public static final int EVOLUTION_DELAY = 1; //Milliseconds
    public static final int NUM_OF_SUB_GRAPH_FOR_VIS = 3;
    public static final String GRAPH_XSD_PKG = "com.ming.graph.xsd";
    public static final Map<String, String> GRAPH_KEYS = new HashMap<>();
    public static final String GRAPH_FOLDER_NAME = "wos_test";
    public static final int BASE_GRAPH_INDEX = 0;
    public static final int RAND_GEN_SEED = 32;
    public static final String AUTHOR_JOURNAL_ID = "ayjid";
    public static final String AUTHOR_JOURNAL_REGEX = "(.*)(\\d{4})(.*)";
    public static final String NOT_DEFINED = "nd";
    public static final String DATA_DIR = "data/";
    public static final int TOP_K_RANKED_NODES = 3;
    public static Map<Graph<Node, Edge>, String> GRAPH_METADATA_MAP = new HashMap<>();
    public static boolean SIM_OVER = false;
    public static double NORMALIZATION_CONSTANT = 0.4;
    public static double POWER_LAW_ALPHA = 2;
    public static Timer SIM_TIMER;
}
