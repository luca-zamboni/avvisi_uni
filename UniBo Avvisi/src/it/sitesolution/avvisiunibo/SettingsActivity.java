package it.sitesolution.avvisiunibo;

import it.sitesolution.avvisiunibo.R;

import java.util.Iterator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * 
 * Activity delle impsotazioni
 * 
 * @author luca
 *
 */
public class SettingsActivity extends SherlockActivity { 
	
	private CheckBox notifica;
	private CheckBox suono; 
	private CheckBox vibrazione;
	
	private SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		setTitle("Impostazioni");
		ActionBar a = getSupportActionBar();
		a.setDisplayHomeAsUpEnabled(true);
		
		notifica = (CheckBox) findViewById(R.id.notifica);
		suono = (CheckBox) findViewById(R.id.suono);
		vibrazione = (CheckBox) findViewById(R.id.vibrazione);
		
		//////////Carico le impsotazioni //////////
		settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		notifica.setChecked(settings.getBoolean("notifica", true));
		suono.setChecked(settings.getBoolean("suono", false));
		vibrazione.setChecked(settings.getBoolean("vibrazione", true));
		
		ViewGroup layout = (ViewGroup) findViewById(R.id.prin);
		for(int i = 1; i< Static.uni.size()+1;i++){
			
			Universita aux = Static.getUniByNumberId(Static.uni, i);
			CheckBox tv = new CheckBox(this);
    		tv.setText(aux.getNome());
    		tv.setBackgroundColor(Color.parseColor(aux.getColor()));
    		tv.setTextColor(Color.WHITE);
    		tv.setId(1000 + aux.getN() * 10);
    		tv.setChecked(settings.getBoolean("fac"+aux.getNome(), false));
    		tv.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				    SharedPreferences.Editor editor = prefs.edit();
				    
				    CheckBox au = (CheckBox) v;
				    Iterator<Universita> i = Static.uni.iterator();
					while (i.hasNext()) {
						Universita aux = i.next();
						editor.putBoolean("fac"+aux.getNome() , false);
						CheckBox c = (CheckBox) findViewById(1000 + aux.getN() * 10);
						c.setChecked(false);
					}
					
				    Universita a = Static.getUniByNumberId(Static.uni, (v.getId() - 1000) / 10);
				    editor.putBoolean("fac"+a.getNome() , true);
				    au.setChecked(true);
					
					editor.commit();		
				}
			});
    		layout.addView(tv);			
        }
		
		controlla();
		notifica.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				controlla();
			}
		});
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()){

	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intentHome = new Intent(this, UniBoAvvisi.class);
	            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intentHome);
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * 
	 * Questa funzione controlla se la checkbox notifica è selezionata
	 * in caso affermativo abilita anche le checkbox di vibrazione e suono
	 * altrimenti le disabilita
	 * 
	 */
	private void controlla(){
		if(!notifica.isChecked()){  // controlla sela checkbox notifica è checkkata se si abilita le altre se no le disabilita
			
			suono.setEnabled(false);
			vibrazione.setEnabled(false); 
			suono.setChecked(false);
			vibrazione.setChecked(false);
			suono.setTextColor(this.getResources().getColor(R.color.grigiastro));
			vibrazione.setTextColor(this.getResources().getColor(R.color.grigiastro));
			
		}else{
			
			suono.setEnabled(true); 
			vibrazione.setEnabled(true);
			suono.setTextColor(this.getResources().getColor(R.color.black));
			vibrazione.setTextColor(this.getResources().getColor(R.color.black));
	        
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		////////// Vado a salvare le impsotazioni //////////
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    SharedPreferences.Editor editor = prefs.edit();

	    
		editor.putBoolean("notifica", notifica.isChecked());
		editor.putBoolean("suono", suono.isChecked());
		editor.putBoolean("vibrazione", vibrazione.isChecked());
		
		editor.commit();
		
		////////// Qui finisce di salvare le impostazioni //////////
		
		Intent inte = getBaseContext().getPackageManager()
	             .getLaunchIntentForPackage( getBaseContext().getPackageName() );
		inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//startActivity(inte);
	}

}



