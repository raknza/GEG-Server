package com.utils;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MathHelper {

    public static double median(List<Integer> total) {
        double j;
        //集合排序
        Collections.sort(total);
        int size = total.size();
        if(size % 2 == 1){
            j = total.get((size-1)/2);
        }else {
            //加0.0是為了把int轉成double型別，否則除以2會算錯
            j = (total.get(size/2-1) + total.get(size/2) + 0.0)/2;
        }
        return j;
    }

    public static List<Integer> getModalNums(int[] arr) {
        int n = arr.length;

        if (n == 0) {
            return new ArrayList<>();
        }

        if (n == 1) {
            return Collections.singletonList(arr[0]);
        }

        Map<Integer,Integer> freqMap = new HashMap<>();
        for (int i = 0; i < n; i++) { // 統計陣列中每個數出現的頻率
            Integer v = freqMap.get(arr[i]);
            // v == null 說明 freqMap 中還沒有這個 arr[i] 這個鍵
            freqMap.put(arr[i],v == null ? 1 : v + 1);
        }

        // 將 freqMap 中所有的鍵值對（鍵為數，值為數出現的頻率）放入一個 ArrayList
        List<Map.Entry<Integer,Integer>> entries = new ArrayList<>(freqMap.entrySet());
        // 對 entries 按出現頻率從大到小排序
        entries.sort((e1, e2) -> e2.getValue() - e1.getValue());

        List<Integer> modalNums = new ArrayList<>();
        modalNums.add(entries.get(0).getKey()); // 排序後第一個 entry 的鍵肯定是一個眾數

        int size = entries.size();
        for (int i = 1; i < size; i++) {
            // 如果之後的 entry 與第一個 entry 的 value 相等，那麼這個 entry 的鍵也是眾數
            if (entries.get(i).getValue().equals(entries.get(0).getValue())) {
                modalNums.add(entries.get(i).getKey());
            } else {
                break;
            }
        }

        return modalNums;
    }
}
