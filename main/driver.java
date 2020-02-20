package main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.util.regex.*;
import java.util.Arrays;
import java.util.Collections;

/**
 *	Top-level driving class for the coding challenge.
 * @author Yanina Likhtenshteyn
 */

class ProductionLine{	
	
	/**
	 * Reads input files
	 * @return A String object containing the entire file's text
	 */
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
	
	/**
	 * Converts strings into integers
	 * @return An integer array
	 */
	private static int[] stringToIntArray(String str){
		String[] strSplit = str.split("\\s+");
		int[] result = new int[strSplit.length];
		for(int i = 0; i < strSplit.length; i++){
			try{
				int n = Integer.parseInt(strSplit[i].trim());
				result[i] = n;
			} catch(NumberFormatException nfe){
				result[i] = -1;
			}
		}
		return result;
	}

	/**
	 * Reads in file containing production times for a given product
	 * Counts number of products and sums total production time
	 * @return An integer array containing the number of products and the total production time
	 */
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
		return new int[]{numProducts, result};
	}

	/**
	 * Reads in file containing swap times between products
	 * @return Returns 2D integer array containing the swap times between products
	 */
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

	public static void main(String args[]){		
	
		int temp[] = initializeProdTime();
		int numProducts = temp[0];
		int totalProdTime = temp[1];

		int[][] swapTimes = initializeSwapTimes(numProducts);

		int[] startingSeq = new int[]{1, 6, 4, 0, 2, 8, 9, 3, 7, 5};

		Solver solver = new Solver(swapTimes, startingSeq);

		solver.solve();

		int[] sequence = solver.getSequence();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		System.out.print("Ending sequence: ");
		for(int i = 0; i < sequence.length; i++){
			int j = sequence[i];
			if(i == 0){
				System.out.print("(" + alphabet.charAt(j) + ", ");
			} else if(i == sequence.length - 1){
				System.out.print(alphabet.charAt(j) + ")\n");
			} else{
				System.out.print(alphabet.charAt(j) + ", ");
			}
		}
		System.out.println("Production time: " + (solver.calcProductionTime() + totalProdTime));
	}
}
