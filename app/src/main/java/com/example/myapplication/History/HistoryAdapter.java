package com.example.myapplication.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.ReportData;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<ReportData> reportList;
    private Context context;


    public HistoryAdapter(List<ReportData> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportData reportData = reportList.get(position);

        // Set the report data to the view holder
        holder.tvTitle.setText(reportData.getTitle());
        holder.tvNama.setText(reportData.getNama());
        holder.tvLokasi.setText(reportData.getLokasi());
        holder.tvTelepon.setText(reportData.getTelepon());
        holder.tvTanggal.setText(reportData.getTanggal());
        holder.tvLaporan.setText(reportData.getLaporan());

        // Load the image from Firebase Storage
        Glide.with(holder.itemView.getContext())
                .load(reportData.getImage())
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvNama, tvLokasi, tvTanggal, tvLaporan, tvTelepon;
        ImageView ivImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvLokasi = itemView.findViewById(R.id.tvLokasi);
            tvTelepon = itemView.findViewById(R.id.tvTelepon);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvLaporan = itemView.findViewById(R.id.tvLaporan);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}

