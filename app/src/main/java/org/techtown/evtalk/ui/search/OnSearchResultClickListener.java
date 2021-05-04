package org.techtown.evtalk.ui.search;

import android.view.View;

public interface OnSearchResultClickListener {
    public void onItemClick(SearchAdapter.ViewHolder holder, View view, int position);
}
