package it.sitesolution.avvisiunibo;

import java.net.URL;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilderFactory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * La classe servizio serve per controllare se ci sono nuovi avvisi
 * 
 * @author Sitesolution.it
 *
 */

public class Servizio extends Service {

	private Timer timer;

	@Override
	public void onStart(Intent intent, int startId) {

		super.onCreate(); 
		TimerTask task = new TimerTask() { 
			@Override
			public void run() {
				controlla();
			} 
		}; 
		timer = new Timer(); 
		timer.schedule(task, 0, 10 * 60 * 1000); // questa funzione esegue il task  ogni 10 minuti
	}

	private void controlla(){ // banalmente controlla se ci sono nuovi avvisi in bacheca dell'università con id salvato nella variabile static di unitnavvisi.java

		Static.inituni();
		
		Iterator<Universita> i = Static.uni.iterator();
        while(i.hasNext()){
        	//NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        	Universita aux = i.next();
        	LettoreDati lettore = new LettoreDati( getApplicationContext(), aux);
    		lettore.controllaNuoviAvvisi();
        }
        
        String task = "http://www.sitesolution.it/software/Avvisi-UniBo/feed/task.php";
        try {
			DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL(task).openStream());
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
	/**
	 * nn ne ho idea a che cosa serva
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
