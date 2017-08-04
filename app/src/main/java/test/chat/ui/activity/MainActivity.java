package test.chat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmModel;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.chat.ChatApplication;
import test.chat.R;
import test.chat.core.models.ChannelModel;
import test.chat.core.web.services.ChatWebService;
import test.chat.ui.adapters.ViewPagerAdapter;
import test.chat.ui.fragment.ChatFragment;
import test.chat.ui.fragment.LiveChatFragment;
import test.chat.ui.view.TabView;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.chat_tabs)
    protected TabLayout tabLayout;
    @BindView(R.id.chat_view_pager)
    protected ViewPager viewPager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    protected ChatWebService service;
    private Realm realm = Realm.getDefaultInstance();

    String[] tabTitle = {"Chat", "Live Chat"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.bind(this);
        ChatApplication.appComponent().inject(this);
        progressBar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.app_name_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TabView) tab.getCustomView()).setBadgeSelected(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TabView) tab.getCustomView()).setBadgeSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        getResponce();
        progressBar.setVisibility(View.INVISIBLE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, BackButtonTest.class));
                break;
            case R.id.btn_write:
                startActivity(new Intent(this, TestToolbarMenuButton.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new ChatFragment(), "Chat");
        pagerAdapter.addFragment(new LiveChatFragment(), "Live Chat");
        viewPager.setAdapter(pagerAdapter);
    }

    private void setupTabIcons() {
        TabView view = new TabView(this);
        view.setCount(getUnreadCount());
        view.setText(tabTitle[0]);
        view.setBadgeSelected(true);
        tabLayout.getTabAt(0).setCustomView(view);

        TabView view2 = new TabView(this);
        view2.setCount(20);
        view2.setText(tabTitle[1]);
        tabLayout.getTabAt(1).setCustomView(view2);
    }

    private void getResponce() {
        service.getChannels()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(channelsResponse -> {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(channelsResponse.getChannels());
                    realm.commitTransaction();
                }, throwable -> Log.e(MainActivity.class.getSimpleName(), throwable.getMessage()));
    }

    public int getUnreadCount() {
        int i = 0;
        List<ChannelModel> channelModels = realm.where(ChannelModel.class).findAll();
        for (ChannelModel channelModel : channelModels) {
            i = i + channelModel.getUnreadMessagesCount();
        }
        return i;
    }
}
