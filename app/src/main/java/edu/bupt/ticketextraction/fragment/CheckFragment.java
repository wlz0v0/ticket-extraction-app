package edu.bupt.ticketextraction.fragment;

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
public class CheckFragment extends Fragment {
    private final String content;

    public CheckFragment(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        TextView txtContent = view.findViewById(R.id.txt_content);
        txtContent.setText(content);
        return view;
    }
}
