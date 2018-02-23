package com.ming.graph.util;

import ch.qos.logback.classic.Logger;
import com.google.common.collect.BiMap;
import com.ming.graph.config.Constants;
import com.ming.graph.impl.EdgeFactory;
import com.ming.graph.impl.VertexFactory;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import com.ming.graph.xsd.GraphType;
import com.ming.graph.xsd.GraphmlType;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ming.graph.config.Constants.GRAPH_KEYS;
import static com.ming.graph.config.Constants.GRAPH_XSD_PKG;

/**
 * Author: bbrighttaer
 * Date: 2/6/2018
 * Time: 7:04 PM
 * Project: GraphProject
 */
public class GraphLoader {
    private static Logger log = (Logger) LoggerFactory.getLogger(GraphLoader.class);
    public static int GRAPH_COUNT = 0;
    public static boolean isDirected;

    public static List<String> getFilePaths(String folderName) {
        List<String> graphFilePathList = new ArrayList<>();
        final String folderPath = GraphLoader.class.getResource("/" + StringUtils
                .removeStart(StringUtils.removeEnd(folderName, "/"), "/")).getPath();
        log.info(folderPath);
        File file = new File(folderPath);
        if (file.isDirectory()) {
            final String[] files = file.list((dir, name) -> StringUtils.endsWith(name, ".graphml"));
            for (int i = 0; i < files.length; i++) {
                graphFilePathList.add(folderPath + "/" + files[i]);
            }
        }
        return graphFilePathList;
    }

    public static Graph<Node, Edge> getGraph(String filePath) throws ParserConfigurationException, SAXException, IOException {
        if (isDirected) {
            GraphMLReader<DirectedGraph<Node, Edge>, Node, Edge> gmlr;
            DirectedGraph<Node, Edge> graph;
            gmlr = new GraphMLReader<>(new VertexFactory(), new EdgeFactory());
            graph = new DirectedSparseGraph<>();
            gmlr.load(filePath, graph);
            setGraphMetadata(graph, gmlr);
            return graph;
        } else {
            GraphMLReader<UndirectedGraph<Node, Edge>, Node, Edge> gmlr;
            UndirectedGraph<Node, Edge> graph;
            gmlr = new GraphMLReader<>(new VertexFactory(), new EdgeFactory());
            graph = new UndirectedSparseGraph<>();
            gmlr.load(filePath, graph);
            setGraphMetadata(graph, gmlr);
            return graph;
        }
    }

    private static void setGraphMetadata(Graph<Node, Edge> graph, GraphMLReader gmlr) {
        BiMap<Node, String> vertexIds = gmlr.getVertexIDs();
        //final BiMap<Edge, String> edgeIDs = gmlr.getEdgeIDs();
        final Map<String, GraphMLMetadata<Node>> vertexMetadata = gmlr.getVertexMetadata();
        final Map<String, GraphMLMetadata<Edge>> edgeMetadata = gmlr.getEdgeMetadata();
        Constants.graphMetadataMap.put(graph, "Graph-" + (++GRAPH_COUNT));
        setNodeProperties(graph, vertexIds, vertexMetadata);
        setEdgeProperties(graph, edgeMetadata);
    }

    private static void setEdgeProperties(Graph<Node, Edge> graph, Map<String, GraphMLMetadata<Edge>> edgeMetadata) {
        graph.getEdges().forEach(e ->
                edgeMetadata.forEach((s, edgeGraphMLMetadata)
                        -> {
                    String keyName = null;
                    if (GRAPH_KEYS.size() > 0)
                        keyName = GRAPH_KEYS.get(s);
                    e.getEdgePropsMap().put((keyName == null) ? s : keyName, edgeGraphMLMetadata.transformer.apply(e));
                }));
    }

    private static void setNodeProperties(Graph<Node, Edge> graph, BiMap<Node, String> vertexIds, Map<String,
            GraphMLMetadata<Node>> vertexMetadata) {
        graph.getVertices().forEach(n -> {
            n.setId(vertexIds.get(n));
            vertexMetadata.forEach((s, nodeGraphMLMetadata) -> {
                String keyName = null;
                if (GRAPH_KEYS.size() > 0)
                    keyName = GRAPH_KEYS.get(s);
                n.getNodePropsMap().put((keyName == null) ? s : keyName, nodeGraphMLMetadata.transformer.apply(n));
            });
        });
    }

    public static String printProps(Map<String, String> props) {
        String separator = ", ";
        StringBuilder b = new StringBuilder();
        props.forEach((s, s2) -> b.append(String.format("\"%s\": %s%s", s, s2, separator)));
        return StringUtils.removeEnd(b.toString(), separator);
    }

    public static void setGraphKeys(File file) {
        try {
            final JAXBContext ctx = JAXBContext.newInstance(GRAPH_XSD_PKG);
            final Unmarshaller unmarshaller = ctx.createUnmarshaller();
            final Object o = unmarshaller.unmarshal(file);
            JAXBElement<GraphmlType> jo = (JAXBElement) o;
            jo.getValue().getKey().forEach(k -> GRAPH_KEYS.put(k.getId(), k.getAttrName()));
            isDirected = ((GraphType) jo.getValue().getGraphOrData().get(0)).getEdgedefault().value()
                    .equalsIgnoreCase("directed");
        } catch (JAXBException e) {
            log.error(e.getMessage());
        }
    }
}
