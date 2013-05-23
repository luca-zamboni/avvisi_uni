package it.sitesolution.avvisiunitn;

import java.util.Iterator;
import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * 
 * La classe servizio serve per controllare se ci sono nuovi avvisi
 * 
 * @author Sitesolution.it
 *
 */

public class Servizio extends Service {

	private Timer timer;
	private Handler hand = new Handler();
	
	private Runnable timedTask = new Runnable(){

		public void run() {
			controlla();
			hand.postDelayed(timedTask, /*10 * 60 */ 1000);
		}

	};

	@Override
	public void onStart(Intent intent, int startId) {

		super.onCreate();
		
		hand.post(timedTask);
	}

	private void controlla(){ // banalmente controlla se ci sono nuovi avvisi in bacheca dell'università con id salvato nella variabile static di unitnavvisi.java

		Static.inituni();
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Iterator<Universita> i = Static.uni.iterator();
		int y = 0;
		while (i.hasNext()) {
			Universita aux = i.next();
			if (settings.getBoolean("fac" + aux.getNome(), false)) {
				Universita a = new Universita(aux.getNome(), y, aux.getXmlUrl(), aux.getColor());
				Static.unicurr = a;
				y++;
			}
		}
    	LettoreDati lettore = new LettoreDati( getApplicationContext(), Static.unicurr);
		lettore.controllaNuoviAvvisi();

	}
	
	/**
	 * nn ne ho idea a che cosa serva
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
