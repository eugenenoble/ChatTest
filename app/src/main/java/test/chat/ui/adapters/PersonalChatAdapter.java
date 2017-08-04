package test.chat.ui.adapters;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;
import test.chat.R;
import test.chat.core.models.ChannelModel;
import test.chat.core.models.MessageModel;


public class PersonalChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageModel> messageModels = new ArrayList<>();

    public PersonalChatAdapter(List<MessageModel> messageModels) {
        this.messageModels.clear();
        this.messageModels.addAll(messageModels);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getSender().getId() != 1747) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new IncomingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.incoming_message_item, parent, false));
            case 1:
                return new OutgoingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.outgoing_message_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = getMessageModel(position);
        switch (holder.getItemViewType()) {
            case 0:
                IncomingViewHolder incomingViewHolder = (IncomingViewHolder) holder;
                Glide.with(incomingViewHolder.itemView.getContext())
                        .load(messageModel.getSender().getPhoto())
                        .apply(RequestOptions.circleCropTransform())
                        .into(incomingViewHolder.chatImage);
                incomingViewHolder.incomingMessage.setText(messageModel.getText());
                break;
            case 1:
                OutgoingViewHolder outgoingViewHolder = (OutgoingViewHolder) holder;
                outgoingViewHolder.outgoingMessage.setText(messageModel.getText());
                outgoingViewHolder.outgoingTime.setText(messageModel.getChannelTime());
                break;
        }


    }

    private MessageModel getMessageModel(int position) {
        return messageModels.get(position);
    }


    public void updateSelfContent(List<MessageModel> newData) {
        this.messageModels.clear();
        this.messageModels.addAll(newData);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class IncomingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_image)
        AppCompatImageView chatImage;
        @BindView(R.id.incoming_message)
        TextView incomingMessage;

        public IncomingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class OutgoingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.outgoing_time)
        TextView outgoingTime;
        @BindView(R.id.outgoing_message)
        TextView outgoingMessage;

        public OutgoingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
