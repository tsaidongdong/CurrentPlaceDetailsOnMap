package com.example.currentplacedetailsonmap.knn;

import java.util.Arrays;

import java.util.HashMap;

import java.util.Map;

import java.util.Scanner;

import java.util.Set;


public class Application implements Base{

    int key_word_id=0;
    public int start(int[][] input_base,int COLUMNCOUNT){
        int userId=6;
        int [][] user_attraction_base= new int[6][COLUMNCOUNT];
        for(int i = 0;i<6;i++){
            for(int j=0;j<COLUMNCOUNT;j++){
                user_attraction_base[i][j]=input_base[i][j];
            }
        }

        //產生相似度矩陣

        double[] similarityMatrix = new ProduceSimilarityMatrix().produceSimilarityMatrix(user_attraction_base, userId);

        // 知道每個用戶之間的相似度值之後，開始獲取每隔相似值對應的userId，然後和相似值關聯，再根據相似值排序，即得到相似愛好的userId，然後再輸出相似推薦的商品

        int[] id = new int[KNEIGHBOUR];//存放K個最近鄰userId

        //產生一個臨時相似度矩陣變量，是為了相似度排序時和userid對應
        double[] tempSimilarity = new double[similarityMatrix.length];

        for(int j = 0; j < tempSimilarity.length; j++) {

            tempSimilarity[j] = similarityMatrix[j];

        }
        Arrays.sort(tempSimilarity);//排序，升序
        int flag = 0;//臨時變量

        double[] similarity = new double[KNEIGHBOUR];//保存前K個相似度，從大到小

        for (int m = tempSimilarity.length - 1; m >= tempSimilarity.length - KNEIGHBOUR; m--) {

            for(int j = 0; j < similarityMatrix.length; j++) {

                if (similarityMatrix[j] == tempSimilarity[m] && similarityMatrix[j] != 0.0){

                    similarity[flag] = tempSimilarity[m];

                    id[flag]=j;//保存前K個相似度的userid

                    flag++;

                }

            }

        }
        key_word_id=id[0];
        return key_word_id;
    }

}
