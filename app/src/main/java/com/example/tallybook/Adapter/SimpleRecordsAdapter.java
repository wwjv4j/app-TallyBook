package com.example.tallybook.Adapter;

import java.util.List;

import org.w3c.dom.Text;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.os.Looper;
import androidx.appcompat.app.AlertDialog;
import com.example.tallybook.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;

import com.example.tallybook.RecordDedailActivity;
import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.MainApplication;

public class SimpleRecordsAdapter extends RecyclerView.Adapter<ViewHolder>{
    private Context mContext;
    private List<BillingRecord> mRecords;
    public SimpleRecordsAdapter(Context context, List<BillingRecord> records) {
        mContext = context;
        mRecords = records;
    }
    // 获取记录的数量
    @Override
    public int getItemCount() {
        return mRecords.size();
    }
    // 创建记录的视图持有者
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_simple_billing_history, parent, false);
        return new ItemHolder(view);
    }

    // 绑定记录的视图持有者
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        BillingRecord record = mRecords.get(position);
        itemHolder.tvCategory.setText(record.getCategory());
        itemHolder.tvAmount.setText(String.valueOf(record.getAmount()));
        itemHolder.tvRemark.setText(record.getRemark());
        itemHolder.ivIcon.setImageResource(R.drawable.icon_circle);

        itemHolder.itemView.setOnClickListener(v -> {
            itemHolder.itemView.setSelected(true);
            Bundle bundle = new Bundle();
            bundle.putSerializable("record", record);
            Intent intent = new Intent(mContext, RecordDedailActivity.class);
            intent.putExtras(bundle);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                itemHolder.itemView.setSelected(false);
                mContext.startActivity(intent);
            }, 100); // 延时 100 毫秒
        });
        itemHolder.itemView.setOnLongClickListener(v->{
            itemHolder.itemView.setSelected(true);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                itemHolder.itemView.setSelected(false);
                showPopupMenu(record);
            }, 100); // 延时 100 毫秒
            return true;
        });
    }

    private void showPopupMenu(BillingRecord record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("选择操作")
                .setItems(new String[]{"删除", "其他操作"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            MainApplication.getInstance().getRecordDB().getBillingRecordDao().deleteRecord(record);
                            Intent intent = new Intent(mContext, mContext.getClass());
                            ((Activity) mContext).finish();
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            // 其他操作
                            Toast.makeText(mContext, "其他操作", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
        builder.create().show();
    }
    // 定义简单记录的持有者
    public class ItemHolder extends ViewHolder {
        public TextView tvCategory;
        public TextView tvAmount;
        public TextView tvRemark;
        public ImageView ivIcon;
        public View itemView;

        public ItemHolder(View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.simple_billing_history_category);
            tvAmount = itemView.findViewById(R.id.simple_billing_history_amount);
            tvRemark = itemView.findViewById(R.id.simple_billing_history_remark);
            ivIcon = itemView.findViewById(R.id.simple_billing_history_icon);
            this.itemView = itemView;
        }
    }
}

