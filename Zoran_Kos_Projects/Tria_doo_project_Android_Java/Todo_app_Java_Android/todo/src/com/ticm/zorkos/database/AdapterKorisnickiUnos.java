package com.ticm.zorkos.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ticm.zorkos.todo.grupe.Grupa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AdapterKorisnickiUnos {
	public static final String DATABASE_NAME = "baza_podatakaGrupa1.db";
	public static final int DATABASE_VERSION = 1;
	public static final String KEY_ID = "id";
	
	private DBHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;
	
	public AdapterKorisnickiUnos(Context c) {
		context = c;
	}
	
	public AdapterKorisnickiUnos openToRead() throws android.database.SQLException {
		sqLiteHelper = new DBHelper(context, DATABASE_NAME, null,DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		
		return this;
		}
	
		public AdapterKorisnickiUnos openToWrite() throws android.database.SQLException {
		sqLiteHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
		}
		
		public void close() {
		sqLiteHelper.close();
		}
		
		public Cursor sadrzajGrupa(){//vraæanje svih zapisa iz tablice grupa
			Cursor c =sqLiteDatabase.rawQuery("SELECT * FROM Grupa",null);
		return c; 
		}
		
		public long korisnickiUnos(String datum, String grupa, String zapis){//upis korisnièkog unosa u tablicu
			ContentValues contentValues = new ContentValues();
			contentValues.put("Datum", datum);		
			contentValues.put("Grupa", grupa);
			contentValues.put("Zapis", zapis);
			
			return sqLiteDatabase.insert("Korisnicki_unos", null, contentValues);
			}
		
		public long upisGrupe(String grupa){
			ContentValues contentValues = new ContentValues();
			contentValues.put("Grupa", grupa);
			return sqLiteDatabase.insert("Grupe", null, contentValues);
		}
		
		
		public String[] listaZadataka(){//popunjavanje liste s unosima
			Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM Korisnicki_unos",null);
			int n= c.getCount();
			String[] lista= new String[n];
			int i = 0;
			while(c.moveToNext()){
			    String zapis = c.getString(c.getColumnIndex("Zapis"));
			    String datum = c.getString(c.getColumnIndex("Datum"));
			    lista[i] = zapis +"   " +  datum;
			    i++;
			}
			c.close();
			return lista;
			}
		
		public Set<String> listaGrupa(int br){
			Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM Korisnicki_unos",null);
			Set<String> grupacija_nova = (Set<String>) new HashSet<String>();
			Set<String> grupacija_stara = (Set<String>) new HashSet<String>();

			
			while(c.moveToNext()){
				String grupa = c.getString(c.getColumnIndex("Grupa"));
				String datum = c.getString(c.getColumnIndex("Datum"));

				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String date = sdf.format(new Date());
				String pDatum = datum.replace(".", "/");				
				
				try {
					Date tablicaDatum = sdf.parse(pDatum);
					Date trenutniDatum = sdf.parse(date);
					
					if(tablicaDatum.after(trenutniDatum)){
						grupacija_nova.add(grupa);
					}
					if(tablicaDatum.before(trenutniDatum)){
						grupacija_stara.add(grupa);
					}
					if(tablicaDatum.equals(trenutniDatum)){
						grupacija_nova.add(grupa);
					}
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			List<String> l = new ArrayList<String>(grupacija_nova);
			List<String> k = new ArrayList<String>(grupacija_stara);
			Collections.sort(l, ALPHABETICAL_ORDER);
			Collections.sort(k, ALPHABETICAL_ORDER);
			
			grupacija_nova = new HashSet<String>(l);
			grupacija_stara = new HashSet<String>(k);
			c.close();
			if(br == 1){
				return grupacija_stara;
			} 
			else{
				return grupacija_nova;
			}
			
		}
		
		public String[] listaGrupaPojedinacno(String nazivGrupe, int br){
			Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM Korisnicki_unos WHERE Grupa='"+nazivGrupe+"'",null);
			int n= c.getCount();
			String[] listaNoviZ= new String[n];
			String[] listaStariZ= new String[n];
			int i = 0;
			while(c.moveToNext()){
				String zapis = c.getString(c.getColumnIndex("Zapis"));
			    String datum = c.getString(c.getColumnIndex("Datum"));
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String date = sdf.format(new Date());
				
				String pDatum = datum.replace(".", "/");

				
				try {
					Date tablicaDatum = sdf.parse(pDatum);
					Date trenutniDatum = sdf.parse(date);
					
					if(tablicaDatum.after(trenutniDatum)){
						listaNoviZ[i] = zapis +"   "+  datum;
					}
					if(tablicaDatum.before(trenutniDatum)){
						listaStariZ[i] = zapis +"   "+  datum;
					}
					if(tablicaDatum.equals(trenutniDatum)){
						listaNoviZ[i] = zapis +"   "+  datum;
					}
										
				} catch (ParseException e) {
					System.out.println("Greska: " + e);
				}
	        								    
				i++;
			}
			ArrayList<String> list1 = new ArrayList<String>(Arrays.asList(listaNoviZ));
			ArrayList<String> list2 = new ArrayList<String>(Arrays.asList(listaStariZ));
			while(list1.contains("")&&list2.contains("")) {
			    list1.remove("");
			    list2.remove("");
			}
			listaNoviZ = list1.toArray(new String[list1.size()]);	
			listaStariZ = list2.toArray(new String[list2.size()]);
			c.close();
			if(br == 1){
				return listaNoviZ;
			} 
			else{
				return listaStariZ;
			}
		}
		
		public List<String> imenaGrupa(){
			Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM Grupe",null);
			int n= c.getCount();
			List<String> listaImeGrupe= new ArrayList<String>(n);
			int i = 0;
			while(c.moveToNext()){
				String imeGrupe = c.getString(c.getColumnIndex("Grupa"));
				listaImeGrupe.add(imeGrupe);
				i++;
			}
			c.close();
			Collections.sort(listaImeGrupe, new SortIgnoreCase());
			return listaImeGrupe;	
		}
		
		public boolean imeGrupe(String ime){
			Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM Grupe WHERE Grupa='"+ime+"'",null);
			if (c.getCount() >  0 ){
				return true;
			}
			c.close();
			return false;
		}
		
		public void obrisiZapis(String grupa, String zapis, String datum){
			sqLiteDatabase.delete("Korisnicki_unos", "Grupa = ? AND Zapis = ? AND Datum = ?", new String[] {grupa, zapis, datum});
		}
		
		public void obrisiZapis(String grupa, String datum){
			sqLiteDatabase.delete("Korisnicki_unos", "Grupa = ? AND Datum = ?", new String[] {grupa, datum});
		}
		
		
		
		public String getGrupaId(String grupa){
			String id = "";
			Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM Grupe WHERE Grupa='"+grupa+"'",null);
			
			int n= c.getCount();
			List<String> listaImeGrupe= new ArrayList<String>(n);
			int i = 0;
			while(c.moveToNext()){
				id = c.getString(c.getColumnIndex("id_grupe"));
				listaImeGrupe.add(id);
				i++;
			}
			id = listaImeGrupe.get(0);
			System.out.println("ID: " + id);
			c.close();
			return id;
		}
		
		public void azurirajGrupu(String staroIme, String novoIme){
			String idG = getGrupaId(staroIme);
			System.out.println("ID: " + idG + " Staro ime: " + staroIme + " Novo ime: " + novoIme);		
			sqLiteDatabase.delete("Grupe", "id_grupe = ?", new String[] {idG});
			upisGrupe(novoIme);
		}
		
		public void obrisiGrupu(String grupa){
			sqLiteDatabase.delete("Grupe", "Grupa = ?", new String[] {grupa});
		}
		
		
		public void obrisiSve(){
			sqLiteDatabase.delete("Korisnicki_unos", null, null);
			sqLiteDatabase.delete("Grupe", null, null);
		}
		
		public String[] zapisSlobodno(){//popunjavanje liste s unosima
			Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM Korisnicki_unos WHERE Grupa='Zapis'",null);
			int n= c.getCount();
			String[] lista= new String[n];
			int i = 0;
			while(c.moveToNext()){
			    String zapis = c.getString(c.getColumnIndex("Zapis"));
			    String datum = c.getString(c.getColumnIndex("Datum"));
			    lista[i] = zapis +"   " +  datum;
			    i++;
			}
			c.close();
			return lista;
			}
		
		private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
		    public int compare(String str1, String str2) {
		        int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
		        if (res == 0) {
		            res = str1.compareTo(str2);
		        }
		        return res;
		    }
		};

	    @SuppressLint("DefaultLocale")
		public class SortIgnoreCase implements Comparator<Object> {
			public int compare(Object o1, Object o2) {
	            String s1 = (String) o1;
	            String s2 = (String) o2;
	            return s1.toLowerCase().compareTo(s2.toLowerCase());
	        }
	    }
		
		
}
