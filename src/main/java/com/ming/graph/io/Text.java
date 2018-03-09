package com.ming.graph.io;

/**
 * Author: bbrighttaer
 */
public class Text {
    private String text;

    public Text() {
    }

    public Text(String text) {
        this.text = text;
    }

    public Text(Number number) {
        this.text = String.valueOf(number);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
