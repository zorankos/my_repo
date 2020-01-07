package com.ticm.zorkos.todo;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.ticm.zorkos.database.AdapterKorisnickiUnos;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity{
	 AdapterKorisnickiUnos da = new AdapterKorisnickiUnos(this);
	 
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		da.openToWrite();
		String[] lista = da.listaZadataka();
		if(lista.length==0){
		da.upisGrupe("Zapis");
		}
		da.close();
		
		Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
 
		Intent intentAndroid = new Intent().setClass(this, StariZapisi.class);
		TabSpec tabSpecAndroid = tabHost
		  .newTabSpec("Android")
		  .setIndicator("Stari zapisi", ressources.getDrawable(R.drawable.abc_ab_solid_dark_holo))
		  .setContent(intentAndroid);
 
		Intent intentApple = new Intent().setClass(this, NoviZapisi.class);
		TabSpec tabSpecApple = tabHost
		  .newTabSpec("Apple")
		  .setIndicator("Novi zapisi", ressources.getDrawable(R.drawable.abc_ab_solid_dark_holo))
		  .setContent(intentApple);
 
 
		// add all tabs 
		tabHost.addTab(tabSpecAndroid);
		tabHost.addTab(tabSpecApple);

		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(1);
	}
	
	public void prihvatiUnos(View target){
		Intent i = new Intent(this, UnosZapisa.class);
		startActivity(i);
	}
	
 
}