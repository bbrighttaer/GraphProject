package com.ming.graph.test;

import com.ming.graph.api.IFilePersistence;
import com.ming.graph.config.Constants;
import com.ming.graph.io.FilePersistenceCSV;
import com.ming.graph.io.Text;
import com.ming.graph.io.TextCsvLine;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: bbrighttaer
 * Date: 3/4/2018
 * Time: 2:33 PM
 * Project: GraphProject
 */
public class IOTest {
    @Test
    public void testCSVPersistence() {
        List<TextCsvLine> lines = new ArrayList<TextCsvLine>() {{
            //line 1 [headers]
            add(new TextCsvLine()
                    .addText(new Text("Size of the data set (KB)"))
                    .addText(new Text("Beginning year"))
                    .addText(new Text("End year"))
                    .addText(new Text("Total number of vertices"))
                    .addText(new Text("Total number of links"))
                    .addText(new Text("Average years' number of vertices"))
                    .addText(new Text("Average years' number of links")));
            //line 2
            add(new TextCsvLine()
                    .addText(new Text(200))
                    .addText(new Text(1997))
                    .addText(new Text(2017))
                    .addText(new Text(50000))
                    .addText(new Text(80000))
                    .addText(new Text(6000))
                    .addText(new Text(7000)));
        }};
        IFilePersistence persistence = new FilePersistenceCSV(new File(Constants.DATA_DIR + "test.txt"));
        persistence.saveLinesToFile(lines, false);
    }
}
