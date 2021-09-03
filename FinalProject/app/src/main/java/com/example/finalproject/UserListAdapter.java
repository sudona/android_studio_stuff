package com.example.finalproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private List<Map<String,String>> user_list_ref;
    private Runnable callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Runnable callback;
        ImageView picture;
        TextView name;
        CardView cardUser;

        public ViewHolder(@NonNull View itemView, Runnable callback_func) {
            super(itemView);
            picture = itemView.findViewById(R.id.list_user_image);
            name = itemView.findViewById(R.id.list_user_name);
            cardUser = itemView.findViewById(R.id.card_user);
            callback = callback_func;
        }

        public void addInfo(Map<String, String> info) {
            Picasso.with(itemView.getContext()).load(info.get("Image")).into(picture);
            name.setText(info.get("Name"));
            cardUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.run();
                    Intent intent = new Intent(itemView.getContext(), UserInfoPage.class);
                    intent.putExtra("position", getAdapterPosition());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public UserListAdapter(List<Map<String,String>> list, Runnable callback_func){
        user_list_ref = list;
        callback = callback_func;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_layout_recycler, parent, false);

        return new ViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.addInfo(user_list_ref.get(position));
    }

    @Override
    public int getItemCount() {
        return user_list_ref.size();
    }
}
