package it.sitesolution.avvisiunibo; 

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * Classe abbastanza copiata e poco capita ma se va meglio cosi
 * 
 * @author luca
 *
 */
public class MioAdapter extends ArrayAdapter {

    private int resource;
    Context context;
    ArrayList<Avviso> data;
    static boolean color;

	public MioAdapter ( Context context, int resourceId, ArrayList<Avviso> objects) {
            super( context, resourceId, objects );
            data=objects;
            resource = resourceId;
            LayoutInflater.from( context );
            this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = convertView;
        Aux h = null;
        
        if(row == null)
        {
        	LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(resource, parent, false);
            h = new Aux();
            h.txtTitle = (TextView)row.findViewById(R.id.titleavv);
           
            row.setTag(h);
        }
        else
        {
            h = (Aux)row.getTag();
        }
        
        // Copiato fino a qui //
        
        // Praticamente prende tutti i dati e poi gli da un titolo un colore
		Iterator<Avviso> i= data.iterator();
        int j=0;
        while(i.hasNext()){
        	Avviso aux;
        	aux = i.next();
        	if(j==position){
        		h.txtTitle.setText(aux.getTesto());
                h.txtTitle.setBackgroundColor(aux.getC());
                h.txtTitle.setTextColor(Color.BLACK);
                if(aux.getLink()!=null && aux.getLink()!=""){
                	h.txtTitle.setOnClickListener(new MyClickListener(j, context,  data));
                }
        	}
        	j++;
        }
        
        return row;
	}
    
    // classe ausiliaria che serve come aiuto
    private class Aux{
        TextView txtTitle;
    }
}