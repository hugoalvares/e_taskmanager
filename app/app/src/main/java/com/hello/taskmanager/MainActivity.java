package com.hello.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.hello.taskmanager.ListAdapter.customButtonListener;
import com.koushikdutta.ion.Ion;

public class MainActivity extends Activity implements AsyncResponse, customButtonListener {

    private final String serverUrl = "http://192.168.0.12:8080/";
    private ArrayList<Task> taskList;
    private ListView listView;
    ListAdapter adapter;
    private final char constPendingStatus = 'P';
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Task selectedTask;

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

    private LatLng getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat,lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            return new LatLng(lat, lon);
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    private void concludeTask(Task task) {
        String imgPath = task.getImgPath();
        int idTask = task.getIdTask();

        uploadImage(idTask, imgPath);

        LatLng latLng = getLocation();
        String latitude = String.valueOf(latLng.getLatitude());
        String longitude = String.valueOf(latLng.getLongitude());

        MyAsyncTask httpAsyncTask = new MyAsyncTask();
        httpAsyncTask.delegate = this;
        httpAsyncTask.execute(serverUrl + "concludetask?idtask=" + Integer.toString(idTask) + "&imgpath=" + Integer.toString(idTask) + ".jpg" + "&latitude=" + latitude + "&longitude=" + longitude);
    }

    private void buildTasks(String jsonResponse) {
        Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
        taskList = (ArrayList<Task>) new Gson().fromJson(jsonResponse, listType);

        ArrayList<String> arStTasks = new ArrayList<String>();
        for (Task currentTask : taskList) {
            if (currentTask.getStatus() == constPendingStatus) {
                arStTasks.add(currentTask.getTitle());
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
        for (Task currentTask : taskList) {
            if (currentTask.getTitle() == title) {
                selectedTask = currentTask;
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
    }

    private File createImageFile() throws IOException {
        String imageFileName = "idtask" + Integer.toString(selectedTask.getIdTask());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, // prefix
                ".jpg",        // suffix
                storageDir     // directory
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            selectedTask.setImgPath(mCurrentPhotoPath);
            concludeTask(selectedTask);
        }
    }

    public void processFinish(String jsonResponse){
        buildTasks(jsonResponse);
    }

    public void uploadImage(int idTask, String imgPath) {
        Ion.with(this)
            .load(serverUrl + "uploadimage")
            .setMultipartFile("file", "image/jpeg", new File(imgPath))
            .setMultipartParameter("idtask", Integer.toString(idTask))
            .asJsonObject();
    }

}