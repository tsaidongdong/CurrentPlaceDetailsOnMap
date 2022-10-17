package com.example.currentplacedetailsonmap.camera;

import org.opencv.core.Mat;

public interface Filter {
    public abstract int apply(final Mat src, final Mat dst);
}
