package edu.bupt.ticketextraction.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.extraction.CabTicket;
import edu.bupt.ticketextraction.extraction.Ocr;
import edu.bupt.ticketextraction.file.filefactory.ImageFileFactory;
import edu.bupt.ticketextraction.file.filefactory.VideoFileFactory;
import edu.bupt.ticketextraction.wallet.Wallet;
import edu.bupt.ticketextraction.wallet.WalletManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 展示钱包信息activity
 *     version: 0.0.1
 * </pre>
 */
public class WalletActivity extends AppCompatActivity {
    private static Wallet curWallet;

    private static final int START_CAMERA = 1;

    public static void setWallet(Wallet Wallet) {
        WalletActivity.curWallet = Wallet;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(curWallet.getWalletName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button shootBtn = findViewById(R.id.camera_shoot_btn);
        shootBtn.setOnClickListener(this::shootBtnOnClickCallback);

        Button recordBtn = findViewById(R.id.camera_record_btn);
        recordBtn.setOnClickListener(this::recordBtnOnClickCallback);

        showResources();
    }

    private void shootBtnOnClickCallback(View view) {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, START_CAMERA);
        } else {
            startShootCamera();
        }
    }

    private void recordBtnOnClickCallback(View view) {
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
            File imageFile = new ImageFileFactory(curWallet.getWalletName()).createFile();
            if (imageFile != null) {
                Uri uri = FileProvider.getUriForFile(this,
                        "edu.bupt.ticketextraction.FileProvider",
                        imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // startActivityForResult已被弃用，但仍然选择使用它XD
                // noinspection deprecation
                startActivityForResult(intent, START_CAMERA);
                // 调用OCR识别
                this.extractTicket(imageFile);
            }
        }
    }

    /**
     * 调用相机录视频
     */
    private void startVideoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File videoFile = new VideoFileFactory(curWallet.getWalletName()).createFile();
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
        CabTicket ticket = Ocr.getInstance().callOcr(file, curWallet.getWalletName());
        curWallet.addTicket(ticket);
    }

    // 在钱包中展示资源文件
    private void showResources() {
        //TODO: 通过curWallet的属性展示资源
    }
}
