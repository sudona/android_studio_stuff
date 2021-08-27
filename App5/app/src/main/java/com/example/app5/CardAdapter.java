package com.example.app5;

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

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    CardInfo[] localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name_card;
        private final ImageView card_pic;

        private final CardView card_info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name_card = itemView.findViewById(R.id.textCard);
            card_pic = itemView.findViewById(R.id.imageCard);
            card_info = itemView.findViewById(R.id.card_box);
        }

        public TextView getName_card() {
            return name_card;
        }

        public ImageView getCard_pic() {
            return card_pic;
        }

        public CardView getCard_info() {
            return card_info;
        }

        public void addInfo(CardInfo cardInfo) {
            name_card.setText(cardInfo.getInfo());
            Picasso.with(itemView.getContext()).load(cardInfo.getPicture()).into(card_pic);
            card_info.setOnClickListener(view -> {
                Intent new_intent = new Intent(view.getContext(), Listings.class) {
                    {
                        putExtra("name", cardInfo.getInfo());
                        putExtra("picture", cardInfo.getPicture());
                        putExtra("actors", cardInfo.getActors());
                        putExtra("type", cardInfo.getType());
                        putExtra("year", cardInfo.getYear());
                    }
                };
                view.getContext().startActivity(new_intent);
            });
        }
    }

    public CardAdapter(CardInfo[] dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cards_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        CardInfo card_at = localDataSet[position];
        holder.addInfo(card_at);
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
