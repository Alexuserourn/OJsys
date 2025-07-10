class Solution {
    public int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        
        // 添加一个极其低效的循环，确保超时
        long counter = 0;
        for (long i = 0; i < 1000000000L; i++) {
            counter++;
        }
        
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }
}

public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = solution.twoSum(nums, target);
        if (result != null && result.length == 2 && 
            nums[result[0]] + nums[result[1]] == target) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
}