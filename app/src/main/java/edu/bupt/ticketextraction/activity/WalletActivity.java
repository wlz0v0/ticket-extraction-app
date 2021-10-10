package edu.bupt.ticketextraction.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.extraction.Ocr;
import edu.bupt.ticketextraction.file.filefactory.ImageFileFactory;
import edu.bupt.ticketextraction.file.filefactory.VideoFileFactory;
import edu.bupt.ticketextraction.fragment.SourceFragment;
import edu.bupt.ticketextraction.tickets.CabTicket;
import edu.bupt.ticketextraction.wallet.Wallet;
import edu.bupt.ticketextraction.wallet.WalletManager;

import java.io.File;
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
public class WalletActivity extends AutoPushPopActivity {
    private Wallet wallet;
    private final ArrayList<SourceFragment> sourceFragments = new ArrayList<>();
    private static final int START_CAMERA = 1;
    public final static String WALLET_EXTRA = "wallet";

    public void removeCabTicket(CabTicket ticket) {
        wallet.removeTicket(ticket);
    }

    public void removeSourceFragment(SourceFragment sourceFragment) {
        sourceFragments.remove(sourceFragment);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        wallet = (Wallet) getIntent().getSerializableExtra(WALLET_EXTRA);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(wallet.getWalletName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button shootBtn = findViewById(R.id.camera_shoot_btn);
        shootBtn.setOnClickListener(this::shootBtnOnClickCallback);

        Button recordBtn = findViewById(R.id.camera_record_btn);
        recordBtn.setOnClickListener(this::recordBtnOnClickCallback);
    }

    // onStart时读取资源数据并展示所有资源
    @Override
    protected void onStart() {
        super.onStart();
        WalletManager.getInstance().readWalletSourceFromData(wallet);
        showSources();
    }

    // onStop时将数据写入文件
    @Override
    protected void onStop() {
        super.onStop();
        WalletManager.getInstance().writeWalletSourceToData(wallet);
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
            File imageFile = new ImageFileFactory(wallet.getWalletName()).createFile();
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
        CabTicket ticket = Ocr.getInstance().callOcr(file, wallet.getWalletName());
        // 识别完直接删除
        //noinspection ResultOfMethodCallIgnored
        file.delete();
        // 将获取的信息添加到钱包中以展示
        wallet.addTicket(ticket);
    }

    // 在钱包中展示资源文件
    // TODO：定一下最多存放几张图片
    private void showSources() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 需要先删除所有已添加的Fragment，再重新添加，不然会重复
        Iterator<SourceFragment> iter = sourceFragments.iterator();
        while (iter.hasNext()) {
            transaction.remove(iter.next());
            iter.remove();
        }
        for (CabTicket ticket : wallet.getTickets()) {
            // 将SourceFragment添加到WalletActivity中
            SourceFragment fragment = new SourceFragment(ticket, this);
            transaction.add(R.id.sources_fragment_container, fragment);
            sourceFragments.add(fragment);
        }
        transaction.commitAllowingStateLoss();
    }
}
