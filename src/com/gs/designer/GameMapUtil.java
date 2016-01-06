package com.gs.designer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by WangGenshen on 1/6/16.
 */
public class GameMapUtil {

    public static void saveBarriers(List<Barrier> barriers, List<Barrier> toMovedBarriers) throws IOException {
        String str = "";
        barriers.removeAll(toMovedBarriers);
        for(int i = 0, size = barriers.size(); i < size; i++) {
            Barrier barrier = barriers.get(i);
            if((i + 1) % 10 == 0) {
                str += "\n";
            }
            if(str.equals("")) {
                str += "{" + barrier.getRow() + ", " + barrier.getCol() + ", " + barrier.getType() + "}";
            } else {
                str += ", {" + barrier.getRow() + ", " + barrier.getCol() + ", " + barrier.getType() + "}";
            }
        }
        outToFile(str);
        System.out.println(str);
    }

    private static void outToFile(String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("barrier.txt")));
        writer.write(str);
        writer.flush();
        writer.close();
    }


}
