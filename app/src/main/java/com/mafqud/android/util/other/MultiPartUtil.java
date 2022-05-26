package com.mafqud.android.util.other;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultiPartUtil {

    @NonNull
    public static MultipartBody.Part fileToMultiPart(Context context, Uri fileUri, String partName) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(context, fileUri);

        //Logger.i("FileUtils", file.getName());
        //Logger.i("FileUtils", file.getPath());
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        file,
                        MediaType.parse(context.getContentResolver().getType(fileUri))
                );

        // MultipartBody.Part is used to send also the actual file partName
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    /**
     * @param context    your activity context
     * @param imagesList pass list of images URIs
     * @return List<MultipartBody.Part> of your (list of images URIs)
     * or null if the list you passed is empty
     */
    public static List<MultipartBody.Part> filesToMultiPart(Context context, List<Uri> imagesList) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        if (imagesList.size() != 0) {
            for (Uri image : imagesList) {
                partList.add(fileToMultiPart(context, image, "images[]"));
            }
        }
        return partList;
    }
}
