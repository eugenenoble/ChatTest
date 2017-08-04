package test.chat.core.web.services;

import retrofit2.http.GET;
import rx.Observable;
import test.chat.core.web.response.ChannelsResponse;
import test.chat.core.web.response.MessagesResponse;

public interface ChatWebService {
    @GET("/api/chat/channels/1/messages?foramt=json")
    Observable<MessagesResponse> getMesseges();

    @GET("/api/chat/channels?format=json")
    Observable<ChannelsResponse> getChannels();
}
