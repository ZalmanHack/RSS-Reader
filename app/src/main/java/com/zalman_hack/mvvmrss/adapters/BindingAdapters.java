package com.zalman_hack.mvvmrss.adapters;

import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BindingAdapters {

    @BindingAdapter("android:imageURL")
    public static void setImageURL(ImageView imageView, String url)
    {
        try {
            Picasso.get().load(url).noFade().into(imageView, new Callback() {
                @Override
                public void onSuccess()
                {
                    imageView.setAlpha(0f);
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) { }
            });
        } catch (Exception ignored) { }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @BindingAdapter("android:dateLong")
    public static void setDateShort(TextView textView, Date date) {
        textView.setText(getDateWithFormat(date, "dd.MM.yyyy HH:mm:ss"));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @BindingAdapter("android:dateShort")
    public static void setDateLong(TextView textView, Date date) {
        textView.setText(getDateWithFormat(date, "dd.MM.yyyy"));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getDateWithFormat(Date date, String format)
    {
        try {
            Locale locale = Locale.getDefault(Locale.Category.FORMAT);
            SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
            return sdf.format(date);
        } catch (Exception ignored) { }
        return "";
    }

}
