package test.chat.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class ChannelModel extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("users")
    @Expose
    private RealmList<UserModel> users;
    @SerializedName("last_message")
    @Expose
    private MessageModel messageModel;
    @SerializedName("unread_messages_count")
    @Expose
    private Integer unreadMessagesCount;
    private Boolean isDeleted = false;

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(RealmList<UserModel> users) {
        this.users = users;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public Integer getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(Integer unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public static RealmResults<ChannelModel> getAllActiveCahnnels(Realm realm){
        return realm.where(ChannelModel.class).equalTo("isDeleted", false).findAll();
    }
}