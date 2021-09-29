package com.example.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.database.RoomDB;
import com.example.finalproject.UserInfoPage;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private RoomDB database;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView name;
        CardView cardUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.list_user_image);
            name = itemView.findViewById(R.id.list_user_name);
            cardUser = itemView.findViewById(R.id.card_user);
        }

        public void addInfo(Map<String, String> info) {
            Picasso.with(itemView.getContext()).load(info.get("Image")).into(picture);
            name.setText(info.get("Name"));
            cardUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), UserInfoPage.class);
                    intent.putExtra("position", getAdapterPosition());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public UserListAdapter(Context context){
        database = RoomDB.getInstance(context);
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_layout_recycler, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.addInfo(database.userDao().getAll().get(position).getRepresentation());
    }

    @Override
    public int getItemCount() {
        return database.userDao().getAll().size();
    }
}
