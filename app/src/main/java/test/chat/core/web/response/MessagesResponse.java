package test.chat.core.web.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import test.chat.core.models.MessageModel;



public class MessagesResponse {
    @SerializedName("messages")
    @Expose
    private List<MessageModel> messages = new ArrayList<>();

    public List<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageModel> messages) {
        this.messages = messages;
    }
}
