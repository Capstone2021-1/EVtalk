package org.techtown.evtalk.ui.restaurant;

import android.view.View;

public interface OnRestaurantClickListener {
    public void onItemClick(RestaurantAdapter.ViewHolder holder, View view, int position);
}
