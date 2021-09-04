package edu.bupt.ticketextraction.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.MainActivity;
import edu.bupt.ticketextraction.wallet.Wallet;
import edu.bupt.ticketextraction.wallet.WalletManager;


/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/25
 *     desc   : 账单fragment
 *     version: 0.0.1
 * </pre>
 */
public class BillFragment extends Fragment {
    private final MainActivity fatherActivity;
    public BillFragment(MainActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        Wallet defaultWallet = new Wallet("default");
        WalletManager.getInstance().addWallet(defaultWallet);

        Button defaultWalletBtn = view.findViewById(R.id.default_wallet_btn);
        defaultWalletBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToWallet());

        Button newWalletBtn = view.findViewById(R.id.new_wallet_btn);
        newWalletBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToCreateWallet());
        return view;
    }
}
