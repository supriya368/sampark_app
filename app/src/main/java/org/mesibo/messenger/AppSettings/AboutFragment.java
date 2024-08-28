package org.mesibo.messenger.AppSettings;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mesibo.messenger.BuildConfig;
import org.mesibo.messenger.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.about, container, false);
        TextView tx = (TextView)v.findViewById(R.id.mesibologo);
        Typeface mesiboFont = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/mesibo_regular.otf");
        if(null != mesiboFont)
            tx.setTypeface(mesiboFont);
        TextView version = (TextView)v.findViewById(R.id.version);
        TextView buildDate = (TextView)v.findViewById(R.id.builddate);
        version.setText("Version: " + BuildConfig.BUILD_VERSION);
        buildDate.setText("Build Time: " + BuildConfig.BUILD_TIMESTAMP);
        return v;
    }

}
