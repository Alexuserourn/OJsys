class Solution {
    public int trap(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int result = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    result += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    result += rightMax - height[right];
                }
                right--;
            }
        }
        return result;
    }
}    

public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        int[][] testCases = {
            {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1},
            {4, 2, 0, 3, 2, 5},
            {1, 0, 1},
            {4, 2, 3}
        };
        
        int[] expected = {6, 9, 1, 1};
        boolean allPassed = true;
        
        for (int i = 0; i < testCases.length; i++) {
            int result = solution.trap(testCases[i]);
            if (result != expected[i]) {
                System.out.println("测试用例 " + i + " 失败: 预期 " + expected[i] + ", 得到 " + result);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
}