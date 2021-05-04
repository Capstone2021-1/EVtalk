package org.techtown.evtalk.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.userinfo.OnCardItemClickListener;
import org.techtown.evtalk.user.SearchResult;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements OnSearchResultClickListener{

    ArrayList<SearchResult> items = new ArrayList<SearchResult>();

    OnSearchResultClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.search_result_layout, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        SearchResult item = items.get(position);
        viewHolder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(SearchResult item) {
        items.add(item);
    }

    public void setOnItemClickListener(OnSearchResultClickListener listener){
        this.listener = listener;
    }


    // 검색 결과 아이템 터치했을 때 작동하는 부분 ex) 검색 결과로 중앙대 클릭
    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {

    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void setItem(SearchResult item) {
            textView.setText(item.getnOa());
            textView2.setText(item.getiOt());
        }

    }
}
