package com.example.csl.mybasemvpmodel.util;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.csl.mybasemvpmodel.R;

/**
 * 作者：蔡颂亮
 * 时间：2018/11/7:10:26
 * 邮箱：
 * 说明：统一的图片加载框架
 */
public class GlideUtil {
    public static void setImage(Context context, ImageView imageView, Object imgPath){
        RequestOptions reqs = new RequestOptions();
        Log.e("图片路径",imgPath+"");
        reqs.error(R.color.white);
        Glide.with(context)
                .load(imgPath)
                .apply(reqs)
                .into(imageView);
    }
    public static void setImageRroud(Context context, ImageView imageView, Object imgPath){
        RequestOptions reqs = new RequestOptions();
        reqs.error(R.mipmap.ic_launcher);
        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .apply(reqs);
        Glide.with(context)
                .load(imgPath)
                .apply(requestOptions)
                .into(imageView);
    }
    public static void setImageNoCach(Context context, ImageView imageView, Object imgPath){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);// 不使用磁盘缓存
        Glide.with(context)
                .load(imgPath)
                .apply(requestOptions)
                .into(imageView);
    }

}
