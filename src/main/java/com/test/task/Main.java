package com.test.task;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by Igor on 17.11.16.
 */
public class Main {

    private static CommandLineParser parser = new GnuParser();
    private static Options options = new Options();
    private static Option source = new Option("s", "source", true, "Source is input file.");
    private static Option output = new Option("o","output", true, "Output is output file.");
    private static Option mode = new Option("m", "mode", true, "Mode is multithreading process or not");

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        String inputFile = null;
        String outputFile;
        options.addOption(source);
        options.addOption(output);
        options.addOption(mode);
        CommandLine line = parser.parse(options, args);
        if(line.hasOption("source"))
           inputFile = line.getOptionValue("source");
        else
            System.exit(0);
        if(line.hasOption("output"))
            outputFile = line.getOptionValue("output");
        else
            outputFile = "D:/";
        if(line.hasOption("mode")){
            if(line.getOptionValue("mode").equals("m"))
                FileCompute.computeMultiThreading(new File(inputFile), new File(outputFile));
            if(line.getOptionValue("mode").equals("s"))
                FileCompute.computeRecursive(new File(inputFile), new File(outputFile));
        }
        else {
            FileCompute.computeMultiThreading(new File(inputFile), new File(outputFile));
        }
    }
}
