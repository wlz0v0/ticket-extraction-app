package edu.bupt.ticketextraction.bill.wallet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import edu.bupt.ticketextraction.utils.Ocr;
import edu.bupt.ticketextraction.utils.file.filefactory.ImageFileFactory;
import edu.bupt.ticketextraction.utils.file.filefactory.VideoFileFactory;

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
public final class WalletActivity extends AutoPushPopActivity {
    public final static String WALLET_EXTRA = "wallet";
    private static final int START_CAMERA = 1;
    private static Wallet wallet;
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
        CabTicket ticket = Ocr.extract(file, wallet.getWalletName());
        // 将获取的信息添加到钱包中以展示
        wallet.addTicket(ticket);
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
}
