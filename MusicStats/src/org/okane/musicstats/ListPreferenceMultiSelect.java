package org.okane.musicstats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListPreferenceMultiSelect extends ListPreference {

	private String checkAllKey;
	private String separator;
	private boolean[] mClickedDialogEntryIndices;

	private static final String DEFAULT_SEPARATOR = "OV=I=XseparatorX=I=VO"; 

	public ListPreferenceMultiSelect(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ListPreferenceMultiSelect);
		checkAllKey = ta.getString(R.styleable.ListPreferenceMultiSelect_checkAll);
		String s = ta.getString(R.styleable.ListPreferenceMultiSelect_separator);
		if (s != null) {
			separator = s;
		} else {
			separator = DEFAULT_SEPARATOR;
		}
		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[getEntries().length];
	}

	public ListPreferenceMultiSelect(Context context) {
		this(context, null);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		CharSequence[] entries = getEntries();
		CharSequence[] entryValues = getEntryValues();

		if (entries == null || entryValues == null || entries.length != entryValues.length ) {
			throw new IllegalStateException(
					"ListPreference requires an entries array and an entryValues array which are both the same length");
		}

		restoreCheckedEntries();
		builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices, new DialogInterface.OnMultiChoiceClickListener() {
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isCheckAllValues(which)) {
					checkAll(dialog, isChecked);
				}
				mClickedDialogEntryIndices[which] = isChecked;
			}
		});
	}

	protected void checkAll(DialogInterface dialog, boolean isChecked) {
		ListView listView = ((AlertDialog) dialog).getListView();
		for (int i = 0; i < listView.getCount(); i++) {
			listView.setItemChecked(i, isChecked);
			mClickedDialogEntryIndices[i] = isChecked;
		}
	}

	protected boolean isCheckAllValues(int which) {
		final CharSequence[] entryValues = getEntryValues();
		if (checkAllKey != null) {
			return entryValues[which].equals(checkAllKey);
		}
		return false;
	}

	private void restoreCheckedEntries() {
		CharSequence[] entryValues = getEntryValues();
		String[] vals = parseStoredValues(getValue());

		if (vals != null) {
			List<String> valuesList = Arrays.asList(vals);
			for (int i = 0; i < entryValues.length; i++) {
				CharSequence entryValue = entryValues[i];
				if (valuesList.contains(entryValue)) {
					mClickedDialogEntryIndices[i] = true;
				}
			}
		}
	}

	private String[] parseStoredValues(CharSequence value) {
		if (value == null || "".equals(value)) {
			return null;
		} else {
			return ((String)value).split(DEFAULT_SEPARATOR);
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		List<String> values = new ArrayList<String>();

		CharSequence[] entryValues = getEntryValues();
		if (positiveResult && entryValues != null) {
			for ( int i=0; i<entryValues.length; i++ ) {
				if ( mClickedDialogEntryIndices[i] == true ) {
					// Don't save the state of check all option - if any
					String val = (String) entryValues[i];
					if(checkAllKey == null || (val.equals(checkAllKey) == false)) {
						values.add(val);
					}
				}
			}

			if (callChangeListener(values)) {
				setValue(join(values, separator));
			}
		}
	}

	protected static String join( Iterable<? extends Object> pColl, String separator )
	{
		Iterator<? extends Object> oIter;
		if (pColl == null || (!( oIter = pColl.iterator()).hasNext()))
			return "";
		StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
		while (oIter.hasNext())
			oBuilder.append(separator).append(oIter.next());
		return oBuilder.toString();
	}

	// TODO: Would like to keep this static but separator then needs to be put in by hand or use default separator "OV=I=XseparatorX=I=VO"...
	/**
	 * 
	 * @param straw String to be found
	 * @param haystack Raw string that can be read direct from preferences
	 * @param separator Separator string. If null, static default separator will be used
	 * @return boolean True if the straw was found in the haystack
	 */
	public static boolean contains(String straw, String haystack, String separator){
		if(separator == null) {
			separator = DEFAULT_SEPARATOR;
		}
		String[] vals = haystack.split(separator);
		for(int i=0; i<vals.length; i++){
			if(vals[i].equals(straw)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void setEntries(CharSequence[] entries) {
		super.setEntries(entries);
		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[entries.length];
	}
}
