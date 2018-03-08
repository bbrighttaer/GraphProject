package com.ming.graph.impl;

/**
 * Author: bbrighttaer
 * Date: 3/8/2018
 * Time: 5:37 PM
 * Project: GraphProject
 */
public class Degree {
    private int d = 0;

    public Degree() {
    }

    public Degree increment(){
        ++d;
        return this;
    }

    public int get(){
        return d;
    }
}
