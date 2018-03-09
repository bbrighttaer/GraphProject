package com.ming.graph.app;

import ch.qos.logback.classic.Logger;
import com.ming.graph.api.IGraphAnalysis;
import com.ming.graph.config.Constants;
import com.ming.graph.impl.DecisionRound;
import com.ming.graph.impl.EvolutionManager;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import com.ming.graph.util.GraphUtils;
import edu.uci.ics.jung.graph.Graph;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.ming.graph.config.Constants.*;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 1:22 PM
 * Project: GraphProject
 */
public class GraphApp {
    private static Logger log = (Logger) LoggerFactory.getLogger(GraphApp.class);

    public static void main(String[] args) {
        Timer loadingTimer;
        loadingTimer = new Timer(3000, e -> log.info("Loading graphs..."));
        loadingTimer.start();
        final List<String> graphPaths = GraphUtils.getFilePaths(GRAPH_FOLDER_NAME);
        if (graphPaths.size() > 0) {
            GraphUtils.setGraphKeys(new File(graphPaths.get(BASE_GRAPH_INDEX)));
        }
        List<Graph<Node, Edge>> graphList = new ArrayList<>();
        graphPaths.forEach(s -> {
            try {
                graphList.add(GraphUtils.getGraph(s));
            } catch (ParserConfigurationException | SAXException | IOException e) {
                log.error(e.getMessage());
            }
        });
        loadingTimer.stop();
        final GraphAnalysis main_graph_analysis = new GraphAnalysis(graphList, new EvolutionManager());
        final List<IGraphAnalysis> analysisList = formPerYearEvolution(graphList);
        analysisList.add(main_graph_analysis);
        SIM_TIMER = new Timer(Constants.EVOLUTION_DELAY, new DecisionRound(analysisList));
        SIM_TIMER.start();
        while (!SIM_OVER) {
        }
    }

    private static List<IGraphAnalysis> formPerYearEvolution(List<Graph<Node, Edge>> graphList) {
        final SortedMap<Integer, List<Graph<Node, Edge>>> graphsIntoYears = GraphUtils.groupGraphsIntoYears(graphList);
        final List<Integer> yrsSet = new ArrayList<>(graphsIntoYears.keySet());
        SortedMap<Integer, List<Graph<Node, Edge>>> topkGraphs = new TreeMap<>();
        for (int i = graphsIntoYears.size() - 1; i > (graphsIntoYears.size() - (Constants.NUM_OF_SUB_GRAPH_FOR_VIS + 1)); i--) {
            topkGraphs.put(yrsSet.get(i), graphsIntoYears.get(yrsSet.get(i)));
        }
        List<IGraphAnalysis> graphAnalysisList = new ArrayList<>();
        topkGraphs.forEach((yr, gList) -> graphAnalysisList.add(new GraphAnalysis(gList, "Year: " + yr.intValue())));
        return graphAnalysisList;
    }
}
