package com.model;

import com.utils.MathHelper;
import java.util.List;

public class Statistics {

    int min;
    int max;
    double average;
    double median;
    List<Integer> modalNums;
    int count;

    public Statistics(List<Integer> numbers){
        min = numbers.get(0);
        max = numbers.get(0);
        int sum = 0;
        for(int i=0; i< numbers.size();i++){
            sum += numbers.get(i);
            if(numbers.get(i) > max){
                max = numbers.get(i);
            }
            if(numbers.get(i) < min){
                min = numbers.get(i);
            }
        }
        average =  sum/numbers.size();
        median = MathHelper.median(numbers);
        modalNums = MathHelper.getModalNums(numbers.stream().mapToInt(i->i).toArray());
        count = numbers.size();
    }

    public double getAverage(){ return average; }
    public int getMin(){ return min;}
    public int getMax(){ return max; }
    public double getMedian(){ return median; }
    public List<Integer> getModalNums(){ return modalNums; }
    public int getCount(){ return count; }

}
