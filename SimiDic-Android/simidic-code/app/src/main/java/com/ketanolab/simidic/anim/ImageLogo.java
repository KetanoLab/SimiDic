package com.ketanolab.simidic.anim;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class ImageLogo implements ViewFactory {

	private Context context;

	public ImageLogo(Context context) {
		this.context = context;
	}

	public View makeView() {
		ImageView imageView = new ImageView(context);
		return imageView;
	}

}
