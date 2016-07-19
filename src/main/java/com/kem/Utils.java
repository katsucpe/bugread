package com.kem;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KATSU on 17/7/2559.
 */
public class Utils {
    public static void sortFileList(File[] allFile){
        Arrays.sort(allFile, (o1, o2) -> {
            int name1 = Integer.parseInt(((File) o1).getName().replace(".xml", ""));
            int name2 = Integer.parseInt(((File) o2).getName().replace(".xml", ""));
            if (name1 > name2) {
                return -1;
            } else if (name1 < name2) {
                return +1;
            } else {
                return 0;
            }
        });
    }

    public static boolean stringContainWordInList(String str, String[] listWords){
        for (String s : listWords)
        {
            if (str.contains(s))
            {
                return true;
            }
        }
        return false;
    }
}
