package main;

public class Product{
	
	public int index;
	public int prodTime;
	public int[] swapTimes;

	public Product(int index, int prodTime, int[] swapTimes){
		this.index = index;
		this.prodTime = prodTime;
		this.swapTimes = swapTimes;
	}

	public int getIndex(){
		return index;
	}

	public int getProdTime(){
		return prodTime;
	}

	public int[] getSwapTimes(){
		return swapTimes;
	}


	@Override
	public String toString(){
		return "Index: " + index + "; Prod. time: " + prodTime + " Swap times: " + java.util.Arrays.toString(swapTimes);
	}
//	public getChangeoverCost(int row, int col){
		
//	}
}
