package com.enmingx.asynctaskexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AsyncTaskActivity extends AppCompatActivity {

    private EditText chronoValue;
    private TextView chronoText;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 获取三个UI组件
        start = (Button)findViewById(R.id.start);
        chronoText = (TextView)findViewById(R.id.chronoText);
        chronoValue = (EditText)findViewById(R.id.chronoValue);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取EditText里的数值
                int value = Integer.parseInt(String.valueOf(chronoValue.getText()));
                // 验证数值是否大于零
                if (value > 0) {
                    new Chronograph().execute(value);
                }
                else {
                    Toast.makeText(AsyncTaskActivity.this, "请输入一个大于零的整数值 !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class Chronograph extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 在计时开始前，先使按钮和EditText不能用
            chronoValue.setEnabled(false);
            start.setEnabled(false);
            chronoText.setText("0:0");
        }
        @Override
        protected Void doInBackground(Integer... params) {
            // 计时
            for (int i = 0; i <= params[0]; i++) {
                for (int j = 0; j < 60; j++) {
                    try {
                        // 发布增量
                        publishProgress(i, j);
                        if (i == params[0]) {
                            return null;
                        }
                        // 暂停一秒
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (isCancelled()) {
                return null;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // 更新UI界面
            chronoText.setText(values[0] + ":" + values[1]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // 重新使按钮和EditText可以使用
            chronoValue.setEnabled(true);
            start.setEnabled(true);
        }
    }
}
