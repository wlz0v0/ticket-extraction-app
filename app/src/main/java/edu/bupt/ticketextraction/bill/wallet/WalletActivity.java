package edu.bupt.ticketextraction.bill.wallet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.bill.tickets.CabTicket;
import edu.bupt.ticketextraction.bill.tickets.TicketFragment;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;
import edu.bupt.ticketextraction.utils.HttpUtils;
import edu.bupt.ticketextraction.utils.file.filefactory.ImageFileFactory;
import edu.bupt.ticketextraction.utils.file.filefactory.VideoFileFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 展示钱包信息activity
 *     version: 0.0.1
 * </pre>
 */
public final class WalletActivity extends AutoPushPopActivity {
    private static final int START_CAMERA = 123456;
    public static Wallet wallet;
    private static File curFile;
    private final ArrayList<TicketFragment> sourceFragments = new ArrayList<>();

    public static void setWallet(Wallet wallet) {
        WalletActivity.wallet = wallet;
    }

    public void removeCabTicket(CabTicket ticket) {
        wallet.removeTicket(ticket);
    }

    public void removeSourceFragment(TicketFragment sourceFragment) {
        sourceFragments.remove(sourceFragment);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        // 设置ActionBar
        assert wallet != null;
        super.setActionBar(this, wallet.getWalletName());

        Button shootBtn = findViewById(R.id.camera_shoot_btn);
        shootBtn.setOnClickListener(view -> shootBtnOnClickCallback());

        Button recordBtn = findViewById(R.id.camera_record_btn);
        recordBtn.setOnClickListener(view -> recordBtnOnClickCallback());
    }

    // onResume时读取资源数据并展示所有资源
    @Override
    protected void onResume() {
        super.onResume();
        WalletManager.getInstance().readWalletSourceFromData(wallet);
        showSources();
    }

    // onPause时将数据写入文件
    @Override
    protected void onPause() {
        super.onPause();
        WalletManager.getInstance().writeWalletSourceToData(wallet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == START_CAMERA) {
            if (resultCode == RESULT_OK) {
                extractTicket(curFile);
            } else if (resultCode == RESULT_CANCELED) {
                //noinspection ResultOfMethodCallIgnored
                curFile.delete();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void shootBtnOnClickCallback() {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, START_CAMERA);
        } else {
            startShootCamera();
        }
    }

    private void recordBtnOnClickCallback() {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, START_CAMERA);
        } else {
            startVideoCamera();
        }
    }

    /**
     * 调用相机拍照
     */
    private void startShootCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File imageFile = new ImageFileFactory(wallet.getWalletName()).createFile();
            if (imageFile != null) {
                Uri uri = FileProvider.getUriForFile(this,
                        "edu.bupt.ticketextraction.FileProvider",
                        imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                curFile = imageFile;
                // startActivityForResult已被弃用，但仍然选择使用它XD
                // noinspection deprecation
                startActivityForResult(intent, START_CAMERA);
            }
        }
    }

    /**
     * 调用相机录视频
     */
    private void startVideoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File videoFile = new VideoFileFactory(wallet.getWalletName()).createFile();
            if (videoFile != null) {
                Uri uri = FileProvider.getUriForFile(this,
                        "edu.bupt.ticketextraction.FileProvider",
                        videoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // startActivityForResult已被弃用，但仍然选择使用它XD
                // noinspection deprecation
                startActivityForResult(intent, START_CAMERA);
            }
        }
    }

    private void extractTicket(File file) {
        bitmapCompress(file);
        CabTicket ticket = HttpUtils.callExtract(this, file, wallet.getWalletName());
        // 将获取的信息添加到钱包中以展示
        wallet.addTicket(ticket);
        WalletManager.getInstance().writeWalletSourceToData(wallet);
        showSources();
    }

    // 在钱包中展示资源文件
    private void showSources() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 需要先删除所有已添加的Fragment，再重新添加，不然会重复
        Iterator<TicketFragment> iter = sourceFragments.iterator();
        while (iter.hasNext()) {
            transaction.remove(iter.next());
            iter.remove();
        }
        for (CabTicket ticket : wallet.getTickets()) {
            // 将SourceFragment添加到WalletActivity中
            TicketFragment fragment = new TicketFragment(ticket, this);
            transaction.add(R.id.sources_fragment_container, fragment);
            sourceFragments.add(fragment);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 压缩图片，质量压缩使之大小小于2M，因为Base64编码后的数据会变大<br>
     * 尺寸压缩使之像素小于4096像素
     *
     * @param file 图片文件
     */
    private void bitmapCompress(File file) {
        Bitmap photoBitMap = null;
        // 用文件获取uri
        Uri uri = Uri.fromFile(file);
        try {
            //用uri获取Bitmap
            //noinspection deprecation
            photoBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert photoBitMap != null;
        // 压缩质量
        int quality = 100;
        int maxFileSize = 2000; // 2MB
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 先质量压缩
        // 图片大小大于2M就循环压缩直到小于2M
        do {
            byteArrayOutputStream.reset();
            // 压缩图片
            photoBitMap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                // 覆盖之前的图片
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            quality -= 10;
        } while (byteArrayOutputStream.toByteArray().length / 1024 > maxFileSize && quality > 0);
        Log.e("image size", String.valueOf(byteArrayOutputStream.toByteArray().length / 1024));

        // 再尺寸压缩
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只读长度进内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inJustDecodeBounds = false;
        int width = options.outWidth;
        int height = options.outHeight;
        int maxWidth = 2048;
        int maxHeight = 2048;
        int scale = 1; // 1表示不缩放
        if (width > height && width > maxWidth) {
            scale = width / maxWidth;
        } else if (width <= height && height > maxHeight) {
            scale = height / maxHeight;
        }
        options.inSampleSize = scale;
        // 根据options进行尺寸缩放
        photoBitMap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        Log.e("image scale", String.valueOf(scale));
        byteArrayOutputStream.reset();
        photoBitMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            // 覆盖之前的图片
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
