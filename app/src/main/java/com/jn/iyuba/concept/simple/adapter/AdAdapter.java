package com.jn.iyuba.concept.simple.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeView;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.succinct.R;

import java.util.ArrayList;
import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HomeAdapter homeAdapter;

    private List<NativeAd> nativeAdList;

    private List<Object> objectList;

    private List<Title> titleList;


    public AdAdapter(HomeAdapter homeAdapter, List<NativeAd> nativeAdList) {
        this.homeAdapter = homeAdapter;

        objectList = new ArrayList<>();
        this.titleList = homeAdapter.getData();
        this.nativeAdList = nativeAdList;
        objectList.addAll(titleList);

        if (!nativeAdList.isEmpty()) {

            objectList.add(2, nativeAdList.get(0));
        }
    }


    @Override
    public int getItemViewType(int position) {


        Object object = objectList.get(position);
        if (object instanceof NativeAd) {//广告
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
            return new AdViewHolder(view);
        } else {

            return homeAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Object object = objectList.get(position);
        if (object instanceof NativeAd nativeAd) {

            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.setData(nativeAd, position);
        } else {

            int adCount = 0;
            for (int i = 0; i < position; i++) {

                Object o = objectList.get(i);
                if (o instanceof NativeAd) {

                    adCount++;
                }
            }
            homeAdapter.onBindViewHolder((HomeAdapter.TitleViewHolder) holder, position - adCount);
//            homeAdapter.onBindViewHolder((BaseViewHolder) holder, position - adCount);
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }


    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {


        ImageView home_iv_img;
        TextView home_tv_num;
        NativeAd nativeAd;

        NativeView nativeView;

        MediaView mediaView;

        TextView ad_title;

        TextView ad_call_to_action;

        TextView ad_source;

        ImageView ad_tv_close;

        int pos;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            home_iv_img = itemView.findViewById(R.id.home_iv_img);
            home_tv_num = itemView.findViewById(R.id.home_tv_num);
            nativeView = itemView.findViewById(R.id.native_small_view);
            mediaView = itemView.findViewById(R.id.ad_media);
            ad_title = itemView.findViewById(R.id.ad_title);
            ad_call_to_action = itemView.findViewById(R.id.ad_call_to_action);
            ad_source = itemView.findViewById(R.id.ad_source);

            ad_tv_close = itemView.findViewById(R.id.ad_tv_close);

            ad_tv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AdAdapter adAdapter = (AdAdapter) getBindingAdapter();
                    if (adAdapter != null) {

                        List<Object> objects = adAdapter.getObjectList();
                        for (Object obj : objects) {

                            if (obj instanceof NativeAd) {

                                objects.remove(obj);
                                adAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }
            });
        }

        private void setData(NativeAd nativeAd, int pos) {

            this.nativeAd = nativeAd;
            this.pos = pos;

            nativeView.setMediaView(mediaView);
            // 注册和填充标题素材视图
            ad_title.setText(nativeAd.getTitle());
            // 注册和填充多媒体素材视图
            nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());

            ad_call_to_action.setText(nativeAd.getCallToAction());
            // 注册和填充其他素材视图
            // 注册原生广告对象
            nativeView.setNativeAd(nativeAd);

            ad_source.setText(nativeAd.getAdSource());

            Log.d("ADAPTER", nativeAd.getCallToAction());
        }
    }
}
