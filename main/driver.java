package main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.util.regex.*;
import java.util.Arrays;
import java.util.Collections;

class ProductionLine{	
	
	private static String readFile(String filePath)
		throws IOException
	{
		File file = new File(filePath);
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filePath));
		byte[] buffer = new byte[(int) file.length()];
		bin.read(buffer);
		String fileStr = new String(buffer);
		return fileStr;
	}
	

	private static int[] stringToIntArray(String str){
		String[] strSplit = str.split("\\s+");
		int[] result = new int[strSplit.length];
		for(int i = 0; i < strSplit.length; i++){
			try{
				int n = Integer.parseInt(strSplit[i].trim());
				result[i] = n;
			} catch(NumberFormatException nfe){
				result[i] = -1;
				//System.out.println(nfe.getMessage());
			}
		}
		return result;
	}

	private static int[] initializeProdTime(){
		String prodTime = "";
		try{
			prodTime = readFile("main/prodTimes.txt");
		} catch(IOException e){
			e.printStackTrace();
		}
		int[] strSplit = stringToIntArray(prodTime);
		int result = 0;
		int numProducts = strSplit.length;
		for(int s : strSplit){
			 result += s;
		}
		return new int[]{result, numProducts};
	}

	private static int[][] initializeSwapTimes(int numProducts){
		String swapTimes = "";
		try{
			swapTimes = readFile("main/swapTimes.txt");
		} catch(IOException e){
			e.printStackTrace();
		}
		int[] strSplit = stringToIntArray(swapTimes);
		int[][] result = new int[numProducts][numProducts];
		for(int i = 0; i < numProducts; i++){
			for(int j = 0; j < numProducts; j++){
				result[i][j] = strSplit[(i * numProducts) + j];
			}
		}
		return result;
	}

/*
	private static Product[] initializeProducts(){
		String prodTimes = "";
		String swapTimes = "";
		try{
			prodTimes = readFile("main/prodTimes.txt");
			swapTimes = readFile("main/swapTimes.txt");
		} catch(IOException e){
			e.printStackTrace();
		}
		
		int[] prod = stringToIntArray(prodTimes);
		int[] swap = stringToIntArray(swapTimes);

		Product[] products = new Product[prod.length];
		int plen = prod.length;
		for(int i = 0; i < plen; i++){
			products[i] = new Product(i, prod[i], Arrays.copyOfRange(swap, i * plen, (i * plen) + plen));
		}
		return products;
	}
*/
	public static void main(String args[]){		
	
		int temp[] = initializeProdTime();
		int numProducts = temp[1];
		int totalProdTime = temp[0];

		int[][] swapTimes = initializeSwapTimes(numProducts);

		int[] startingSeq = new int[]{1, 6, 4, 0, 2, 8, 9, 3, 7, 5};

		Solver solver = new Solver(swapTimes, startingSeq);

		//System.out.println(Arrays.deepToString(swapTimes));

		//int calcTime = solver.calcProductionTime();
		//calcTime += totalProdTime;
		//System.out.println(calcTime);

		while(true){
			if(!solver.solve()) break;
		}

		//System.out.println(solver.swapTwo().toString());	
	}

}
