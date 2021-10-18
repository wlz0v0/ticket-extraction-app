package edu.bupt.ticketextraction.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/18
 *     desc   : 底部菜单栏控制的fragment
 *     version: 0.0.1
 * </pre>
 */
public final class CheckFragment extends Fragment {
    private final String content;

    public CheckFragment(String content) {
        this.content = content;
    }

    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        TextView txtContent = view.findViewById(R.id.txt_content);
        txtContent.setText(content);
        return view;
    }
}
