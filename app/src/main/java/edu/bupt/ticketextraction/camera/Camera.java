package edu.bupt.ticketextraction.camera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/06
 *     desc   : Camera类，可以调相机拍照录制。
 *     version: 0.0.1
 * </pre>
 */
public class Camera extends AppCompatActivity {
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    // Documents: https://developer.android.google.cn/training/camera/photobasics
    @SuppressLint("QueryPermissionsNeeded")
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 确保存在相机activity来处理此intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // 创建照片保存路径
            File photoFile = null;
            try {
                photoFile = createCameraFile(".jpg");
            } catch (IOException ex) {
                // 创建文件路径错误
                Log.e("ERROR", "Creating a file ERROR!");
            }
            // 仅当文件路径成功创建时程序继续
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "edu.bupt.ticketextraction.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    // 注意文件后缀必须是图像或视频的后缀: .jpg .... | .mp4 ....
    private File createCameraFile(String suffix) throws IOException {
        // Create an image file name

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                suffix,         /* suffix */
                storageDir      /* directory */
        );

        // 保存一个文件：ACTION_VIEW intents的用途的路径
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
