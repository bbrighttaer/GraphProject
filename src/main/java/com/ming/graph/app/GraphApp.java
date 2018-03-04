package com.ming.graph.app;

import ch.qos.logback.classic.Logger;
import com.ming.graph.impl.DecisionRound;
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

import static com.ming.graph.config.Constants.BASE_GRAPH_INDEX;
import static com.ming.graph.config.Constants.GRAPH_FOLDER_NAME;
import static com.ming.graph.config.Constants.SIM_OVER;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 1:22 PM
 * Project: GraphProject
 */
public class GraphApp {
    private static Logger log = (Logger) LoggerFactory.getLogger(GraphApp.class);

    public static void main(String[] args) {
        Timer loadingTimer, simTimer;
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
        final GraphAnalysis ga = new GraphAnalysis(graphList);
        new Thread(() -> ga.getDataMining().computeFeatures(graphList)).start();
        new Thread(() -> ga.getDataMining().computePerYearData(graphList)).start();
        simTimer = new Timer(1000, new DecisionRound(ga));
        simTimer.start();
        while (!SIM_OVER) {
        }
        ga.getDataMining().computeDegreeDistribution(ga.getInitialGraph());
    }
}
