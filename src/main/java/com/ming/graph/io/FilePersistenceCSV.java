package com.ming.graph.io;

import ch.qos.logback.classic.Logger;
import com.ming.graph.api.IFilePersistence;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Author: bbrighttaer
 */
public class FilePersistenceCSV implements IFilePersistence {
    private static Logger log = (Logger) LoggerFactory.getLogger(FilePersistenceCSV.class);
    private File file;

    public FilePersistenceCSV(File file) {
        this.file = file;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public <E extends Line> void saveLinesToFile(List<E> linesList, boolean append) {
        try{
            FileUtils.writeLines(file, linesList, append);
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }
}
