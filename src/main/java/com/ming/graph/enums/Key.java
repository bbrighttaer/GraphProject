package com.ming.graph.enums;

/**
 * Author: bbrighttaer
 * Date: 2/21/2018
 * Time: 5:13 PM
 * Project: GraphProject
 */
public enum Key {

    ;
    private final String name;

    Key(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

