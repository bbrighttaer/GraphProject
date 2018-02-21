package com.ming.graph.test;

import ch.qos.logback.classic.Logger;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import com.ming.graph.util.GraphLoader;
import edu.uci.ics.jung.graph.Graph;
import junit.framework.TestCase;
import org.junit.Assert;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: bbrighttaer
 * Date: 2/6/2018
 * Time: 7:17 PM
 * Project: GraphProject
 */
public class GraphTest extends TestCase {
    private static Logger log = (Logger) LoggerFactory.getLogger(GraphTest.class);
    public void testGraphLoading(){
        final List<String> graphPaths = GraphLoader.getFilePaths("data");
        List<Graph<Node, Edge>> graphList = new ArrayList<>();
        graphPaths.forEach(s -> {
            try {
                graphList.add(GraphLoader.getDirectedGraph(s));
            } catch (ParserConfigurationException | SAXException | IOException e) {
                log.error(e.getMessage());
            }
        });
        for (int i = 0; i < graphList.size(); i++) {
            Graph g = graphList.get(i);
            log.info("{}: node count = {}, edge count = {}", graphPaths.get(i), g.getVertexCount(), g.getEdgeCount());
        }
        log.info("No. of loaded graphs: {}", graphList.size());
        graphList.get(0).getVertices().forEach(v -> System.out.println(v.stringify()));
        graphList.get(0).getEdges().forEach(e -> System.out.println(e.stringify()));
        Assert.assertEquals(graphPaths.size(), graphList.size());
    }
}
