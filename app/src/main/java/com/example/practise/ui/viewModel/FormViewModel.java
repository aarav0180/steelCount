package com.example.practise.ui.viewModel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practise.domain.model.Form;
import com.example.practise.domain.usecase.SubmitFormUseCase;
import com.example.practise.state.FormState;

import java.util.ArrayList;
import java.util.List;

public class FormViewModel extends AndroidViewModel {

    private final MutableLiveData<FormState> _formState = new MutableLiveData<>(new FormState());
    public LiveData<FormState> formState = _formState;

    private final SubmitFormUseCase submitFormUseCase;
    private final List<Uri> selectedImages = new ArrayList<>();

    public FormViewModel(@NonNull Application application, SubmitFormUseCase submitFormUseCase) {
        super(application);
        this.submitFormUseCase = submitFormUseCase;
    }

    public void addImage(Uri uri) {
        selectedImages.add(uri);
        updateState();
    }

    public void removeImage(Uri uri) {
        selectedImages.remove(uri);
        updateState();
    }

    private void updateState() {
        FormState current = _formState.getValue();
        if (current == null) current = new FormState();

        current.imageUris = new ArrayList<>(selectedImages);
        _formState.setValue(current);
    }

    public void submitForm(Form form) {
        FormState current = _formState.getValue();
        if (current == null) current = new FormState();

        current.isLoading = true;
        _formState.setValue(current);

        // Mimic backend: simulate async with thread, you can replace with real repo call
        new Thread(() -> {
            try {
                Thread.sleep(3000); // simulate network delay

                // Here you would normally convert images to ZIP and send to backend
                // For now, mimic a response with itemCount = total images * 2 (random logic)
                int detectedItems = selectedImages.size() * 2;

                FormState newState = new FormState();
                newState.isLoading = false;
                newState.imageUris = new ArrayList<>(selectedImages);
                newState.itemCount = detectedItems;
                newState.isSubmitFinished = true;

                _formState.postValue(newState);

            } catch (InterruptedException e) {
                FormState errorState = new FormState();
                errorState.isLoading = false;
                errorState.errorMessage = "Submission interrupted";
                _formState.postValue(errorState);
            }
        }).start();
    }

    public void resetForm() {
        selectedImages.clear();
        _formState.setValue(new FormState());
    }
}
