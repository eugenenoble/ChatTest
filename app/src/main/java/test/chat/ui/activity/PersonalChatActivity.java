package test.chat.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.chat.ChatApplication;
import test.chat.R;
import test.chat.core.models.ChannelModel;
import test.chat.core.models.MessageModel;
import test.chat.core.web.services.ChatWebService;
import test.chat.ui.adapters.PersonalChatAdapter;

public class PersonalChatActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 2;
    private final static int SELECT_IMAGE = 1;
    private PersonalChatAdapter adapter;

    @BindView(R.id.etMsg)
    protected EditText etMsg;
    @BindView(R.id.rView)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    protected ChatWebService service;
    private Realm realm = Realm.getDefaultInstance();
    List<MessageModel> messageModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
        ButterKnife.bind(this);
        ChatApplication.appComponent().inject(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new PersonalChatAdapter(messageModels);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        getResponce();

    }

    @OnClick(R.id.addBtn)
    public void OnClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            getImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 1) {
            getImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getResponce() {
        progressBar.setVisibility(View.VISIBLE);
        service.getMesseges()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messagesResponse -> {
                    messageModels.addAll(messagesResponse.getMessages());
                    adapter.updateSelfContent(messageModels);
                    progressBar.setVisibility(View.INVISIBLE);
                }, throwable -> Log.e(PersonalChatActivity.class.getSimpleName(), throwable.getMessage()));

    }

    public void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), SELECT_IMAGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
