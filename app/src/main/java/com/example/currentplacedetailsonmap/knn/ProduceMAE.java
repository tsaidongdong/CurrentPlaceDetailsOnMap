package com.example.currentplacedetailsonmap.knn;

/**

* 計算MAE平均絕對誤差

* @author line

*

*/

public class ProduceMAE implements Base{

    //求誤差

    public double[] produceMAE(double[][] m,int[][]test){

    double mae= 0.0;

    double []mm=new double[TESTROWCOUNT ];

    for(int i=0;i<TESTROWCOUNT ;i++ ) {

        double sum_fencha= 0.0;

        int num=0;

        for(int j=0;j<PREFROWCOUNT;j++){

            if(test[i][j]!=0&& m[i][j]!=0){

                sum_fencha+=Math.abs(m[i][j]-(double)test[i][j]);//相差取絕對值

                num++;

            }

        }if (num==0) mae=0;else mae= sum_fencha/num;

        mm[i]=mae;

    }

    return mm;

    }

}