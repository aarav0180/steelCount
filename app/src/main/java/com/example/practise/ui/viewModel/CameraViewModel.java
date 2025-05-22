package com.example.practise.ui.viewModel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraViewModel extends ViewModel {

    private final MutableLiveData<Bitmap> capturedImage = new MutableLiveData<>();
    private final MutableLiveData<String> predictionResult = new MutableLiveData<>();
    private final MutableLiveData<Integer> detectedCount = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isProcessing = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Bitmap> getCapturedImage() {
        return capturedImage;
    }

    public LiveData<String> getPredictionResult() {
        return predictionResult;
    }

    public LiveData<Integer> getDetectedCount() {
        return detectedCount;
    }

    public LiveData<Boolean> getIsProcessing() {
        return isProcessing;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setCapturedImage(Bitmap image) {
        capturedImage.setValue(image);
        errorMessage.setValue(null);
    }

    /**
     * Simulates running the ML model for detection
     */
    public void runModelOnImage(Bitmap image) {
        if (image == null) {
            errorMessage.setValue("No image provided.");
            return;
        }

        isProcessing.setValue(true);
        errorMessage.setValue(null);

        // Simulated model inference
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate processing delay

                // Hardcoded example output from ML model
                int result = 8;
                predictionResult.postValue(String.valueOf(result));
                detectedCount.postValue(result);
            } catch (Exception e) {
                errorMessage.postValue("Failed to process image.");
            } finally {
                isProcessing.postValue(false);
            }
        }).start();
    }

    public void resetState() {
        capturedImage.setValue(null);
        predictionResult.setValue(null);
        detectedCount.setValue(null);
        errorMessage.setValue(null);
        isProcessing.setValue(false);
    }
}
