package com.digitusrevolution.rideshare.poc;

public class SimpleTest {

	public static void main(String[] args) {
		
		float f1 = 1;
	    float f2 = 2;
	    int i1 = Float.compare(f1,f2);
	   
	    if(i1 > 0){
	      System.out.println("First is grater");
	    }else if(i1 < 0){
	      System.out.println("Second is grater");
	    }else{
	      System.out.println("Both are equal");
	    }
		
	}
}
