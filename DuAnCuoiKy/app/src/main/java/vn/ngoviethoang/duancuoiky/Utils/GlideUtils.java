package vn.ngoviethoang.duancuoiky.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import vn.ngoviethoang.duancuoiky.R;

public class GlideUtils {
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView);
    }

    public static void loadCircularImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView);
    }
}

