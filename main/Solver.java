package main;

import java.util.*;

/**
 * Solver class implements the algorithm that finds the minimal production time
 * @author Yanina Likhtenshteyn
 */
public class Solver{

	int[][] swapTimes; // 2D array containing swap time between products
	int[] sequence; // maintains the current sequence of products
	int[] currentSwaps; // maintains the current swap times between products
	TreeMap<Integer, ArrayList<Integer>> swapTree; // swap difference value, list of indices
	HashMap<Integer, Integer> swapMap; // index, swap difference value

	public Solver(int[][] swapTimes, int[] sequence){
		this.swapTimes = swapTimes;
		this.sequence = sequence;
		initializeSwapList();
		swapTree = new TreeMap<Integer, ArrayList<Integer>>();
		swapMap = new HashMap<Integer, Integer>();
	}
	
	/**
	 * Gets current product sequence.
	 */
	public int[] getSequence(){
		return sequence;
	}

	/**
	 * Initialize the currentSwap list by checking the swap times between two adjacent products
	 */
	private void initializeSwapList(){
		currentSwaps = new int[sequence.length - 1];
		for(int i = 0; i < sequence.length - 1; i++){
			currentSwaps[i] = swapTimes[sequence[i]][sequence[i + 1]];
		}
	}

	/**
	 * Update swap list after a swap occurs.
	 * Only updates values around some given index, instead of updating the entire list.
	 */
	private void updateSwapList(int i){
		if(i == 0){
			currentSwaps[i] = swapTimes[sequence[i]][sequence[i + 1]];
			currentSwaps[i + 1] = swapTimes[sequence[i + 1]][sequence[i + 2]];
		} else if(i == sequence.length - 2){
			currentSwaps[i - 1] = swapTimes[sequence[i - 1]][sequence[i]];
			currentSwaps[i] = swapTimes[sequence[i]][sequence[i + 1]];
		} else{
			currentSwaps[i - 1] = swapTimes[sequence[i - 1]][sequence[i]];
			currentSwaps[i] = swapTimes[sequence[i]][sequence[i + 1]];
			currentSwaps[i + 1] = swapTimes[sequence[i + 1]][sequence[i + 2]];
		}
	}

	/**
	 * Run through the currentSwaps list and sum the swap times
	 * @return Total swap time for a given configuration
	 */
	public int calcProductionTime(){
		int result = 0;
		for(int i : currentSwaps){
			result += i;
		}
		return result;
	}

	/**
	 * Swaps two adjacent products in a list.
	 * Swaps occur between the ith and (i + 1)th elements in a sequence.
	 */
	private void swapTwo(int index){

		int temp = sequence[index];
		sequence[index] = sequence[index + 1];
		sequence[index + 1] = temp;

	}

	/**
	 * Helper method used to look up swap times between two products
	 * @return Swap time between products i and j
	 */
	private int lookupSwap(int i, int j){
		return swapTimes[sequence[i]][sequence[j]];
	}

	/**
	 * Contains main logic of the algorithm
	 * @return Integer indicating the index of the optimal swap position
	 */
	private int findIndex(int start, int end){
		int max = Integer.MIN_VALUE;
		int maxDiffIndex = -1;
		for(int i = start; i < end; i++){
			// At each iteration, a calculation is performed to assess what the new swap time would 
			// be between a product at i and a product at (i + 1)
			// That is, what does the new swap time look like when product[i + 1] = product[i] and
			// product[i] = product[i + 1]. The swap between products i and (i + 1) also affects 
			// products at (i - 1) and (i + 2), so new swap times are calculated for those as well and 
			// added to the new swap time total.
			// Then a difference between the new swap time and current swap time is calculated that 
			// will determine which swap is optimal

			int newSwapTime = 0;
			int currentSwapTime = 0;

			if(i == 0){
				// beginning of array calc swap times for 0th position and 1st position
				newSwapTime += lookupSwap(i + 1, i); 
				newSwapTime += lookupSwap(i, i + 2);

				currentSwapTime += currentSwaps[i];
				currentSwapTime += currentSwaps[i + 1];
			}
			else if(i == sequence.length - 2){
				// end of array - calc swap times for (length - 1)th position and (length - 2)th position
				newSwapTime += lookupSwap(i - 1, i + 1);
				newSwapTime += lookupSwap(i + 1, i);
					  
				currentSwapTime += currentSwaps[i - 1];
				currentSwapTime += currentSwaps[i];
			}
			else{
				// middle of array - calc swap times for (i - 1)th position, ith position, (i + 1)th position
				newSwapTime += lookupSwap(i - 1, i + 1);
				newSwapTime += lookupSwap(i + 1, i);
				newSwapTime += lookupSwap(i, i + 2);
					  
				currentSwapTime += currentSwaps[i - 1];
				currentSwapTime += currentSwaps[i];
				currentSwapTime += currentSwaps[i + 1];
			}
			
			int diff = currentSwapTime - newSwapTime;
			
			// populate hash map
			swapMap.put(i, diff);

			// populate tree map
			if(swapTree.containsKey(diff)){
				ArrayList<Integer> ind = swapTree.get(diff);
				ind.add(i);
				swapTree.put(diff, ind);
			} else{
				ArrayList<Integer> ind = new ArrayList<Integer>();
				ind.add(i);
				swapTree.put(diff, ind);
			}
		}
		
		// get rightmost (largest) value from tree map
		// this is the value whose index will produce the optimal swap - O(logn)
		max = swapTree.lastKey();

		// if max <= 0, then there are no improving swaps that can be made
		if(max <= 0) return -1;

		// get index of largest value - O(1)
		maxDiffIndex = swapTree.get(max).get(0);

		ArrayList<Integer> delFromTree = new ArrayList<Integer>();
		delFromTree.add(max);

		// get adjacent indices that need to be updated after swap is performed
		// add them to the list that determines which values to delete from the tree
		if(maxDiffIndex == 0){
			delFromTree.add(swapMap.get(maxDiffIndex + 1));
		} else if(maxDiffIndex == sequence.length - 2){
			delFromTree.add(swapMap.get(maxDiffIndex - 1));
		} else{
			delFromTree.add(swapMap.get(maxDiffIndex - 1));
			delFromTree.add(swapMap.get(maxDiffIndex + 1));
		}

		// delete values from tree to make room for updated values in next iteration
		for(Integer n : delFromTree){
			if(swapTree.get(n).size() > 1){
				swapTree.get(n).remove(1);
			}	
			else{
				swapTree.remove(n);
			}
		}
		return maxDiffIndex;
	}

	/**
	 * Runs algorithm to find minimal production time
	 */
	public void solve(){
		// run findIndex once on entire sequence to populate tree and hash map
		int maxDiffIndex = findIndex(0, sequence.length - 1);

		// after first pass through findIndex to obtain the maxDiffIndex
		// the tree map is built
		// at this point, only operate at indices that were recently changed
		// operations performed on tree will be O(logn) time
		while(maxDiffIndex != -1){
			swapTwo(maxDiffIndex);
			updateSwapList(maxDiffIndex);
			if(maxDiffIndex == 0){
				maxDiffIndex = findIndex(0, maxDiffIndex + 2);
			} else if(maxDiffIndex == sequence.length - 2){
				maxDiffIndex = findIndex(maxDiffIndex - 1, maxDiffIndex);
			} else{
				maxDiffIndex = findIndex(maxDiffIndex - 1, maxDiffIndex + 2);
			}
		}	
	}
}
