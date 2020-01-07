package com.ticm.zorkos.todo.grupe;

import com.ticm.zorkos.database.AdapterKorisnickiUnos;
import com.ticm.zorkos.todo.MainActivity;
import com.ticm.zorkos.todo.NoviZapisi;
import com.ticm.zorkos.todo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	private final SparseArray<Grupa> groups;
	public LayoutInflater inflater;
	public Activity activity;
	private int brojZapisa;
	Context context;
	AdapterKorisnickiUnos da;
	Grupa group;


	public int getBrojZapisa() {
		return brojZapisa;
	}

	public void setBrojZapisa(int brojZapisa) {
		this.brojZapisa = brojZapisa;
	}



	public MyExpandableListAdapter(Context context, Activity act,
			SparseArray<Grupa> groups) {
		this.context = context;
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
		da = new AdapterKorisnickiUnos(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (getChildrenCount(groupPosition) != 0) {
			
			convertView = inflater.inflate(R.layout.listrow_group, null);

			Grupa group = (Grupa) getGroup(groupPosition);
			TextView text = null;

			System.out.println("Broj zapisa: "
					+ getChildrenCount(groupPosition));
			text = (TextView) convertView.findViewById(R.id.textViewGroup);
			text.setText(group.string);

			/**
			 * convertView.setOnLongClickListener(new View.OnLongClickListener()
			 * {
			 * 
			 * @Override public boolean onLongClick(View v) {
			 * 
			 *           String imeGrupe = groupu.string.toString();
			 *           Toast.makeText(activity, imeGrupe,
			 *           Toast.LENGTH_SHORT).show(); return true; }
			 * 
			 *           });
			 **/
			ImageView deleteGroup = null;

			deleteGroup = (ImageView) convertView
					.findViewById(R.id.deleteGroup);

			deleteGroup.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					AlertDialog alertDialog = new AlertDialog.Builder(activity)
							.create();
					
					alertDialog.setTitle("Želite li obrisati sve zapise u grupi?");

					alertDialog.setMessage("Ukupan broj zapisa: " + String.valueOf(getChildrenCount(groupPosition)));

					alertDialog.setButton("DA",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Grupa group = (Grupa) getGroup(groupPosition);
									
									
									da.openToWrite();
									while(getChildrenCount(groupPosition) != 0){
										String dijete = (String) getChild(groupPosition, getChildrenCount(groupPosition)-1);
										String[] zapisDatum = dijete.split("   ");
										String datum = zapisDatum[1].trim();
										
										da.obrisiZapis(group.string, datum);
										group.children.remove(getChildrenCount(groupPosition)-1);
										
									}
									da.close();
									notifyDataSetChanged();
								}
							});
					alertDialog.show();
				}

			});
		} else {
			convertView = inflater.inflate(R.layout.blank, null);
		}
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, final ViewGroup parent) {

		final String children = (String) getChild(groupPosition, childPosition);
		TextView text = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrowdetails, null);
		}
		text = (TextView) convertView.findViewById(R.id.textView1);
		text.setText(children);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(activity, children, Toast.LENGTH_SHORT).show();
			}
		});

		final ImageView delete = (ImageView) convertView
				.findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(activity)
						.create();

				alertDialog.setTitle("Želite li obrisati zapis?");

				alertDialog.setMessage("Potvrda");

				alertDialog.setButton("DA",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String[] zapisDatum = children.split("   ");
								// "^(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)$"
								String zapis = zapisDatum[0].trim();
								String datum = zapisDatum[1].trim();
								Grupa group = (Grupa) getGroup(groupPosition);

								String grupa = group.string.trim();
								System.out.println("Zapis: " + zapis
										+ " Datum: " + datum + " Grupa: "
										+ grupa);
								try {
									da.openToWrite();
									da.obrisiZapis(grupa, zapis, datum);
									da.close();

									group.children.remove(childPosition);
									notifyDataSetChanged();

								} catch (SQLException e) {
									System.out.println("Greska: " + e);
								}
							}
						});
				alertDialog.show();

			}

		});

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	/**
	 * public int dohvatiKljuc(String zapisDjeteta){ for(int i = 0, nsize =
	 * group.children.size(); i < nsize; i++) { String zapisGrupe =
	 * group.children.get(i).toString(); System.out.println(zapisGrupe +
	 * "Stari " + zapisDjeteta); if(zapisGrupe.equals(zapisDjeteta)){
	 * setBrojZapisa(i); } }
	 * 
	 * return brojZapisa; }
	 */
}
