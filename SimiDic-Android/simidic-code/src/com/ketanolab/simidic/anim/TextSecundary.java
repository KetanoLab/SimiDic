package com.ketanolab.nusimi.anim;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.ketanolab.nusimi.R;

public class TextSecundary implements ViewFactory {

	private Context context;

	public TextSecundary(Context context) {
		this.context = context;
	}

	public View makeView() {
		TextView t = new TextView(context);
		t.setGravity(Gravity.CENTER);
		t.setTextSize(15);
		t.setTextColor(context.getResources().getColor(R.color.negro2));
		return t;
	}

}
