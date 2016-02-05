package com.ketanolab.simidic.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ketanolab.simidic.R;

public class DownloadsListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<Integer> images;
	private ArrayList<String> names;
	private ArrayList<String> authors;
	private ArrayList<String> descriptions;
	private ArrayList<String> extras;
	private ArrayList<Boolean> bars;
	private ArrayList<Integer> values;

	public DownloadsListAdapter(Context contexto) {
		inflater = LayoutInflater.from(contexto);
		images = new ArrayList<Integer>();
		names = new ArrayList<String>();
		authors = new ArrayList<String>();
		descriptions = new ArrayList<String>();
		extras = new ArrayList<String>();
		bars = new ArrayList<Boolean>();
		values = new ArrayList<Integer>();
	}

	public int getCount() {
		return names.size();
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
			convertView = inflater.inflate(R.layout.download_list_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.descargaImageView);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.nameItem);
			holder.authorTextView = (TextView) convertView
					.findViewById(R.id.authorItem);
			holder.descriptionTextView = (TextView) convertView
					.findViewById(R.id.descriptionItem);
			holder.sizeTextView = (TextView) convertView
					.findViewById(R.id.extraItem);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageResource(images.get(position));
		holder.nameTextView.setText(names.get(position));
		holder.authorTextView.setText(authors.get(position));
		holder.descriptionTextView.setText(descriptions.get(position));
		holder.sizeTextView.setText(extras.get(position));
		if (bars.get(position)) {
			holder.progressBar.setVisibility(View.VISIBLE);
		} else {
			holder.progressBar.setVisibility(View.GONE);
		}
		holder.progressBar.setProgress(values.get(position));
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView nameTextView;
		TextView authorTextView;
		TextView descriptionTextView;
		TextView sizeTextView;
		ProgressBar progressBar;
	}

	public void adicionarItem(int image, String name, String author,
			String description, String extra) {
		images.add(image);
		names.add(name);
		authors.add(author);
		descriptions.add(description);
		extras.add(extra);
		bars.add(false);
		values.add(0);
		notifyDataSetChanged();
	}

	public void updateItem(int position, int image, String text, boolean bar) {
		images.set(position, image);
		extras.set(position, text);
		bars.set(position, bar);
		notifyDataSetChanged();
	}
	public void updateProgress(int position, int progress) {
		values.set(position, progress);
	}

	public void eliminarTodo() {
		images.clear();
		names.clear();
		authors.clear();
		descriptions.clear();
		extras.clear();
		notifyDataSetChanged();
	}

}
