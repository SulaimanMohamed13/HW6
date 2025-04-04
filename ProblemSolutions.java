/******************************************************************
 *
 *   Sulaiman Mohamed / 272 001
 *
 *   Problem Solutions Implementation (Revised Approach)
 *
 ********************************************************************/

import java.util.*;

public class ProblemSolutions {

    public static int lastBoulder(int[] stones) {
        // Create a max heap using reverse order comparator
        Queue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        
        // Add all stones to the heap
        for (int stone : stones) {
            maxHeap.offer(stone);
        }
        
        // Process stones until one or none remains
        while (maxHeap.size() >= 2) {
            int first = maxHeap.poll();
            int second = maxHeap.poll();
            int difference = first - second;
            
            if (difference > 0) {
                maxHeap.add(difference);
            }
        }
        
        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }

    public static ArrayList<String> showDuplicates(ArrayList<String> list) {
        // Count occurrences of each string
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String item : list) {
            frequencyMap.merge(item, 1, Integer::sum);
        }
        
        // Filter and collect duplicates
        ArrayList<String> duplicates = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.add(entry.getKey());
            }
        }
        
        // Sort alphabetically
        duplicates.sort(null);
        return duplicates;
    }

    public static ArrayList<String> pair(int[] nums, int target) {
        Arrays.sort(nums);
        ArrayList<String> pairs = new ArrayList<>();
        int i = 0, j = nums.length - 1;
        
        while (i < j) {
            int sum = nums[i] + nums[j];
            
            if (sum == target) {
                pairs.add(String.format("(%d, %d)", nums[i], nums[j]));
                // Skip duplicates
                while (i < j && nums[i] == nums[i + 1]) i++;
                while (i < j && nums[j] == nums[j - 1]) j--;
                i++;
                j--;
            } 
            else if (sum < target) {
                i++;
            } 
            else {
                j--;
            }
        }
        
        return pairs;
    }
}
