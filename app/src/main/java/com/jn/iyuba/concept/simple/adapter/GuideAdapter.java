package com.jn.iyuba.concept.simple.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jn.iyuba.succinct.R;

import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideHolder> {


    private List<Integer> resList;


    public GuideAdapter(List<Integer> resList) {
        this.resList = resList;
    }

    @NonNull
    @Override
    public GuideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide, parent, false);
        return new GuideHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideHolder holder, int position) {

        holder.guide_iv_pic.setImageResource(resList.get(position).intValue());
    }

    @Override
    public int getItemCount() {
        return resList.size();
    }

    class GuideHolder extends RecyclerView.ViewHolder {

        ImageView guide_iv_pic;

        public GuideHolder(@NonNull View itemView) {
            super(itemView);

            guide_iv_pic = itemView.findViewById(R.id.guide_iv_pic);
        }
    }
}
