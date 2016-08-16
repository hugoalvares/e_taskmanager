package com.hello.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.hello.taskmanager.ListAdapter.customButtonListener;

public class MainActivity extends Activity implements AsyncResponse, customButtonListener {

    private final String serverUrl = "http://192.168.0.12:8080/";
    private ArrayList<Task> taskList;
    private ListView listView;
    ListAdapter adapter;
    private final char constPendingStatus = 'P';
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTasks();
        interval();
    }

    private void interval() {
        // 10 seconds interval to update list
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
            getTasks();
            handler.postDelayed(this, 10000);
            }
        }, 10000);
    }

    private void getTasks() {
        MyAsyncTask httpAsyncTask = new MyAsyncTask();
        httpAsyncTask.delegate = this;
        httpAsyncTask.execute(serverUrl + "gettasks");
    }

    private void concludeTask(int idTask, String imgPath) {
        MyAsyncTask httpAsyncTask = new MyAsyncTask();
        httpAsyncTask.delegate = this;
        httpAsyncTask.execute(serverUrl + "concludetask?idtask=" + Integer.toString(idTask) + "&imgpath=" + imgPath);
    }

    private void buildTasks(String jsonResponse) {
        Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
        taskList = (ArrayList<Task>) new Gson().fromJson(jsonResponse, listType);

        // mapping because I couldn't do this the right way
        ArrayList<String> arStTasks = new ArrayList<String>();
        int index = 0;
        for (Task currentTask : taskList) {
            if (currentTask.getStatus() == constPendingStatus) {
                arStTasks.add(currentTask.getTitle());
                currentTask.setIndex(index);
                index++;
            }
        }

        listView = (ListView) findViewById(R.id.listview);
        adapter = new ListAdapter(MainActivity.this, arStTasks);
        adapter.setCustomButtonListner(MainActivity.this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        startTask(value);
    }

    private void startTask(String title) {
        // @ToDo fix this
        int idTask = 0;
        for (Task currentTask : taskList) {
            if (currentTask.getTitle() == title) {
                idTask = currentTask.getIdTask();
                break;
            }
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i("error", "error creating image");
            }
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        concludeTask(idTask, mCurrentPhotoPath);
    }

    private File createImageFile() throws IOException {
        String imageFileName = "taskimg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, // prefix
                ".jpg",        // suffix
                storageDir     // directory
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void processFinish(String jsonResponse){
        buildTasks(jsonResponse);
    }

}