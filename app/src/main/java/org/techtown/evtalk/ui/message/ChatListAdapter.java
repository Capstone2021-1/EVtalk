package org.techtown.evtalk.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatItem> items = new ArrayList<ChatItem>();
    private Context context;

    public ChatListAdapter(Context context) { this.context = context; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.chat_list_item, parent, false);
        return new ChatListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatListAdapter.ListViewHolder) {
            ChatItem item = items.get(position);
            ((ChatListAdapter.ListViewHolder) holder).setItem(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ChatItem item){
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView lastContent;

        public ListViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView12);
            lastContent = itemView.findViewById(R.id.textView14);
        }

        public void setItem(ChatItem item){
            name.setText(item.getName());
            lastContent.setText(item.getContent());
        }
    }
}
