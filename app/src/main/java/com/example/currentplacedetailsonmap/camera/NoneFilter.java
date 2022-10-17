package com.example.currentplacedetailsonmap.camera;

import org.opencv.core.Mat;

public class NoneFilter implements Filter {

    @Override
    public int apply(Mat src, Mat dst) {
        return 0;
    }
}
