package com.example.malecu.youtubelistdownload;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by malecu on 06/01/17.
 */

public class YtVideoHelper {

    protected String TAG = YtVideoHelper.class.getCanonicalName();
    protected Context context;

    public YtVideoHelper(Context parent) {
        context = parent;
    }


    public void writeInputStreamToFile(String fileName, InputStream inputStream) {

        writeInputStreamToFile(fileName, inputStream, null);
    }

    /**
     * Write stream to file
     *
     * @param fileName
     * @param inputStream
     * @param folder
     */
    public void writeInputStreamToFile(String fileName, InputStream inputStream, String folder) {

        String mainFolder = context.getString(R.string.main_folder);

        try {
            // check for main folder creation
            createFolder(mainFolder);

            String path = Environment.getExternalStorageDirectory() + File.separator + mainFolder + File.separator;

            if (folder != null) {
                // create playlist folder
                createFolder(mainFolder + File.separator + folder);
                path = path + folder + File.separator;
            }

            File file = new File(path, fileName);
            FileOutputStream outputStreamWriter = new FileOutputStream(file, true);

            // write to file
            byte[] buf = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStreamWriter.write(buf, 0, len);
            }

            outputStreamWriter.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void createFolder(String folderName) throws IOException {

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);

        boolean success = true;

        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        if (!success)
            throw new IOException("Folder " + folderName + " couldn't be created");
    }
}
