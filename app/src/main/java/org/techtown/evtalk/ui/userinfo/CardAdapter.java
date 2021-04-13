package org.techtown.evtalk.ui.userinfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.R;
import org.techtown.evtalk.user.Card;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements OnCardItemClickListener {
    ArrayList<Card> items = new ArrayList<Card>();

    OnCardItemClickListener listener;

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

    public void onItemClick(CardAdapter.ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView membership_image;
        TextView membership_text;
        Bitmap bmImg;

        public ViewHolder(View itemView, final OnCardItemClickListener listener) {
            super(itemView);
            Log.d("cardadapter", "4");
            membership_image = itemView.findViewById(R.id.membership_image);
            membership_text = itemView.findViewById(R.id.membership_text);

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

        public void setItem(Card item) {
            Log.d("cardadapter", "5");
            membership_image.setImageBitmap(getBitmap(item.getImage()));
            //membership_image.setImageBitmap(UtoB(item.getImage()));
            membership_text.setText(item.getName());
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
