package com.ketanolab.simidic.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ketanolab.simidic.R;


public class AdaptadorViewPager extends PagerAdapter {

	private List<String> listaTitulos;
	private List<String> listaSubtitulos;
	private List<CharSequence> listaExtras;
	private List<Integer> listaBitmaps;
	private Context contexto;

	public AdaptadorViewPager(Context contexto) {
		super();
		this.contexto = contexto;
		this.listaTitulos = new ArrayList<String>();
		this.listaSubtitulos = new ArrayList<String>();
		this.listaExtras = new ArrayList<CharSequence>();
		this.listaBitmaps = new ArrayList<Integer>();
	}

	public void adicionarItem(int bitmap, String titulo, String subtitulo, String extra) {
		listaTitulos.add(titulo);
		listaSubtitulos.add(subtitulo);		
		listaExtras.add(Html.fromHtml(extra + "<br /><br /><a href=\"http://creativecommons.org/licenses/by-nc-nd/3.0/deed.es\">Creative Commons-Atribución-NoComercial-SinDerivadas 3.0 Unported</a>"));
		listaBitmaps.add(bitmap);
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		LinearLayout linearLayout = (LinearLayout) View.inflate(contexto, R.layout.diccionario2, null);

		// titulo
		TextView textoTitulo = (TextView) linearLayout.findViewById(R.id.textview_titulo);
		textoTitulo.setText(listaTitulos.get(position));
		// Subtitulo
		TextView textoSubtitulo = (TextView) linearLayout.findViewById(R.id.textview_subtitulo);
		textoSubtitulo.setText(listaSubtitulos.get(position));
		// Extra
		TextView textoExtra = (TextView) linearLayout.findViewById(R.id.textview_extra);
		textoExtra.setText(listaExtras.get(position));
		textoExtra.setMovementMethod(LinkMovementMethod.getInstance());
		// Imagen
		ImageView imagen = (ImageView) linearLayout.findViewById(R.id.imageview_logo);
		imagen.setImageResource(listaBitmaps.get(position));

		((ViewPager) collection).addView(linearLayout, 0);
		return linearLayout;
	}

	@Override
	public int getCount() {
		return listaTitulos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((LinearLayout) view);
	}
	
	public String getNombreProducto(int posicion) {
		return listaTitulos.get(posicion);
	}



}
