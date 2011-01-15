package com.wifi.sapguestconnect.preferences;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogHelper;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

public class SettingsActivity extends PreferenceActivity 
{
    private LogHelper logHelper;
    private boolean isLogEnabled;
    private Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// Init Log
		logHelper = LogHelper.getLog();
		isLogEnabled = logHelper.isLogEnabled();
		
		// Log
		logHelper.toLog(isLogEnabled, "SettingsActivity -> onCreate () started.");
		
		// super
		super.onCreate(savedInstanceState);
		
		// Init Resources
		resources = getResources();
		
//		// TEST LOCALIZATION
//      String languageToLoad = "he";
//      Locale locale = new Locale(languageToLoad);
//      Locale.setDefault(locale);
//      Configuration config = new Configuration();
//      config.locale = locale;
//      resources.updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//      // END TEST
		
		
		// Set Shared Prefs Name
		String sharedPrefsName = resources.getString(R.string.shared_settings_file_name);
		getPreferenceManager().setSharedPreferencesName(sharedPrefsName);
		
		// Init Preferences from Resource
		addPreferencesFromResource(R.xml.preferences);
		
		// Init Preferences
		initRunAsServicePreferenceUI();
		
		// Init Show Icon Preference
		initShowIconPreferenceUI();
		
		// Init Start At Boot Preference
		initStartAtBootPreferenceUI();
		
//		// Init Enable Connection Sound Preference
//		initEnableConnectSoundPreferenceUI();
		
		// Init Ringtone Preference
		initRingtonePreferenceUI();
	}
	
	
	private void initRunAsServicePreferenceUI()
	{
		// Log
		logHelper.toLog(isLogEnabled, "SettingsActivity -> initRunAsServicePreference () started.");
		
		Preference runAsServicePref = getPreferenceByKey (R.string.pref_settings_run_as_service_key);
		
        runAsServicePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                        	PreferencesFacade.refreshRunAsService(preference.getContext());
                        	
                        	//initShowIconPreferenceUI();
                        	//initStartAtBootPreferenceUI();
                        	
                        	return true;
                        }
        });
	}
	
	private void initShowIconPreferenceUI()
	{
		// Log
		logHelper.toLog(isLogEnabled, "SettingsActivity -> initShowIconPreference () started.");
		
		Preference showIconPref = getPreferenceByKey (R.string.pref_settings_show_icon_key);
		
		showIconPref.setDependency(resources.getString(R.string.pref_settings_run_as_service_key));
		
		showIconPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	PreferencesFacade.refreshShowIcon(preference.getContext());
            
            	return true;
            }
		});

	}
	
	private void initStartAtBootPreferenceUI()
	{
		// Log
		logHelper.toLog(isLogEnabled, "SettingsActivity -> initStartAtBootPreference () started.");
		
        Preference startAtBootPref = getPreferenceByKey (R.string.pref_settings_start_at_boot_key);
        startAtBootPref.setDependency(resources.getString(R.string.pref_settings_run_as_service_key));
	}
	
	private void initRingtonePreferenceUI()
	{
		// Log
		logHelper.toLog(isLogEnabled, "SettingsActivity -> initRingtonePreferenceUI () started.");

        Preference enableConnectSoundPref = getPreferenceByKey (R.string.pref_settings_ringtone_key);
        enableConnectSoundPref.setDependency(resources.getString(R.string.pref_settings_enable_connection_sound_key));		
	}
	
	private Preference getPreferenceByKey(int preferenceKey)
	{
		// Log
		logHelper.toLog(isLogEnabled, "SettingsActivity -> getPreferenceByKey () started.");
		
		String preferenceKeyStr = resources.getString(preferenceKey);
		
        return (Preference) findPreference(preferenceKeyStr);
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	
}
