package com.example.travel_master_yyz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.travel_master_yyz.dao.SessionManager;

public class LanguageActivity extends BaseActivity {

    private Spinner spinnerLanguage;
    private TextView btnConfirm,jump;

    private String[] languageNames = {"中文", "English", "日本語"};
    private String[] languageCodes = {"zh", "en", "ja"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        spinnerLanguage = findViewById(R.id.spinner_language);
        btnConfirm = findViewById(R.id.btn_confirm);
        jump = findViewById(R.id.tv_us);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, languageNames);
        spinnerLanguage.setAdapter(adapter);

        // 设置当前选中语言
        String currentLangCode = new SessionManager(this).getLanguage();
        int selectedIndex = 0;
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(currentLangCode)) {
                selectedIndex = i;
                break;
            }
        }
        spinnerLanguage.setSelection(selectedIndex);

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LanguageActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIndex = spinnerLanguage.getSelectedItemPosition();
                String selectedLangCode = languageCodes[selectedIndex];

                // 保存语言设置
                SessionManager sessionManager = new SessionManager(LanguageActivity.this);
                sessionManager.saveLanguage(selectedLangCode);

                // 重启整个应用，语言立即生效
                Intent intent = new Intent(LanguageActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();

                // 结束所有 Activity
            }
        });
    }
}
