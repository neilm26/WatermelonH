package com.example.watermelonh;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.watermelonh.databinding.ActivityMainBinding;

import android.widget.ImageView;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.MemoryFormat;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.watermelonh.Constants.*;



//Main app, previous app was to test various functions.
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        try {
            //load side and front watermelon examples
            Log.d("PytorchHelloWorld", "Loading input image");

            InputStream imageStreamSide = getAssets().open("watermelon_t.jpg");
            originalBitmapSide = BitmapFactory.decodeStream(imageStreamSide);

            Log.d("Success,", "Loading second input image");
            InputStream imageStreamFront = getAssets().open("watermelon_f.jpg");
            originalBitmapFront = BitmapFactory.decodeStream(imageStreamFront);

            module = LiteModuleLoader.load(assetFilePath(this, "model.pt"));
            Log.d("Success,", "Loading model");

        } catch (IOException e) {
            Log.e("IOException", "Error reading assets", e);
            finish();
        }

        //give permissions before making temp directory
        //if permissions are not allowed, do something
        //debugging purposes
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //permissions are acting funny
            Log.e("Permissions", "permissions are not enabled");

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }
        } else {
            //permission granted, something else is fucking up the imageDir creation
        }

        try {
            imageDir = new File(getExternalFilesDir(null), "Watermelon_Images");
            //File newFile = new File(imageDir, "default_image.jpg");
            Uri contentUri = createUri(imageDir);

            Intent data = new Intent(Intent.ACTION_QUICK_VIEW);
            data.setDataAndType(contentUri, "resource/folder");

            data.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            data.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(data, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            String rootPath = imageDir.getAbsolutePath();

            Log.e("path", rootPath);

            if (!imageDir.exists()) {
                imageDir.mkdirs();
                Log.e("Directory", "to be created");
                if (imageDir.mkdirs()) {
                    Log.e("Directory", "directory created");
                } else {
                    Log.e("Directory", "failed. Directory not created");
                }
            } else if (imageDir.exists()) {
                Log.e("Directory", "directory already created");
            }

            saveFile(imageBitmap, "/watermelon_t", imageDir);
            saveFile(imageBitmapFront,"/watermelon_f",imageDir);

        } catch (SecurityException e) {
            Log.e("Directory", "No directory created", e);
            finish();
        }


        // showing image on UI
        ImageView sideOfWatermelon = (ImageView) findViewById(R.id.mouse);

        sideOfWatermelon.setImageBitmap(originalBitmapSide);

        //gthbtkn: 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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public Uri createUri(File file) {
        Uri uri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".provider",
                file);
        return uri;
    }

    private void saveFile(Bitmap imageToSave, String fileName, File parent) {

        File file = new File(parent.getAbsolutePath(), fileName);
        if (file.exists()) {
            Log.e("Image creating",file.getPath());
        }
        Log.e("Image created",file.getPath());

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String pytorchTensor(Bitmap bitmap, Module module) {
        // preparing input tensor
        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB, MemoryFormat.CHANNELS_LAST);

        // running the model
        final Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();

        // getting tensor content as java array of floats
        final float[] scores = outputTensor.getDataAsFloatArray();

        // searching for the index with maximum score
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxScoreIdx = i;
            }
        }

        return com.example.watermelonh.ImageNetClassesMine.IMAGENET_CLASSES[maxScoreIdx];
    }

    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    String currentPhotoPath;

    private File saveToFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_" + ".jpg";

        File image = new File(imageDir,imageFileName);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Bitmap saveFileFromCamera(ActivityResult result) {

        Bitmap savedBitmap = null;

        if(result.getResultCode() == RESULT_OK && result.getData() != null){
            Bundle bundle = result.getData().getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");

            try {
                File tempFile = saveToFile();

                Log.e("file path",currentPhotoPath);

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,90,outStream);

                byte[] bitmapData = outStream.toByteArray();
                FileOutputStream fileOutStream = new FileOutputStream(tempFile);

                fileOutStream.write(bitmapData);
                fileOutStream.flush();
                fileOutStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            savedBitmap = bitmap;
        }
        return savedBitmap;
    }
}