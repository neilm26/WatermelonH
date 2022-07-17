package com.example.watermelonh;

import android.graphics.Bitmap;

import org.pytorch.Module;

import java.io.File;

public class Constants {

    public static Bitmap originalBitmapSide;
    public static Bitmap originalBitmapFront;

    public static String result;
    public static String resultFront;
    public static int frontQuality;
    public static int sideQuality;
    public static String notWatermelon = "Not a watermelon, please try again";
    public static Bitmap imageBitmap; //gotta rename it
    public static Bitmap imageBitmapFront;
    public static File imageDir;
    public static Module module;
    public static Module watermelonModule;
}
