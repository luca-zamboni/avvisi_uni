package it.sitesolution.avvisiunitn;

import android.graphics.Color;

/**
 * 
 * Classe che serve a salvare i dettagli di ogni singolo avvisi
 * 
 * testo Il testo
 * link  Il link
 * c     Il colore 
 * 
 * @author luca
 * 
 */
public class Avviso {
 

	private String testo;
	private String link="";
	private int c; 
	
	/**
	 * 
	 * Costruttore di default degli avvisi
	 * 
	 * @param t è il testo dell' avviso
	 * @param l è il link dell' avviso
	 */
	public Avviso(String t,String l){
		testo=t.replace("\n", ""); 
		if(l!=null)
			link=l;
		else
			link="";
	}
	
	/**
	 * 
	 * Costruttore come quello sopra ma setti anche il colore
	 * 
	 * @param t è il testo dell' avviso
	 * @param l è il link dell' avviso nn serve piu a un cazzo ma lo lascio xkè cosi nn devo cambiare codice da altre parti
	 * @param c è il colore
	 */
	public Avviso(String t,String l,int c){
		testo=t.replace("\n", ""); 
		link=l;
		this.c=c;
	}
	
	/**
	 *    Per confrontare due avvisi confronta i due testi
	 */
	@Override
	public boolean equals(Object o) {
		Avviso aux = (Avviso) o;
		return testo == aux.testo;
	}
	
	@Override
	public String toString() {
		return testo;
	}
	
	/**
	 * 
	 * @return il titolo dell' avviso
	 */
	public String getTesto() {
		return testo;
	}

	/**
	 * 
	 * @param testo setta il testo dell' avviso
	 */
	public void setTesto(String testo) {
		this.testo = testo;
	}

	/**
	 * 
	 * @return il link dell' avviso
	 */
	public String getLink() {
		return link;
	}

	/**
	 * 
	 * @param link setta il link dell' avviso
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * 
	 * @return il colore
	 */
	public int getC() {
		return c;
	}

	/**
	 * 
	 * @param c è il colore da settare
	 */
	public void setC(int c) {
		this.c = c;
	}
	
	
}
