package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 06/13/2017.
 */

public class CardSwipe extends PagerAdapter
{
    Context context ;
    ArrayList<String> image;
    public static ImageView imageView;

    public CardSwipe(Context applicationContext, ArrayList<String> image)
    {
        this.context = applicationContext ;
        this.image = image;
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_swipe, null);
        try
        {
            imageView = (ImageView)view.findViewById(R.id.ivImages);
//            imageView.setImageResource(image.get(position));
           // Bitmap bmp = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
            // ImageView image = (ImageView) findViewById(R.id.imageView1);
            Picasso.with(context).load(image.get(position))
                    .resize(700, 400).onlyScaleDown().skipMemoryCache().into(imageView);
//            imageView.setImageResource(image.get(position));
//            Glide.with(context).load(image[position]).into(imageCover);

            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
