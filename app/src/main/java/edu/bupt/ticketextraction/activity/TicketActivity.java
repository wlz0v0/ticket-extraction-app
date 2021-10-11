package edu.bupt.ticketextraction.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.tickets.CabTicket;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/05
 *     desc   : 展示票据信息的Activity
 *     version: 0.0.1
 * </pre>
 */
public final class TicketActivity extends AutoPushPopActivity {
    public final static String TICKET_EXTRA = "ticket";

    @SuppressLint("SetTextI18n")
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
        CabTicket ticket = (CabTicket) getIntent().getSerializableExtra(TICKET_EXTRA);
        // 绑定所有TextView
        // 断言ticket非空
        assert ticket != null;
        TextView unitPrice = findViewById(R.id.unit_price_textview);
        TextView distance = findViewById(R.id.distance_textview);
        TextView totalPrice = findViewById(R.id.total_price_textview);
        TextView date = findViewById(R.id.date_textview);
        unitPrice.setText(ticket.getUnitPrice().toString());
        distance.setText(ticket.getDistance().toString());
        totalPrice.setText(ticket.getTotalPrice().toString());
        date.setText(ticket.getDate());
    }
}
