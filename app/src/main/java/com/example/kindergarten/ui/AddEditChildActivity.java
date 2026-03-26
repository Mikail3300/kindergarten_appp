package com.example.kindergarten.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kindergarten.R;
import com.example.kindergarten.data.ChildRepository;
import com.example.kindergarten.databinding.ActivityAddEditChildBinding;
import com.example.kindergarten.model.Child;

import java.util.Calendar;
import java.util.Locale;

public class AddEditChildActivity extends AppCompatActivity {
    private ActivityAddEditChildBinding binding;
    private Child editingChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupSpinner();
        setupAutoComplete();
        setupSeekBar();
        setupDateAndTimePickers();
        readIncomingChild();
        setupSaveButton();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.group_names,
                android.R.layout.simple_spinner_item
        );
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGroup.setAdapter(groupAdapter);
    }

    private void setupAutoComplete() {
        ArrayAdapter<CharSequence> teacherAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.teacher_names,
                android.R.layout.simple_dropdown_item_1line
        );
        binding.autoTeacher.setAdapter(teacherAdapter);
    }

    private void setupSeekBar() {
        binding.seekActivity.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                binding.tvActivityLevel.setText(getString(R.string.activity_level_template, progress));
            }

            @Override
            public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
            }
        });
    }

    private void setupDateAndTimePickers() {
        binding.btnSelectBirthDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String formattedDate = String.format(Locale.getDefault(), "%02d.%02d.%d", dayOfMonth, month + 1, year);
                binding.tvBirthDateValue.setText(formattedDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        binding.btnSelectPickupTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                binding.tvPickupTimeValue.setText(formattedTime);
            },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            ).show();
        });
    }

    private void readIncomingChild() {
        editingChild = getIntent().getParcelableExtra(MainActivity.EXTRA_CHILD);
        if (editingChild == null) {
            binding.tvScreenTitle.setText(R.string.add_child_title);
            return;
        }

        binding.tvScreenTitle.setText(R.string.edit_child_title);
        binding.etChildName.setText(editingChild.getChildName());
        binding.etParentName.setText(editingChild.getParentName());
        binding.tvBirthDateValue.setText(editingChild.getBirthDate());
        binding.tvPickupTimeValue.setText(editingChild.getPickupTime());
        binding.autoTeacher.setText(editingChild.getTeacherName(), false);
        binding.checkAllergies.setChecked(editingChild.hasAllergies());
        binding.toggleNap.setChecked(editingChild.isNapEnabled());
        binding.seekActivity.setProgress(editingChild.getActivityLevel());
        binding.tvActivityLevel.setText(getString(R.string.activity_level_template, editingChild.getActivityLevel()));
        binding.etNotes.setText(editingChild.getNotes());

        if ("Девочка".equalsIgnoreCase(editingChild.getGender())) {
            binding.radioGirl.setChecked(true);
        } else {
            binding.radioBoy.setChecked(true);
        }

        String[] groups = getResources().getStringArray(R.array.group_names);
        for (int index = 0; index < groups.length; index++) {
            if (groups[index].equals(editingChild.getGroupName())) {
                binding.spinnerGroup.setSelection(index);
                break;
            }
        }
    }

    private void setupSaveButton() {
        binding.btnSave.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gentle_pulse));
            Child child = buildChildFromForm();
            if (child == null) {
                return;
            }

            ChildRepository repository = new ChildRepository(this);
            if (editingChild == null) {
                long newId = repository.addChild(child);
                child.setId(newId);
            } else {
                child.setId(editingChild.getId());
                repository.updateChild(child);
            }

            Intent result = new Intent();
            result.putExtra(MainActivity.EXTRA_CHILD, child);
            setResult(RESULT_OK, result);
            finish();
            overridePendingTransition(R.anim.fade_out, R.anim.slide_out_right);
        });
    }

    private Child buildChildFromForm() {
        String childName = binding.etChildName.getText().toString().trim();
        String parentName = binding.etParentName.getText().toString().trim();
        String groupName = binding.spinnerGroup.getSelectedItem().toString();
        String teacherName = binding.autoTeacher.getText().toString().trim();
        String birthDate = binding.tvBirthDateValue.getText().toString().trim();
        String pickupTime = binding.tvPickupTimeValue.getText().toString().trim();
        String gender = binding.radioGirl.isChecked() ? getString(R.string.girl) : getString(R.string.boy);
        String notes = binding.etNotes.getText().toString().trim();

        if (childName.isEmpty() || parentName.isEmpty() || teacherName.isEmpty()
                || getString(R.string.not_selected).equals(birthDate)
                || getString(R.string.not_selected).equals(pickupTime)) {
            Toast.makeText(this, R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
            return null;
        }

        Child child = new Child();
        child.setChildName(childName);
        child.setParentName(parentName);
        child.setGroupName(groupName);
        child.setTeacherName(teacherName);
        child.setBirthDate(birthDate);
        child.setPickupTime(pickupTime);
        child.setGender(gender);
        child.setAllergies(binding.checkAllergies.isChecked());
        child.setNapEnabled(binding.toggleNap.isChecked());
        child.setActivityLevel(binding.seekActivity.getProgress());
        child.setNotes(notes);
        return child;
    }
}
