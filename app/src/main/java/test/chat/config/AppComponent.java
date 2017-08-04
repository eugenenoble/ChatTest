package test.chat.config;

import javax.inject.Singleton;

import dagger.Component;
import test.chat.ChatApplication;
import test.chat.ui.activity.MainActivity;
import test.chat.ui.activity.PersonalChatActivity;


@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, WebServiceModule.class})
public interface AppComponent {
    void inject(ChatApplication chatApplication);
    void inject(MainActivity chatApplication);
    void inject(PersonalChatActivity personalChatActivity);
}
