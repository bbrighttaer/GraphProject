package com.ming.graph.impl;


import ch.qos.logback.classic.Logger;
import com.ming.graph.api.IGraphAnalysis;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: bbrighttaer
 * Date: 2/7/2018
 * Time: 1:47 PM
 * Project: GraphProject
 */
public class DecisionRound implements ActionListener{
    private static Logger log = (Logger) LoggerFactory.getLogger(DecisionRound.class);
    final private IGraphAnalysis ga;
    private int tsCount = 0;

    public DecisionRound(IGraphAnalysis ga) {
        this.ga = ga;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(tsCount == 0){
            ga.showUI("Complex Graph Analysis");
            tsCount++;
        }
        /**
         *
         */
        ga.printNodeStatistics();

        /**
         *
         */
        ga.evolveGraph();

        /**
         *
         */
        ga.updateUI();
    }
}
