package edu.bupt.ticketextraction.bill.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.main.MainActivity;
import edu.bupt.ticketextraction.bill.wallet.RenameWalletActivity;
import edu.bupt.ticketextraction.utils.file.filefactory.FileFactory;
import edu.bupt.ticketextraction.export.WalletCheckBoxFragment;
import edu.bupt.ticketextraction.bill.wallet.WalletManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static edu.bupt.ticketextraction.export.WalletCheckBoxFragment.checkBoxFragmentHashMap;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/03
 *     desc   : 钱包fragment，在发票fragment的界面中展示钱包
 *     version: 0.0.1
 * </pre>
 */
public final class WalletButtonFragment extends Fragment {
    public final static String NEW_WALLET_NAME = "new wallet name";
    private final MainActivity fatherActivity;
    /**
     * 启动RenameWalletActivity，并获取其返回的name<br>
     * 这个Launcher必须要定义在字段中
     */
    private final ActivityResultLauncher<Intent> launcher;
    private String walletName;
    private Button walletBtn;

    public WalletButtonFragment(String walletName, MainActivity fatherActivity) {
        this.walletName = walletName;
        this.fatherActivity = fatherActivity;
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent intent = result.getData();
            // 当用户不点击按钮而是直接返回的情况下，intent为null
            if (intent != null) {
                String newWalletName = intent.getStringExtra(NEW_WALLET_NAME);
                // 新钱包名不应为空
                assert newWalletName != null;
                if (newWalletName.isEmpty()) {
                    fatherActivity.showBottomToast(fatherActivity, "钱包名不能为空！", 5);
                } else {
                    // 重命名钱包
                    this.renameWallet(newWalletName);
                    fatherActivity.showBottomToast(fatherActivity, "重命名成功！", 5);
                }
            }
        });
    }

    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_button, container, false);
        walletBtn = view.findViewById(R.id.wallet_btn);
        // 设置点击监听器
        walletBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToWallet(WalletManager.getInstance().getWallet(walletName)));
        // 设置长按监听器
        walletBtn.setOnLongClickListener(this::onLongClickListenerCallback);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        walletBtn.setText(walletName);
    }

    private boolean onLongClickListenerCallback(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fatherActivity);
        // 设置弹窗，可以重命名或删除钱包
        builder.setMessage("希望进行的操作").
                setCancelable(false).
                setPositiveButton("删除", this::positiveButtonCallback).
                setNeutralButton("取消", this::neutralButtonCallback).
                setNegativeButton("重命名", this::negativeButtonCallback);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    // 删除按钮点击回调
    private void positiveButtonCallback(DialogInterface dialog, int which) {
        // 设置确认弹窗，用户是否确认要删除钱包
        AlertDialog.Builder builder = new AlertDialog.Builder(fatherActivity);
        builder.setMessage("确认要删除此钱包吗？一旦删除，发票信息无法恢复！").
                setCancelable(false).
                setPositiveButton("确认", (dialog1, which1) -> {
                    // 删除钱包相关所有东西
                    this.removeWallet();
                    // 关闭子弹窗
                    dialog1.dismiss();
                    // 关闭父弹窗
                    dialog.dismiss();
                    fatherActivity.showBottomToast(fatherActivity, "删除成功！", 5);
                }).
                setNegativeButton("取消", (dialog1, which1) -> {
                    // 关闭子弹窗
                    dialog1.dismiss();
                    // 关闭父弹窗
                    dialog.dismiss();
                });
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }

    // 取消按钮点击回调
    private void neutralButtonCallback(@NotNull DialogInterface dialog, int which) {
        dialog.dismiss();
    }

    // 重命名按钮点击回调
    private void negativeButtonCallback(@NotNull DialogInterface dialog, int which) {
        launcher.launch(new Intent(this.fatherActivity, RenameWalletActivity.class));
        // 关闭弹窗
        dialog.dismiss();
    }

    /**
     * 删除与钱包相关的所有东西
     */
    private void removeWallet() {
        // 删除目录
        WalletManager.getInstance().deleteWalletDirectory(walletName);

        // 删除对应Wallet实例
        WalletManager.getInstance().deleteWallet(WalletManager.getInstance().getWallet(walletName));
        WalletManager.getInstance().writeWalletsToData();

        // 删除页面中的WalletButtonFragment
        FragmentTransaction transaction = fatherActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commit();

        // 删除容器中的WalletButtonFragment
        MainActivity.walletButtonFragments.remove(this);

        WalletCheckBoxFragment cbFragment = checkBoxFragmentHashMap.get(walletName);
        if (cbFragment != null) {
            // 删除页面中的对应WalletCheckBox
            cbFragment.removeWalletCheckBoxFragment();
            // 删除容器中的fragment
            MainActivity.walletCheckBoxFragments.remove(cbFragment);
            // 删除映射关系
            checkBoxFragmentHashMap.remove(walletName);
        }
    }

    /**
     * 重命名钱包
     */
    private void renameWallet(String newWalletName) {
        File oldDir = new File(FileFactory.WALLETS_DIR + walletName + "/");
        // 修改钱包名
        WalletManager.getInstance().setWalletName(walletName, newWalletName);
        // 修改WalletCheckBoxFragment显示的钱包名
        WalletCheckBoxFragment checkBox = checkBoxFragmentHashMap.get(walletName);
        // 移除旧钱包的键值对
        checkBoxFragmentHashMap.remove(walletName);
        // 添加新钱包的键值对
        checkBoxFragmentHashMap.put(newWalletName, checkBox);
        this.walletName = newWalletName;
        if (checkBox != null) {
            checkBox.setWalletName(walletName);
        }
        // 修改WalletButtonFragment显示的钱包名
        walletBtn.setText(walletName);
        // 修改目录名
        File newDir = new File(FileFactory.WALLETS_DIR + walletName + "/");
        // 忽略返回值
        // noinspection ResultOfMethodCallIgnored
        oldDir.renameTo(newDir);
        // 提示信息
        fatherActivity.showBottomToast(fatherActivity, "重命名成功", 5);
    }
}
