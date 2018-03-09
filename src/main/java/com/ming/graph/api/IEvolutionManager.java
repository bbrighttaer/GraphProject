package com.ming.graph.api;

/**
 * Author: bbrighttaer
 * Date: 3/8/2018
 * Time: 7:40 PM
 * Project: GraphProject
 */
public interface IEvolutionManager {
    void afterEvolution(IGraphAnalysis ga);

    void thenShutdown(int c);
}
