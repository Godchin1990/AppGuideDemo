package com.titzanyic.appguidedemo;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener{
	// ����ViewPager����
		private ViewPager viewPager;
		// ����ViewPager������
		private GuideViewPagerAdapter vpAdapter;
		// ����һ��ArrayList�����View
		private ArrayList<View> views = new ArrayList<View>();;
		boolean isLast = true;
		// ����ͼƬ��Դ
		private static final int[] pics = { R.drawable.guide1, R.drawable.guide2,
				R.drawable.guide3 };
		// �ײ�С���ͼƬ
		private ImageView[] points;
		// ��¼��ǰѡ��λ��
		private int currentIndex;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setContentView(R.layout.guide_activity);
			initView();
			initData();
		}
		
		protected void initView() {
			viewPager = (ViewPager) findViewById(R.id.viewpager);// ʵ����ViewPager
			vpAdapter = new GuideViewPagerAdapter(views);// ʵ����ViewPager������
			
		}
		protected void initData() {
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			for (int i = 0; i < pics.length; i++) {
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(mParams);
				iv.setImageResource(pics[i]);
				iv.setScaleType(ScaleType.CENTER_CROP);
				views.add(iv);
				if (i == 2) {
					iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							startLogin();
						}
					});
				}
			}
			
			viewPager.setAdapter(vpAdapter);// ��������
			viewPager.setOnPageChangeListener(this);// ���ü���
			initPoint();// ��ʼ���ײ�С��
			
		}
		
		/**
		 * ��ʼ���ײ�С��
		 */
		private void initPoint() {
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
			points = new ImageView[pics.length];
			for (int i = 0; i < pics.length; i++) {
				
				points[i] = (ImageView) linearLayout.getChildAt(i);// �õ�һ��LinearLayout�����ÿһ����Ԫ��
				points[i].setEnabled(true);// Ĭ�϶���Ϊ��ɫ
				points[i].setOnClickListener(this);// ��ÿ��С�����ü���
				points[i].setTag(i);// ����λ��tag������ȡ���뵱ǰλ�ö�Ӧ
			}
			currentIndex = 0;// ���õ���Ĭ�ϵ�λ��
			points[currentIndex].setEnabled(false);// ����Ϊ��ɫ����ѡ��״̬
		}
		/**
		 * ������״̬�ı�ʱ����
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 2) {
				isLast = false;
			} else if (arg0 == 0 && isLast) {
				// �˴�Ϊ����Ҫ��������ټ��뵱ǰҳ���жϿ�֪���ǵ�һҳ�������һҳ
				if (viewPager.getCurrentItem() == 2) {
					startLogin();
				}
				
			} else {
				isLast = true;
			}
		}
		/**
		 * ����ǰҳ�汻����ʱ����
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		/**
		 * ���µ�ҳ�汻ѡ��ʱ����
		 */
		@Override
		public void onPageSelected(int position) {
			setCurDot(position);// ���õײ�С��ѡ��״̬
		}
		/**
		 * ͨ������¼����л���ǰ��ҳ��
		 */
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			setCurView(position);
			setCurDot(position);
		}
		/**
		 * ���õ�ǰҳ���λ��
		 */
		private void setCurView(int position) {
			if (position < 0 || position >= pics.length) {
				return;
			}
			viewPager.setCurrentItem(position);
		}
		/**
		 * ���õ�ǰ��С���λ��
		 */
		private void setCurDot(int positon) {
			if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
				return;
			}
			points[positon].setEnabled(false);
			points[currentIndex].setEnabled(true);
			currentIndex = positon;
		}
		
		private void startLogin() {
			//����������󲢵��ҳ��ʱ����Ӧ���¼�
		}
		
}
