package test.chat.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import atownsend.swipeopenhelper.BaseSwipeOpenViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import test.chat.R;
import test.chat.core.models.ChannelModel;
import test.chat.core.models.MessageModel;
import test.chat.ui.view.BadgeView;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChatViewHolder> {
    private List<ChannelModel> channelModels = new ArrayList<>();

    public void setListener(ItemInteractionListener listener) {
        this.listener = listener;
    }

    private ItemInteractionListener listener;

    public ChannelAdapter(RealmResults<ChannelModel> resultList) {
        this.channelModels.addAll(resultList);

        resultList.addChangeListener(c -> {
            channelModels.clear();
            channelModels.addAll(ChannelModel.getAllActiveCahnnels(Realm.getDefaultInstance()));
            notifyDataSetChanged();
        });
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChannelModel channelModel = holder.getChannelModel();
        if (channelModel.getUnreadMessagesCount() != 0) {
            holder.chatCount.setText(String.valueOf(channelModel.getUnreadMessagesCount()));
        } else {
            holder.chatCount.setVisibility(View.INVISIBLE);
        }
        Glide.with(holder.itemView.getContext())
                .load(channelModel.getMessageModel().getSender().getPhoto())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgProfile);

        holder.time.setText(channelModel.getMessageModel().getChannelTime());
        holder.tvMsg.setText(channelModel.getMessageModel().getText());
        holder.tvName.setText(channelModel.getMessageModel().getSender().getFirstName() + " " +
                channelModel.getMessageModel().getSender().getLastName());
    }


    @Override
    public int getItemCount() {
        return channelModels.size();
    }


    class ChatViewHolder extends BaseSwipeOpenViewHolder {

        @BindView(R.id.chat_count)
        protected BadgeView chatCount;
        @BindView(R.id.tv_msg)
        protected AppCompatTextView tvMsg;
        @BindView(R.id.tv_name)
        protected AppCompatTextView tvName;
        @BindView(R.id.time)
        protected AppCompatTextView time;
        @BindView(R.id.profile_image)
        protected AppCompatImageView imgProfile;
        @BindView(R.id.delete_button)
        protected AppCompatTextView deleteButton;
        @BindView(R.id.content_view)
        protected LinearLayout contentView;

        public ChatViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.delete_button)
        protected void onDeleteClick() {
            if (listener != null)
                listener.onItemRemoved(getChannelModel().getId());
        }

        @OnClick(R.id.content_view)
        protected void onContentClick() {
            if (listener != null)
                listener.onItemClick(getChannelModel().getId());
        }

        private ChannelModel getChannelModel() {
            return channelModels.get(getAdapterPosition());
        }

        @NonNull
        @Override
        public View getSwipeView() {
            return contentView;
        }

        @Override
        public float getEndHiddenViewSize() {
            return 0;
        }

        @Override
        public float getStartHiddenViewSize() {
            return deleteButton.getMeasuredWidth();
        }

        @Override
        public void notifyStartOpen() {
        }

        @Override
        public void notifyEndOpen() {

        }
    }

    public interface ItemInteractionListener {
        void onItemClick(Integer channelModel);

        void onItemRemoved(Integer channelModel);
    }
}
