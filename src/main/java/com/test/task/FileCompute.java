package com.test.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Igor on 17.11.16.
 */
public class FileCompute {

    private static AtomicLong totalSize = new AtomicLong(0);

    public static void computeMultiThreading(File inputFile, File outputFile) throws IOException, InterruptedException {
        FileWriter writer = new FileWriter(outputFile);
        getFolderSizeMultiThreading(inputFile);
        String result = "Path: " + inputFile.getAbsolutePath() +
                ", size = " + totalSize.longValue() + " bytes";
        writer.write(result);
        printFiles(inputFile, writer);
        writer.close();
    }

    public static void computeRecursive(File inputFile, File outputFile) throws IOException, InterruptedException {
        FileWriter writer = new FileWriter(outputFile);
        String result = "Path: " + inputFile.getAbsolutePath() +
                ", size = " + getFolderSize(inputFile) + " bytes";
        writer.write(result);
        printFiles(inputFile, writer);
        writer.close();
    }

    private static void getFolderSizeMultiThreading(File directory) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                totalSize.getAndAdd(file.length());
            }
            else {
                service.submit(() -> {
                    try {
                        getFolderSizeMultiThreading(file);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                });
            }
        }
        service.shutdown();
        service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
    }

    private static long getFolderSize(File dir) {
        long totalSize = 0;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                totalSize += file.length();
            } else {
                totalSize += getFolderSize(file);
            }
        }
        return totalSize;
    }

    private static void printFiles(File inputFile, FileWriter writer) throws IOException {
        File[] listOfFiles = inputFile.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                writer.write("\nFile " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                writer.write("\nDirectory " + listOfFiles[i].getName());
            }
        }
    }
}
