package com.example.capstone_project.utils;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
public class QRCodeGenerator {
    private static QRCodeGenerator instance;
    private QRCodeGenerator(){}
    public static QRCodeGenerator getInstance(){
        if(instance == null) instance = new QRCodeGenerator();
        return instance;
    }
    public Bitmap generateQRCode(String data) throws WriterException{
        return encodeAsBitmap(data);
    }
    private Bitmap encodeAsBitmap(String data) throws WriterException{

        try {
            com.google.zxing.common.BitMatrix result = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 1024, 1024);
            int width = result.getWidth();
            int height = result.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Black for true, white for false
                }
            }
            return bitmap;
        } catch (WriterException e) {
            throw new WriterException("Error encoding QR code");
        }
    }
}
