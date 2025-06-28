class Solution {
    public int romanToInt(String s) {
        // 定义罗马数字映射关系
        // 使用中文注释测试编码问题
        int result = 0;
        int prev = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            int curr = getValue(s.charAt(i));
            if (curr >= prev) {
                result += curr;
            } else {
                result -= curr;
            }
            prev = curr;
        }
        return result;
    }
    
    private int getValue(char ch) {
        switch(ch) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return 0;
        }
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