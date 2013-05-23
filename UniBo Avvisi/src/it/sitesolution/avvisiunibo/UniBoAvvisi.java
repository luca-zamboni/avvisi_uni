package it.sitesolution.avvisiunibo;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;  
import com.actionbarsherlock.app.SherlockFragmentActivity; 
import com.google.ads.AdRequest; 
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 
 * Activity principale nnt da aggiungere
 * 
 * @author luca
 * 
 */
public class UniBoAvvisi extends SherlockFragmentActivity {
	
	private PullToRefreshListView rLW;
	private LettoreDati lettore;
	
	ActionBar ac;

	// static ArrayList notifiche = new ArrayList();
	// E' la list view che tiene tutti gli avvisi
	// private ListView main;

	/* Tutta roba per la pubblicità */
	private AdView adMobView;
	private static final String ADMOB_ID = "a150784e7db5658";
	private AdRequest adMobRequest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newlayout);
		
		
		rLW = (PullToRefreshListView) findViewById(R.id.lista);

		rLW.setOnRefreshListener(new OnRefreshListener<ListView>() {

			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
			
		});
		
		// starta il servizio che andrà a controllare ogni 60 * 10 secondi se ci sono
		// nuovi avvisi
		
		startService(new Intent(this, Servizio.class));

		//startAds();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Static.inituni();
		
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Iterator<Universita> i = Static.uni.iterator();
		int y = 0;
		while (i.hasNext()) {
			Universita aux = i.next();
			if (settings.getBoolean("fac" + aux.getNome(), false)) {
				setTitle(aux.getNome());
				Universita a = new Universita(aux.getNome(), y, aux.getXmlUrl(), aux.getColor());
				Static.unicurr = a;
				y++;
			}
		}
		
		
		
if(Static.unicurr == null){
			
		    ArrayList<Avviso> arr = new ArrayList<Avviso>();
		    String testo = "Vai in impostazioni e scegli la tua università";
		    arr.add(new Avviso(testo , "null" , Color.WHITE));
		    arr.add(new Avviso(testo , "null" , Color.parseColor("#D3E4E2")));
		    arr.add(new Avviso(testo , "null" , Color.WHITE));
		    arr.add(new Avviso(testo , "null" , Color.parseColor("#D3E4E2")));
		    arr.add(new Avviso(testo , "null" , Color.WHITE));
		    arr.add(new Avviso(testo , "null" , Color.parseColor("#D3E4E2")));
		    AlertDialog.Builder ab=new AlertDialog.Builder(this);
		    ab.setTitle("Scegli la tua università");
		    ab.setItems(Static.getAllStringUni(), new OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					
					Static.unicurr = Static.getUniByNumberId(Static.uni, arg1+1);
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				    SharedPreferences.Editor editor = prefs.edit();
				    editor.putBoolean("fac"+Static.unicurr.getNome() , true);
					editor.commit();	
					findViewById(R.id.line).setBackgroundColor(Color.parseColor(Static.unicurr.getColor()));
					lettore = new LettoreDati(getApplicationContext(), Static.unicurr); // inizializzo l lettore
					MioAdapter ad = new MioAdapter(getApplicationContext(), R.layout.unavviso, lettore.prelevaDati()); // all' adapter passo i dati letti dal lettore
					rLW.setAdapter(ad); // passo alla list view l'adapter degli avvisi
					
				}
			});
		    ab.show();
		    
			MioAdapter ad = new MioAdapter(getApplicationContext(), R.layout.unavviso, arr); // all' adapter passo i dati letti dal lettore
			rLW.setAdapter(ad); // passo alla list view l'adapter degli avvisi	
			
		}else{

			findViewById(R.id.line).setBackgroundColor(Color.parseColor(Static.unicurr.getColor())); 
			lettore = new LettoreDati(getApplicationContext(), Static.unicurr); // inizializzo l lettore
			MioAdapter ad = new MioAdapter(getApplicationContext(), R.layout.unavviso, lettore.prelevaDati()); // all' adapter passo i dati letti dal lettore
			rLW.setAdapter(ad); // passo alla list view l'adapter degli avvisi
			
		}
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.mainmenu, (com.actionbarsherlock.view.Menu) menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) { 

			case R.id.impostazioni:
	
				startActivity(new Intent(this, SettingsActivity.class));
				// Starto l'activity impostazioni
	
				break;
	
			case R.id.info:
	
				// quando si dice copiato brutalmente.......
				// Praticamente crea una text view che conterrà il titolo
				// mi è toccato far cosi xkè altrimenti il link non andava
				final TextView message = new TextView(UniBoAvvisi.this);
				final SpannableString s = new SpannableString(
						UniBoAvvisi.this.getText(R.string.testodialog));
				Linkify.addLinks(s, Linkify.WEB_URLS);
				message.setText(s);
				message.setTextColor(Color.WHITE);
				message.setPadding(10, 5, 5, 5);
				message.setMovementMethod(LinkMovementMethod.getInstance());
	
				Dialog dialog = new AlertDialog.Builder(UniBoAvvisi.this)
						.setTitle(R.string.titolodialog).setCancelable(true)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(message).create();
				dialog.show();
	
				break;
	
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			
			try {
				
			} catch (Exception e) {
				
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			if( Static.unicurr!=null){
				lettore = new LettoreDati(getApplicationContext(), Static.unicurr); // inizializzo l lettore
				MioAdapter ad = new MioAdapter(getApplicationContext(), R.layout.unavviso, lettore.aggiorna()); // all' adapter passo i dati letti dal lettore
				rLW.setAdapter(ad); // passo alla list view l'adapter degli avvisi	*/
				// Call onRefreshComplete when the list has been refreshed.
				
			}
			rLW.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private void startAds() {

		AdView adView = new AdView(this, AdSize.BANNER, ADMOB_ID);
		LinearLayout layout = (LinearLayout) findViewById(R.id.pubblicita);
		layout.addView(adView);
		adView.loadAd(new AdRequest());
	}
}
