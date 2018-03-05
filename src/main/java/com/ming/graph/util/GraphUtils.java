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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ming.graph.config.Constants.*;

/**
 * Author: bbrighttaer
 * Date: 2/6/2018
 * Time: 7:04 PM
 * Project: GraphProject
 */
public class GraphUtils {
    private static Logger log = (Logger) LoggerFactory.getLogger(GraphUtils.class);
    public static int GRAPH_COUNT = 0;
    public static boolean isDirected;

    public static List<String> getFilePaths(String folderName) {
        List<String> graphFilePathList = new ArrayList<>();
        final String folderPath = GraphUtils.class.getResource("/" + StringUtils
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
        String graphLabel = StringUtils.substringBefore(StringUtils.substringAfterLast(filePath, "/"), ".graphml");
        if (isDirected) {
            GraphMLReader<DirectedGraph<Node, Edge>, Node, Edge> gmlr;
            DirectedGraph<Node, Edge> graph;
            gmlr = new GraphMLReader<>(new VertexFactory(), new EdgeFactory());
            graph = new DirectedSparseGraph<>();
            gmlr.load(filePath, graph);
            setGraphMetadata(graph, gmlr, graphLabel);
            return graph;
        } else {
            GraphMLReader<UndirectedGraph<Node, Edge>, Node, Edge> gmlr;
            UndirectedGraph<Node, Edge> graph;
            gmlr = new GraphMLReader<>(new VertexFactory(), new EdgeFactory());
            graph = new UndirectedSparseGraph<>();
            gmlr.load(filePath, graph);
            setGraphMetadata(graph, gmlr, graphLabel);
            return graph;
        }
    }

    private static void setGraphMetadata(Graph<Node, Edge> graph, GraphMLReader gmlr, String graphLabel) {
        BiMap<Node, String> vertexIds = gmlr.getVertexIDs();
        //final BiMap<Edge, String> edgeIDs = gmlr.getEdgeIDs();
        final Map<String, GraphMLMetadata<Node>> vertexMetadata = gmlr.getVertexMetadata();
        final Map<String, GraphMLMetadata<Edge>> edgeMetadata = gmlr.getEdgeMetadata();
        Constants.GRAPH_METADATA_MAP.put(graph, graphLabel);
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

    //specific to web of science graphs
    public static Integer getYearOfEdge(Edge e) {
        String ayjid = e.getEdgePropsMap().get(AUTHOR_JOURNAL_ID);
        Pattern p = Pattern.compile(AUTHOR_JOURNAL_REGEX);
        Matcher m = p.matcher(ayjid);
        if (m.matches())
            return Integer.valueOf((m.group(2)));
        return 0;
    }

    //specific to web of science graphs
    public static Integer getYear(String s) {
        Pattern p = Pattern.compile(".*(\\d{4})_\\d+_\\d+");
        Matcher m = p.matcher(s);
        if (m.matches())
            return Integer.valueOf((m.group(1)));
        return 0;
    }

    public static String getFirstAuthor(Edge e) {
        String ayjid = e.getEdgePropsMap().get(AUTHOR_JOURNAL_ID);
        Pattern p = Pattern.compile(AUTHOR_JOURNAL_REGEX);
        Matcher m = p.matcher(ayjid);
        if (m.matches())
            return (m.group(1));
        return NOT_DEFINED;
    }

    public static String getJournal(Edge e) {
        String ayjid = e.getEdgePropsMap().get(AUTHOR_JOURNAL_ID);
        Pattern p = Pattern.compile(AUTHOR_JOURNAL_REGEX);
        Matcher m = p.matcher(ayjid);
        if (m.matches())
            return (m.group(3));
        return NOT_DEFINED;
    }

    public static SortedMap<Integer, List<Graph<Node, Edge>>> groupGraphsIntoYears(List<Graph<Node, Edge>> graphList) {
        SortedMap<Integer, List<Graph<Node, Edge>>> groupedGraphs = new TreeMap<>();
        for (Graph<Node, Edge> g : graphList) {
            final Integer year = getYear(GRAPH_METADATA_MAP.get(g));
            if (groupedGraphs.containsKey(year)) groupedGraphs.get(year).add(g);
            else groupedGraphs.put(year, new ArrayList<Graph<Node, Edge>>() {{
                add(g);
            }});
        }
        return groupedGraphs;
    }
}
