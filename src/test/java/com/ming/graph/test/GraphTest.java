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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ming.graph.config.Constants.GRAPH_KEYS;
import static com.ming.graph.config.Constants.GRAPH_XSD_PKG;

/**
 * Author: bbrighttaer
 * Date: 2/6/2018
 * Time: 7:17 PM
 * Project: GraphProject
 */
public class GraphTest extends TestCase {
    private static Logger log = (Logger) LoggerFactory.getLogger(GraphTest.class);

    public void testGraphLoading() {
        final List<String> graphPaths = GraphLoader.getFilePaths("data");
        setkeys(graphPaths, false);
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
        log.info("Vertices:");
        graphList.get(0).getVertices().forEach(v -> System.out.println(v.toString()));
        log.info("Edges");
        graphList.get(0).getEdges().forEach(e -> System.out.println(e.toString()));
        Assert.assertEquals(graphPaths.size(), graphList.size());
    }

    public void testJaxbLoading() throws JAXBException {
        final List<String> graphPaths = GraphLoader.getFilePaths("test");
        final JAXBContext ctx = JAXBContext.newInstance(GRAPH_XSD_PKG);
        final Unmarshaller unmarshaller = ctx.createUnmarshaller();
        final Object o = unmarshaller.unmarshal(new File(graphPaths.get(0)));
        Assert.assertNotNull(o);
//        JAXBElement<GraphmlType> jo = (JAXBElement) o;
//        jo.getValue().getKey().forEach(k
//                -> System.out.println(String.format("%s -> %s", k.getId(), k.getAttrName())));
        setkeys(graphPaths, true);
    }

    private void setkeys(List<String> graphPaths, boolean print) {
        if (graphPaths.size() > 0) {
            GraphLoader.setGraphKeys(new File(graphPaths.get(0)));
            if (print)
                GRAPH_KEYS.forEach((s, s2) -> System.out.println(String.format("%s -> %s", s, s2)));
        }
    }
}
