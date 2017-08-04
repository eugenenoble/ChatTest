package test.chat.config;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import test.chat.core.web.services.ChatWebService;


@Module
public class WebServiceModule {
    @Provides
    @Singleton
    public ChatWebService provideChatWebService(Retrofit retrofit) {
        return retrofit.create(ChatWebService.class);
    }
}
