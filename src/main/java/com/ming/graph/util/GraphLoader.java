package com.ming.graph.util;

import ch.qos.logback.classic.Logger;
import com.google.common.base.Supplier;
import com.google.common.collect.BiMap;
import com.ming.graph.config.Constants;
import com.ming.graph.fmwk.MetadataId;
import com.ming.graph.impl.EdgeFactory;
import com.ming.graph.impl.VertexFactory;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: bbrighttaer
 * Date: 2/6/2018
 * Time: 7:04 PM
 * Project: GraphProject
 */
public class GraphLoader {
    private static Logger log = (Logger) LoggerFactory.getLogger(GraphLoader.class);

    public static List<String> getFilePaths(String folderName) {
        List<String> graphFilePathList = new ArrayList<>();
        final String folderPath = GraphLoader.class.getResource("/" + StringUtils
                .removeStart(StringUtils.removeEnd(folderName, "/"), "/")).getPath();
        System.out.println(folderPath);
        File file = new File(folderPath);
        if (file.isDirectory()) {
            final String[] files = file.list((dir, name) -> StringUtils.endsWith(name, ".graphml"));
            for (int i = 0; i < files.length; i++) {
                graphFilePathList.add(folderPath + "/" + files[i]);
            }
        }
        return graphFilePathList;
    }

    public static Graph<Node, Edge> getDirectedGraph(String filePath) throws ParserConfigurationException, SAXException, IOException {
        GraphMLReader<DirectedGraph<Node, Edge>, Node, Edge> gmlr =
                new GraphMLReader<>(new VertexFactory(), new EdgeFactory());
        DirectedGraph<Node, Edge> graph = new DirectedSparseGraph<>();
        gmlr.load(filePath, graph);

        if (graph != null) {
            BiMap<Node, String> vertexIds = gmlr.getVertexIDs();
            //final BiMap<Edge, String> edgeIDs = gmlr.getEdgeIDs();
            final Map<String, GraphMLMetadata<Node>> vertexMetadata = gmlr.getVertexMetadata();
            final Map<String, GraphMLMetadata<Edge>> edgeMetadata = gmlr.getEdgeMetadata();
            Constants.graphMetadataMap.put(graph, gmlr.getGraphMetadata());
            setNodeProperties(graph, vertexIds, vertexMetadata);
            setEdgeProperties(graph, edgeMetadata);
        }
        return graph;
    }

    private static void setEdgeProperties(DirectedGraph<Node, Edge> graph, Map<String, GraphMLMetadata<Edge>> edgeMetadata) {
        graph.getEdges().forEach(e -> {
            final Field[] fields = Edge.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                if (f.isAnnotationPresent(MetadataId.class)) {
                    final String id = f.getAnnotation(MetadataId.class).value();
                    String val;
                    try {
                        val = edgeMetadata.get(id).transformer.apply(e);
                    } catch (Exception e1) {
                        val = null;
                    }
                    try {
                        final Method m = Edge.class.getDeclaredMethod("set" + StringUtils.capitalize(f.getName()), String.class);
                        m.invoke(e, val);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private static void setNodeProperties(DirectedGraph<Node, Edge> graph, BiMap<Node, String> vertexIds, Map<String,
            GraphMLMetadata<Node>> vertexMetadata) {
        graph.getVertices().forEach(n -> {
            n.setId(vertexIds.get(n));
            final Field[] fields = Node.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                if (f.isAnnotationPresent(MetadataId.class)) {
                    final String id = f.getAnnotation(MetadataId.class).value();
                    String val;
                    try {
                        val = vertexMetadata.get(id).transformer.apply(n);
                    } catch (Exception e) {
                        val = null;
                    }
                    try {
                        final Method m = Node.class.getDeclaredMethod("set" + StringUtils.capitalize(f.getName()), String.class);
                        m.invoke(n, val);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static Graph getUnDirectedGraph(String filePath) throws ParserConfigurationException, SAXException, IOException {
        Supplier<Number> vertexFactory = new Supplier<Number>() {
            int n = 0;

            public Number get() {
                return n++;
            }
        };
        Supplier<Number> edgeFactory = new Supplier<Number>() {
            int n = 0;

            public Number get() {
                return n++;
            }
        };

        GraphMLReader<UndirectedGraph<Number, Number>, Number, Number> gmlr =
                new GraphMLReader<UndirectedGraph<Number, Number>, Number, Number>(vertexFactory, edgeFactory);
        UndirectedGraph<Number, Number> graph = new UndirectedSparseGraph<Number, Number>();
        gmlr.load(filePath, graph);
        return graph;
    }
}
