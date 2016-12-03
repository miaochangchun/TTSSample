package com.example.sinovoice.ttssample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sinovoice.util.ConfigUtil;
import com.example.sinovoice.util.HciCloudSysHelper;
import com.example.sinovoice.util.HciCloudTtsHelper;
import com.sinovoice.hcicloudsdk.common.HciErrorCode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etText;
    private Button btnPlay;
    private Button btnPause;
    private Button btnResume;
    private Button btnStop;
    private HciCloudSysHelper mHciCloudSysHelper;
    private HciCloudTtsHelper mHciCloudTtsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initSinovoice();
    }

    @Override
    protected void onDestroy() {
        if (mHciCloudTtsHelper != null) {
            mHciCloudTtsHelper.releaseTtsPlayer();
        }
        if (mHciCloudSysHelper != null) {
            mHciCloudSysHelper.release();
        }
        super.onDestroy();
    }

    /**
     * 灵云系统和tts功能初始化
     */
    private void initSinovoice() {
        mHciCloudSysHelper = HciCloudSysHelper.getInstance();
        mHciCloudTtsHelper = HciCloudTtsHelper.getInstance();
        int errorCode = mHciCloudSysHelper.init(this);
        if (errorCode != HciErrorCode.HCI_ERR_NONE) {
            Toast.makeText(this, "系统初始化失败，错误码=" + errorCode, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean bool = mHciCloudTtsHelper.initTtsPlayer(this, ConfigUtil.CAP_KEY_TTS_LOCAL);
        if (bool == false) {
            Toast.makeText(this, "播放器初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 控件初始化
     */
    private void initView() {
        etText = (EditText) findViewById(R.id.et_text);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnResume = (Button) findViewById(R.id.btn_resume);
        btnStop = (Button) findViewById(R.id.btn_stop);

        btnPause.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnResume.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause:
                mHciCloudTtsHelper.pauseTtsPlayer();
                break;
            case R.id.btn_play:
                mHciCloudTtsHelper.playTtsPlayer(etText.getText().toString(), ConfigUtil.CAP_KEY_TTS_LOCAL);
                break;
            case R.id.btn_resume:
                mHciCloudTtsHelper.resumeTtsPlayer();
                break;
            case R.id.btn_stop:
                mHciCloudTtsHelper.stopTtsPlayer();
                break;
            default:
                break;
        }
    }
}
