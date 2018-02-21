package com.ming.graph.model;

import com.ming.graph.fmwk.MetadataId;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 2:51 PM
 * Project: GraphProject
 */
public class Edge {
    //private String id;
    @MetadataId("d15")
    private String bridge;
    @MetadataId("d14")
    private String lanes;
    @MetadataId("d13")
    private String ref;
    @MetadataId("d12")
    private String key;
    @MetadataId("d11")
    private String geometry;
    @MetadataId("d10")
    private String osmid;
    @MetadataId("d9")
    private String length;
    @MetadataId("d8")
    private String name;
    @MetadataId("d7")
    private String oneway;
    @MetadataId("d6")
    private String highway;

    public Edge() {
    }

    public Edge(String bridge, String lanes, String ref, String key, String geometry,
                String osmid, String length, String name, String oneway, String highway) {
        this.bridge = bridge;
        this.lanes = lanes;
        this.ref = ref;
        this.key = key;
        this.geometry = geometry;
        this.osmid = osmid;
        this.length = length;
        this.name = name;
        this.oneway = oneway;
        this.highway = highway;
    }

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }

    public String getLanes() {
        return lanes;
    }

    public void setLanes(String lanes) {
        this.lanes = lanes;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getOsmid() {
        return osmid;
    }

    public void setOsmid(String osmid) {
        this.osmid = osmid;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOneway() {
        return oneway;
    }

    public void setOneway(String oneway) {
        this.oneway = oneway;
    }

    public String getHighway() {
        return highway;
    }

    public void setHighway(String highway) {
        this.highway = highway;
    }

    public String stringify() {
        return "Edge{" +
                "bridge='" + bridge + '\'' +
                ", lanes='" + lanes + '\'' +
                ", ref='" + ref + '\'' +
                ", key='" + key + '\'' +
                ", geometry='" + geometry + '\'' +
                ", osmid='" + osmid + '\'' +
                ", length='" + length + '\'' +
                ", name='" + name + '\'' +
                ", oneway='" + oneway + '\'' +
                ", highway='" + highway + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return name;
    }
}
