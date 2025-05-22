package com.example.practise.ui.form;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practise.R;
import com.example.practise.domain.model.Form;
import com.example.practise.domain.usecase.SubmitFormUseCase;
import com.example.practise.state.FormState;
import com.example.practise.ui.viewModel.FormViewModel;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

public class FormFragment extends Fragment {

    private FormViewModel viewModel;
    private ImageCarouselAdapter adapter;

    private ProgressBar progressBar;
    private Button submitButton;
    private Button addImageButton;

    private EditText editItemName;
    private EditText editItemCategory;
    private EditText editTitle;
    private EditText editDescription;

    private EditText editItemCount;
    private Button buttonIncrement;
    private Button buttonDecrement;

    private final List<Uri> imageList = new ArrayList<>();

    private ActivityResultLauncher<Intent> pickImagesLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<String[]> requestAllPermissionsLauncher;
    private Uri cameraImageUri;

    private ActivityResultLauncher<Intent> cropImageLauncher;
    private Uri croppedImageUri;


    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            android.os.Build.VERSION.SDK_INT >= 33
                    ? Manifest.permission.READ_MEDIA_IMAGES
                    : Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Bind form inputs ---
        editItemName     = view.findViewById(R.id.input_item_name);
        editItemCategory = view.findViewById(R.id.input_item_category);
        editTitle        = view.findViewById(R.id.input_title);
        editDescription  = view.findViewById(R.id.input_description);

        // --- Bind item-count controls ---
        editItemCount    = view.findViewById(R.id.input_item_no);
        buttonIncrement  = view.findViewById(R.id.button_increment);
        buttonDecrement  = view.findViewById(R.id.button_decrement);

        // Optional: limit max digits
        editItemCount.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(4) });

        // --- Wire + / – buttons ---
        buttonIncrement.setOnClickListener(v -> {
            int cur = parseItemCount();
            editItemCount.setText(String.valueOf(cur + 1));
        });
        buttonDecrement.setOnClickListener(v -> {
            int cur = parseItemCount();
            if (cur > 0) {
                editItemCount.setText(String.valueOf(cur - 1));
            }
        });

        // --- Bind other views ---
        progressBar     = view.findViewById(R.id.progress_bar);
        submitButton    = view.findViewById(R.id.submit_button);
        addImageButton  = view.findViewById(R.id.add_image_button);

        // --- RecyclerView carousel ---
        RecyclerView rv = view.findViewById(R.id.image_carousel);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageCarouselAdapter(imageList, uri -> viewModel.removeImage(uri));
        rv.setAdapter(adapter);

        // --- Permissions launcher ---
        requestAllPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                perms -> {
                    boolean ok = true;
                    for (Boolean granted : perms.values()) if (!granted) { ok = false; break; }
                    Toast.makeText(getContext(),
                            ok ? "Permissions granted" : "Please grant all permissions",
                            Toast.LENGTH_SHORT).show();
                }
        );

        // --- ViewModel setup ---
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull @Override
            @SuppressWarnings("unchecked")
            public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> cls) {
                return (T) new FormViewModel(requireActivity().getApplication(),
                        new SubmitFormUseCase(null));
            }
        }).get(FormViewModel.class);

        // --- Observe state ---
        viewModel.formState.observe(getViewLifecycleOwner(), this::renderState);

        // --- Button listeners ---
        addImageButton.setOnClickListener(v -> showImageSourceDialog());
        submitButton.setOnClickListener(v -> {
            Form form = collectFormData();
            if (!viewModel.formState.getValue().isSubmitFinished) {
                viewModel.submitForm(form);
            } else {
                Toast.makeText(getContext(), "Finished! Resetting form.", Toast.LENGTH_SHORT).show();
                viewModel.resetForm();
                clearForm();
            }
        });

        setupActivityResultLaunchers();
    }

    private void renderState(FormState state) {
        // show/hide loader
        progressBar.setVisibility(state.isLoading ? View.VISIBLE : View.GONE);

        // update carousel
        List<Uri> uris = state.imageUris != null ? state.imageUris : new ArrayList<>();
        imageList.clear(); imageList.addAll(uris);
        adapter.updateImages(imageList);

        // pre-fill item count
        editItemCount.setText(String.valueOf(state.itemCount));
    }

    private Form collectFormData() {
        String name        = editItemName.getText().toString().trim();
        String category    = editItemCategory.getText().toString().trim();
        String title       = editTitle.getText().toString().trim();
        String desc        = editDescription.getText().toString().trim();
        int    finalCount  = parseItemCount();

        // null-safe image URI list
        FormState st = viewModel.formState.getValue();
        List<String> imagePaths = new ArrayList<>();
        if (st != null && st.imageUris != null) {
            for (Uri u : st.imageUris) {
                imagePaths.add(u.toString());
            }
        }

        return new Form(name, category, title, desc, imagePaths, finalCount);
    }

    private int parseItemCount() {
        try {
            return Integer.parseInt(editItemCount.getText().toString().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private void clearForm() {
        editItemName.setText("");
        editItemCategory.setText("");
        editTitle.setText("");
        editDescription.setText("");
        editItemCount.setText("0");
        imageList.clear();
        adapter.updateImages(imageList);
    }

    private void showImageSourceDialog() {
        String[] opts = {"Camera", "Gallery"};
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(opts, (dlg, which) -> {
                    if (which == 0) openCamera();
                    else            openGallery();
                }).show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestAllPermissionsLauncher.launch(REQUIRED_PERMISSIONS);
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "New Picture");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        cameraImageUri = requireActivity()
                .getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        takePictureLauncher.launch(i);
    }

    private void openGallery() {
        String permission = android.os.Build.VERSION.SDK_INT >= 33
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestAllPermissionsLauncher.launch(REQUIRED_PERMISSIONS);
            return;
        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickImagesLauncher.launch(i);
    }

    private void setupActivityResultLaunchers() {
        pickImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                res -> {
                    if (res.getResultCode() == Activity.RESULT_OK && res.getData() != null) {
                        Intent data = res.getData();
                        if (data.getClipData() != null) {
                            int cnt = data.getClipData().getItemCount();
                            for (int i = 0; i < cnt; i++) {
                                Uri u = data.getClipData().getItemAt(i).getUri();
                                //viewModel.addImage(u);
                                launchCropper(u);

                            }
                        } else if (data.getData() != null) {
                            //viewModel.addImage(data.getData());
                            launchCropper(data.getData());

                        }
                    }
                });

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                res -> {
                    if (res.getResultCode() == Activity.RESULT_OK && cameraImageUri != null) {
                        //viewModel.addImage(cameraImageUri);
                        launchCropper(cameraImageUri);
                    }
                });

        cropImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                res -> {
                    if (res.getResultCode() == Activity.RESULT_OK) {
                        final Uri resultUri = UCrop.getOutput(res.getData());
                        if (resultUri != null) {
                            viewModel.addImage(resultUri);
                        }
                    }
                });

    }
    private void launchCropper(Uri sourceUri) {
        croppedImageUri = Uri.fromFile(new java.io.File(
                requireContext().getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));

        Intent cropIntent = UCrop.of(sourceUri, croppedImageUri)
                .withAspectRatio(1, 1)  // optional: set custom aspect
                .withMaxResultSize(1080, 1080)
                .getIntent(requireContext());

        cropImageLauncher.launch(cropIntent);
    }

}
