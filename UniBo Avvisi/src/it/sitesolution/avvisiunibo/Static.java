package it.sitesolution.avvisiunibo;


import java.util.ArrayList;
import java.util.Iterator;

public class Static {
	
	static ArrayList<Universita> uni;
	static Universita unicurr;
	static int numUni;
	
	public static void inituni(){
		
		uni = new ArrayList<Universita>();
		unicurr = null;
		
		int j=1;
		uni.add(new Universita("Medicina e Chirurgia", j,
				"http://www.sitesolution.it/software/Avvisi-UniBO/feed/medicina.xml", "#FF1821"));
		j++;
		uni.add(new Universita("Economia Rimini", j,
				"http://www.sitesolution.it/software/Avvisi-UniBO/feed/economia.xml", "#EFAA00"));
		j++;
		uni.add(new Universita("Ingegneria", j,
				"http://www.sitesolution.it/software/Avvisi-UniBO/feed/ingegneria.xml", "#18964A"));
		j++;
		uni.add(new Universita("Farmacia", j,
				"http://www.sitesolution.it/software/Avvisi-UniBO/feed/farmacia.xml", "#FF1810"));
		
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
