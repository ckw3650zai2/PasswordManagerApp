package my.edu.utar.passwordmanager;

import android.app.Application;
import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyListItem extends Application {

    private static List<WebObj> webObjList = new ArrayList<WebObj>();
    private SQLiteAdapter mySQLiteAdapter;
    private static int nextId = 0;
    private static int counter =0;


    public MyListItem(){
        fillList();
    }

    private void fillList() {

    }

    public static List<WebObj> getWebObjList() {
        return webObjList;
    }

    public static void setWebObjList(List<WebObj> webObjList) {
        MyListItem.webObjList = webObjList;
    }

    public static int getNextId() {

        counter = webObjList.size()-1;

        for(int x =0;x<webObjList.size();x++){

            if(x == counter){
                WebObj storage = webObjList.get(x);
                nextId = storage.getId()+1;
                Log.i("Inserted: ","my initial next Id is? "+nextId);
            }
        }

        return nextId;
    }

    public static void setNextId(int nextId) {
        MyListItem.nextId = nextId;
    }
}
