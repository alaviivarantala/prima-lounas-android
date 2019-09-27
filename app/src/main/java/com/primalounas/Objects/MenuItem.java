package com.primalounas.Objects;

import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.primalounas.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuItem extends AbstractItem<MenuItem.ViewHolder> {

    private String menuItemDay;
    private String menuItemSalad;
    private String menuItemFood;
    private String menuItemSoup;

    public MenuItem(String menuItemDay, String menuItemSalad, String menuItemFood, String menuItemSoup) {
        this.menuItemDay = menuItemDay;
        this.menuItemSalad = menuItemSalad;
        this.menuItemFood = menuItemFood;
        this.menuItemSoup = menuItemSoup;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_menuitem;
    }

    @Override
    public int getType() {
        return R.id.fa_card_menu_item;
    }

    @NotNull
    @Override
    public MenuItem.ViewHolder getViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends FastAdapter.ViewHolder<MenuItem> {

        @BindView(R.id.textview_dayname) TextView menuItemDay;
        @BindView(R.id.textview_saladname) TextView menuItemSalad;
        @BindView(R.id.textview_foodname) TextView menuItemFood;
        @BindView(R.id.textview_soupname) TextView menuItemSoup;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(@NotNull MenuItem item, @NotNull List<Object> list) {
            menuItemDay.setText(item.menuItemDay);
            menuItemSalad.setText(item.menuItemSalad);
            menuItemFood.setText(item.menuItemFood);
            menuItemSoup.setText(item.menuItemSoup);
        }

        @Override
        public void unbindView(@NotNull MenuItem item) {
            menuItemDay.setText(null);
            menuItemSalad.setText(null);
            menuItemFood.setText(null);
            menuItemSoup.setText(null);
        }
    }
}