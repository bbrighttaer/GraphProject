package com.ming.graph.impl;


import ch.qos.logback.classic.Logger;
import com.ming.graph.api.IGraphAnalysis;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 1:47 PM
 * Project: GraphProject
 */
public class DecisionRound implements ActionListener {
    private static Logger log = (Logger) LoggerFactory.getLogger(DecisionRound.class);
    final private List<IGraphAnalysis> ga;
    private int tsCount = 0;

    public DecisionRound(List<IGraphAnalysis> analysisList) {
        this.ga = analysisList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (tsCount == 0) {
            ga.forEach(g -> g.showUI("Main Complex Graph Evolution Process"));
            tsCount++;
        }
        /**
         *
         */
        ga.forEach(g -> g.printNodeStatistics());

        /**
         *
         */
        ga.forEach(g -> g.evolveGraph());

        /**
         *
         */
        ga.forEach(g -> g.updateUI());
        ++tsCount;
    }
}
