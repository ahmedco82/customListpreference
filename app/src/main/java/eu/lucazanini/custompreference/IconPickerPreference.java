package eu.lucazanini.custompreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

// @Based on the nice version of LucaZanini. Thank you!
public class IconPickerPreference extends ListPreference {
	private int currentIndex = 0;
	//private Integer[] allIcons= null;
	//private List<Integer> allIcons = null;
	Integer[] allIcons = {R.drawable.nav_business, R.drawable.nav_health, R.drawable.nav_sports};

	private class CustomListPreferenceAdapter extends ArrayAdapter<IconItem> {

		private Context context;
		private List<IconItem>icons;
		private int resource;

		//Integer[] allIcons = {R.drawable.nav_business, R.drawable.nav_health, R.drawable.nav_sports};
		public CustomListPreferenceAdapter(Context context, int resource, List<IconItem> objects) {
			super(context, resource, objects);
			this.context = context;
			this.resource = resource;
			this.icons = objects;

		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		@Override
	   public View getView(final int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		  //Log.i("GET_getView","Don");
		   if(convertView == null) {
			 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  convertView = inflater.inflate(resource, parent, false);
			   holder = new ViewHolder();
				holder.iconName = (TextView) convertView.findViewById(R.id.iconName);
				 holder.iconImage = (ImageView) convertView.findViewById(R.id.iconImage);
				  holder.radioButton = (RadioButton) convertView.findViewById(R.id.iconRadio);
				   convertView.setTag(holder);
		       }else {
			    holder = (ViewHolder) convertView.getTag();
			}


			holder.iconName.setText(icons.get(position).name);

			// int identifier = context.getResources().getIdentifier( icons.get(position).file, "drawable", context.getPackageName());
			// holder.iconImage.setImageResource(identifier);
			// set Icons :
		    // allIcons = new Integer[]{R.drawable.nav_business, R.drawable.nav_health, R.drawable.nav_sports};
		     holder.iconImage.setImageResource(allIcons[position]);
			  holder.radioButton.setChecked(icons.get(position).isChecked);
			   convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				ViewHolder holder = (ViewHolder) v.getTag();
				 for(int i = 0; i < icons.size(); i++) {
					if(i == position) {
						icons.get(i).isChecked = true;
						}else {
						 icons.get(i).isChecked = false;
						}
					}
					getDialog().dismiss();
				}
			});
			return convertView;
		}
	}

	private class IconItem {

		private int file;
		private boolean isChecked;
		private String  name;

		public IconItem(CharSequence name, int file, boolean isChecked) {
			this(name.toString(), file, isChecked);
		}
		public IconItem(String name, int file, boolean isChecked) {
			this.name = name;
			this.file = file;
			this.isChecked = isChecked;
		}
	}

	private class ViewHolder {
		protected ImageView  iconImage;
		protected TextView   iconName;
		protected RadioButton radioButton;
	}

	private Context context;
	private ImageView icon;

	private Integer[] iconFile;
	private CharSequence[] iconName;
	private List<IconItem> icons;
	private SharedPreferences preferences;
	private Resources resources;
	private String  defaultIconFile;
	private int selectedIconFile;
	private TextView summary;

	public IconPickerPreference(Context context, AttributeSet attrs) {
	  super(context, attrs);
		this.context = context;
		 resources = context.getResources();
		  preferences = PreferenceManager.getDefaultSharedPreferences(context);
		   TypedArray a = context.getTheme().obtainStyledAttributes( attrs, R.styleable.attrs_icon, 0, 0);
		  try{
			defaultIconFile = a.getString(R.styleable.attrs_icon_iconFile);
		} finally {
			a.recycle();
		}
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		CharSequence[] entries = getEntries();
		CharSequence[] values = getEntryValues();
		//selectedIconFile = values[currentIndex].toString();
		 selectedIconFile = allIcons[currentIndex];
		 icon = (ImageView) view.findViewById(R.id.iconSelected);
		 updateIcon();
		 summary = (TextView) view.findViewById( R.id.summary);
		 summary.setText( entries[ currentIndex]);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
	  super.onDialogClosed(positiveResult);
		if(icons != null){
	     Log.i("size_0",""+icons.size());
		   for(int i = 0; i<iconName.length; i++) {
            IconItem item = icons.get(i);
			 if(item.isChecked){
			  persistString( ""+i);
			    currentIndex = i;
				 selectedIconFile = item.file;
					 updateIcon();
					summary.setText(item.name);
					break;
				}
			}
		}
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
	  String number = "0";
		if (restorePersistedValue) {
			// Restore existing state
			number = this.getPersistedString( "0");
		} else {
			persistString( number);
		}
		try {
			currentIndex = Integer.parseInt(number);
		} catch( Exception e) {
			; // skip any error, it will be corrected to 0
		}
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
	 builder.setNegativeButton("Cancel", null);
	  builder.setPositiveButton(null, null);
	   iconName = getEntries();
		 iconFile = allIcons;
		   if(iconName == null || iconFile == null || iconName.length != iconFile.length) {
		    throw new IllegalStateException("IconPickerPreference requires an entries array and an entryValues array which are both the same length");
		  }
		 icons = new ArrayList<IconItem>();
		 for(int i = 0; i < iconName.length; i++) {
	      IconItem item = new IconItem(iconName[i], iconFile[i], (i==currentIndex));
	     icons.add(item);
		}
	    CustomListPreferenceAdapter customListPreferenceAdapter = new CustomListPreferenceAdapter(context,R.layout.item_picker , icons);
		builder.setAdapter(customListPreferenceAdapter , null);
	}


	private void updateIcon(){
		//int identifier = resources.getIdentifier( selectedIconFile, "drawable", context.getPackageName());
		icon.setImageResource(selectedIconFile);
		//icon.setTag(selectedIconFile);
		Log.i("GET_CurrentValue",""+selectedIconFile);
	}
}


   /*
	Integer[] orbitalBodies = { R.drawable.nav_business , R.drawable.nav_health , R.drawable.nav_sports };
	 // int identifier = context.getResources().getIdentifier(icons.get(position).file, "drawable", context.getPackageName());
	 holder.iconImage.setImageResource(orbitalBodies[position]);
   */