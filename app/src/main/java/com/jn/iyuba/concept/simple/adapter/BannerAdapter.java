package com.jn.iyuba.concept.simple.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Title;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyHolder> {


    private List<Title> titleList;

    private Callback callback;

    public BannerAdapter(List<Title> titleList) {
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        Title title = titleList.get(position);
        holder.setTitle(title);
        holder.setCallback(callback);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public List<Title> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<Title> titleList) {
        this.titleList = titleList;
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        ImageView banner_iv_pic;
        TextView banner_tv_title;
        private Callback callback;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            banner_iv_pic = itemView.findViewById(R.id.banner_iv_pic);
            banner_tv_title = itemView.findViewById(R.id.banner_tv_title);
        }

        public Callback getCallback() {
            return callback;
        }

        public void setCallback(Callback callback) {
            this.callback = callback;
        }

        public void setTitle(Title title) {

            banner_tv_title.setText(title.getTitleCn());

            if (title.getPic() == null || title.getPic().equals("")) {

                Glide.with(banner_iv_pic.getContext()).load(R.mipmap.logo).into(banner_iv_pic);
            } else {

                Glide.with(banner_iv_pic.getContext()).load(title.getPic()).into(banner_iv_pic);
            }
            banner_iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (callback != null) {

                        callback.getTitle(getAdapterPosition(), title);
                    }
                }
            });
        }
    }

    public interface Callback {

        void getTitle(int position, Title title);
    }
}
