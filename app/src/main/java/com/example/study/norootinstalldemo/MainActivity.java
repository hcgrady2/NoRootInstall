package com.example.study.norootinstalldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityDemo";
    private EditText mCmdInputEt;
    private Button mRunShellBtn;
    private TextView mOutputTv;
    private Button run_shell;

    String cmd2 = " app_process -Djava.class.path=/data/local/tmp/classes.dex /system/bin com.example.study.norootdemo.shellService.Main";
    String cmd = " app_process -Djava.class.path=/data/user/0/com.example.study.norootinstalldemo/files/classes.dex /system/bin com.example.study.norootdemo.shellService.Main";

    private void initView(){
        mCmdInputEt = findViewById(R.id.et_cmd);
        mRunShellBtn = findViewById(R.id.btn_runshell);
        mOutputTv = findViewById(R.id.tv_output);
        run_shell = findViewById(R.id.run_shell);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        run_shell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                 /*       Process proc;
                        try {
                            Log.i(TAG, "start run");

                            String data = null;
                            StringBuilder builder=new StringBuilder();
                       //     builder.append("cmd");
                         //   builder.append("");
                        //    builder.append(File.separator);
                            builder.append("su ls");
                            Runtime rt = Runtime.getRuntime();
                            proc = rt.exec(builder.toString());
                            InputStream stderr = proc.getErrorStream();
                            InputStreamReader isr = new InputStreamReader(stderr);
                            BufferedReader br = new BufferedReader(isr);
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                                data += line + "\n";
                            }
                            int exitVal = proc.waitFor();

                            Log.i(TAG, "run: the result" + data);



                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(TAG, "run: error:" + e.toString());

                        }
*/


                        String path1 = getApplicationContext().getFilesDir().getAbsolutePath();


                        Log.i(TAG, "start run,the path:" + path1);

                        Process p = null;
                        try {
                            p = Runtime.getRuntime().exec(cmd);



                            //  Runtime.getRuntime().exec(su -c "ls /data");
                            //   Process proc = Runtime.getRuntime().exec("su -s sh  -c /data/initcommand.sh");

                            String data = null;
                            BufferedReader ie = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                            String error = null;
                            while ((error = ie.readLine()) != null
                                    && !error.equals("null")) {
                                data += error + "\n";
                            }
                            String line = null;
                            while ((line = in.readLine()) != null
                                    && !line.equals("null")) {
                                data += line + "\n";
                            }
                            Log.i(TAG, " the result" + data);



                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i(TAG, "run: error" + e.toString());

                        }






                    }
                }).start();



            }
        });


        mRunShellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmd = mCmdInputEt.getText().toString();
                if (TextUtils.isEmpty(cmd)) {
                    Toast.makeText(MainActivity.this, "输入内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                runShell(cmd);
            }
        });
    }

    private void runShell(final String cmd){
        if (TextUtils.isEmpty(cmd)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SocketClient(cmd, new SocketClient.onServiceSend() {
                    @Override
                    public void getSend(String result) {
                        showTextOnTextView(result);
                    }
                });
            }
        }).start();
    }

    private void showTextOnTextView(final String text){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(mOutputTv.getText())) {
                    mOutputTv.setText(text);
                } else {
                    mOutputTv.setText(mOutputTv.getText() + "\n" + text);
                }
            }
        });
    }


    public static boolean runtimeExec(String cmd, File path) {
        Process proc;
        try {
            StringBuilder builder=new StringBuilder();
            builder.append("cmd /c ");
            builder.append(path);
            builder.append(File.separator);
            builder.append(cmd);
            Runtime rt = Runtime.getRuntime();
            proc = rt.exec(builder.toString());
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = proc.waitFor();
            return exitVal == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}