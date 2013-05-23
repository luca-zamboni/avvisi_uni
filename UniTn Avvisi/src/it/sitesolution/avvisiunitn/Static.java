package it.sitesolution.avvisiunitn;

import java.util.ArrayList;
import java.util.Iterator;

public class Static {
	
	static ArrayList<Universita> uni;
	static Universita unicurr;
	static int numUni;
	
	public static void inituni(){
		
		uni = new ArrayList<Universita>();
		unicurr = null;

		int j = 1;
		uni.add(new Universita("Scienze", j,
				"http://www.sitesolution.it/software/Avvisi-UniTN/feed/scienze.xml", "#32cd32"));
		j++;
		uni.add(new Universita("Ingegneria", j,
				"http://www.sitesolution.it/software/Avvisi-UniTN/feed/ingegneria.xml", "#339999"));
		j++;
		uni.add(new Universita("Economia", j,
				"http://www.sitesolution.it/software/Avvisi-UniTN/feed/economia.xml", "#cc9933"));
		j++;
		uni.add(new Universita("Giurisprudenza", j,
				"http://www.sitesolution.it/software/Avvisi-UniTN/feed/giurisprudenza.xml",  "#003399"));
		j++;
		uni.add(new Universita("Lettere e Filosofia", j,
				"http://www.sitesolution.it/software/Avvisi-UniTN/feed/lettere-e-filosofia.xml","#660000" ));
		j++;
		uni.add(new Universita("Sociologia", j,
				"http://www.sitesolution.it/software/Avvisi-UniTN/feed/sociologia.xml","#cc3300"));
		
		numUni = j;
	}
	
	public static String[] getAllStringUni(){
		Iterator<Universita> i = uni.iterator();
		String [] uniString = new String[numUni];
		while(i.hasNext()){
			Universita aux = i.next();
			uniString[aux.getN()-1] = aux.getNome();
		}
		return uniString;
	}

	public static Universita getUniByNumberId(ArrayList<Universita> arr,int index) {
		
		Iterator<Universita> i = arr.iterator();
		while(i.hasNext()){
			Universita u = i.next();
			if(u.getN() == index){
				return u;
			}
		}
		
		return null;
	}
	
}
