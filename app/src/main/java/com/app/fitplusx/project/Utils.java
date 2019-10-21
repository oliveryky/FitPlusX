package com.app.fitplusx.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;


public class Utils {
    private static final String PICTURE_PATH = "/profilePic.png";

    private static String dir;

    public static void setDirPath(String dir) {
        dir = dir;
    }


    public static double stringToHeight(String str) {
        String[] arr = str.split("\'");

        return Double.parseDouble(arr[0]) * 12 + Double.parseDouble(arr[1]);
    }

    public static String heightToString(double height) {
        return (int) height / 12 + "\'" + (int) height % 12;
    }

    public static Bitmap getBitmap(String imagePath) {
        File file = new File(imagePath);
        if(!file.exists()) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        return bitmap;
    }

    public static void writeBitmapToFile(Bitmap bitmap, String path) {
        try {
            File file = new File(path);
            FileOutputStream outputStream = new FileOutputStream(file, false);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getAge(String birthdate) {
        if(birthdate.isEmpty()) {
            return 0;
        }
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                .parseCaseInsensitive().parseLenient()
                .appendPattern("[M/d/yyyy]")
                .appendPattern("[M/dd/yyyy]")
                .appendPattern("[MM/dd/yyyy]")
                .appendPattern("[MM/d/yyyy]");

        DateTimeFormatter formatter = builder.toFormatter();

        LocalDate birthDate = LocalDate.parse(birthdate, formatter), now = LocalDate.now();

        return Period.between(birthDate, now).getYears();
    }
}
