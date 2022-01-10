package edu.bupt.ticketextraction.bill.tickets;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.bill.wallet.WalletActivity;
import edu.bupt.ticketextraction.utils.file.filefactory.FileFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
public final class TicketFragment extends Fragment {
    // 资源文件是图片还是视频
    // private final boolean isImage;
    // 对应发票
    private final CabTicket cabTicket;
    // 父Activity
    private final WalletActivity fatherActivity;

    public TicketFragment(CabTicket cabTicket, WalletActivity fatherActivity) {
        // isImage = true;
        this.cabTicket = cabTicket;
        this.fatherActivity = fatherActivity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_source, container, false);
        // 设置包含文本框的RelativeLayout为可点击
        // 把这个东西当做按钮
        RelativeLayout sourceBtn = view.findViewById(R.id.source_button);
        sourceBtn.setClickable(true);
        sourceBtn.setLongClickable(true);
        // 点击跳转到发票具体信息
        sourceBtn.setOnClickListener(view1 -> jumpFromWalletToTicket());
        // 长按可删除发票或者发票验真
        sourceBtn.setOnLongClickListener(this::onLongClickListenerCallback);

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
        intent.putExtra(TicketActivity.TICKET_EXTRA, cabTicket);
        startActivity(intent);
    }

    private boolean onLongClickListenerCallback(View v) {
        // 设置弹窗，可以重命名或删除钱包
        // 菜单弹窗，提供多个选项
        String[] items = {"发票验真", "删除发票"};
        QMUIDialog.CheckableDialogBuilder builder =
                new QMUIDialog.CheckableDialogBuilder(fatherActivity);
        builder.setTitle("发票管理").
                setCheckedIndex(0).
                addItems(items, (dialog, which) -> {
                    // 设置被点击的选项
                    builder.setCheckedIndex(which);
                }).
                addAction("取消", (dialog, index) -> dialog.dismiss()).
                addAction("确定", ((dialog, index) -> {
                    switch (index) {
                        case 0:
                            // 验真
                            checkTicketValid(dialog);
                            break;
                        case 1:
                            // 删除
                            deleteTicket(dialog);
                            break;
                        default:
                            throw new AssertionError();
                    }
                })).
                show();
        return true;
    }

    // 删除发票
    private void deleteTicket(DialogInterface dialog) {
        // 设置确认弹窗，用户是否确认要删除该资源
        new QMUIDialog.MessageDialogBuilder(fatherActivity).
                setMessage("确认要删除此发票吗？一旦删除，信息无法恢复！").
                setCancelable(false).
                addAction("取消", (dialog1, which1) -> {
                    // 关闭子弹窗
                    dialog1.dismiss();
                    // 关闭父弹窗
                    dialog.dismiss();
                }).
                addAction("确认", (dialog1, which1) -> {
                    // 删除该资源相关所有东西
                    removeSource();
                    // 关闭子弹窗
                    dialog1.dismiss();
                    // 关闭父弹窗
                    dialog.dismiss();
                }).
                show();
    }

    // 发票验真
    private void checkTicketValid(@NotNull DialogInterface dialog) {
        dialog.dismiss();
        // TODO: 2022/1/10 验真
    }

    /**
     * 删除该资源，包括对应的Fragment、钱包中的信息
     */
    private void removeSource() {
        // 删除页面中的TicketFragment
        FragmentTransaction transaction = fatherActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commit();

        // 删除容器中的Fragment
        fatherActivity.removeSourceFragment(this);

        // 删除钱包中的信息
        fatherActivity.removeCabTicket(cabTicket);

        // 删除对应图片
        File file = new File(FileFactory.WALLETS_DIR + WalletActivity.wallet.getWalletName() + "/" + cabTicket.SOURCE_NAME);
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }
}
