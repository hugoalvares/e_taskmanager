package com.hello.taskmanager;

/**
 * Created by Hugo on 15/08/2016.
 */
public class Task {

    private int idtask;
    private String title;
    private char status;
    private String imgPath;
    private int index;

    public int getIdTask() {
        return idtask;
    }

    public void setIdTask(int idtask) {
        this.idtask = idtask;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
