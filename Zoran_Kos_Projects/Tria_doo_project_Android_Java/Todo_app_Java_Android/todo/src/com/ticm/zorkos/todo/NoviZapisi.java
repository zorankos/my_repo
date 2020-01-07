package com.ticm.zorkos.todo;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.ticm.zorkos.database.AdapterKorisnickiUnos;
import com.ticm.zorkos.todo.grupe.Grupa;
import com.ticm.zorkos.todo.grupe.MyExpandableListAdapter;

public class NoviZapisi extends Activity{

	AdapterKorisnickiUnos da = new AdapterKorisnickiUnos(this);

	SparseArray<Grupa> groups = new SparseArray<Grupa>();
	Grupa group; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_novo);
		
		da.openToRead();
		//da.obrisiSve();
		Set<String> listaGrupa = da.listaGrupa(2);
		da.close();
		
		String[] listaGrupa1 = listaGrupa.toArray(new String[0]);

	    if(listaGrupa1.length == 0){
	    	napraviListu();
	    }
	    
	    createData(listaGrupa1, 1);
	    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
	    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, this, groups);
	    listView.setAdapter(adapter); 
	    
	}
	
	  public void createData(String[] listaGrupa, int br) {  
		    for (int j = 0; j < listaGrupa.length; j++) {
		    	group = new Grupa(listaGrupa[j]);
				da.openToRead();
				String[] zapisGrupe = da.listaGrupaPojedinacno(listaGrupa[j], br);
				da.close();
		      for (int i = 0; i < zapisGrupe.length; i++) {
		    	if(zapisGrupe[i] != null)
		        group.children.add(zapisGrupe[i]);
		      }
		      groups.append(j, group);
		    }
		  }
	  

	
	public void prihvatiUnos(View target){
		Intent i = new Intent(this, UnosZapisa.class);
		finish();
		startActivity(i);
	}

	public void napraviListu(){
		Grupa group = new Grupa("Zapis");
		groups.append(0, group);
	}

}
