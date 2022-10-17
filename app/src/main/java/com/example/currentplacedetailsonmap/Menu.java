package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.currentplacedetailsonmap.camera.CameraActivity;

public class Menu extends AppCompatActivity {


    private String[] tags1;
    private String[] tags2;
    private String[] tags3;
    private String[] tags4;
    private String[] tags5;
    private String[] tags6;
    private Button put;
    private String KeyWord;

    private Button btn_to_map;
    private Button btn_to_camera;
    private Button btn_exit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Bundle bundle=getIntent().getExtras();

        tags1=bundle.getStringArray("tags1");

        tags2=bundle.getStringArray("tags2");

        tags3=bundle.getStringArray("tags3");

        tags4=bundle.getStringArray("tags4");

        tags5=bundle.getStringArray("tags5");

        tags6=bundle.getStringArray("tags6");

        //put=(Button)findViewById(R.id.button);
        btn_to_map=(Button)findViewById(R.id.btn_to_map);
        btn_to_camera=(Button)findViewById(R.id.btn_to_drive);
        btn_exit=(Button)findViewById(R.id.button_to_exit2) ;

        /*Like_analyze like_analyze=new Like_analyze();
        KeyWord=like_analyze.classification(tags1,tags2,tags3,tags4,tags5,tags6);

        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(KeyWord);
            }
        });*/
        btn_to_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Menu.this, MapsActivity.class);
                Bundle bundle = new Bundle();

                bundle.putStringArray("tags1", tags1);
                bundle.putStringArray("tags2", tags2);
                bundle.putStringArray("tags3", tags3);
                bundle.putStringArray("tags4", tags4);
                bundle.putStringArray("tags5", tags5);
                bundle.putStringArray("tags6", tags6);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_to_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Menu.this, CameraActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });






    }


}
