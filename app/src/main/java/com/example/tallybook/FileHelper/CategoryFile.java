package com.example.tallybook.FileHelper;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryFile {
    public static List<String> categoryList; // 声明一个消费类别列表

    public CategoryFile() {
        
    }
    public static void saveArrayToFile(Context context, String fileName, List<String> arrayList) {
        String[] array = arrayList.toArray(new String[0]);
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<String> readArrayFromFile(Context context, String fileName) {
        String[] array = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            array = (String[]) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(array != null) {
            categoryList = new ArrayList<>(Arrays.asList(array));
        }
        return categoryList;
    }

    public static void addCategory(String category) {
        categoryList.add(category);
    }

    public static List<String> getCategoryList() {
        return categoryList;
    }
}