package test.chat.config;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Modifier;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.inject.Singleton;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import test.chat.BuildConfig;
import test.chat.core.web.AuthenticationInterceptor;

@Module
public class NetworkModule {

    private static final String ENDPOINT = "https://iostest.db2dev.com/";

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Context context, HttpLoggingInterceptor httpLoggingInterceptor) {


        // VERY-VERY unsafe. Only for test example
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        SSLContext sslContext = null;

        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
            sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (Exception e) {

        }
        // END OF UNSAFE CODE

        long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(new File(context.getCacheDir(), "http"), SIZE_OF_CACHE);

        AuthenticationInterceptor authenticationInterceptor =
                new AuthenticationInterceptor(Credentials.basic("iostest", "iostest2k17!"));

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.cache(cache)
                .addInterceptor(authenticationInterceptor)
                .addInterceptor(httpLoggingInterceptor);
        if (sslContext != null) {
            httpClient.sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier((hostname, session) -> true);
        }

        return httpClient.build();
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient httpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();
    }
}
