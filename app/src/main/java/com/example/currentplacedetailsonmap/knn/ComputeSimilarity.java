package com.example.currentplacedetailsonmap.knn;

import java.util.ArrayList;

import java.util.List;

/**

* 從兩行數據中，獲取需要對比的要求數據

* @author line

*

*/

public class ComputeSimilarity {

    public double computeSimilarity(int[] item1,int[] item2) {

        List<Integer> list1 = new ArrayList<Integer>();//因為不知道兩行userid的評分是否有效即都不為0，所以定義集合來儲存不知道的有效評分

        List<Integer> list2 = new ArrayList<Integer>();

        for (int i = 0; i < item1.length; i++) {

            if(item1[i] != 0 || item2[i] !=0) {//如果相同列上有0就捨去

            list1.add(new Integer(item1[i]));//因為合格數據個數不確定，所以用集合表示

            list2.add(new Integer(item2[i]));

            }
        }
        return new PearsonCorrelation().pearsonCorrelation(list1,list2);//返回相似度值
    }

}