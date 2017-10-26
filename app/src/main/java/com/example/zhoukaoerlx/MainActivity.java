package com.example.zhoukaoerlx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String url = "http://tingapi.ting.baidu.com/v1/restserver/ting";
    private ImageView mIvTop;
    /**
     * haha
     */
    private TextView mTvTitle;
    /**
     * haha
     */
    private TextView mTvTime;
    /**
     * haha
     */
    private TextView mTvComment;
    private RecyclerView mRv;
    private List<ItemBean.SongListBean> list=new ArrayList<>();
    private MyAdapter myAdapter;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader();
        initView();
        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        OkHttpUtils.getInstance().doGet(url, params, ItemBean.class, new OnNetListener<ItemBean>() {
            @Override
            public void onSuccess(ItemBean itemBean) {
                myAdapter.refreshData(itemBean.getSong_list());
                ImageLoader.getInstance().displayImage(itemBean.getBillboard().getPic_s192(),mIvTop,options);
                mTvComment.setText(itemBean.getBillboard().getComment());
                mTvTitle.setText(itemBean.getBillboard().getName());
                mTvTime.setText(itemBean.getBillboard().getUpdate_date());
            }
        });
    }

    private void initImageLoader() {
        // 设置图片下载期间显示的图片
// 设置图片Uri为空或是错误的时候显示的图片
// 设置图片加载或解码过程中发生错误显示的图片
// 设置下载的图片是否缓存在内存中
// 设置下载的图片是否缓存在SD卡中
// 设置成圆角图片
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();
    }

    private void initView() {
        mIvTop = (ImageView) findViewById(R.id.iv_top);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvComment = (TextView) findViewById(R.id.tv_comment);
        mRv = (RecyclerView) findViewById(R.id.rv);
        //设置布局管理器

        mRv.setLayoutManager(new LinearLayoutManager(this));
      //  mRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        //设置适配器
        myAdapter = new MyAdapter(this,list);
        mRv.setAdapter(myAdapter);
//加载更多
        mRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if(newState==0&&lastVisibleItemPosition==(linearLayoutManager.getChildCount()-1)){
                    Map<String, String> params = new HashMap<>();
                    params.put("type", "2");
                    OkHttpUtils.getInstance().doGet(url, params, ItemBean.class, new OnNetListener<ItemBean>() {
                        @Override
                        public void onSuccess(ItemBean itemBean) {
                            List<ItemBean.SongListBean> moreList=itemBean.getSong_list();
                            myAdapter.loadMore(moreList);
                            ImageLoader.getInstance().displayImage(itemBean.getBillboard().getPic_s192(),mIvTop,options);
                            mTvComment.setText(itemBean.getBillboard().getComment());
                            mTvTitle.setText(itemBean.getBillboard().getName());
                            mTvTime.setText(itemBean.getBillboard().getUpdate_date());
                        }
                    });
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

}
