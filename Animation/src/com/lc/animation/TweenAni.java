package com.lc.animation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TweenAni extends Activity implements OnClickListener {

	ImageView mImageView;
	Button btnScale;
	Button btnRotate;
	Button btnAlpha;
	Button btnTranslate;
	final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.activity_tween_ani);
		mImageView = (ImageView) findViewById(R.id.imageView1);

		btnScale = (Button) findViewById(R.id.button1);
		btnScale.setOnClickListener(this);
		btnRotate = (Button) findViewById(R.id.button2);
		btnRotate.setOnClickListener(this);
		btnAlpha = (Button) findViewById(R.id.button3);
		btnAlpha.setOnClickListener(this);
		btnTranslate = (Button) findViewById(R.id.button4);
		btnTranslate.setOnClickListener(this);*/
		
		 
		LinearLayout linearLayout = new LinearLayout(this);
		 
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		 
		setContentView(linearLayout);
		 
		TextView TV1 = new TextView(this);
		 
		TV1.setText("全部不透明＝255");
		 
		TV1.setTextColor(Color.argb(255, 0, 255, 0));
		
		 
		linearLayout.addView(TV1, new LinearLayout.LayoutParams(WRAP_CONTENT,
		 
		WRAP_CONTENT));
		 
		TextView TV2 = new TextView(this);
		 
		TV2.setText("部分透明＝155");
		 
		TV2.setTextColor(Color.argb(155, 0, 255, 0));
		 
		linearLayout.addView(TV2, new LinearLayout.LayoutParams(WRAP_CONTENT,
		 
		WRAP_CONTENT));
		 
		TextView TV3 = new TextView(this);
		 
		TV3.setText("部分透明＝55");
		 
		TV3.setTextColor(Color.argb(55, 0, 255, 0));
		 
		linearLayout.addView(TV3, new LinearLayout.LayoutParams(WRAP_CONTENT,
		 
		WRAP_CONTENT));
		 
		TextView TV4 = new TextView(this);
		 
		TV4.setText("全部透明＝0");
		 
		TV4.setTextColor(Color.argb(0, 0, 255, 0));
		 
		linearLayout.addView(TV4, new LinearLayout.LayoutParams(WRAP_CONTENT,
		 
		WRAP_CONTENT));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnScale) {
			final Animation scaleAnimation = AnimationUtils.loadAnimation(this,
					R.anim.scale_animation);
			mImageView.setAnimation(scaleAnimation);
			mImageView.startAnimation(scaleAnimation);
		} else if (v == btnRotate) {
			final Animation rotateAnimation = AnimationUtils.loadAnimation(
					this, R.anim.rotate_animation);
			mImageView.setAnimation(rotateAnimation);
			mImageView.startAnimation(rotateAnimation);
		} else if (v == btnAlpha) {
			final Animation alphaAnimation = AnimationUtils.loadAnimation(this,
					R.anim.alpha_animation);
			mImageView.setAnimation(alphaAnimation);
			mImageView.startAnimation(alphaAnimation);
		} else if (v == btnTranslate) {
			final Animation translateAnimation = AnimationUtils.loadAnimation(
					this, R.anim.translate_animation);
			mImageView.setAnimation(translateAnimation);
			mImageView.startAnimation(translateAnimation);
		}
	}
}
