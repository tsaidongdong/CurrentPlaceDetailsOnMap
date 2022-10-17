package com.example.currentplacedetailsonmap;

import com.example.currentplacedetailsonmap.knn.Application;

import java.util.Collections;
import java.util.Vector;

public class Like_analyze {
    /*private static Vector<String> tags=new Vector<>();//store a char
    private static Vector<Character> char_tags=new Vector<>();//store a char
    private static Vector<Character> set_char=new Vector<>();//store a char
    private static Vector<Integer> count_number=new Vector<>();//store the set_char count
    private static char key_char;//store the key char*/

    private static Vector<String> tags1_label= new Vector<String>();//store the tags analyze
    private static Vector<String> tags2_label= new Vector<String>();//store the tags analyze
    private static Vector<String> tags3_label= new Vector<String>();//store the tags analyze
    private static Vector<String> tags4_label= new Vector<String>();//store the tags analyze
    private static Vector<String> tags5_label= new Vector<String>();//store the tags analyze
    private static Vector<String> tags6_label= new Vector<String>();//store the tags analyze
    private static Vector<Integer> tags1_number= new Vector<Integer>();//store the tags analyze
    private static Vector<Integer> tags2_number= new Vector<Integer>();//store the tags analyze
    private static Vector<Integer> tags3_number= new Vector<Integer>();//store the tags analyze
    private static Vector<Integer> tags4_number= new Vector<Integer>();//store the tags analyze
    private static Vector<Integer> tags5_number= new Vector<Integer>();//store the tags analyze
    private static Vector<Integer> tags6_number= new Vector<Integer>();//store the tags analyze
    private static Vector<Float> tags1_data= new Vector<Float>();//store the tags analyze
    private static Vector<Float> tags2_data= new Vector<Float>();//store the tags analyze
    private static Vector<Float> tags3_data= new Vector<Float>();//store the tags analyze
    private static Vector<Float> tags4_data= new Vector<Float>();//store the tags analyze
    private static Vector<Float> tags5_data= new Vector<Float>();//store the tags analyze

    private  static Vector<String> result_label= new Vector<String>();//store the tags analyze
    private static Vector<Double> result_data= new Vector<Double>();//store the tags analyze

    private float distance1=0;
    private float distance2=0;
    private float distance3=0;
    private float distance4=0;
    private float distance5=0;

    private Vector<String> user_attraction_label= new Vector<String>();//store the all tags number

    protected String classification(String[] string1,String[] string2,String[] string3,String[] string4,String[] string5,String[] string6){
        String KeyWord=null;
        int key_id;
        //System.out.println("in the classification");
        for(int a=0;a<string1.length;a++){
            //System.out.println("in the classification for");
            int b=0;
            for(b=0;b<tags1_label.size();b++){
                if(string1[a]==tags1_label.get(b)) {
                    tags1_number.set(b,tags1_number.get(b)+1);
                    break;
                }
            }
            if(b==tags1_label.size()){
                tags1_label.add(string1[a]);
                tags1_number.add(1);
            }
        }
        for(int a=0;a<tags1_label.size();a++){
            System.out.println(tags1_label.get(a));
            System.out.println(tags1_number.get(a));
        }

        for(int a=0;a<string2.length;a++){
            //System.out.println("in the classification for");
            int b=0;
            for(b=0;b<tags2_label.size();b++){
                if(string2[a]==tags2_label.get(b)) {
                    tags2_number.set(b,tags2_number.get(b)+1);
                    break;
                }
            }
            if(b==tags2_label.size()){
                tags2_label.add(string2[a]);
                tags2_number.add(1);
            }
        }
        for(int a=0;a<tags2_label.size();a++){
            System.out.println(tags2_label.get(a));
            System.out.println(tags2_number.get(a));
        }

        for(int a=0;a<string3.length;a++){
            //System.out.println("in the classification for");
            int b=0;
            for(b=0;b<tags3_label.size();b++){
                if(string3[a]==tags3_label.get(b)) {
                    tags3_number.set(b,tags3_number.get(b)+1);
                    break;
                }
            }
            if(b==tags3_label.size()){
                tags3_label.add(string3[a]);
                tags3_number.add(1);
            }
        }
        for(int a=0;a<tags3_label.size();a++){
            System.out.println(tags3_label.get(a));
            System.out.println(tags3_number.get(a));
        }

        for(int a=0;a<string4.length;a++){
            //System.out.println("in the classification for");
            int b=0;
            for(b=0;b<tags4_label.size();b++){
                if(string4[a]==tags4_label.get(b)) {
                    tags4_number.set(b,tags4_number.get(b)+1);
                    break;
                }
            }
            if(b==tags4_label.size()){
                tags4_label.add(string4[a]);
                tags4_number.add(1);
            }
        }
        for(int a=0;a<tags4_label.size();a++){
            System.out.println(tags4_label.get(a));
            System.out.println(tags4_number.get(a));
        }

        for(int a=0;a<string5.length;a++){
            //System.out.println("in the classification for");
            int b=0;
            for(b=0;b<tags5_label.size();b++){
                if(string5[a]==tags5_label.get(b)) {
                    tags5_number.set(b,tags5_number.get(b)+1);
                    break;
                }
            }
            if(b==tags5_label.size()){
                tags5_label.add(string5[a]);
                tags5_number.add(1);
            }
        }
        for(int a=0;a<tags5_label.size();a++){
            System.out.println(tags5_label.get(a));
            System.out.println(tags5_number.get(a));
        }

        for(int a=0;a<string6.length;a++){
            //System.out.println("in the classification for");
            int b=0;
            for(b=0;b<tags6_label.size();b++){
                if(string5[a]==tags6_label.get(b)) {
                    tags6_number.set(b,tags6_number.get(b)+1);
                    break;
                }
            }
            if(b==tags6_label.size()){
                tags6_label.add(string5[a]);
                tags6_number.add(1);
            }
        }
        for(int a=0;a<tags6_label.size();a++){
            System.out.println(tags6_label.get(a));
            System.out.println(tags6_number.get(a));
        }




        for(int a=0;a<tags1_label.size();a++){
            int b=0;
            for(b=0;b<user_attraction_label.size();b++){
                if (user_attraction_label.get(b) == tags1_label.get(a)) {
                    break;
                }
            }
            if(b==user_attraction_label.size()){
                user_attraction_label.add(tags1_label.get(a));
            }
        }
        for(int a=0;a<tags2_label.size();a++){
            int b=0;
            for(b=0;b<user_attraction_label.size();b++){
                if (user_attraction_label.get(b) == tags2_label.get(a)) {
                    break;
                }
            }
            if(b==user_attraction_label.size()){
                user_attraction_label.add(tags2_label.get(a));
            }
        }
        for(int a=0;a<tags3_label.size();a++){
            int b=0;
            for(b=0;b<user_attraction_label.size();b++){
                if (user_attraction_label.get(b) == tags3_label.get(a)) {
                    break;
                }
            }
            if(b==user_attraction_label.size()){
                user_attraction_label.add(tags3_label.get(a));
            }
        }
        for(int a=0;a<tags4_label.size();a++){
            int b=0;
            for(b=0;b<user_attraction_label.size();b++){
                if (user_attraction_label.get(b) == tags4_label.get(a)) {
                    break;
                }
            }
            if(b==user_attraction_label.size()){
                user_attraction_label.add(tags4_label.get(a));
            }
        }
        for(int a=0;a<tags5_label.size();a++){
            int b=0;
            for(b=0;b<user_attraction_label.size();b++){
                if (user_attraction_label.get(b) == tags5_label.get(a)) {
                    break;
                }
            }
            if(b==user_attraction_label.size()){
                user_attraction_label.add(tags5_label.get(a));
            }
        }
        for(int a=0;a<tags6_label.size();a++){
            int b=0;
            for(b=0;b<user_attraction_label.size();b++){
                if (user_attraction_label.get(b) == tags6_label.get(a)) {
                    break;
                }
            }
            if(b==user_attraction_label.size()){
                user_attraction_label.add(tags6_label.get(a));
            }
        }

        int [][] user_attraction_base=new int[6][user_attraction_label.size()];

        for(int a=0;a<user_attraction_label.size();a++) {//反向檢近來
            int b=0;
            for(b=0;b<tags1_label.size();b++){
                if(user_attraction_label.get(a)==tags1_label.get(b)){
                    user_attraction_base[0][a]=tags1_number.get(b);
                    break;
                }
            }
            if (b==tags1_label.size()){
                user_attraction_base[0][a]=0;
            }

            b=0;
            for(b=0;b<tags2_label.size();b++){
                if(user_attraction_label.get(a)==tags2_label.get(b)){
                    user_attraction_base[1][a]=tags2_number.get(b);
                    break;
                }
            }
            if (b==tags2_label.size()){
                user_attraction_base[1][a]=0;
            }

            b=0;
            for(b=0;b<tags3_label.size();b++){
                if(user_attraction_label.get(a)==tags3_label.get(b)){
                    user_attraction_base[2][a]=tags3_number.get(b);
                    break;
                }
            }
            if (b==tags3_label.size()){
                user_attraction_base[2][a]=0;
            }

            b=0;
            for(b=0;b<tags4_label.size();b++){
                if(user_attraction_label.get(a)==tags4_label.get(b)){
                    user_attraction_base[3][a]=tags4_number.get(b);
                    break;
                }
            }
            if (b==tags4_label.size()){
                user_attraction_base[3][a]=0;
            }

            b=0;
            for(b=0;b<tags5_label.size();b++){
                if(user_attraction_label.get(a)==tags5_label.get(b)){
                    user_attraction_base[4][a]=tags5_number.get(b);
                    break;
                }
            }
            if (b==tags5_label.size()){
                user_attraction_base[4][a]=0;
            }

            b=0;
            for(b=0;b<tags6_label.size();b++){
                if(user_attraction_label.get(a)==tags6_label.get(b)){
                    user_attraction_base[5][a]=tags6_number.get(b);
                    break;
                }
            }
            if (b==tags6_label.size()){
                user_attraction_base[5][a]=0;
            }
        }

        Application application=new Application();
        key_id=application.start(user_attraction_base,user_attraction_base[0].length);
        int max_label_id=0;
        for(int i=0;i<user_attraction_label.size();i++){
            if(user_attraction_base[key_id][i]>max_label_id){
                max_label_id=user_attraction_base[key_id][i];
            }
        }
        KeyWord= user_attraction_label.get(max_label_id);


        return KeyWord;


    }



}
