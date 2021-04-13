package org.techtown.evtalk.ui.userinfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.user.Card;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements OnCardItemClickListener {
    ArrayList<Card> items = new ArrayList<Card>();

    OnCardItemClickListener listener;

    LinearLayout card_layout;

    static String card_kinds;

    CardAdapter(String card_kinds){
        this.card_kinds = card_kinds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.cardlayout, viewGroup, false);
        Log.d("cardadapter", "1");
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Card item = items.get(position);
        viewHolder.setItem(item);
        Log.d("cardadapter", "2");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Card item) {
        items.add(item);
    }

    public void setItems(ArrayList<Card> items) {
        this.items = items;
    }

    public Card getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Card item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnCardItemClickListener listener){
        this.listener = listener;
    }

    // 아이템 터치했을 때 작동하는 부분
    public void onItemClick(CardAdapter.ViewHolder holder, View view, int position) {
        card_layout = view.findViewById(R.id.card_layout);
        if (listener != null) {
            listener.onItemClick(holder, view, position);

            if(card_kinds.equals("membership")) {
                if (MainActivity.membership.contains(items.get(position))) {
                    MainActivity.membership.remove(items.get(position));
                    card_layout.setBackgroundColor(Color.WHITE);
                } else {
                    MainActivity.membership.add(items.get(position));
                    card_layout.setBackgroundColor(Color.CYAN);
                }
            }else if(card_kinds.equals("payment")){
                if (MainActivity.payment.contains(items.get(position))) {
                    MainActivity.payment.remove(items.get(position));
                    card_layout.setBackgroundColor(Color.WHITE);
                } else {
                    MainActivity.payment.add(items.get(position));
                    card_layout.setBackgroundColor(Color.CYAN);
                }
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView card_image;
        TextView card_text;
        LinearLayout card_layout;
        Bitmap bmImg;

        public ViewHolder(View itemView, final OnCardItemClickListener listener) {
            super(itemView);
            Log.d("cardadapter", "4");
            card_image = itemView.findViewById(R.id.card_image);
            card_text = itemView.findViewById(R.id.card_text);
            card_layout = itemView.findViewById(R.id.card_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }


        // 처음에 아이템 설정해주는 부분
        public void setItem(Card item) {
            Log.d("cardadapter", "5");
            card_image.setImageBitmap(getBitmap(item.getImage()));
            card_text.setText(item.getName());

            if(card_kinds.equals("membership")){
                if(!MainActivity.membership.isEmpty()){
                    if(MainActivity.membership.contains(item)){
                        card_layout.setBackgroundColor(Color.CYAN);
                    }else{
                        card_layout.setBackgroundColor(Color.WHITE);
                    }
                }
            }else if(card_kinds.equals("payment")){
                if(!MainActivity.payment.isEmpty()){
                    if(MainActivity.payment.contains(item)){
                        card_layout.setBackgroundColor(Color.CYAN);
                    }else{
                        card_layout.setBackgroundColor(Color.WHITE);
                    }
                }
            }

        }



        // URL 비트맵 변환
        public Bitmap getBitmap(String imgPath) {
            Thread imgThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(imgPath);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bmImg = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                    }
                }
            };
            imgThread.start();
            try {
                imgThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                return bmImg;
            }
        }
    }


}
