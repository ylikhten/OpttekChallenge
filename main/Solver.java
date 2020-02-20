package main;

public class Solver{

	int[][] swapTimes;
	int[] sequence;
	int[] currentSwaps;
	int[] indicesToOperate = new int[3];

	public Solver(int[][] swapTimes, int[] sequence){
		this.swapTimes = swapTimes;
		this.sequence = sequence;
		currentSwaps = new int[sequence.length - 1];
		updateSwapList();
	}

	private void updateSwapList(){
		for(int i = 0; i < sequence.length - 1; i++){
			currentSwaps[i] = swapTimes[sequence[i]][sequence[i + 1]];
		}
		System.out.println("current: " + java.util.Arrays.toString(currentSwaps));
		System.out.println("sequence: " + java.util.Arrays.toString(sequence));
	}


	public int calcProductionTime(){
		int result = 0;
		for(int i : currentSwaps){
			result += i;
		}
		return result;
	}

	private void swapTwo(int index){

		int temp = sequence[index];
		sequence[index] = sequence[index + 1];
		sequence[index + 1] = temp;

	}

	private int lookupSwap(int i, int j){
		return swapTimes[sequence[i]][sequence[j]];
	}

	private int[] findMinIndex(){
		int max = Integer.MIN_VALUE;
		int maxDiffIndex = -1;
		// It might be necessary to check if any lookup values are -1
		for(int i = 0; i < sequence.length - 1; i++){
			int newSwapTime = 0;
			int currentSwapTime = 0;

			if(i == 0){
				// edge case - beginning of array
				newSwapTime += lookupSwap(i + 1, i);
				newSwapTime += lookupSwap(i, i + 2);

				currentSwapTime += currentSwaps[i];
				currentSwapTime += currentSwaps[i + 1];

				//indicesToOperate = new int[]{i, i + 1, i + 2};

			}
			else if(i == sequence.length - 2){
				// edge case - end of array
				newSwapTime += lookupSwap(i - 1, i + 1);
				newSwapTime += lookupSwap(i + 1, i);
					  
				currentSwapTime += currentSwaps[i - 1];
				currentSwapTime += currentSwaps[i];

				//indicesToOperate = new int[]{i - 2, i - 1, i};
			}
			else{
				newSwapTime += lookupSwap(i - 1, i + 1);
				newSwapTime += lookupSwap(i + 1, i);
				newSwapTime += lookupSwap(i, i + 2);
					  
				currentSwapTime += currentSwaps[i - 1];
				currentSwapTime += currentSwaps[i];
				currentSwapTime += currentSwaps[i + 1];
				//indicesToOperate = new int[]{i - 1, i, i + 1};
			}
			int diff = currentSwapTime -newSwapTime;
				  
				  
			System.out.println("iteration: " + i + " diff swap time: " + diff);
				  
				  
			if(diff > 0 && diff > max){
			  
				max = diff;
				maxDiffIndex = i;
				System.out.println("maxDiffIndex: " + maxDiffIndex);
			}
		}
		
		return new int[]{max, maxDiffIndex};
	}

	public boolean solve(){
		int currentProdTime = calcProductionTime();
		//int maxDiffIndex = -1;
		//int max = Integer.MIN_VALUE;
		//int[] maxDiffIndex = new int[2];

		//maxDiffIndex = findMinIndex();
		int max = Integer.MIN_VALUE;
		int maxDiffIndex = -1;
		// It might be necessary to check if any lookup values are -1
		for(int i = 0; i < sequence.length - 1; i++){
			int newSwapTime = 0;
			int currentSwapTime = 0;

			if(i == 0){
				// edge case - beginning of array
				newSwapTime += lookupSwap(i + 1, i);
				newSwapTime += lookupSwap(i, i + 2);

				currentSwapTime += currentSwaps[i];
				currentSwapTime += currentSwaps[i + 1];

				//indicesToOperate = new int[]{i, i + 1, i + 2};

			}
			else if(i == sequence.length - 2){
				// edge case - end of array
				newSwapTime += lookupSwap(i - 1, i + 1);
				newSwapTime += lookupSwap(i + 1, i);
					  
				currentSwapTime += currentSwaps[i - 1];
				currentSwapTime += currentSwaps[i];

				//indicesToOperate = new int[]{i - 2, i - 1, i};
			}
			else{
				newSwapTime += lookupSwap(i - 1, i + 1);
				newSwapTime += lookupSwap(i + 1, i);
				newSwapTime += lookupSwap(i, i + 2);
					  
				currentSwapTime += currentSwaps[i - 1];
				currentSwapTime += currentSwaps[i];
				currentSwapTime += currentSwaps[i + 1];
				//indicesToOperate = new int[]{i - 1, i, i + 1};
			}
			int diff = currentSwapTime -newSwapTime;
				  
				  
			System.out.println("iteration: " + i + " diff swap time: " + diff);
				  
				  
			if(diff > 0 && diff > max){
			  
				max = diff;
				maxDiffIndex = i;
				System.out.println("maxDiffIndex: " + maxDiffIndex);
			}
		}


		if(maxDiffIndex != -1){
			swapTwo(maxDiffIndex);
			updateSwapList();
			int newProdTime = calcProductionTime();
			return true;
		}

		

		return false;

	}

}
