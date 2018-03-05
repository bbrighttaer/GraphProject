package com.ming.graph.io;

import java.lang.instrument.Instrumentation;

/**
 * Author: bbrighttaer
 * Date: 3/4/2018
 * Time: 5:11 PM
 * Project: GraphProject
 */
public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
}
