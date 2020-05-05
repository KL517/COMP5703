package com.example.capstonetest;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

public class MyIO {

    static public ArrayList readItemsFromFile(Context context){
        //retrieve the app's private folder.
        //this folder cannot be accessed by other apps
        ArrayList list;
        File filesDir = context.getFilesDir();
        //prepare a file to read the data
        File todoFile = new File(filesDir,"DFdata.txt");
        //if file does not exist, create an empty list
        if(!todoFile.exists()){
            list = new ArrayList<String>();
        }else{
            try{
                //read data and put it into the ArrayList
                list=new ArrayList<String>(FileUtils.readLines(todoFile, "UTF-8"));

            }
            catch(IOException ex){
                list = new ArrayList<String>();
            }
        }
        return list;
    }

    static public void saveItemsToFile(Context context,ArrayList list){
        File filesDir = context.getFilesDir();
        //using the same file for reading. Should use define a global string instead.
        File todoFile = new File(filesDir,"DFdata.txt");
        try{
            //write list to file
            FileUtils.writeLines(todoFile,list);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
