package com.ming.graph.impl;

import com.google.common.base.Supplier;
import com.ming.graph.api.IDataMining;
import com.ming.graph.api.IFilePersistence;
import com.ming.graph.config.Constants;
import com.ming.graph.io.FilePersistenceCSV;
import com.ming.graph.io.Text;
import com.ming.graph.io.TextCsvLine;
import com.ming.graph.model.Edge;
import com.ming.graph.model.Node;
import com.ming.graph.util.GraphUtils;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.algorithms.scoring.PageRankWithPriors;
import edu.uci.ics.jung.algorithms.shortestpath.BFSDistanceLabeler;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by brigh on 5/24/2017.
 */
public class DataMining implements IDataMining {
    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<Node, Edge> visViewer;
    //private final GraphType graphType;
    private Supplier<Graph<Node, Edge>> graphFactory;
    private Supplier<Node> vertexFactory;
    private Supplier<Edge> edgeFactory;
    private Node maxInDegreeNode;
    private Node maxOutDegreeNode;
    private boolean printLog;

    public DataMining(boolean printLog) {
        this.printLog = printLog;
    }

    @Override
    public void generalInfo(Graph<Node, Edge> graph) {
        //in and out degrees
        Collection<Node> vertices = graph.getVertices();
        int maxInDegree = 0;
        int maxOutDegree = 0;
        for (Node v : vertices) {
            int inDegree = graph.inDegree(v);
            int outDegree = graph.outDegree(v);
            if (inDegree > maxInDegree) {
                maxInDegree = inDegree;
                this.maxInDegreeNode = v;
            }
            if (outDegree > maxOutDegree) {
                maxOutDegree = outDegree;
                this.maxOutDegreeNode = v;
            }
        }
        if (printLog) {
            System.out.println(String.format("Edge count: %d, Vertex count: %d", graph.getEdgeCount(),
                    graph.getVertexCount()));
            System.out.println("Max in-degree: " + maxInDegree + ", vertexID: " + this.maxInDegreeNode);
            System.out.println("Max out-degree: " + maxOutDegree + ", vertexID: " + this.maxOutDegreeNode);
        }
    }

    @Override
    public void rankingAnalysis(Graph<Node, Edge> graph) {
        //BetweennessCentrality
        if (printLog)
            System.out.println("---- BetweennessCentrality Ranking ----");
        BetweennessCentrality bCentrality = new BetweennessCentrality(graph, true, false);
        bCentrality.evaluate();
        bCentrality.printRankings(true, true);

        //PageRank
        if (printLog)
            System.out.println("---- PageRank ranking ----");
        final PageRank pageRank = new PageRank(graph, .1);
        pageRank.evaluate();
        final List pageRankVertices = new ArrayList(graph.getVertices());
        pageRankSorting(pageRank, pageRankVertices);

        //PageRank with Priors
        if (printLog)
            System.out.println("---- PageRank with Priors -----");
        PageRankWithPriors pageRankWithPriors = new PageRankWithPriors(graph, o -> getDoubleVal(pageRankVertices.get(0)), 0.1);
        pageRankWithPriors.evaluate();
        final List pageRankWithPriorsVertices = new ArrayList(graph.getVertices());
        pageRankSorting(pageRankWithPriors, pageRankWithPriorsVertices);

    }

    private void pageRankSorting(final PageRankWithPriors pageRank, List pageRankVertices) {
        Collections.sort(pageRankVertices, (o1, o2) -> (
                Double.compare(getDoubleVal(pageRank.getVertexScore(o2)), getDoubleVal(pageRank.getVertexScore(o1)))
        ));
        for (Object v :
                pageRankVertices) {
            System.out.println(v.toString() + ", rank score: " + pageRank.getVertexScore(v));
        }
    }

    @Override
    public void clustering(Graph<Node, Edge> graph) {
        edgeBetweeness(graph);
        weaklyConnectedComponents(graph);
    }

    public void edgeBetweeness(Graph<Node, Edge> graph) {
        //EdgeBetweenness
        System.out.println("------ EdgeBetweenness Clustering ----");
        EdgeBetweennessClusterer betweennessClusterer = new EdgeBetweennessClusterer(200);
        Object[] clusters = betweennessClusterer.apply(graph).toArray();
        System.out.println("Number of clusters: " + clusters.length);
        for (int i = 0; i < clusters.length; i++) {
            System.out.println(String.format("---- Cluster %d ---", (i + 1)));
            Set cluster = (Set) clusters[i];
            System.out.println(Arrays.asList(cluster.toArray()) + ", size: " + cluster.size());
        }
    }

    public Set<Set> weaklyConnectedComponents(Graph<Node, Edge> graph) {
        //WCC
//        System.out.println("------ Weakely Connected Component (WCC) Clustering ----");
        WeakComponentClusterer wcc = new WeakComponentClusterer();
//        Object[] wccClusters = wcc.apply(graph).toArray();
//        System.out.println("Number of WCC clusters: " + wccClusters.length);
//        for (int i = 0; i < wccClusters.length; i++) {
//            System.out.println(String.format("---- Cluster %d ---", (i + 1)));
//            Set cluster = (Set) wccClusters[i];
//            System.out.println("size = " + cluster.size());
//            System.out.println(Arrays.asList(cluster.toArray()));
//        }
        return wcc.apply(graph);
    }

    @Override
    public Graph<Node, Edge> getBiggestSubGraph(Graph<Node, Edge> graph) {
        final Set<Set> clusters = weaklyConnectedComponents(graph);
        Set maxCluster = new HashSet();
        for (Set cluster : clusters) {
            if (cluster.size() > maxCluster.size())
                maxCluster = cluster;
        }
        Graph<Node, Edge> subGraph = new UndirectedSparseGraph<>();
        Set<Edge> subGraphEdgeSet = new HashSet<>();
        maxCluster.forEach(n -> subGraphEdgeSet.addAll(graph.getIncidentEdges((Node) n)));
        subGraphEdgeSet.forEach(edge -> subGraph.addEdge(edge, graph.getIncidentVertices(edge)));
        return subGraph;
    }

    @Override
    public List<Node> pageRank(Graph<Node, Edge> graph) {
        final PageRank pageRank = new PageRank(graph, .1);
        pageRank.evaluate();
        final List<Node> pageRankVertices = new ArrayList<>(graph.getVertices());
        pageRankVertices.sort((o1, o2) -> Double.compare(getDoubleVal(pageRank.getVertexScore(o2)),
                getDoubleVal(pageRank.getVertexScore(o1))));
        return pageRankVertices;
    }

    @Override
    public void topologyAnalysis(Graph<Node, Edge> graph) {
        Object[] verticesArr = graph.getVertices().toArray();
        Object firstVertex = verticesArr[0];
        Object lastVertex = verticesArr[verticesArr.length - 1];

        //BFSDistanceLabeler
        System.out.println(String.format("---------- BFSDistanceLabeler %s ----------", firstVertex.toString()));
        BFSDistanceLabeler distanceLabeler = new BFSDistanceLabeler();
        System.out.println("Root vertex: " + firstVertex.toString());
        distanceLabeler.labelDistances(graph, firstVertex);
        Map distanceDecorator = distanceLabeler.getDistanceDecorator();
        distanceDecorator.forEach((k, v) -> System.out.println(String.format("Vertex: %s , distance: %s",
                k.toString(),
                getDoubleVal(v))));

        //KNeighborhoodExtractor
        System.out.println("------- KNeighborhood Extractor --------");
        KNeighborhoodFilter neighborhoodFilter = new KNeighborhoodFilter(firstVertex, 1,
                KNeighborhoodFilter.EdgeType.IN_OUT);
        Graph subGraph = neighborhoodFilter.apply(graph);
        //visualizeGraph(subGraph);

        //DijkstraShortestPath
        System.out.println(String.format("------- Djikstra's Shortest path from %s --------", firstVertex.toString()));
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);
        Map distanceMap = shortestPath.getDistanceMap(firstVertex);
        distanceMap.forEach((v, d) -> System.out.println(String.format("Vertex: %s , distance: %s",
                v.toString(),
                getDoubleVal(d))));
    }

    @Override
    public void randomlyGeneratedGraphs(Graph<Node, Edge> graph) {
        //BarabasiAlbertGenerator
        System.out.println("---- Barabasi Albert Generator ----");
        int init_vertices = 1;
        int edges_to_add_per_timestep = 1;
        int random_seed = 0;
        int num_timesteps = 20;
        setUpBarabasiVariables();
        BarabasiAlbertGenerator<Node, Edge> barabasiGraphGen =
                new BarabasiAlbertGenerator<>(graphFactory,
                        vertexFactory, edgeFactory, init_vertices,
                        edges_to_add_per_timestep, random_seed,
                        new HashSet<>(graph.getVertices()));
        barabasiGraphGen.evolveGraph(num_timesteps);
        Graph barabasiGraph = barabasiGraphGen.get();
        visualizeGraph(barabasiGraph, "BarabasiAlbertGenerator");

        //Kleinberg Small world generator
        System.out.println("---- Small World Graph Generator ----");
        KleinbergSmallWorldGenerator smallWorldGenerator = new KleinbergSmallWorldGenerator(graphFactory,
                vertexFactory,
                edgeFactory,
                4,
                3);
        Graph smallWorldGraph = smallWorldGenerator.get();
        visualizeGraph(smallWorldGraph, "Kleinberg Small world");

        //EppsteinPowerLaw Generator
        EppsteinPowerLawGenerator powerLawGenerator = new EppsteinPowerLawGenerator(graphFactory, vertexFactory,
                edgeFactory, 30,
                200, 50);
        Graph powerlawGraph = powerLawGenerator.get();
        visualizeGraph(powerlawGraph, "Eppstein Power law");
    }

    public void writeFeatures(List<Graph<Node, Edge>> graphList, String fileNameSuffix) {
        List<TextCsvLine> lines = new ArrayList<TextCsvLine>() {{
            //line 1 [headers]
            add(new TextCsvLine()
                    .addText(new Text("Size of the data set (bytes)"))
                    .addText(new Text("Beginning year"))
                    .addText(new Text("End year"))
                    .addText(new Text("Total number of vertices"))
                    .addText(new Text("Total number of links"))
                    .addText(new Text("Average years' number of vertices"))
                    .addText(new Text("Average years' number of links")));
        }};
        Set<Integer> yearsSet = new TreeSet<>();
        int totalVerticesCount = 0;
        int totalEdgesCount = 0;
        long totalSize = 0L;

        for (Graph<Node, Edge> graph : graphList) {
            Integer year = GraphUtils.getYear(Constants.GRAPH_METADATA_MAP.get(graph));
            yearsSet.add(year);
            totalEdgesCount += graph.getEdgeCount();
            totalVerticesCount += graph.getVertexCount();
            totalSize += (ObjectSizeCalculator.getObjectSize(graph));
        }
        List<Integer> yrs = new ArrayList(yearsSet);
        lines.add(new TextCsvLine()
                .addText(new Text(totalSize))
                .addText(new Text(yrs.get(0)))
                .addText(new Text(yrs.get(yearsSet.size() - 1)))
                .addText(new Text(totalVerticesCount))
                .addText(new Text(totalEdgesCount))
                .addText(new Text(((double) totalVerticesCount) / yearsSet.size()))
                .addText(new Text(((double) totalEdgesCount) / yearsSet.size())));
        IFilePersistence persistence = new FilePersistenceCSV(new File(Constants.DATA_DIR + "features_" + fileNameSuffix + ".csv"));
        persistence.saveLinesToFile(lines, false);
    }

    public void writeFeatures(Graph<Node, Edge> graph, String fileNameSuffix) {
        List<TextCsvLine> lines = new ArrayList<TextCsvLine>() {{
            //line 1 [headers]
            add(new TextCsvLine()
                    .addText(new Text("Size of the data set (bytes)"))
                    .addText(new Text("Total number of vertices"))
                    .addText(new Text("Total number of links"))
                    .addText(new Text("Average years' number of vertices"))
                    .addText(new Text("Average years' number of links")));
        }};
        long totalSize = 0L;
        totalSize += (ObjectSizeCalculator.getObjectSize(graph));
        lines.add(new TextCsvLine()
                .addText(new Text(totalSize))
                .addText(new Text(graph.getVertexCount()))
                .addText(new Text(graph.getEdgeCount())));
        IFilePersistence persistence = new FilePersistenceCSV(new File(Constants.DATA_DIR + "features_" + fileNameSuffix + ".csv"));
        persistence.saveLinesToFile(lines, false);
    }

    @Override
    public void writeDegreeDistribution(Graph<Node, Edge> graph, String fileNameSuffix) {
        int numNodes = graph.getVertexCount();
        Map<Integer, Degree> degDistMap = new HashMap<>();
//        for (int i = 1; i < numNodes; i++)
//            degDistMap.put(i, new Degree());
        for (Node node : graph.getVertices()) {
            final int degree = graph.getIncidentEdges(node).size();
            if (degDistMap.containsKey(degree))
                degDistMap.get(degree).increment();
            else degDistMap.put(degree, new Degree().increment());
        }
        TextCsvLine lineX = new TextCsvLine();
        TextCsvLine line = new TextCsvLine();
        TextCsvLine lineProb = new TextCsvLine();
        degDistMap.forEach((x, d) -> {
            lineX.addText(new Text(x));
            line.addText(new Text(d.get()));
            lineProb.addText(new Text(((double) d.get()) / numNodes));
        });
        IFilePersistence persistence = new FilePersistenceCSV(new File(Constants.DATA_DIR + "degreeDist_" + fileNameSuffix + ".csv"));
        persistence.saveLinesToFile(new ArrayList<TextCsvLine>() {{
            add(lineX);
            add(line);
            add(lineProb);
        }}, false);
    }

    public void writePerYearData(List<Graph<Node, Edge>> graphList, String fileNameSuffix) {
        List<TextCsvLine> lines = new ArrayList<TextCsvLine>() {{
            //line 1 [headers]
            add(new TextCsvLine()
                    .addText(new Text("Year"))
                    .addText(new Text("Total number of edges"))
                    .addText(new Text("Total number of vertices")));
        }};
        final SortedMap<Integer, List<Graph<Node, Edge>>> groupGraphs = GraphUtils.groupGraphsIntoYears(graphList);
        groupGraphs.forEach((year, gList) -> lines.add(new TextCsvLine()
                .addText(new Text(year))
                .addText(new Text(countEdges(gList)))
                .addText(new Text(countVertices(gList)))));
        IFilePersistence persistence = new FilePersistenceCSV(new File(Constants.DATA_DIR + "perYearInfo_" + fileNameSuffix + ".csv"));
        persistence.saveLinesToFile(lines, false);
    }

    public void writeAccumulatedPerYearData(List<Graph<Node, Edge>> graphList, String fileNameSuffix) {
        List<TextCsvLine> lines = new ArrayList<TextCsvLine>() {{
            //line 1 [headers]
            add(new TextCsvLine()
                    .addText(new Text("Year"))
                    .addText(new Text("Total number of edges"))
                    .addText(new Text("Total number of vertices")));
        }};
        final SortedMap<Integer, List<Graph<Node, Edge>>> groupGraphs = GraphUtils.groupGraphsIntoYears(graphList);
        final Integer[] keys = new Integer[groupGraphs.keySet().size()];
        int accumNumEdges = 0;
        int accumNumNodes = 0;
        for (Integer year : groupGraphs.keySet()) {
            final List<Graph<Node, Edge>> graphListYr = groupGraphs.get(year);
            for (Graph<Node, Edge> graph : graphListYr) {
                accumNumNodes += graph.getVertexCount();
                accumNumEdges += graph.getEdgeCount();
            }
            lines.add(new TextCsvLine()
                    .addText(new Text(year))
                    .addText(new Text(accumNumEdges))
                    .addText(new Text(accumNumNodes)));
        }
        IFilePersistence persistence = new FilePersistenceCSV(new File(Constants.DATA_DIR + "accumulatedPerYearInfo_" + fileNameSuffix + ".csv"));
        persistence.saveLinesToFile(lines, false);
    }

    public void writeDegreeAgainstNodeData(Graph<Node, Edge> graph, boolean sorted, String fileNameSuffix) {
        TextCsvLine xLine = new TextCsvLine();
        TextCsvLine yLine = new TextCsvLine();
        List<Node> vertices = new ArrayList<>();
        vertices.addAll(graph.getVertices());
        if (sorted)
            Collections.sort(vertices, new NodeComparator(graph));
        vertices.forEach(node -> yLine.addText(new Text(graph.getIncidentEdges(node).size())));
        for (int i = 1; i <= vertices.size(); i++) xLine.addText(new Text(i));
        IFilePersistence persistence = new FilePersistenceCSV(new File(Constants.DATA_DIR + "degreeAgainstNode_" + fileNameSuffix + ".csv"));
        persistence.saveLinesToFile(new ArrayList<TextCsvLine>() {{
            add(xLine);
            add(yLine);
        }}, false);
    }

    private int countVertices(List<Graph<Node, Edge>> graphList) {
        int count = 0;
        for (Graph g : graphList)
            count += g.getVertexCount();
        return count;
    }

    private int countEdges(List<Graph<Node, Edge>> graphList) {
        int count = 0;
        for (Graph g : graphList)
            count += g.getEdgeCount();
        return count;
    }

    private double getDoubleVal(Object o) {
        return Double.valueOf(o.toString());
    }

    private Color toGrayscale(int num) {
        int v = ((num / 50) * 255);
        return new Color(v, v, v);
    }

    @Override
    public void visualizeGraph(final Graph<Node, Edge> graph, String title) {
        final Dimension dimension = new Dimension(1200, 1050);
        // create a simple graph for the demo
        final Layout<Node, Edge> layout = new SpringLayout<>(graph);
        //layout.setSize(dimension);
        VisualizationModel<Node, Edge> visModel = new DefaultVisualizationModel<>(layout, dimension);
        visViewer = new VisualizationViewer<>(visModel);
        visViewer.setPreferredSize(dimension);

        //Renderer options
        visViewer.getRenderer().setVertexRenderer(new BasicVertexRenderer<>());
        visViewer.getRenderContext().setVertexFillPaintTransformer(node -> {
            if (maxOutDegreeNode != null && this.maxOutDegreeNode.equals(node))
                return Color.ORANGE;
            else if (maxInDegreeNode != null && this.maxInDegreeNode.equals(node))
                return Color.CYAN;
            else
                return Color.WHITE;
        });
        visViewer.getRenderContext().setVertexShapeTransformer(node -> new Ellipse2D.Double(0, 0, 7, 7));
        visViewer.getRenderContext().setEdgeDrawPaintTransformer(edge -> Color.GRAY);
        //visViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        visViewer.getRenderer().getVertexLabelRenderer().setPositioner(new BasicVertexLabelRenderer.InsidePositioner());
        visViewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);

        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<Number, Number>();
        visViewer.setVertexToolTipTransformer(Node::getId);
        visViewer.setEdgeToolTipTransformer(Edge::printLabel);
        visViewer.addGraphMouseListener(new GraphMouseListenerImpl<>());
        visViewer.setGraphMouse(graphMouse);
        visViewer.addKeyListener(graphMouse.getModeKeyListener());
        visViewer.addKeyListener(graphMouse.getModeKeyListener());
        visViewer.setToolTipText("<html><center>Type 'p' for Pick mode<p>Type 't' for Transform mode");

        // create a frome to hold the graph
        final JFrame frame = new JFrame(title);
        Container content = frame.getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(visViewer);
        content.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menubar = new JMenuBar();
        menubar.add(graphMouse.getModeMenu());
        panel.setCorner(menubar);

        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(e -> scaler.scale(visViewer, 1.1f, visViewer.getCenter()));
        JButton minus = new JButton("-");
        minus.addActionListener(e -> scaler.scale(visViewer, 1 / 1.1f, visViewer.getCenter()));

        JPanel controls = new JPanel();
        controls.add(plus);
        controls.add(minus);
        content.add(controls, BorderLayout.SOUTH);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public VisualizationViewer<Node, Edge> getVisViewer() {
        return visViewer;
    }

    private void setUpBarabasiVariables() {
        graphFactory = SparseMultigraph::new;
        vertexFactory = new VertexFactory();
        edgeFactory = new EdgeFactory();
    }

    static class GraphMouseListenerImpl<V> implements GraphMouseListener<V> {

        public void graphClicked(V v, MouseEvent me) {
        }

        public void graphPressed(V v, MouseEvent me) {
        }

        public void graphReleased(V v, MouseEvent me) {
        }
    }
}