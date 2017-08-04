package test.chat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import atownsend.swipeopenhelper.SwipeOpenItemTouchHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import test.chat.R;
import test.chat.core.models.ChannelModel;
import test.chat.ui.activity.PersonalChatActivity;
import test.chat.ui.adapters.ChannelAdapter;


public class ChatFragment extends Fragment implements ChannelAdapter.ItemInteractionListener {
    @BindView(R.id.rView)
    RecyclerView recyclerView;

    private ChannelAdapter adapter;
    private SwipeOpenItemTouchHelper helper;

    private Realm realm = Realm.getDefaultInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_layout, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ChannelAdapter(realm.where(ChannelModel.class).findAll());
        adapter.setListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        helper = new SwipeOpenItemTouchHelper(new SwipeOpenItemTouchHelper.SimpleCallback(
                SwipeOpenItemTouchHelper.START | SwipeOpenItemTouchHelper.END));
        helper.closeAllOpenPositions();
        helper.attachToRecyclerView(recyclerView);
        helper.setCloseOnAction(true);
        helper.setPreventZeroSizeViewSwipes(true);
    }

    @Override
    public void onItemClick(Integer channelModel) {
        helper.closeAllOpenPositions();
        startActivity(new Intent(getActivity(), PersonalChatActivity.class));
    }

    @Override
    public void onItemRemoved(Integer channelModel) {
        realm.executeTransaction(trans -> {
            ChannelModel result = trans.where(ChannelModel.class).equalTo("id", channelModel).findFirst();
            if (result != null)
                result.deleteFromRealm();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}
