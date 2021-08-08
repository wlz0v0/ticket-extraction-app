package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/18
 *     desc   : 底部菜单栏控制的fragment
 *     version: 0.0.1
 * </pre>
 */
public class BottomMenuFragment extends Fragment {
    private final String content;

    public BottomMenuFragment(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        TextView txt_content = view.findViewById(R.id.txt_content);
        txt_content.setText(content);
        return view;
    }
}
