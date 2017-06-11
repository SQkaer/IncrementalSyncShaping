package com.alibaba.middleware.race.sync.unitTest;

import com.alibaba.middleware.race.sync.server.ServerPipelinedComputation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import static com.alibaba.middleware.race.sync.server.ServerPipelinedComputation.*;

/**
 * Created by yche on 6/6/17.
 */
public class FileStatistics {
    private static ArrayList<String> myFiles = new ArrayList<>();

    static {
        myFiles.add("/tmp/canal.txt");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(5000);
        long programStart = System.currentTimeMillis();

        readFilesIntoPageCache(myFiles);

        // 1st: init
        initSchemaTable("middleware3", "student");
        initRange(600, 700);

        // 2nd: computations
        OneRoundComputation("/tmp/canal.txt", new FindResultListener() {
            @Override
            public void sendToClient(String result) {
//                System.out.println(result);
            }
        });

        // 3rd: join computation thread
        JoinComputationThread();

        // 4th: write results
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/tmp/yche.rs"));
        for (Map.Entry<Long, String> entry : ServerPipelinedComputation.inRangeRecord.entrySet()) {
            bufferedWriter.write(entry.getValue());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

        long programEnd = System.currentTimeMillis();
        System.out.println("program duration:" + (programEnd - programStart));
    }
}