package main;

import java.util.*;

public class Solver{

	int[][] swapTimes;
	int[] sequence;
	int[] currentSwaps;
	int[] swapDifferentials;
	TreeMap<Integer, ArrayList<Integer>> swapTree; // swap value, list of indices
	HashMap<Integer, Integer> swapMap; // index to swap value
	//int[] indicesToOperate = new int[3];
	//PriorityQueue<SwapCandidate> pSwap;

	public Solver(int[][] swapTimes, int[] sequence){
		this.swapTimes = swapTimes;
		this.sequence = sequence;
		//currentSwaps = new int[sequence.length - 1];
		initializeSwapList();
		swapDifferentials = new int[sequence.length - 1];
		swapTree = new TreeMap<Integer, ArrayList<Integer>>();
		swapMap = new HashMap<Integer, Integer>();
		//initializePQueue();
	}
	
/*	private void initializePQueue(){
		for(int i = 0; i < sequence.length - 1; i++){
			// pQueue population time complexity: O(n)
			pSwap.add(new SwapCandidate(i, swapTimes[sequence[i]][sequence[i + 1]]));

		}
		printQueue();
	}
*/

	private void initializeSwapList(){
		currentSwaps = new int[sequence.length - 1];
		for(int i = 0; i < sequence.length - 1; i++){
			currentSwaps[i] = swapTimes[sequence[i]][sequence[i + 1]];
		}
		System.out.println("current: " + java.util.Arrays.toString(currentSwaps));
		System.out.println("sequence: " + java.util.Arrays.toString(sequence));

	}

	private void updateSwapList(int i){
		//for(int i = 0; i < sequence.length - 1; i++){
		//	currentSwaps[i] = swapTimes[sequence[i]][sequence[i + 1]];
		//}
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

		System.out.println("current: " + java.util.Arrays.toString(currentSwaps));
		System.out.println("sequence: " + java.util.Arrays.toString(sequence));
	}

/*	private void printQueue(){
		//PriorityQueue<SwapCandidate> i = new PriorityQueue<SwapCandidate>();
		Iterator<SwapCandidate> itr = pSwap.iterator();
		while(itr.hasNext()){
			//SwapCandidate j = pSwap.poll();
			System.out.print(itr.next() + "\n");
			//i.add(j);

		}
		System.out.println("");
		//pSwap = i;
	}
*/
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

	private int findIndex(int start, int end){
		int max = Integer.MIN_VALUE;
		int maxDiffIndex = -1;
		// It might be necessary to check if any lookup values are -1
		for(int i = start; i < end; i++){
			int newSwapTime = 0;
			int currentSwapTime = 0;

			if(i == 0){
				// edge case - beginning of array
				newSwapTime += lookupSwap(i + 1, i);
				newSwapTime += lookupSwap(i, i + 2);

				currentSwapTime += currentSwaps[i];
				currentSwapTime += currentSwaps[i + 1];


			}
			else if(i == sequence.length - 2){
				// edge case - end of array
				newSwapTime += lookupSwap(i - 1, i + 1);
				newSwapTime += lookupSwap(i + 1, i);
					  
				currentSwapTime += currentSwaps[i - 1];
				currentSwapTime += currentSwaps[i];

			}
			else{
				newSwapTime += lookupSwap(i - 1, i + 1);
				newSwapTime += lookupSwap(i + 1, i);
				newSwapTime += lookupSwap(i, i + 2);
					  
				currentSwapTime += currentSwaps[i - 1];
				currentSwapTime += currentSwaps[i];
				currentSwapTime += currentSwaps[i + 1];
			}
			int diff = currentSwapTime -newSwapTime;
			
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
			
			System.out.println("iteration: " + i + " diff swap time: " + diff);
				  
			/*	  
			if(diff > 0 && diff > max){
			  
				max = diff;
				maxDiffIndex = i;
				System.out.println("maxDiffIndex: " + maxDiffIndex);
			}
			*/
		}
		
		// get rightmost (largest) value from tree map O(logn)
		max = swapTree.lastKey();
	
		if(max <= 0) return -1;

		System.out.println("Tree: " + swapTree);
		// get index of largest value O(1)
		maxDiffIndex = swapTree.get(max).get(0);
		System.out.println("Map: " + swapMap);

		ArrayList<Integer> delFromTree = new ArrayList<Integer>();
		delFromTree.add(max);
		// get next indices that need to be changed from the map
		if(maxDiffIndex == 0){
			delFromTree.add(swapMap.get(maxDiffIndex + 1));
		} else if(maxDiffIndex == sequence.length - 2){
			delFromTree.add(swapMap.get(maxDiffIndex - 1));
		} else{
			delFromTree.add(swapMap.get(maxDiffIndex - 1));
			delFromTree.add(swapMap.get(maxDiffIndex + 1));
		}

		// delete node from tree to make room for new values in next iteration
		for(Integer n : delFromTree){
			if(swapTree.get(n).size() > 1){
				swapTree.get(n).remove(1);
			}	
			else{
				swapTree.remove(n);
			}
		}
		System.out.println("Max diff Index: " + maxDiffIndex);		
		return maxDiffIndex;
	}


	public void solve(){
		int currentProdTime = calcProductionTime();
		//int maxDiffIndex = -1;
		
		int maxDiffIndex = findIndex(0, sequence.length - 1);
		// after first pass through to find the maxDiffIndex
		// the tree map is built
		// at this point, only operate on the tree map
		// and operations will be O(logn) time
	/*	while(maxDiffIndex != -1){
			swapTwo(maxDiffIndex);
			updateSwapList(maxDiffIndex);
		}*/
		while(maxDiffIndex != -1){
			swapTwo(maxDiffIndex);
			updateSwapList(maxDiffIndex);
			//int tempIndex = 0;
			if(maxDiffIndex == 0){
				maxDiffIndex = findIndex(0, maxDiffIndex + 2);
			} else if(maxDiffIndex == sequence.length - 2){
				maxDiffIndex = findIndex(maxDiffIndex - 1, maxDiffIndex);
			} else{
				maxDiffIndex = findIndex(maxDiffIndex - 1, maxDiffIndex + 2);
			}

			//initializeSwapList();
			int newProdTime = calcProductionTime();
			//return true;
		}	

		/*if(maxDiffIndex != -1){
			swapTwo(maxDiffIndex);
			updateSwapList(maxDiffIndex);
			//initializeSwapList();
			int newProdTime = calcProductionTime();
			return true;
		}*/	

		//return false;

	}

}
