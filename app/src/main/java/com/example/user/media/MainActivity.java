package com.example.user.media;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

//import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    private Chronometer chronometer;
//    MainActivity MainActivity = new MainActivity();
    Button recBtn, stopBtn;
    MediaRecorder mediaRecorder = null;
    ListView listView;
    private MyAdapter myAdapter = new MyAdapter();
    String[] mFileNameArr;
    private MediaPlayer mediaPlayer=new MediaPlayer();
    private CharSequence[] list;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaListPath();
        chronometer = (Chronometer) findViewById(R.id.chronometer2);
        chronometer.setFormat("%s");

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(listOnItemClickListener);
        mediaListPath();

        chronometer.setBase(SystemClock.elapsedRealtime());
        recBtn = (Button) findViewById(R.id.button2);
        recBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "record.amr";
                try {
                    File SDCardpath = Environment.getExternalStorageDirectory();
                    File myDataPath = new File(SDCardpath.getAbsolutePath() + "/download");
                    if (!myDataPath.exists()) myDataPath.mkdirs();
                    File recodeFile = new File(SDCardpath.getAbsolutePath() + "/download/" + fileName);

                    mediaRecorder = new MediaRecorder();

                    //設定音源
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    //設定輸出檔案的格式
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    //設定編碼格式
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    //設定錄音檔位置
                    mediaRecorder.setOutputFile(createFilePath());


                    mediaRecorder.prepare();

                    //開始錄音
                    mediaRecorder.start();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stopBtn = (Button) findViewById(R.id.button);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    chronometer.stop();
                    listView.setAdapter(myAdapter);
                    mediaListPath();
                    listView.setOnItemClickListener(listOnItemClickListener);
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
            }

        });


    }

    public void mediaListPath() {


        FilenameFilter mediafilefilter = new FilenameFilter() {
            private String[] filter = { ".mp3", ".ogg", ".3gp", ".mp4" };

            @Override
            public boolean accept(File dir, String filename) {
                for (int i = 0; i < filter.length; i++) {
                    if (filename.indexOf(filter[i]) != -1)
                        return true;
                }
                return false;
            }
        };

        File mediaDiPath = getExtDir("/mrteago/audio");
        File[] mediaInDir = mediaDiPath.listFiles(mediafilefilter);

        list = new CharSequence[mediaInDir.length];
        mFileNameArr = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            list[i] = mediaInDir[i].getName();
            mFileNameArr[i] = list[i] + "";
            Log.e("lunch", mFileNameArr[i] + "");
        }

    }

    private AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            String selectFile = mFileNameArr[position] + "";
            Log.e("word", selectFile);
            playmid(selectFile);
        }
    };

    public void playmid(String selectFile) {


        try {
            mediaPlayer.stop();
            mediaPlayer.setDataSource(getExtDir("/mrteago/audio/").getPath() +"/"+ selectFile);
            Log.e("DataSource",getExtDir("/mrteago/audio/").getPath() +"/"+ selectFile);
            //mediaPlayer.setDataSource("/sdcard/mrteago/audio" + a);
            mediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        }
        mediaPlayer.start();
    }
    private File getExtDir(String subDir) {

        File path = new File(Environment.getExternalStorageDirectory(), subDir);

        if (!path.mkdirs()) {

        }
        return path;
    }
    private  class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFileNameArr.length;
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mFileNameArr.length > 0) {
                TextView mTextView = new TextView(getApplicationContext());
                mTextView.setText(mFileNameArr[position]);
                mTextView.setTextSize(20);
                mTextView.setTextColor(Color.rgb(255, 153, 0));
                mTextView.setGravity(Gravity.CENTER);
                return mTextView;
            } else {
                TextView mTextView = new TextView(getApplicationContext());
                mTextView.setText(" ");
                mTextView.setTextSize(20);
                mTextView.setTextColor(Color.rgb(255, 153, 0));
                mTextView.setGravity(Gravity.CENTER);
                return mTextView;
            }

        }
    }
    private String createFilePath() {
        File mediaDiPath = getExtDir("/mrteago/audio/");
//		File sdCardDir = Environment.getExternalStorageDirectory();
//		File testtea1 = new File(sdCardDir, "mrteago");
//		File testtea = new File(testtea1, "audio");
//		if (!testtea.exists()) {
//			testtea.mkdir();
//		}
        Calendar c = Calendar.getInstance();
        int[] a = { c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), c.get(Calendar.SECOND) };
        String now = "";

        for (int i = 0; i < a.length; i++) {
            if (i == a.length - 1) {
                now = now + a[i];
            } else if ((i < 3)) {
                now = now + a[i] + "-";
            } else {
                now = now + a[i] + ":";
            }
        }

        // String fileName = now + ".mp3";
        String fileName = formatter.format(new Date(System.currentTimeMillis())) + ".mp3";
        String path = new File(mediaDiPath, fileName).getAbsolutePath();


        return path;
    }
}
