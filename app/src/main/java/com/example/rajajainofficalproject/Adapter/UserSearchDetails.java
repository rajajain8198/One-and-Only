package com.example.rajajainofficalproject.Adapter;

import android.content.ContentProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rajajainofficalproject.R;

public class UserSearchDetails extends RecyclerView.Adapter<UserSearchDetails.ViewHolder> {

    Context context;
    public UserSearchDetails(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.seraching_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.tvTitle.setText("");
        holder.tvDescription.setText("");
        Glide.with(context).load("url").into(holder.imgSearchImage);
        holder.imgArrowDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDescription;
        ImageView imgSearchImage, imgArrowDropDown;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            imgSearchImage = itemView.findViewById(R.id.img_image);
            imgArrowDropDown = itemView.findViewById(R.id.img_dots);
            checkBox = itemView.findViewById(R.id.img_star);

        }
    }

}
