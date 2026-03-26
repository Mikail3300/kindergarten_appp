package com.example.kindergarten.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kindergarten.R;
import com.example.kindergarten.data.ChildRepository;
import com.example.kindergarten.databinding.ActivityChildDetailBinding;
import com.example.kindergarten.model.Child;

public class ChildDetailActivity extends AppCompatActivity implements ConfirmDeleteDialogFragment.DeleteListener {
    private ActivityChildDetailBinding binding;
    private Child child;

    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    child = result.getData().getParcelableExtra(MainActivity.EXTRA_CHILD);
                    fillUi();
                    Intent response = new Intent();
                    response.putExtra(MainActivity.EXTRA_ACTION, MainActivity.ACTION_UPDATED);
                    setResult(RESULT_OK, response);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        child = getIntent().getParcelableExtra(MainActivity.EXTRA_CHILD);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fillUi();
        setupButtons();
    }

    private void fillUi() {
        if (child == null) {
            finish();
            return;
        }

        binding.tvChildName.setText(child.getChildName());
        binding.tvParentName.setText(getString(R.string.parent_template, child.getParentName()));
        binding.tvGroupName.setText(getString(R.string.group_template, child.getGroupName()));
        binding.tvTeacherName.setText(getString(R.string.teacher_template, child.getTeacherName()));
        binding.tvBirthDate.setText(getString(R.string.birth_date_template, child.getBirthDate()));
        binding.tvPickupTime.setText(getString(R.string.pickup_time_template, child.getPickupTime()));
        binding.tvGender.setText(getString(R.string.gender_template, child.getGender()));
        binding.tvAllergies.setText(child.getAllergiesText());
        binding.tvNap.setText(child.getNapText());
        binding.tvActivityLevel.setText(getString(R.string.activity_level_template, child.getActivityLevel()));
        binding.tvNotes.setText(getString(R.string.notes_template, child.getNotes() == null || child.getNotes().isEmpty() ? getString(R.string.no_notes) : child.getNotes()));

        int imageRes = "Девочка".equalsIgnoreCase(child.getGender()) ? R.drawable.ic_child_female : R.drawable.ic_child_male;
        binding.ivDetailAvatar.setImageResource(imageRes);
    }

    private void setupButtons() {
        binding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditChildActivity.class);
            intent.putExtra(MainActivity.EXTRA_CHILD, child);
            editLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        });

        binding.btnDelete.setOnClickListener(v ->
                ConfirmDeleteDialogFragment.newInstance(child.getId(), child.getChildName())
                        .show(getSupportFragmentManager(), "detail_delete_dialog"));
    }

    @Override
    public void onDeleteConfirmed(long childId) {
        ChildRepository repository = new ChildRepository(this);
        repository.deleteChild(childId);

        Intent result = new Intent();
        result.putExtra(MainActivity.EXTRA_ACTION, MainActivity.ACTION_DELETED);
        setResult(RESULT_OK, result);
        finish();
        overridePendingTransition(R.anim.fade_out, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.fade_out, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
