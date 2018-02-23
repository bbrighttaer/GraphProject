package com.ming.graph.test;

import ch.qos.logback.classic.Logger;
import com.ming.graph.util.GraphLoader;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Author: bbrighttaer
 * Date: 2/6/2018
 * Time: 6:09 PM
 * Project: GraphProject
 */
public class DirCheck extends TestCase{
    private static Logger log = (Logger) LoggerFactory.getLogger(DirCheck.class);

    public void testDataSet(){
        final String path = DirCheck.class.getResource("/data_coauthors").getPath();
        System.out.println(path);
        File file = new File(path);
        if(file.isDirectory()){
            final String[] files = file.list((dir, name) -> StringUtils.endsWith(name, ".graphml"));
            log.info("No. of files: {}", files.length);
            log.info(Arrays.toString(files));
        }
        Assert.assertNotNull(file);
    }

    public void testGraphFilePaths(){
        final List<String> data = GraphLoader.getFilePaths("data_coauthors");
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            bldr.append(data.get(i)).append("\n");
        }
        log.info("\n{}", bldr.toString());
        Assert.assertNotEquals(0, data.size());
    }
}
