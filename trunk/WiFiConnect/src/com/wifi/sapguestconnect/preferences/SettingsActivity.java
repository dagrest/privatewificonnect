package com.wifi.sapguestconnect.preferences;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;

public class SettingsActivity extends PreferenceActivity 
{
    private Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// Log
		LogManager.LogFunctionCall("SettingsActivity", "onCreate()");
		
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
		
		// Init Location Settings UI
		initLocationSettingsUI();
		
		// Init Location Custom Settings UI
		initLocationCustomSettingsUI();
	}
	
	
	private void initRunAsServicePreferenceUI()
	{
		// Log
		LogManager.LogFunctionCall("SettingsActivity", "initRunAsServicePreferenceUI()");
		
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
		LogManager.LogFunctionCall("SettingsActivity", "initShowIconPreferenceUI()");
		
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
		LogManager.LogFunctionCall("SettingsActivity", "initStartAtBootPreferenceUI()");
		
        Preference startAtBootPref = getPreferenceByKey (R.string.pref_settings_start_at_boot_key);
        startAtBootPref.setDependency(resources.getString(R.string.pref_settings_run_as_service_key));
	}
	
	private void initRingtonePreferenceUI()
	{
		// Log
		LogManager.LogFunctionCall("SettingsActivity", "initRingtonePreferenceUI()");

        Preference enableConnectSoundPref = getPreferenceByKey (R.string.pref_settings_ringtone_key);
        enableConnectSoundPref.setDependency(resources.getString(R.string.pref_settings_enable_connection_sound_key));		
	}

	private void initLocationSettingsUI()
	{
		LogManager.LogFunctionCall("SettingsActivity", "initLocationSettingsUI()");
		
		Preference locationSettingsPref = getPreferenceByKey (R.string.pref_settings_location_key);
		locationSettingsPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String location = (String) newValue;
				
				if (location.equalsIgnoreCase(resources.getString(R.string.pref_settings_location_custom_id)))
				{
					setLocationCustomSettingsUIEnabled(true);
				}
				else
				{
					setLocationCustomSettingsUIEnabled(false);
				}
				
				return true;
			}
		});
	}
	
	private void initLocationCustomSettingsUI()
	{
		// Log
		LogManager.LogFunctionCall("SettingsActivity", "initLocationCustomSettingsUI()");
		
		setLocationCustomSettingsUIEnabled(PreferencesFacade.isUseCustomLocation(this));
	}
	
	private void setLocationCustomSettingsUIEnabled(boolean isEnabled)
	{
		Preference customSettingsPref = getPreferenceByKey (R.string.pref_settings_location_custom_settings_key);
		customSettingsPref.setEnabled(isEnabled);
	}
	
	private Preference getPreferenceByKey(int preferenceKey)
	{
		// Log
		LogManager.LogFunctionCall("SettingsActivity", "preferenceKey()");
		
		String preferenceKeyStr = resources.getString(preferenceKey);
		
        return (Preference) findPreference(preferenceKeyStr);
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		
		LogManager.LogFunctionCall("SettingsActivity", "onResume()");
	}
	
}
