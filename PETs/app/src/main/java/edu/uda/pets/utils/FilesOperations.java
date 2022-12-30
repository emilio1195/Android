package edu.uda.pets.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FilesOperations {

    public static String readFile(Context context, String file_name) throws IOException{
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(context.getAssets().open(file_name), StandardCharsets.UTF_8));
        String content = "";
        String line;

        while ((line = reader.readLine()) != null){
            content += line;
        }

        return content;
    }
}
