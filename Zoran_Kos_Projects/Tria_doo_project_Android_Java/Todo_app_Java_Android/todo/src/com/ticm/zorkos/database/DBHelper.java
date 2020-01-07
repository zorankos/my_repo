package com.ticm.zorkos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {//kreiranje baze podataka
		//String upit1 = "CREATE TABLE Korisnik (id_korisnika INTEGER, Ime_korisnika TEXT);";
		String upit2 = "CREATE TABLE Korisnicki_unos (id_zapisa INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Grupa TEXT, Datum TEXT, Zapis TEXT);";
		String upit3 = "CREATE TABLE Grupe (id_grupe INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Grupa TEXT);";
		//db.execSQL(upit1);
		db.execSQL(upit2);
		db.execSQL(upit3);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
