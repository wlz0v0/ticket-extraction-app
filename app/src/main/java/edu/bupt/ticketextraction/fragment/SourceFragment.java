package edu.bupt.ticketextraction.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.TicketActivity;
import edu.bupt.ticketextraction.activity.WalletActivity;
import edu.bupt.ticketextraction.extraction.CabTicket;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/05
 *     desc   : 用于在WalletActivity中展示资源文件信息的Fragment
 *              TODO:先做图片的
 *     version: 0.0.1
 * </pre>
 */
public class SourceFragment extends Fragment {
    // 资源文件是图片还是视频
    private final boolean isImage;
    // 对应发票
    private final CabTicket cabTicket;
    // 父Activity
    private final WalletActivity fatherActivity;

    public SourceFragment(CabTicket cabTicket, WalletActivity fatherActivity) {
        isImage = true;
        this.cabTicket = cabTicket;
        this.fatherActivity = fatherActivity;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_source, container, false);
        // 绑定按钮
        Button sourceBtn = view.findViewById(R.id.source_button);
        sourceBtn.setOnClickListener(view1 -> jumpFromWalletToTicket());

        // 设置相关文本
        TextView distance = view.findViewById(R.id.distance);
        TextView totalPrice = view.findViewById(R.id.total_price);
        distance.setText(cabTicket.getDistance().toString());
        totalPrice.setText(cabTicket.getTotalPrice().toString());
        return view;
    }

    // 从钱包Activity跳转到票据Activity
    private void jumpFromWalletToTicket() {
        Intent intent = new Intent(fatherActivity, TicketActivity.class);
        intent.putExtra("ticket", cabTicket);
        startActivity(intent);
    }
}
