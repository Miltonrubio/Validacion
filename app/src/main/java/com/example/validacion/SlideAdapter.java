package com.example.validacion;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {


    private List<SlideItem> slideItems;
    private ViewPager2 viewPager2;

    SlideAdapter(List<SlideItem> slideItems, ViewPager2 viewPager2) {
        this.slideItems = slideItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches_container, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setImage(slideItems.get(position));

        if (position == slideItems.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return slideItems.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }


        void setImage(SlideItem slideItem) {

            String fotoSlide = slideItem.getImage();

            //    String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/0a0ae57527c33e5c433a7322844ad09c.jpg";
            if (!TextUtils.isEmpty(fotoSlide)) {
                Glide.with(itemView.getContext())
                        .load(fotoSlide)
                        .error(R.drawable.default_image)
                        .into(imageView);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.default_image)
                        .into(imageView);
            }
        }
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            slideItems.addAll(slideItems);
            notifyDataSetChanged();
        }
    };


}
