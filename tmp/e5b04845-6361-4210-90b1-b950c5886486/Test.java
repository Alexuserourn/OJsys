class Solution {
    
 

int trap(int* height, int heightSize) {
    if (heightSize == 0) return 0;
    int left = 0, right = heightSize - 1;
    int left_max = 0, right_max = 0;
    int ans = 0;

    while (left < right) {
        if (height[left] < height[right]) {
            if (height[left] >= left_max) {
                left_max = height[left];
            } else {
                ans += left_max - height[left];
            }
            left++;
        } else {
            if (height[right] >= right_max) {
                right_max = height[right];
            } else {
                ans += right_max - height[right];
            }
            right--;
        }
    }
    return ans;
}

int main() {
    int height1[] = {0,1,0,2,1,0,1,3,2,1,2,1};
    int heightSize1 = sizeof(height1) / sizeof(height1[0]);
    printf("示例 1 结果: %d\n", trap(height1, heightSize1));

    int height2[] = {4,2,0,3,2,5};
    int heightSize2 = sizeof(height2) / sizeof(height2[0]);
    printf("示例 2 结果: %d\n", trap(height2, heightSize2));

    return 0;
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