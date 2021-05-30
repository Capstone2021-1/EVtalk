package org.techtown.evtalk.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.userinfo.CardAdapter;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnChatListClickListener{
    ArrayList<ChatItem> items = new ArrayList<ChatItem>();
    private Context context;

    OnChatListClickListener listener;

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

    public void setOnItemClickListener(OnChatListClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ListViewHolder holder, View view, int position) {
        Button btn = view.findViewById(R.id.enter_chatroom);
        if (listener != null) {
            listener.onItemClick(holder, view, position);

        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView lastContent;
        Button btn;

        public ListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView12);
            lastContent = itemView.findViewById(R.id.textView14);
            btn = itemView.findViewById(R.id.enter_chatroom);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ChatListAdapter.ListViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(ChatItem item){
            name.setText(item.getName());
            lastContent.setText(item.getContent());
        }
    }
}
