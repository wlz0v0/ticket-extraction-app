package edu.bupt.ticketextraction.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.MainActivity;
import edu.bupt.ticketextraction.activity.SendToEmailActivity;

import java.util.HashMap;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/09
 *     desc   : 钱包check box fragment，用于导出fragment中
 *              邮件只发送选中的check box
 *     version:
 * </pre>
 */
public class WalletCheckBoxFragment extends Fragment {
    private final String walletName;
    private final MainActivity fatherActivity;
    // 创建此HashMap是为了通过钱包名得到对应的CheckBoxFragment
    // 目前只用于删除WalletCheckBox
    public static HashMap<String, WalletCheckBoxFragment> checkBoxFragmentHashMap;

    public WalletCheckBoxFragment(String walletName, MainActivity fatherActivity) {
        this.walletName = walletName;
        this.fatherActivity = fatherActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_in_export, container, false);
        // 绑定新建的check box
        CheckBox checkBox = view.findViewById(R.id.wallet_check_box);
        checkBox.setText(walletName);
        // 添加到checkBoxes中以供发邮件使用
        SendToEmailActivity.checkBoxes.add(checkBox);
        return view;
    }

    public void hideWalletCheckBoxFragment() {
        FragmentTransaction transaction = fatherActivity.getSupportFragmentManager().beginTransaction();
        transaction.hide(this);
        transaction.commitAllowingStateLoss();
    }
}