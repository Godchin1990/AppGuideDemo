##android中利用viewpager实现应用引导界面

最后实现的效果是这样的:

![预览图](http://7te8st.com1.z0.glb.clouddn.com/appguideAppGuideDemo121.gif)

###主要思路:
1.创建GuideViewPagerAdapter.java 继承自PagerAdapter
2.创建GuideActivity.java实现初始化和事件监听

###需要的资源:

1.底部用于导航的小圆点,分别为实心和空心
2.三张用于导航时的图片

你可以点击这里下载:[资源](https://github.com/zhang935989088/AppGuideDemo/tree/master/res/drawable)
你可以到Github直接查看代码和其中的注释：[AppGuideDemo](https://github.com/zhang935989088/AppGuideDemo)
扫描二维码可以直接安装
![扫描安装到手机](http://7te8st.com1.z0.glb.clouddn.com/appguideappguidecode.png)

###实现

**1.导入资源文件，创建布局和样式**
>整体为RelativeLayout布局,主体为ViewPager组件充满父窗体。用于稍微填充需要展示的三张导航图片。然后在底部偏上的位置，创建3个使用LinearLayout包裹的ImageView组件,利用空心和实心的效果，展示当前用户滚动的页数。

在Drawable中创建guid_point.xml,利用Selector定义按钮在两种状态下加载不同的背景图片
```xml
<selector
  xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_enabled="true" android:drawable="@drawable/point_normal" />
    <item android:state_enabled="false" android:drawable="@drawable/point_select" />
</selector>
```
在style中加入圆点的style，便于引用
```xml
<style name="app_guide_points">
	<item name="android:layout_width">wrap_content</item>
	<item name="android:layout_height">wrap_content</item>
	<item name="android:gravity">center_vertical</item>
	<item name="android:clickable">true</item>
	<item name="android:src">@drawable/guide_point</item>
</style>
```


创建布局文件:guide_activity.xml

```xml
<RelativeLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

     <android.support.v4.view.ViewPager
        android:id="@+id/viewpager"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

     <LinearLayout
        android:id="@+id/ll"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="24.0dip"
        android:orientation="horizontal">
 
        <ImageView
            style="@style/app_guide_points"
            android:padding="15.0dip" />
 
        <ImageView
           style="@style/app_guide_points"
           android:padding="15.0dip"/>
 
        <ImageView
            style="@style/app_guide_points"
            android:padding="15.0dip"/>
 
    </LinearLayout>
</RelativeLayout>
```
**2.创建GuideViewPagerAdapter.java**
>当滚动到不同的页面的时候，分别返回对应的View。

```java

public class GuideViewPagerAdapter extends PagerAdapter {

		private List<View> views;
		public GuideViewPagerAdapter( List<View> advPics) {
			this.views = advPics;
		}
		/**
		 * 获得当前界面数
		 */
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}
		/**
		 * 初始化position位置的界面
		 */
		@Override
		public Object instantiateItem(View view, int position) {
			((ViewPager) view).addView(views.get(position), 0);
			return views.get(position);
		}
		/**
		 * 判断是否由对象生成界面
		 */
		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return (view == arg1);
		}
		/**
		 * 销毁position位置的界面
		 */
		@Override
		public void destroyItem(View view, int position, Object arg2) {
			((ViewPager) view).removeView(views.get(position));
		}
}
```

**3.创建GuideActivity.java实现初始化和事件监听**
>首先是监听ViewPager的滑动事件，页面滑动的时候，利用定义好的Adapter返回不同的View，并且需要设置小圆点的样式，以便于显示当前滚动的情况。同样，底部的小圆点也要监听其点击事件，点击不同的小圆点，设置Adapter不同的位置。

```java

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

```
**4.通常需要配置当前的Activity为全屏状态**
```xml
<activity
   android:theme="@android:style/Theme.NoTitleBar.Fullscreen">
```

###祝你好运！
