package com.alibaba.middleware.race.sync.unused.unitTest;

import com.alibaba.middleware.race.sync.unused.server.ServerPipelinedComputation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.alibaba.middleware.race.sync.unused.server.ServerPipelinedComputation.*;

/**
 * Created by yche on 6/6/17.
 */
public class FileStatistics {
    private static ArrayList<String> myFiles = new ArrayList<>();

    static {
//        for(int i = 10; i > 0 ; i--)
//            myFiles.add("/home/will/Workspace/test/canal_data/" + i + ".txt");
//        myFiles.add("/tmp/canal.txt");

        for (int i = 10; i > 0; i--) {
//            myFiles.add("/tmp/" + i + ".txt");
            myFiles.add("/home/yche/OutData/" + i + ".txt");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        Thread.sleep(5000);
        long programStart = System.currentTimeMillis();

//        readFilesIntoPageCache(myFiles);

        // 1st: init
        //initSchemaTable("middleware", "student");
//        initSchemaTable("middleware2", "teacher");
        initRange(100000, 2000000);
        initFindResultListener(new FindResultListener() {
            @Override
            public void sendToClient(String result) {
//                System.out.println(result);
            }
        });

        // 2nd: computations
        for (String name : myFiles)
            OneRoundComputation(name);

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