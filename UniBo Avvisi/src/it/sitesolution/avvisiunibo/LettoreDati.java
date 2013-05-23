package it.sitesolution.avvisiunibo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;


/**
 * 
 * Questa classe serve per andare a caricare i dati da internet o da sd se ofline
 * è la classe piu contorta dell' intero progetto
 * Questa classe si incarica di mandare le notifiche all' utente
 * 
 * @author luca
 *
 */
public class LettoreDati {
	
	// classico context preso dall' activity
	private Context c;
	
	// cartella e file dove salvare i dati
	final String DIR = Environment.getExternalStorageDirectory()+ "/.SiteSolution/AvvisiUnibo/";
	private String file = "";
	
	// prende il contesto delle notifiche
	private NotificationManager notiManager;  
	
	// serve x le notifiche lasciarlo settato cosi
	private static final int APP_ID = 0; 

	// url dell' xml dove trovare i dati degli avvisi in internet
	private String xmlUrl;
	
	// l' universita di riferimento
	Universita uni;

	
	/**
	 * 
	 * E' l'unico costruttore di questa classe
	 * Il context e il notification manager devono essere passati dall' activity nn ricordo il xkè
	 * 
	 * @param c Il context
	 * @param un L' universita dove andare a prendere i feed
	 */
	public LettoreDati(Context c, Universita un) {
		this.c = c;
		xmlUrl = un.getXmlUrl();
		file = un.getNome();
		this.uni = un;
		notiManager =(NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	// Array List che contiene tutti gli avvisi
	ArrayList<Avviso> dati = new ArrayList<Avviso>();

	/**
	 * 
	 * Questa funzione scarica da internet gli avvisi se connessi ad internet
	 * 
	 */
	private ArrayList<Avviso> scaricaDati() {
		Document doc;
		ArrayList<Avviso> aux = new ArrayList<Avviso>();
		Avviso n;
		String testo,link;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL(xmlUrl).openStream());
			Element root = doc.getDocumentElement();
			NodeList avvisi = root.getChildNodes();
			int jjj = 0;
			// grande figata di for che si capisce molto poco ma funziona. ps. nn capisco xkè solo i nodi dispari
			for (int i = 1; i < avvisi.getLength(); i += 2) {
				// setta i colori in alternanza cosi come se sembrasse la bacheca vera
				NodeList lista = avvisi.item(i).getChildNodes();
				testo = "";
				link="";
				for (int j = 1; j < lista.getLength(); j += 2) {
					if(j==1){
						testo = testo + lista.item(j).getTextContent() ;
						//Log.e("ll",j+"");
					}
					if(j==3){
						testo = testo + " \nInserito il  " + lista.item(j).getTextContent() ;
					}
					if(j==5){
						if(lista.item(j).equals("none"))
							testo = testo + " da " + lista.item(j).getTextContent() ;
					}
					if(j==7){
						link = lista.item(j).getTextContent();
					}
				}
				if (jjj % 2 == 0) { 
					n = new Avviso(testo , link , Color.WHITE);
				} else {
					n = new Avviso(testo, link , Color.parseColor("#D3E4E2"));
				}
				aux.add(n); // bella li
				jjj++;
			}
		} catch (Exception e) {
			 //message("dio santissimo :" + e.getMessage());
		}
		return aux;
	}
	
	public ArrayList<Avviso> aggiorna(){
		if(isConnected()){
			controllaNuoviAvvisi();
			return scaricaDati();
		}else{
			
			try {
				message("Non siete connessi a Internet");
				return carica();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		return dati;
	}

	/**
	 * 
	 * Funzione che controlla se ci sono nuovi avvisi se si manda una notifica
	 * 
	 */
	public void controllaNuoviAvvisi() {
		if (isConnected()) {
			dati = scaricaDati();
			try {
				if (new File(DIR, file).exists()) {
					
					Iterator<Avviso> i = dati.iterator(); // iterator dei dati caricati da internet
					ArrayList<Avviso> aux = carica();
					Iterator<Avviso> e = aux.iterator();  // iterator dei dati caricati da sd
					Avviso a, b;
					a = (Avviso) e.next();
					b = (Avviso) i.next();

		    		
					// confronta i primi due avvisi e se sono diversi manda una notifica

					if (!b.getTesto().equals(a.getTesto())) {
					
						notifica("Nuovi avvisi sulla bacheca di "+uni.getNome());
						
						try {
							scrivi();
							
						} catch (Exception u) {
							//message(e.getMessage());
						}
					}
				} else {
					try {
						scrivi();
					} catch (Exception u) {
						//message(e.getMessage());
					}
				}
			} catch (Exception e) {
				 //message(e.getMessage());
			}
		}
	}

	/**
	 *  
	 * Funzione che ritorna un ArrayList degli avvisi
	 * 
	 * @return Ritorna un ArrayList degli avvisi
	 */
	public ArrayList<Avviso> prelevaDati() {

		try {
			controllaNuoviAvvisi();
			dati = carica();

		} catch (Exception e) {

			/* Se entra qui c'è un cacchio di errore che non so dove sia */
			// message("error");
		}
		return dati;
	}

	/**
	 * 
	 * 
	 * 
	 * @return true se connesso a internet, false atrimenti
	 */
	private boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected;
		try {
			isConnected = activeNetwork.isConnectedOrConnecting();
		} catch (Exception e) {
			isConnected = false;
		}
		return isConnected;
	}

	/**
	 * 
	 * Scrive sulla SD gli avvisi
	 * 
	 * @throws Exception
	 */
	private void scrivi() throws Exception {
		new File(DIR).mkdirs();
		File file = new File(DIR + this.file);
		file.delete();
		file.createNewFile();
		FileOutputStream fl = new FileOutputStream(DIR + this.file); 
		PrintStream output = new PrintStream(fl);
		Iterator<Avviso> i = dati.iterator();
		while (i.hasNext()) {
			Avviso aux =  i.next();
			output.println(aux.getTesto());
			output.println(aux.getLink());
		}
		output.close();
	}

	/**
	 * 
	 * Carica dalla SD gli avvisi in caso che nn sia connesso ad intenet
	 * 
	 * @return Un oggetto ArrayList che contiene tutti gli avvisi
	 * @throws IOException
	 */
	private ArrayList<Avviso> carica() throws IOException {
		ArrayList<Avviso> aux = new ArrayList<Avviso>();
		InputStream i = new FileInputStream(DIR + file);
		BufferedReader r = new BufferedReader(new InputStreamReader(i));
		String line,link;
		int jjj = 0;
		while ((line = r.readLine()) != null) {
			r.readLine();
			link=r.readLine();
			r.readLine();
			if (jjj % 2 == 0){
				aux.add(new Avviso(line,link, Color.WHITE));
			}
			else{
				aux.add(new Avviso(line,link, Color.parseColor("#D3E4E2")));
			}
			jjj++;
		}
		r.close();
		return aux;
	}

	/**
	 * 
	 * Funzione che invia una notifica x un nuovo avviso
	 * 
	 * @param m
	 *            Il testo che verrà scritto sulla notifica
	 */
	private void notifica(String m) {


		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
		boolean controlla = false;
		if(Static.unicurr.getN() == uni.getN() && settings.getBoolean("fac"+Static.unicurr.getNome(), false)){
			controlla = true;
		}

		//message(controlla+"");
		if (settings.getBoolean("notifica", false) && controlla) {
			Intent intent = new Intent(c, UniBoAvvisi.class);
			Notification notification = new Notification( 
					R.drawable.unibo, "Nuovi avvisi in bacheca",
					System.currentTimeMillis());

			if (settings.getBoolean("suono", false)) {
				notification.defaults |= Notification.DEFAULT_SOUND;
				notification.defaults |= Notification.DEFAULT_LIGHTS;
			}
			if (settings.getBoolean("vibrazione", false)) {
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				notification.defaults |= Notification.DEFAULT_LIGHTS;
			}

			notification.setLatestEventInfo(c, "AvvisiUnitn", m,
					PendingIntent.getActivity(c, 0, intent, 0));

			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notiManager.notify(APP_ID, notification);
		}

	}

	/**
	 * 
	 * Serve per il debugging
	 * 
	 * @param s
	 *            Stringa del messaggio
	 */
	private void message(String s) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(c, s, duration);
		toast.show();
	}
}
