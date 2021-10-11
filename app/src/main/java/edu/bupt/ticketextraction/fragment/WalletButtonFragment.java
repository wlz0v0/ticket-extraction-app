package edu.bupt.ticketextraction.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.MainActivity;
import edu.bupt.ticketextraction.file.filefactory.FileFactory;
import edu.bupt.ticketextraction.wallet.WalletManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
    private String walletName;
    private final MainActivity fatherActivity;

    public WalletButtonFragment(String walletName, MainActivity fatherActivity) {
        this.walletName = walletName;
        this.fatherActivity = fatherActivity;
    }

    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_button, container, false);
        Button walletBtn = view.findViewById(R.id.wallet_btn);
        // 设置点击监听器
        walletBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToWallet(WalletManager.getInstance().getWallet(walletName)));
        // 设置长按监听器
        walletBtn.setOnLongClickListener(this::onLongClickListenerCallback);
        walletBtn.setText(walletName);
        return view;
    }

    private boolean onLongClickListenerCallback(View v) {
        //TODO 添加重命名和删除钱包操作
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
    private void negativeButtonCallback(DialogInterface dialog, int which) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fatherActivity);
        builder.setView(R.layout.editable_dialog).
                setPositiveButton("重命名", (dialog1, which1) -> {
                    EditText editText = fatherActivity.findViewById(R.id.dialog_et);
                    //TODO: 这里会闪退
                    String newWalletName = editText.getText().toString();
                    // 新名称不能为空！
                    if (!newWalletName.isEmpty()) {
                        this.renameWallet(newWalletName);
                        // 关闭子弹窗
                        dialog1.dismiss();
                        // 关闭父弹窗
                        dialog.dismiss();
                    } else {
                        // 弹出警告并重新输入新名称
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(fatherActivity);
                        builder1.setMessage("钱包名不能为空！").
                                setPositiveButton("确认", (dialog2, which2) -> dialog2.dismiss());
                    }
                }).
                setNegativeButton("取消", (dialog1, which1) -> {
                    // 关闭子弹窗
                    dialog1.dismiss();
                    // 关闭父弹窗
                    dialog.dismiss();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 删除与钱包相关的所有东西
     */
    private void removeWallet() {
        // 删除目录
        WalletManager.getInstance().deleteWalletDirectory(walletName);

        // 删除对应Wallet实例
        WalletManager.getInstance().deleteWallet(WalletManager.getInstance().getWallet(walletName));

        // 删除页面中的WalletButtonFragment
        FragmentTransaction transaction = fatherActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commit();

        // 删除容器中的WalletButtonFragment
        MainActivity.walletButtonFragments.remove(this);

        WalletCheckBoxFragment cbFragment = WalletCheckBoxFragment.checkBoxFragmentHashMap.get(walletName);
        if (cbFragment != null) {
            // 删除页面中的对应WalletCheckBox
            cbFragment.removeWalletCheckBoxFragment();
            // 删除容器中的fragment
            MainActivity.walletCheckBoxFragments.remove(cbFragment);
            // 删除映射关系
            WalletCheckBoxFragment.checkBoxFragmentHashMap.remove(walletName);
        }
    }

    /**
     * 重命名钱包
     */
    private void renameWallet(String newWalletName) {
        File oldDir = new File(FileFactory.WALLETS_DIR + walletName + "/");
        Button walletBtn = fatherActivity.findViewById(R.id.wallet_btn);
        this.walletName = newWalletName;
        walletBtn.setText(walletName);
        WalletCheckBoxFragment checkBox = WalletCheckBoxFragment.checkBoxFragmentHashMap.get(walletName);
        if (checkBox != null) {
            checkBox.setText(walletName);
        }
        File newDir = new File(FileFactory.WALLETS_DIR + walletName + "/");
        // 忽略返回值
        //noinspection ResultOfMethodCallIgnored
        oldDir.renameTo(newDir);
    }
}
