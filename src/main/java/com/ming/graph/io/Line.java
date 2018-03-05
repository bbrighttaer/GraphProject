package com.ming.graph.io;

import java.util.LinkedList;

/**
 * Author: bbrighttaer
 */
public abstract class Line<T> {
    protected LinkedList<T> line;

    public Line() {
        this(null);
    }

    public Line(LinkedList<T> line) {
        this.line = line;
    }

    public LinkedList<T> getLine() {
        return line;
    }

    public void setLine(LinkedList<T> line) {
        this.line = line;
    }

    public abstract String print();

    public abstract String println();

    public <L extends Line<T>> L join(L l){
        line.addAll(l.line);
        l.setLine(this.line);
        return l;
    }

    @Override
    public String toString() {
        return print();
    }
}
