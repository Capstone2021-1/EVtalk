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

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    ArrayList<Card> items = new ArrayList<Card>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.cardlayout, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Card item = items.get(position);
        viewHolder.setItem(item);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView card_image;
        TextView card_text;
        LinearLayout card_layout;
        Bitmap bmImg;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("cardadapter", "4");
            card_image = itemView.findViewById(R.id.card_image);
            card_text = itemView.findViewById(R.id.card_text);
            card_layout = itemView.findViewById(R.id.card_layout);
        }


        // 처음에 아이템 설정해주는 부분
        public void setItem(Card item) {
            Log.d("cardadapter", "5");
            card_image.setImageBitmap(getBitmap(item.getImage()));
            card_text.setText(item.getName());
            card_layout.setBackgroundColor(Color.WHITE);
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
