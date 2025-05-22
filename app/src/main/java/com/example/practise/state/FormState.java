package com.example.practise.state;


import android.net.Uri;
import java.util.List;

public class FormState {
    public boolean isLoading = false;
    public String errorMessage = null;
    public List<Uri> imageUris = null;
    public int itemCount = 0;
    public boolean isSubmitFinished = false;
}
