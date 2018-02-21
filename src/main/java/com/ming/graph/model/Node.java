package com.ming.graph.model;

import com.ming.graph.fmwk.MetadataId;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 2:48 PM
 * Project: GraphProject
 */
public class Node {
    private String id;
    @MetadataId("d3")
    private String osmid;
    @MetadataId("d5")
    private String y;
    @MetadataId("d4")
    private String x;

    public Node() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOsmid() {
        return osmid;
    }

    public void setOsmid(String osmid) {
        this.osmid = osmid;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String stringify() {
        return "Node{" +
                "id='" + id + '\'' +
                ", osmid='" + osmid + '\'' +
                ", y='" + y + '\'' +
                ", x='" + x + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return id;
    }
}
