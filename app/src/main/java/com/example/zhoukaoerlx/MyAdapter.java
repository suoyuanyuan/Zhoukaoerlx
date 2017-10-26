package com.example.zhoukaoerlx;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by 索园 on 2017/10/16.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<ItemBean.SongListBean> list;
    private DisplayImageOptions options;
    public MyAdapter(Context context, List<ItemBean.SongListBean> list) {
        this.context = context;
        this.list = list;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemBean.SongListBean songListBean = list.get(position);
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        ImageLoader.getInstance().displayImage(songListBean.getPic_small(),myViewHolder.iv_left,options);
        myViewHolder.tv_songname.setText(songListBean.getTitle());
        myViewHolder.tv_songzj.setText(songListBean.getAlbum_title());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_left;
        private TextView tv_songname;
        private TextView tv_songzj;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            tv_songname = (TextView) itemView.findViewById(R.id.tv_songname);
            tv_songzj = (TextView) itemView.findViewById(R.id.tv_songzj);
        }
    }
    //加载更多
    public void loadMore(List<ItemBean.SongListBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void refreshData(List<ItemBean.SongListBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
