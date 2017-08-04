package test.chat.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.chat.R;


public class TabView extends RelativeLayout {
    @BindView(R.id.tabTitle)
    TextView tabTitle;
    @BindView(R.id.tabCount)
    BadgeView tabCount;

    public BadgeView getBadgeView() {
        return tabCount;
    }

    public TabView(Context context) {
        super(context);
        init();
    }

    public TabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.custom_tab, this);
        ButterKnife.bind(this, view);
        setBadgeSelected(false);
    }

    public void setCount(int count) {
        if (count > 0) {
            tabCount.setVisibility(View.VISIBLE);
            tabCount.setText(String.valueOf(count));
        } else
            tabCount.setVisibility(View.GONE);
    }

    public void setBadgeSelected(boolean isSelected) {
        if (isSelected) {
            tabCount.setBadgeBackgroundColor(ContextCompat.getColor(getContext(), R.color.bage_color_enabled));
            tabCount.setTextColor(Color.WHITE);
        } else {
            tabCount.setBadgeBackgroundColor(ContextCompat.getColor(getContext(), R.color.terxt_color_disabled));
            tabCount.setTextColor(ContextCompat.getColor(getContext(), R.color.bage_text_color_disabled));
        }
    }

    public void setText(String text) {
        tabTitle.setText(text);
    }
}
