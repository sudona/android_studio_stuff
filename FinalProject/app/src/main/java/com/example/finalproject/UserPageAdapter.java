package com.example.finalproject;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserPageAdapter extends RecyclerView.Adapter<UserPageAdapter.ViewHolder> {
    LinkedHashMap<String, String> userRef;
    List<String> list_string;
    

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText userInfo;
        TextView infoTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userInfo = itemView.findViewById(R.id.user_card_info);
            infoTitle = itemView.findViewById(R.id.user_card_info_title);
        }

        public void addInfo(String key, String value, LinkedHashMap<String, String> userRef) {
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
                    userRef.put(key, editable.toString());
                }
            });
        }
    }
    
    public UserPageAdapter(LinkedHashMap<String,String> user) {
        this.userRef = user;
        list_string = new ArrayList<>(user.size());
        for (Map.Entry<String, String> entrySet: user.entrySet()) {
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
        String value = userRef.get(key);
        holder.addInfo(key, value, userRef);
    }

    @Override
    public int getItemCount() {
        return list_string.size();
    }
}
