package eu.lucazanini.custompreference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

// http://www.lucazanini.eu/en/2014/android/display-icon-preferences/#comment-29209

public class IconPickerFragment extends PreferenceFragment {

  @Override
    public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
    }

}
