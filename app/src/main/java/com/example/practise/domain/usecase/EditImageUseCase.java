package com.example.practise.domain.usecase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import java.io.File;

import com.yalantis.ucrop.UCrop;


public class EditImageUseCase {

    public Uri applyCrop(Context context, Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(context.getCacheDir(), "cropped.jpg"));
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .start((Activity) context);
        return destinationUri;
    }

    public Uri applyRotate(Context context, Uri sourceUri, float angle) {
        // Implement rotation logic or use a library
        return sourceUri;
    }

    public Uri applyFlip(Context context, Uri sourceUri) {
        // Implement flip logic or use a library
        return sourceUri;
    }
}

