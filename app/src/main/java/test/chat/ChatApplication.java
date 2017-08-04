package test.chat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import test.chat.config.AppComponent;
import test.chat.config.AppModule;
import test.chat.config.DaggerAppComponent;


public class ChatApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private AppComponent appComponent;

    public static AppComponent appComponent() {
        return ((ChatApplication) context.getApplicationContext()).appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);


        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("chat.models")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
