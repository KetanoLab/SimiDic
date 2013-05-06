package com.ketanolab.simidic.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ketanolab.simidic.R;

public class AdaptadorLista extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Integer> imagenes;
	private List<String> titulos;
	private List<CharSequence> subtitulos;

	public AdaptadorLista(Context contexto) {
		inflater = LayoutInflater.from(contexto);
		imagenes = new ArrayList<Integer>();
		titulos = new ArrayList<String>();
		subtitulos = new ArrayList<CharSequence>();
	}

	public int getCount() {
		return titulos.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_lista, null);
			holder = new ViewHolder();
			holder.imagen = (ImageView) convertView
					.findViewById(R.id.imagen_item);
			holder.titulo = (TextView) convertView
					.findViewById(R.id.titulo_item);
			holder.subtitulo = (TextView) convertView
					.findViewById(R.id.subtitulo_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imagen.setImageResource(imagenes.get(position));
		holder.titulo.setText(titulos.get(position));
		holder.subtitulo.setText(subtitulos.get(position));
		return convertView;
	}

	static class ViewHolder {
		ImageView imagen;
		TextView titulo;
		TextView subtitulo;
	}

	public void adicionarItem(int recurso, String titulo, CharSequence subtitulo) {
		imagenes.add(recurso);
		titulos.add(titulo);
		subtitulos.add(subtitulo);
		notifyDataSetChanged();
	}

	public void adicionarItem(String titulo, CharSequence subtitulo) {
		imagenes.add(0);
		titulos.add(titulo);
		subtitulos.add(subtitulo);
	}

	public void adicionarItem(String titulo) {
		imagenes.add(0);
		titulos.add(titulo);
		subtitulos.add("");
	}
	
	public void eliminarTodo() {
		imagenes.clear();
		titulos.clear();
		subtitulos.clear();
		notifyDataSetChanged();
	}

}
