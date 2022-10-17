package com.example.currentplacedetailsonmap.knn;

/**

* 基礎靜態文件數據

* @author line

*

*/

public interface Base {

    public static final int KNEIGHBOUR = 5; //number of neighbors最近鄰個數

    //public static final int COLUMNCOUNT = 1682; //number of items 項目總數

    public static final int PREFROWCOUNT = 5; //number of users in base訓練集上的用戶數目

    public static final int TESTROWCOUNT = 462; //number of users in test測試集上的用戶數目

    public static final String BASE = "u1.base";//訓練集

    public static final int BASE_LINE = 80000;//base數據集的行數

    public static final String TEST = "u1.test";//測試集

    public static final int TEST_LINE = 20000;//test數據集的行數

    public static final String BASE_GENRE = "u.user";//用戶屬性集

    public static final String BASE_ITEMS_GENRE = "u.item";//用戶屬性集

    public static final int ITEMS_GENRE_LINE = 19;//test數據集的行數

}