package test.chat.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class MessageModel extends RealmObject {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("sender")
    @Expose
    private UserModel sender;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("is_read")
    @Expose
    private Boolean isRead;

    @Ignore
    private SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    @Ignore
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getChannelTime() {
        try {
            return format.format(parser.parse(getCreateDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}