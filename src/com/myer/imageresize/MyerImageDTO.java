package com.myer.imageresize;

public class MyerImageDTO {

	public String imageType;
	public double recommendedSize;
	public double currentSize;
	public double overShootValue;

	
	public double getCurrentSize() {
		return currentSize;
	}
	public double getOverShootValue() {
		return overShootValue;
	}
	public double getRecommendedSize() {
		return recommendedSize;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public void setCurrentSize(double currentSize) {
		this.currentSize = currentSize;
	}
	public void setOverShootValue(double overShootValue) {
		this.overShootValue = overShootValue;
	}
	public void setRecommendedSize(double recommendedSize) {
		this.recommendedSize = recommendedSize;
	}
}
