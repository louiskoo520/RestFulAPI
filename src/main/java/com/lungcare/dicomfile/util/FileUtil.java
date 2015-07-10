package com.lungcare.dicomfile.util;

import java.io.File;
import java.util.Arrays;

public class FileUtil {
    //for test
    public static void main(String []args) {
        File dirFile = new File("G:\\wjlProgramFiles\\local-git-repository\\LungCareRestFulAPI\\src\\main\\webapp\\allBmps\\efed2711-b150-1b1f-521a-6aa2343236d2\\coronal");
        File [] sortedFiles = listSortedFiles(dirFile);
    	for(int i=0;i<sortedFiles.length;i++){
    		System.out.println(sortedFiles[i].getName());
    	}
    }

    //list sorted files
    public static File[] listSortedFiles(File dirFile) {
    assert dirFile.isDirectory();

        File[] files = dirFile.listFiles();
        
        FileWrapper [] fileWrappers = new FileWrapper[files.length];
        for (int i=0; i<files.length; i++) {
            fileWrappers[i] = new FileWrapper(files[i]);
        }
        
        Arrays.sort(fileWrappers);
        
        File []sortedFiles = new File[files.length];
        for (int i=0; i<files.length; i++) {
            sortedFiles[i] = fileWrappers[i].getFile();
        }
        
        return sortedFiles;
    }
}


class FileWrapper implements Comparable<Object> {
    /** File */
    private File file;
    
    public FileWrapper(File file) {
        this.file = file;
    }
     
    public int compareTo(Object obj) {
        assert obj instanceof FileWrapper;
        
        FileWrapper castObj = (FileWrapper)obj;
                
        if (this.file.getName().compareTo(castObj.getFile().getName()) > 0) {
            return 1;
        } else if (this.file.getName().compareTo(castObj.getFile().getName()) < 0) {
            return -1;
        } else {
            return 0;
        }
    }
    
    public File getFile() {
        return this.file;
    }
}
