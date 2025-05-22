package com.example.practise.domain.usecase;

import com.example.practise.domain.model.Form;
import com.example.practise.domain.repository.FormRepository;

public class SubmitFormUseCase {

    private final FormRepository repository;

    public SubmitFormUseCase(FormRepository repository) {
        this.repository = repository;
    }

    public void execute(Form form, FormRepository.SubmitCallback callback) {
        repository.submitForm(form, new FormRepository.SubmitCallback() {
            @Override
            public void onSuccess(Form updatedForm) {
                callback.onSuccess(updatedForm);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}

