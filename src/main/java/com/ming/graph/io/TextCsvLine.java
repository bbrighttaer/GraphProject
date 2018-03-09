package com.ming.graph.io;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Author: bbrighttaer
 */
public class TextCsvLine extends Line<Text> {
    public static char SEPARATOR = ',';

    public TextCsvLine() {
        super(new LinkedList<>());
    }

    @Override
    public String print() {
        StringBuilder strb = new StringBuilder();
        final ListIterator<Text> it = this.line.listIterator();
        while (it.hasNext()) {
            final Text t = it.next();
            strb.append(t.getText());
            if (!t.equals(this.line.getLast()))
                strb.append(SEPARATOR);
        }
        return strb.toString();
    }

    @Override
    public String println() {
        return print().concat("\n");
    }

    public TextCsvLine addText(Text text) {
        this.line.add(text);
        return this;
    }

    public void removeText(Text text) {
        this.line.remove(text);
    }

    public Text removeText(int index) {
        return this.line.remove(index);
    }

    public Text backspace() {
        return this.line.removeLast();
    }

    @Override
    public String toString() {
        return print();
    }
}
