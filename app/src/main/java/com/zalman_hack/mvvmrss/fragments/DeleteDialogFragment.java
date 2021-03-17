package com.zalman_hack.mvvmrss.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.viewmodels.FeedViewModel;

import java.util.Objects;

public class DeleteDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String ARG_PARAM1 = "channel";
    private FeedViewModel feedViewModel;
    private Channel channel;

    public DeleteDialogFragment() {
        super();
    }

    public static DeleteDialogFragment newInstance(Channel channel) {
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            channel = getArguments().getParcelable(ARG_PARAM1);
            feedViewModel = new ViewModelProvider(requireActivity()).get(FeedViewModel.class);
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.delete_message_text)
                .setPositiveButton(R.string.delete, this)
                .setNegativeButton(R.string.cancel, this)
                .setMessage(channel.name);
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            feedViewModel.deleteChannelOf(channel);
        }
    }
}