package com.parting_soul.support.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.parting_soul.base.R;

import java.util.Random;

/**
 * @author parting_soul
 * @date 2019/4/23
 */
public class ImageLoader {
    private static final int[] RES = {R.color.default_green, R.color.default_pink
            , R.color.default_purple, R.color.default_red};
    private static final Random RANDOM = new Random();

    private static final RequestOptions REQUEST_OPTIONS = new RequestOptions()
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    @SuppressLint("CheckResult")
    public static void load(Activity activity, String url, ImageView imageView) {
        int randomRes = getRandomRes();
        REQUEST_OPTIONS.placeholder(randomRes)
                .error(randomRes);

        Glide.with(activity)
                .load(url)
                .apply(REQUEST_OPTIONS)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void load(Activity activity, String url, ImageView imageView, RequestListener<Bitmap> listeners) {
        int randomRes = getRandomRes();
        REQUEST_OPTIONS.placeholder(randomRes)
                .error(randomRes);

        Glide.with(activity)
                .asBitmap()
                .load(url)
                .listener(listeners)
                .apply(REQUEST_OPTIONS)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void loadWithNoCache(Activity activity, String url, ImageView imageView, RequestListener<Bitmap> listeners) {
        RequestOptions requestOptions = new RequestOptions()
                .dontAnimate()
                .skipMemoryCache(true);

        Glide.with(activity)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .listener(listeners)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void load(Fragment fragment, String url, ImageView imageView, RequestListener<Bitmap> listeners) {
        int randomRes = getRandomRes();
        REQUEST_OPTIONS.placeholder(randomRes)
                .error(randomRes);

        Glide.with(fragment)
                .asBitmap()
                .load(url)
                .listener(listeners)
                .apply(REQUEST_OPTIONS)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void load(Fragment fragment, String url, ImageView imageView) {
        int randomRes = getRandomRes();
        REQUEST_OPTIONS.placeholder(randomRes)
                .error(randomRes);

        Glide.with(fragment)
                .load(url)
                .apply(REQUEST_OPTIONS)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void loadWithSize(Fragment fragment, String url, ImageView imageView, int width, int height) {
        int randomRes = getRandomRes();
        REQUEST_OPTIONS.placeholder(randomRes)
                .error(randomRes);

        Glide.with(fragment)
                .load(url)
                .override(width, height)
                .apply(REQUEST_OPTIONS)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void load(Context context, String url, ImageView imageView) {
        int randomRes = getRandomRes();
        REQUEST_OPTIONS.placeholder(randomRes)
                .error(randomRes);
        Glide.with(context)
                .load(url)
                .apply(REQUEST_OPTIONS)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void load(Context context, Object url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(REQUEST_OPTIONS)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void loadWithNoPlaceHolder(Activity activity, String url, ImageView imageView) {
        loadWithPlaceHolder(activity, url, -1, -1, imageView);
    }

    @SuppressLint("CheckResult")
    public static void loadWithNoPlaceHolder(Fragment fragment, Object url, ImageView imageView) {
        loadWithPlaceHolder(fragment, url, -1, -1, imageView);
    }

    @SuppressLint("CheckResult")
    public static void loadWithPlaceHolder(Fragment fragment, Object url, @DrawableRes int placeHolder, @DrawableRes int errorImg, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        if (placeHolder != -1) {
            options.placeholder(placeHolder);
        }

        if (errorImg != -1) {
            options.error(errorImg);
        }

        Glide.with(fragment)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    public static void loadWithPlaceHolder(Activity activity, Object url, @DrawableRes int placeHolder, @DrawableRes int errorImg, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);


        if (placeHolder != -1) {
            options.placeholder(placeHolder);
        }

        if (errorImg != -1) {
            options.error(errorImg);
        }

        Glide.with(activity)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * @return ：随机获取图片
     */
    public static int getRandomRes() {
        return RES[RANDOM.nextInt(RES.length)];
    }

}
