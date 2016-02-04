//package com.example.toolbar.top;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//
//import com.example.strawberryradio.R;
//
//public class ThreeActivity extends Activity{
//	
//	private AdvertisementLayout topVp;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.three_activity);
//		
//		topVp = (AdvertisementLayout) findViewById(R.id.topVp);
////		topVp.setScale(1.2);
////		topVp.setDefaultImage(R.drawable.product_detail_default);
//		topVp.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		initData();
//	}
//	
//	private void initData(){
//		List<String> listAd = new ArrayList<String>();
//		//�޸�ͼƬ·��
//		listAd.add("http://img.oss.shuihulu.com/items/bbbba4388d3a22f6c3adf65f7f05c326.jpg");
//		listAd.add("http://img.oss.shuihulu.com/hot/43a62ac7f9e78026b3928b1b208d6d84.jpg");
//		listAd.add("http://img.oss.shuihulu.com/hot/a197e579866b4b1ba8d16dd27c09c7df.jpg");
////		listAd.add("drawable://" + R.drawable.image);
////		listAd.add("drawable://" + R.drawable.image);
////		listAd.add("drawable://" + R.drawable.image);
//		
//		topVp.setListAd(listAd);
//		topVp.startPlay();
//	}
//}
