package org.mesibo.messenger.AppSettings;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import org.mesibo.messenger.SampleAPI;
import org.mesibo.messenger.R;

import java.util.Objects;

public class DataUsageFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        SharedPreferences sharedPreferences;
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
                //add xml
                final ActionBar ab = ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).getSupportActionBar();
                assert ab != null;
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setTitle("Data usage settings");

                addPreferencesFromResource(R.xml.data_usage);
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                Preference preference = findPreference("auto");
                PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference("preferenceScreen");
                PreferenceCategory myPrefCatcell = (PreferenceCategory) findPreference("preferenceCategorycell");
                PreferenceCategory myPrefCatwifi = (PreferenceCategory) findPreference("preferenceCategorywifi");
                PreferenceCategory myPrefCatroam = (PreferenceCategory) findPreference("preferenceCategoryroam");

                //temporary
                preferenceScreen.removePreference(myPrefCatcell);
                preferenceScreen.removePreference(myPrefCatwifi);
                preferenceScreen.removePreference(myPrefCatroam);
        }

        @Override
        public void onResume() {
                super.onResume();
                //unregister the preferenceChange listener
                getPreferenceScreen().getSharedPreferences()
                        .registerOnSharedPreferenceChangeListener(this);
                displaySwitches();
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                PreferenceCategory myPrefCatcell = (PreferenceCategory) findPreference("preferenceCategorycell");
                PreferenceCategory myPrefCatwifi = (PreferenceCategory) findPreference("preferenceCategorywifi");
                PreferenceCategory myPrefCatroam = (PreferenceCategory) findPreference("preferenceCategoryroam");
                Preference preference = findPreference(key);
                if (preference instanceof SwitchPreferenceCompat && key.equalsIgnoreCase("auto")) {
                        SwitchPreferenceCompat datausage = (SwitchPreferenceCompat) preference;

                        boolean enabled = datausage.isChecked();
                        SampleAPI.setMediaAutoDownload(enabled);

                        try {
                                //when this is null
                                myPrefCatcell.setEnabled(!enabled);
                                myPrefCatwifi.setEnabled(!enabled);
                                myPrefCatroam.setEnabled(!enabled);
                        } catch (Exception e) {}


                }

        }

        @Override
        public void onPause() {
                super.onPause();
                //unregister the preference change listener
                getPreferenceScreen().getSharedPreferences()
                        .unregisterOnSharedPreferenceChangeListener(this);
        }



        public void displaySwitches() {
                Preference preference = findPreference("auto");
                boolean autoDownload = SampleAPI.getMediaAutoDownload();
                //preference.setEnabled(autoDownload);
                SwitchPreferenceCompat datausage = (SwitchPreferenceCompat) preference;
                datausage.setChecked(autoDownload);
        }

}
