package com.kem.NgramCounter;

import java.util.HashMap;

/**
 * Created by KATSU on 30/8/2016.
 */
public class NgramResult {
    public String[] results;
    private HashMap<String, Integer> resultDic = new HashMap<String, Integer>();
    public HashMap<String, Integer> getProcessResult(){
        if(results.length != resultDic.keySet().size()){
            for (String s:results) {
                 int splitPoint = s.lastIndexOf(':');
                String k = s.substring(0,splitPoint).trim();
                int c = Integer.parseInt(s.substring(splitPoint + 1, s.length()));
                if(resultDic.containsKey(k)){
                    int oldVal = resultDic.get(k);
                     resultDic.replace(k, oldVal + c);
                }else{
                    resultDic.put(k, c);
                }
            }
        }
        return resultDic;
    }
}
