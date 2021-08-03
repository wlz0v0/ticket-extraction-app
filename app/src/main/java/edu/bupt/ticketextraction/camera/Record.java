package edu.bupt.ticketextraction.camera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;

import static androidx.core.app.ActivityCompat.startActivityForResult;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/03
 *     desc   :
 *     version:
 * </pre>
 */
public class Record {
    static final int REQUEST_VIDEO_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


}
