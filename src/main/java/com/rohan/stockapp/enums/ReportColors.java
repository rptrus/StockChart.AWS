package com.rohan.stockapp.enums;

public enum ReportColors {
  GREEN(0x5cbf56),
  COOLBLUE(0x0191C8)
  ;
  
  int rgb;
  
  ReportColors(int color) {
	  this.rgb = color;
  }
  
  public int getReportColor() {
	  return rgb;
  }
  
}
