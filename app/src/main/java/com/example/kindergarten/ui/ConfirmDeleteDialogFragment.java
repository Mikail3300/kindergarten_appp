package com.example.kindergarten.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmDeleteDialogFragment extends DialogFragment {
    public interface DeleteListener {
        void onDeleteConfirmed(long childId);
    }

    private static final String ARG_CHILD_ID = "arg_child_id";
    private static final String ARG_CHILD_NAME = "arg_child_name";

    public static ConfirmDeleteDialogFragment newInstance(long childId, String childName) {
        ConfirmDeleteDialogFragment fragment = new ConfirmDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CHILD_ID, childId);
        args.putString(ARG_CHILD_NAME, childName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        long childId = args != null ? args.getLong(ARG_CHILD_ID) : -1L;
        String childName = args != null ? args.getString(ARG_CHILD_NAME, "") : "";

        return new AlertDialog.Builder(requireContext())
                .setTitle(com.example.kindergarten.R.string.delete_dialog_title)
                .setMessage(getString(com.example.kindergarten.R.string.delete_dialog_message, childName))
                .setPositiveButton(com.example.kindergarten.R.string.delete, (dialog, which) -> {
                    if (getActivity() instanceof DeleteListener) {
                        ((DeleteListener) getActivity()).onDeleteConfirmed(childId);
                    }
                })
                .setNegativeButton(com.example.kindergarten.R.string.cancel, null)
                .create();
    }
}
