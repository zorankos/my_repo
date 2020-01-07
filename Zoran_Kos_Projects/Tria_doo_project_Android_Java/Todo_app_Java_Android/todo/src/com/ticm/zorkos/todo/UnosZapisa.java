package com.ticm.zorkos.todo;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ticm.zorkos.database.AdapterKorisnickiUnos;
import com.ticm.zorkos.todo.grupe.Grupa;

public class UnosZapisa extends Activity {
	private Spinner spinner;
	private Button button2;
	private Button buttonGrupa;
	private Button buttonIzmijeni;
	private Button buttonObrisi;
	private final AdapterKorisnickiUnos da = new AdapterKorisnickiUnos(this);
	Context context = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_text);

		da.openToRead();
		dodajSpinnera();
		da.close();

		dodajButtonSlusaca();

	}

	// Unos grupe
	private void dodajButtonSlusaca() {
		button2 = (Button) findViewById(R.id.prihvatiBtn);
		buttonGrupa = (Button) findViewById(R.id.buttonNovaGrupa);
		buttonIzmijeni = (Button) findViewById(R.id.buttonEditGrupa);
		buttonObrisi = (Button) findViewById(R.id.buttonNovaGrupa);

		buttonGrupa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				LayoutInflater li = (LayoutInflater) context
						.getApplicationContext().getSystemService(
								Context.LAYOUT_INFLATER_SERVICE);
				View promptsView = li.inflate(R.layout.unosgrupe, null);

				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
						context);

				alertBuilder.setView(promptsView);

				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.unos_grupe_text);

				alertBuilder.setCancelable(true).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				final AlertDialog alertDialog = alertBuilder.create();
				alertDialog.show();

				alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								final String tekst = userInput.getText()
										.toString();
								System.out.println("Text: " + tekst);
								if (tekst.trim().length() > 0) {
									da.openToWrite();
									if (da.imeGrupe(tekst) == true) {
										userInput
												.setError("Ime postoji u bazi");
									} else {
										da.upisGrupe(tekst);
										dodajSpinnera();
										alertDialog.dismiss();
									}
									da.close();
								} else {
									userInput.setError("Prazan unos");
								}
							}
						});
			}
		});

		buttonIzmijeni.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				LayoutInflater li = (LayoutInflater) context
						.getApplicationContext().getSystemService(
								Context.LAYOUT_INFLATER_SERVICE);
				View promptsView = li.inflate(R.layout.unosgrupe, null);

				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
						context);

				alertBuilder.setView(promptsView);

				final String grupa = String.valueOf(spinner.getSelectedItem());
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.unos_grupe_text);
				userInput.setText(grupa);
				userInput.setText(grupa);
				alertBuilder.setCancelable(true).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				final AlertDialog alertDialog = alertBuilder.create();
				alertDialog.show();

				alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								final String tekst = userInput.getText()
										.toString();
								System.out.println("Tekst izmjene: " + tekst);
								if (tekst.trim().length() > 0) {
									if (da.imeGrupe(tekst) == true) {
										userInput
												.setError("Ime postoji u bazi");
									} else {
										da.openToWrite();
										da.azurirajGrupu(grupa, tekst);
										dodajSpinnera();
										da.close();
										alertDialog.dismiss();
									}
								} else {
									userInput.setError("Prazan unos");
								}
							}
						});
			}
		});

		buttonObrisi.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(context)
						.create();
				final String grupa = String.valueOf(spinner.getSelectedItem());
				alertDialog.setTitle("Želite li grupu?");
				alertDialog.setButton("DA",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								da.openToWrite();
								da.obrisiGrupu(grupa);
								dodajSpinnera();
								da.close();
							}
						});
				alertDialog.show();
			}
		});

		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);

				final int day = datePicker.getDayOfMonth();
				final int month = datePicker.getMonth() + 1;
				final int year = datePicker.getYear();

				String dan = "";
				if (day < 10)
					dan = "0" + day;
				else
					dan = String.valueOf(day);

				String mjesec = "";
				if (month < 10)
					mjesec = "0" + month;
				else
					mjesec = String.valueOf(month);

				String godina = String.valueOf(year);
				final String datum = dan + "." + mjesec + "." + godina;
				final String grupa = String.valueOf(spinner.getSelectedItem());

				EditText reg1 = (EditText) findViewById(R.id.zapis);
				final String zapis = reg1.getText().toString();

				if (zapis.trim().length() > 0) {
					da.openToWrite();
					da.korisnickiUnos(datum, grupa, zapis);
					da.close();

					showAlertDialog(UnosZapisa.this, "Unos je ispravan!",
							"Potvrdite za povratak!", false);
				} else {
					Toast.makeText(context, "Prazan unos zapisa",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void povratak(View target) {
		Intent i = new Intent(this, MainActivity.class);
		finish();
		startActivity(i);
	}

	private void dodajSpinnera() {
		spinner = (Spinner) findViewById(R.id.spinner1);
		List<String> listaGrupa = da.imenaGrupa();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listaGrupa);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting alert dialog icon
		// alertDialog.setIcon((status) ? R.drawable.azuriraj :
		// R.drawable.azuriraj);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent nextScreen = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(nextScreen);
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
