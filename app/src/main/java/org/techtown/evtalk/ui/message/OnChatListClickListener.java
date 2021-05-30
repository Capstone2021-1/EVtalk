package org.techtown.evtalk.ui.message;

import android.view.View;

public interface OnChatListClickListener {
    public void onItemClick(ChatListAdapter.ListViewHolder holder, View view, int position);
}
