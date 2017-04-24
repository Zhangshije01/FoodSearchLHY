package com.yang.foodsearch.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.foodsearch.R;
import com.yang.foodsearch.bean.Comment;
import com.yang.foodsearch.util.HttpUtil;

public class CommentAdapter extends MyBaseAdapter<Comment>{
	
	int[] stars ={R.mipmap.movie_star10,
			R.mipmap.movie_star20,
			R.mipmap.movie_star30,
			R.mipmap.movie_star35,
			R.mipmap.movie_star40,
			R.mipmap.movie_star45,
			R.mipmap.movie_star50};
	
	public CommentAdapter(Context context, List<Comment> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder vh;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item_comment_layout, parent,false);
			vh = new ViewHolder();
			vh.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_item_comment_avatar);
			vh.ivRating = (ImageView) convertView.findViewById(R.id.iv_item_comment_rating);
			vh.ivImg1 = (ImageView) convertView.findViewById(R.id.iv_item_comment_img1);
			vh.ivImg2 = (ImageView) convertView.findViewById(R.id.iv_item_comment_img2);
			vh.ivImg3 = (ImageView) convertView.findViewById(R.id.iv_item_comment_img3);
			vh.tvName = (TextView) convertView.findViewById(R.id.tv_item_comment_name);
			vh.tvContent = (TextView) convertView.findViewById(R.id.tv_item_comment_content);
			vh.tvPrice = (TextView) convertView.findViewById(R.id.tv_item_comment_price);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		Comment comment = getItem(position);
		//��ʾͷ��
		HttpUtil.displayImage(comment.getAvatar(), vh.ivAvatar);
		//��ʾ��������
		vh.tvName.setText(comment.getUsername());
		//��ʾ���Ѵ��
		String rating = comment.getRating();
		if(rating.equals("star10")){
			vh.ivRating.setImageResource(stars[0]);
		}else if(rating.equals("star20")){
			vh.ivRating.setImageResource(stars[1]);
		}else if(rating.equals("star30")){
			vh.ivRating.setImageResource(stars[2]);
		}else if(rating.equals("star35")){
			vh.ivRating.setImageResource(stars[3]);
		}else if(rating.equals("star40")){
			vh.ivRating.setImageResource(stars[4]);
		}else if(rating.equals("star45")){
			vh.ivRating.setImageResource(stars[5]);
		}else{
			vh.ivRating.setImageResource(stars[6]);
		}
		
		//��ʾ�˾��۸�
		String avgPrice = comment.getAvgPrice();
		if(!TextUtils.isEmpty(avgPrice)){
			vh.tvPrice.setText(comment.getAvgPrice()+"/��");
		}else{
			vh.tvPrice.setText(comment.getAvgPrice());
		}
		
		
		//��ʾ��������
		vh.tvContent.setText(comment.getContent());
		//��ʾͼƬ
		ImageView[] imageViews = new ImageView[3];
		imageViews[0] = vh.ivImg1;
		imageViews[1] = vh.ivImg2;
		imageViews[2] = vh.ivImg3;
		
		for(ImageView iv:imageViews){
			iv.setVisibility(View.GONE);
		}
		
		String[] imgs = comment.getImgs();
		
		if(imgs!=null){
			for(int i=0;i<imgs.length;i++){
				imageViews[i].setVisibility(View.VISIBLE);
				HttpUtil.displayImage(imgs[i], imageViews[i]);
			}
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		ImageView ivAvatar,ivRating,ivImg1,ivImg2,ivImg3;
		TextView tvName,tvContent,tvPrice;
	}

}
