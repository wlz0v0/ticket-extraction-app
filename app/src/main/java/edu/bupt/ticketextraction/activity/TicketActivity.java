package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.extraction.CabTicket;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/05
 *     desc   : 展示票据信息的Activity
 *     version: 0.0.1
 * </pre>
 */
public class TicketActivity extends AppCompatActivity {
    // 通过该回调函数监听返回键是否被点击
    // 被点击则结束此activity并返回wallet activity
    // 等号右侧必须是android.R.id.home
    // R.id.home会出现bug，可以运行但与getItemId()不相等
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("发票信息");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 获取对应ticket
        CabTicket ticket = (CabTicket) getIntent().getSerializableExtra("ticket");
        // TODO：根据ticket显示信息
    }
}
