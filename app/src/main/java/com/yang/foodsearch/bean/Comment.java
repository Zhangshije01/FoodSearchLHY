package com.yang.foodsearch.bean;

import java.io.Serializable;
import java.util.Arrays;

public class Comment implements Serializable{
	String avatar;//ͷ��
	String username;//�û���
	String rating;//���
	String avgPrice;//�˾��۸�(������������������ʱ���ṩ���˾��۸�)
	String content;//��������
	String[] imgs;//������ͼ (������������������ʱ���ṩ����ͼ����������ṩ��ͼ�����ȡ����ͼ)
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(String avgPrice) {
		this.avgPrice = avgPrice;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getImgs() {
		return imgs;
	}
	public void setImgs(String[] imgs) {
		this.imgs = imgs;
	}
	@Override
	public String toString() {
		return "Comment [avatar=" + avatar + ", username=" + username + ", rating=" + rating + ", avgPrice=" + avgPrice
				+ ", content=" + content + ", imgs=" + Arrays.toString(imgs) + "]";
	}
	
	
}
