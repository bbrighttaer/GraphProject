package com.ming.graph.api;

import com.ming.graph.app.GraphAnalysis;

/**
 * Author: bbrighttaer
 * Date: 3/8/2018
 * Time: 7:40 PM
 * Project: GraphProject
 */
public interface IEvolutionManager {
    void afterEvolution(GraphAnalysis ga);

    void thenShutdown(int c);
}
