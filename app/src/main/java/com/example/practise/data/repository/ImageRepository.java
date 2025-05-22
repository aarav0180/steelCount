package com.example.practise.data.repository;

import android.os.Handler;
import android.os.Looper;

import com.example.practise.domain.model.Form;
import com.example.practise.domain.repository.FormRepository;

public class ImageRepository implements FormRepository {

    @Override
    public void submitForm(Form form, SubmitCallback callback) {
        // Simulate backend processing (mock)
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate delay

                // Simulate a backend model returning item count
                int simulatedCount = 7;

                // Update the form with the item count
                Form updatedForm = form.withItemCount(simulatedCount);

                // Post result to main thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onSuccess(updatedForm);
                });

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onError("Failed to submit form: " + e.getMessage());
                });
            }
        }).start();
    }
}
