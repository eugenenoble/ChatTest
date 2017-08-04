package test.chat.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.chat.R;

public class BackButtonTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_button_test);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.back_to_main)
    public void onClick(){
        finish();
    }
}
