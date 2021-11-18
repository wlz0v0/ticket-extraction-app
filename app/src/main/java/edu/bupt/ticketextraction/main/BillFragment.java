package edu.bupt.ticketextraction.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.bill.wallet.Wallet;
import edu.bupt.ticketextraction.bill.wallet.WalletManager;
import org.jetbrains.annotations.NotNull;


/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/25
 *     desc   : 账单fragment
 *     version: 0.0.1
 * </pre>
 */
public final class BillFragment extends Fragment {
    private final MainActivity fatherActivity;

    public BillFragment(MainActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        Wallet defaultWallet = new Wallet("默认");
        WalletManager.getInstance().addWallet(defaultWallet);
        WalletManager.getInstance().createWalletDirectory();

        Button defaultWalletBtn = view.findViewById(R.id.default_wallet_btn);
        defaultWalletBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToWallet(WalletManager.getInstance().getWallet("默认")));

        Button newWalletBtn = view.findViewById(R.id.new_wallet_btn);
        newWalletBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToCreateWallet());
        return view;
    }
}
