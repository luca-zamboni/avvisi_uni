package it.sitesolution.avvisiunibo;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;


public class MyClickListener implements android.view.View.OnClickListener{

	
	int index=0;
	Context c;
	ArrayList<Avviso> list;
	public MyClickListener(int index,Context c,ArrayList<Avviso> list){
		super();
		this.index=index;
		this.c=c;
		this.list=list;
	}

	public void onClick(View arg0) {
		int j=0;
		Iterator<Avviso> i = list.iterator();
        while(i.hasNext()){
        	Avviso aux;
        	aux = i.next();
        	if(j==this.index){		
        		String url = aux.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try{
                    c.startActivity(intent);
                }catch (Exception e) {
					// TODO: handle exception
				}
        	}
        	j++;
        }
		
	}
	
}
