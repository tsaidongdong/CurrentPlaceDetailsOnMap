package com.example.currentplacedetailsonmap.camera.convolution;

import com.example.currentplacedetailsonmap.camera.Filter;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

public final class StrokeEdgesFilter implements Filter {
    
    private final Mat mKernel = new MatOfInt(
            0, 0,   1, 0, 0,
            0, 1,   2, 1, 0,
            1, 2, -16, 2, 1,
            0, 1,   2, 1, 0,
            0, 0,   1, 0, 0
    );
    private final Mat mEdges = new Mat();
    
    @Override
    public int apply(final Mat src, final Mat dst) {
        Imgproc.filter2D(src, mEdges, -1, mKernel);
        Core.bitwise_not(mEdges, mEdges);
        Core.multiply(src, mEdges, dst, 1.0/255.0);
        return 0;
    }
}
