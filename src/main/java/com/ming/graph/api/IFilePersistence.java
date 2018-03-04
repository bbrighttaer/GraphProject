package com.ming.graph.api;

import com.ming.graph.io.Line;

import java.io.File;
import java.util.List;

/**
 * Author: bbrighttaer
 * Date: 3/4/2018
 * Time: 2:41 PM
 * Project: GraphProject
 */
public interface IFilePersistence {
    File getFile();

    void setFile(File file);

    <E extends Line> void saveLinesToFile(List<E> linesList, boolean append);
}
