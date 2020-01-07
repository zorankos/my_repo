package com.ticm.zorkos.todo;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ExpandableListView;

import com.ticm.zorkos.database.AdapterKorisnickiUnos;
import com.ticm.zorkos.todo.grupe.Grupa;
import com.ticm.zorkos.todo.grupe.MyExpandableListAdapter;

public class StariZapisi extends Activity{
	AdapterKorisnickiUnos da = new AdapterKorisnickiUnos(this);
	SparseArray<Grupa> groups = new SparseArray<Grupa>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_novo);
		
		da.openToWrite();
		String[] slobodniZapis = da.zapisSlobodno();
		System.out.println("Broj " + String.valueOf(slobodniZapis.length));
		/**
		if (slobodniZapis.length == 0){
			da.korisnickiUnos("12.03.2014", "Slobodno", "novi zapis");
		}**/
		da.close();
		
		da.openToRead();
		//da.obrisiSve();
		String[] lista = da.listaZadataka();
		Set<String> listaGrupa = da.listaGrupa(1);
		da.close();
		
		String[] listaGrupa1 = listaGrupa.toArray(new String[0]);

		/**
		mainListView = (ListView) findViewById(R.id.listView1);
		
		ArrayList<String> list = new ArrayList<String>();
	    list.addAll(Arrays.asList(lista));

	    listAdapter = new ArrayAdapter<String>(this, R.layout.redovi, list);
	    mainListView.setAdapter(listAdapter);
		**/
	    if(listaGrupa1.length == 0){
	    	napraviListu();
	    }
	    
	    createData(listaGrupa1, 2);
	    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
	    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, this,groups);
	    listView.setAdapter(adapter);
	    
	}
	
	  public void createData(String[] listaGrupa, int br) {  
		    for (int j = 0; j < listaGrupa.length; j++) {
		      Grupa group = new Grupa(listaGrupa[j]);
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
