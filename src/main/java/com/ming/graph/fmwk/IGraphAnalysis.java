package com.ming.graph.fmwk;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 1:54 PM
 * Project: GraphProject
 *  Key graph operations for every time step here
 */
public interface IGraphAnalysis {
    void evolveGraph();

    void printNodeStatistics();

    void showUI(String title);

    void updateUI();
}
