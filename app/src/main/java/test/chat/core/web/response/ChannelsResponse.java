package test.chat.core.web.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import test.chat.core.models.ChannelModel;



public class ChannelsResponse {
    @SerializedName("channels")
    @Expose
    private List<ChannelModel> channels = new ArrayList<>();

    public List<ChannelModel> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelModel> channels) {
        this.channels = channels;
    }

}
