package eu.lucazanini.custompreference;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PreferencesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	 Fragment iconPickerFragment = new IconPickerFragment();
	 FragmentManager fm = getFragmentManager();
	 FragmentTransaction ft = fm.beginTransaction();
	 ft.replace(android.R.id.content, iconPickerFragment);
	 ft.commit();

    }

}
