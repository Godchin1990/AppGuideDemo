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
	// 定义ViewPager对象
		private ViewPager viewPager;
		// 定义ViewPager适配器
		private GuideViewPagerAdapter vpAdapter;
		// 定义一个ArrayList来存放View
		private ArrayList<View> views = new ArrayList<View>();;
		boolean isLast = true;
		// 引导图片资源
		private static final int[] pics = { R.drawable.guide1, R.drawable.guide2,
				R.drawable.guide3 };
		// 底部小点的图片
		private ImageView[] points;
		// 记录当前选中位置
		private int currentIndex;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setContentView(R.layout.guide_activity);
			initView();
			initData();
		}
		
		protected void initView() {
			viewPager = (ViewPager) findViewById(R.id.viewpager);// 实例化ViewPager
			vpAdapter = new GuideViewPagerAdapter(views);// 实例化ViewPager适配器
			
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
			
			viewPager.setAdapter(vpAdapter);// 设置数据
			viewPager.setOnPageChangeListener(this);// 设置监听
			initPoint();// 初始化底部小点
			
		}
		
		/**
		 * 初始化底部小点
		 */
		private void initPoint() {
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
			points = new ImageView[pics.length];
			for (int i = 0; i < pics.length; i++) {
				
				points[i] = (ImageView) linearLayout.getChildAt(i);// 得到一个LinearLayout下面的每一个子元素
				points[i].setEnabled(true);// 默认都设为灰色
				points[i].setOnClickListener(this);// 给每个小点设置监听
				points[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
			}
			currentIndex = 0;// 设置当面默认的位置
			points[currentIndex].setEnabled(false);// 设置为白色，即选中状态
		}
		/**
		 * 当滑动状态改变时调用
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 2) {
				isLast = false;
			} else if (arg0 == 0 && isLast) {
				// 此处为你需要的情况，再加入当前页码判断可知道是第一页还是最后一页
				if (viewPager.getCurrentItem() == 2) {
					startLogin();
				}
				
			} else {
				isLast = true;
			}
		}
		/**
		 * 当当前页面被滑动时调用
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		/**
		 * 当新的页面被选中时调用
		 */
		@Override
		public void onPageSelected(int position) {
			setCurDot(position);// 设置底部小点选中状态
		}
		/**
		 * 通过点击事件来切换当前的页面
		 */
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			setCurView(position);
			setCurDot(position);
		}
		/**
		 * 设置当前页面的位置
		 */
		private void setCurView(int position) {
			if (position < 0 || position >= pics.length) {
				return;
			}
			viewPager.setCurrentItem(position);
		}
		/**
		 * 设置当前的小点的位置
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
			//当滑动到最后并点击页面时，响应的事件
		}
		
}
