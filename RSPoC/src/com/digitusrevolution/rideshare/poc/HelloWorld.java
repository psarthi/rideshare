package com.digitusrevolution.rideshare.poc;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author psarthi
 * 
 * Variables
 * For loop, While, If, Else, Switch, Break/Continue
 * Arrays, 2D array
 * For Each
 * Collection
 * Class and objects
 *
 */

public class HelloWorld {
	
	public static void main(String args[]) {
		
		int myNumber = 1;
		float myFloatNumber = 5.99f;
		String myText = "Hello";
		char myChar = 'P';
		boolean myBoolean = true;
		int[] myNumberArray = {1,2,3,4};
		String[] myTextArray = {"Apple","Mango","Orange","Guava"};
		char[] myCharArray = {'A','B','C','D'};
		int[][] my2DNumberArray = {{1,2,3,4},{11,12,13,14}};
		Integer[] myIntegerArray = {2,3,4,5};
		
		LinkedList<String> list = new LinkedList<String>();

		for (int i=0; i <10; i ++) {
			list.add("String + "+i);
			if (i==3) break;
		}
		
		while (myNumber < 5) {
			System.out.println("Number is: "+myNumber);
			if (myNumber>3) continue;
			if (myNumber <7) break;
			myNumber++;
		}
		
		switch (myNumber) {
		case 1:
			System.out.println("I am 1");
			break;
		case 2:
			System.out.println("I am 2");
			break;
		case 6:
			System.out.println("I am 6");
			break;
		default:
			System.out.println("I am something else: "+myNumber);
			break;
		}
		
		do {
			System.out.println("Initial Number is: "+myNumber);
			myNumber++;
		} while (myNumber < 6);
		
		if (myBoolean) {
			System.out.println("I am True");
			myBoolean = !myBoolean;
		} else {
			System.out.println("I am False");
			myBoolean = !myBoolean;
		}
		
		System.out.println(list.toString());
		System.out.println("Hello World" + "I am String with value: " + myText);
		System.out.println("Hello World" + "I am int with value: " + myNumber);
		System.out.println("Hello World" + "I am float with value: " + myFloatNumber);
		System.out.println("Hello World" + "I am boolean with value: " + myBoolean);
		System.out.println("Hello World" + "I am character with value: " + myChar);
		
		
		for (int i : myNumberArray) {
			System.out.println("For Each - Array Value is: "+i);
		}
		
		for (int i=0; i<myNumberArray.length; i++) {
			System.out.println("For - Array Value is :"+i);
		}
		
		for (int i=0; i<myTextArray.length; i++ ) {
			System.out.println("For: String array value is: "+myTextArray[i]);
			
		}
		
		for (char myChar1 : myCharArray) {
			System.out.println("For each: Char array value is: "+myChar1);
		}
		
		for (String string : myTextArray) {
			System.out.println("For each: String array value is: "+string);
		}
		
		for (int i=0; i<my2DNumberArray.length;i++) {
			for (int j=0; j<my2DNumberArray[i].length;j++) {
				System.out.println("2D array value is: "+my2DNumberArray[i][j]);
			}
		}
		
		for (Integer myNum : myIntegerArray) {
			System.out.println("Integer Array value is: "+myNum);
		}
		
	}

}
