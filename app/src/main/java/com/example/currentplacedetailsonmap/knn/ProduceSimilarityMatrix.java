package com.example.currentplacedetailsonmap.knn;

/**

* 產生相似矩陣,通過一個userId找其最近鄰userId喜歡的產品，則相似度矩陣為一行n列矩陣，

* 若是全部比較一個矩陣所有userId的相關度產生一個n行n列矩陣

* @author line

*

*/

public class ProduceSimilarityMatrix implements Base{

    //在計算MAE會用到

    public double[][] produceSimilarityMatrix(int[][] preference) {

        double[][] similarityMatrix = new double[PREFROWCOUNT][PREFROWCOUNT];//行和列都是所有的用戶，因為是每一行和每一行相比，所以得到的相似矩陣為正方形

        for (int i = 0; i < PREFROWCOUNT; i++) {

            for (int j = 0; j < PREFROWCOUNT; j++) {

            if (i == j) {

                continue;

            }

            //數據是兩行之間對比，其實只需要填滿相似度矩陣的左下方或者右上方即可（減少重複運算）

            similarityMatrix[i][j] =

            new ComputeSimilarity().computeSimilarity(preference[i], preference[j]);//參數是從第一行開始，和其他每一行比較相似度

            }

        }

        return similarityMatrix;//返回相似度矩陣

    }

    //計算某個userId的相似度矩陣，用戶之間的相似度是每個用戶的每件商品評分的相似度，也就是說相似度矩陣是行是用戶列也是用戶，是正方形矩陣，對角線上的值都為1

    //參數i是輸入的userid

    public double[] produceSimilarityMatrix(int[][] preference,int i) {
        double[] similarityMatrix = new double[PREFROWCOUNT];//定義一個相似度矩陣，行和列都是所有的用戶，因為是每一行和每一行相比，所以得到的相似矩陣為正方形

        for (int j = 0; j < PREFROWCOUNT; j++) {//循環和其他userId對比其所有商品

            if(j==(i-1)){//不比較同行，i-1是因為數組索引比userid小1

            continue;//跳出循環，繼續下一次循環

            }

            similarityMatrix[j] =new ComputeSimilarity().computeSimilarity(preference[i-1], preference[j]);//參數是從第一行開始，和其他每一行比較相似度

        }

        return similarityMatrix;//返回相似度矩陣，只有在userid-1行有數據，其他行列數據都為0，因為只是userid-1行和其他行對比

    }

    //根據性別屬性，產生用戶性別屬性相似度

    public double[] produceSimilarityMatrixGener(int[] preference,int userId) {


        double[] similarityMatrix = new double[PREFROWCOUNT];//定義一個相似度矩陣，行和列都是所有的用戶，因為是每一行和每一行相比，所以得到的相似矩陣為正方形

        for (int j = 0; j < PREFROWCOUNT; j++) {//循環和其他userId對比其所有商品

            if(j==(userId-1)){//不比較同行，i-1是因為數組索引比userid小1

                continue;//跳出循環，繼續下一次循環

            }

            if(preference[j]==preference[userId-1])

                similarityMatrix[j] = 1;

            else

                similarityMatrix[j] = 0;

        }

        return similarityMatrix;//返回相似度矩陣，只有在userid-1行有數據，其他行列數據都為0，因為只是userid-1行和其他行對比

    }

    //基於項目

    /*public double[] produceSimilarityMatrixItems(int[][] preference,int i) {

        double[] similarityMatrix = new double[COLUMNCOUNT];

        for (int j = 0; j < COLUMNCOUNT; j++) {

            if(j==(i-1)){//不比較同行

                continue;//跳出循環，繼續下一次循環

            }

            similarityMatrix[j] =new ComputeSimilarity().computeSimilarity(preference[i-1], preference[j]);//參數是從第一行開始，和其他每一行比較相似度

        }

        return similarityMatrix;//返回相似度矩陣，只有在userid-1行有數據，其他行列數據都為0，因為只是userid-1行和其他行對比

    }*/

}