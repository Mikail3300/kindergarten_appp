package com.example.kindergarten.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kindergarten.R;
import com.example.kindergarten.adapter.ChildAdapter;
import com.example.kindergarten.data.ChildRepository;
import com.example.kindergarten.databinding.ActivityMainBinding;
import com.example.kindergarten.model.Child;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChildAdapter.OnChildActionListener, ConfirmDeleteDialogFragment.DeleteListener {

    public static final String EXTRA_CHILD = "extra_child";
    public static final String EXTRA_ACTION = "extra_action";
    public static final String ACTION_DELETED = "action_deleted";
    public static final String ACTION_UPDATED = "action_updated";
    private static final String SORT_BY_NAME = "child_name ASC";
    private static final String SORT_BY_GROUP = "group_name ASC, child_name ASC";

    private ActivityMainBinding binding;
    private ChildRepository repository;
    private ChildAdapter adapter;
    private String currentOrder = SORT_BY_NAME;

    private final ActivityResultLauncher<Intent> addEditLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadChildren(binding.etSearch.getText().toString());
                    Snackbar.make(binding.getRoot(), R.string.saved_successfully, Snackbar.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadChildren(binding.etSearch.getText().toString());
                    String action = result.getData() != null ? result.getData().getStringExtra(EXTRA_ACTION) : "";
                    int message = ACTION_DELETED.equals(action) ? R.string.deleted_successfully : R.string.saved_successfully;
                    Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new ChildRepository(this);
        adapter = new ChildAdapter(this);

        setSupportActionBar(binding.toolbar);
        binding.ivHeaderIcon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gentle_pulse));

        setupRecyclerView();
        setupListeners();
        loadChildren("");
    }

    private void setupRecyclerView() {
        binding.recyclerChildren.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerChildren.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnAddChild.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gentle_pulse));
            Intent intent = new Intent(this, AddEditChildActivity.class);
            addEditLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                loadChildren(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void loadChildren(String query) {
        List<Child> children = repository.searchChildren(query, currentOrder);
        adapter.submitList(children);
        binding.tvEmpty.setVisibility(children.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_add) {
            binding.btnAddChild.performClick();
            return true;
        } else if (itemId == R.id.menu_sort_name) {
            currentOrder = SORT_BY_NAME;
            loadChildren(binding.etSearch.getText().toString());
            return true;
        } else if (itemId == R.id.menu_sort_group) {
            currentOrder = SORT_BY_GROUP;
            loadChildren(binding.etSearch.getText().toString());
            return true;
        } else if (itemId == R.id.menu_export) {
            exportChildrenToFile();
            return true;
        } else if (itemId == R.id.menu_about) {
            Snackbar.make(binding.getRoot(), R.string.about_text, Snackbar.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportChildrenToFile() {
        try {
            List<Child> children = repository.searchChildren(binding.etSearch.getText().toString(), currentOrder);
            File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (directory == null) {
                Snackbar.make(binding.getRoot(), R.string.export_failed, Snackbar.LENGTH_SHORT).show();
                return;
            }

            File file = new File(directory, "kindergarten_export.txt");
            StringBuilder builder = new StringBuilder();
            builder.append("Учет детей в садике\n\n");
            for (Child child : children) {
                builder.append("Ребенок: ").append(child.getChildName()).append("\n")
                        .append("Родитель: ").append(child.getParentName()).append("\n")
                        .append("Группа: ").append(child.getGroupName()).append("\n")
                        .append("Воспитательница: ").append(child.getTeacherName()).append("\n")
                        .append("Дата рождения: ").append(child.getBirthDate()).append("\n")
                        .append("Время ухода: ").append(child.getPickupTime()).append("\n")
                        .append("Пол: ").append(child.getGender()).append("\n")
                        .append("Аллергия: ").append(child.getAllergiesText()).append("\n")
                        .append("Тихий час: ").append(child.getNapText()).append("\n")
                        .append("Уровень активности: ").append(child.getActivityLevel()).append("\n")
                        .append("Комментарий: ").append(child.getNotes()).append("\n\n");
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(builder.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            Snackbar.make(binding.getRoot(), getString(R.string.export_success, file.getAbsolutePath()), Snackbar.LENGTH_LONG).show();
        } catch (Exception exception) {
            Snackbar.make(binding.getRoot(), R.string.export_failed, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChildClick(Child child) {
        Intent intent = new Intent(this, ChildDetailActivity.class);
        intent.putExtra(EXTRA_CHILD, child);
        detailLauncher.launch(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
    }

    @Override
    public void onEditClick(Child child) {
        Intent intent = new Intent(this, AddEditChildActivity.class);
        intent.putExtra(EXTRA_CHILD, child);
        addEditLauncher.launch(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
    }

    @Override
    public void onDeleteClick(Child child) {
        ConfirmDeleteDialogFragment.newInstance(child.getId(), child.getChildName())
                .show(getSupportFragmentManager(), "delete_dialog");
    }

    @Override
    public void onDeleteConfirmed(long childId) {
        repository.deleteChild(childId);
        loadChildren(binding.etSearch.getText().toString());
        Snackbar.make(binding.getRoot(), R.string.deleted_successfully, Snackbar.LENGTH_SHORT).show();
    }
}
