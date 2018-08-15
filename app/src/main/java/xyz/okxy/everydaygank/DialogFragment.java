package xyz.okxy.everydaygank;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DialogFragment extends android.support.v4.app.DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        ImageView imageView = view.findViewById(R.id.dialog_image_view);

        Glide.with(getActivity()).load(R.drawable.nav_ic_head).into(imageView);

        AlertDialog.Builder builder = new AlertDialog
                .Builder(getActivity())
                .setView(view)
                .setPositiveButton("关闭",null);
        return builder.create();
    }
}
