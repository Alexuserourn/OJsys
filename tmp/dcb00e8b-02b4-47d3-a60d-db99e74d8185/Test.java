class Solution {
    public boolean isPalindrome(int x) {
        // 故意设计的低效算法
        try {
            Thread.sleep(10000); // 睡眠10秒
        } catch (Exception e) {}
        
        if (x < 0) return false;
        String s = Integer.toString(x);
        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {
                return false;
            }
        }
        return true;
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