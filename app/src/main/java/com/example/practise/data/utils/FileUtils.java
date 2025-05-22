package com.example.practise.data.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    public static File zipImages(Context context, List<Uri> imageUris) throws IOException {
        File zipFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "images.zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));

        for (Uri uri : imageUris) {
            InputStream is = context.getContentResolver().openInputStream(uri);
            if (is != null) {
                String fileName = "img_" + System.currentTimeMillis() + ".jpg";
                zos.putNextEntry(new ZipEntry(fileName));

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
                is.close();
            }
        }

        zos.close();
        return zipFile;
    }

    public static Uri createImageFile(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }
}
