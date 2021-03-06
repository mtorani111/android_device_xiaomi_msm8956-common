/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.screwd.settings.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.preference.ListPreference;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;

public class DeviceSettings extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    public static final String KEY_VIBSTRENGTH = "vib_strength";
    public static final String KEY_KCAL_RGB_RED = "kcal_rgb_red";
    public static final String KEY_KCAL_RGB_BLUE = "kcal_rgb_blue";
    public static final String KEY_KCAL_RGB_GREEN = "kcal_rgb_green";
    public static final String KEY_KCAL_RGB_MIN = "kcal_rgb_min";
    public static final String KEY_KCAL_SAT_INTENSITY = "kcal_sat_intensity";
    public static final String KEY_KCAL_SCR_CONTR = "key_kcal_scr_contr";
    public static final String KEY_KCAL_SCR_VAL = "key_kcal_scr_val";
    public static final String KEY_KCAL_SCR_HUE = "key_kcal_scr_hue";
    public static final String KEY_RESTORE_ON_BOOT = "restore_on_boot";
    public static final String KEY_RESTORE_DELAY = "delay_list";

    private VibratorStrengthPreference mVibratorStrength;
    private SwitchPreference restoreOnBootPreference;
    private ListPreference restoreDelayPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.main);

        mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        if (mVibratorStrength != null) {
            mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        }
        restoreOnBootPreference = (SwitchPreference) findPreference(KEY_RESTORE_ON_BOOT);
        Boolean shouldRestore = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DeviceSettings.KEY_RESTORE_ON_BOOT, false);
        restoreOnBootPreference.setChecked(shouldRestore);
        restoreOnBootPreference.setOnPreferenceChangeListener(this);

        restoreDelayPreference = (ListPreference) findPreference(KEY_RESTORE_DELAY);
        int restoreDelay = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString(DeviceSettings.KEY_RESTORE_DELAY, "5"));
        switch (restoreDelay) {
          case 0:
            restoreDelayPreference.setValueIndex(0);
            break;
          case 3:
            restoreDelayPreference.setValueIndex(1);
            break;
          case 5:
            restoreDelayPreference.setValueIndex(2);
            break;
          case 10:
            restoreDelayPreference.setValueIndex(3);
            break;
          default:
            restoreDelayPreference.setValueIndex(2);
            break;
        }
        restoreDelayPreference.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == restoreOnBootPreference) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            boolean value = (Boolean) newValue;
            editor.putBoolean(DeviceSettings.KEY_RESTORE_ON_BOOT, value);
            editor.commit();
        }
        else if (preference == restoreDelayPreference) {
            String delay = newValue.toString();
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putString(DeviceSettings.KEY_RESTORE_DELAY, delay);
            editor.commit();
        }
        return true;
    }
}
