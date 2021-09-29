package com.example.finalproject.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.database.RoomDB;
import com.example.finalproject.user.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserPageAdapter extends RecyclerView.Adapter<UserPageAdapter.ViewHolder> {
    RoomDB database;
    User userRef;
    List<String> list_string;
    LinkedHashMap<String, String> linkedUserRef;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText userInfo;
        TextView infoTitle;
        RoomDB database;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userInfo = itemView.findViewById(R.id.user_card_info);
            infoTitle = itemView.findViewById(R.id.user_card_info_title);
            database = RoomDB.getInstance(itemView.getContext());
        }

        public void addInfo(String key, String value, User userRef) {
            infoTitle.setText(key);
            userInfo.setText(value);
            userInfo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    userRef.addFromRep(key, editable.toString());
                    database.userDao().update(userRef);
                }
            });
        }
    }
    
    public UserPageAdapter(Context context, int position) {
        database = RoomDB.getInstance(context);
        userRef = database.userDao().getAll().get(position);
        this.linkedUserRef = userRef.getRepresentation();
        list_string = new ArrayList<>();
        for (Map.Entry<String, String> entrySet: linkedUserRef.entrySet()) {
            String key = entrySet.getKey();
            if (!key.equals("Image")) {
                list_string.add(key);
            }
        }
    }

    @NonNull
    @Override
    public UserPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_card_user_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPageAdapter.ViewHolder holder, int position) {
        String key = list_string.get(position);
        String value = linkedUserRef.get(key);
        holder.addInfo(key, value, userRef);
    }

    @Override
    public int getItemCount() {
        return list_string.size();
    }
}
