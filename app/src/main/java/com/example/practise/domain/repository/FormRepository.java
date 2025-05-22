package com.example.practise.domain.repository;

import com.example.practise.domain.model.Form;

public interface FormRepository {
    void submitForm(Form form, SubmitCallback callback);

    interface SubmitCallback {
        void onSuccess(Form updatedForm);
        void onError(String message);
    }
}
