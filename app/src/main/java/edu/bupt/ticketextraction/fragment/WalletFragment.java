package edu.bupt.ticketextraction.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.MainActivity;
import edu.bupt.ticketextraction.activity.WalletActivity;
import edu.bupt.ticketextraction.wallet.WalletManager;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/03
 *     desc   : 钱包fragment，在发票fragment的界面中展示钱包
 *     version: 0.0.1
 * </pre>
 */
public class WalletFragment extends Fragment {
    private final String walletName;
    private final MainActivity fatherActivity;

    public WalletFragment(String walletName, MainActivity fatherActivity) {
        this.walletName = walletName;
        this.fatherActivity = fatherActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        Button walletBtn = view.findViewById(R.id.wallet_btn);
        // 设置点击监听器
        walletBtn.setOnClickListener(view1 -> {
            WalletActivity.setWallet(WalletManager.getInstance().getWallet(walletName));
            fatherActivity.jumpFromMainToWallet();
        });
        // 设置长按监听器
        walletBtn.setOnLongClickListener(this::onLongClickListenerCallback);
        walletBtn.setText(walletName);
        return view;
    }

    private boolean onLongClickListenerCallback(View v) {
        //TODO 添加重命名和删除钱包操作
        return true;
    }
}
