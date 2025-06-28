public class Solution {
    public void bubbleSort(int[] arr) {
        // 请在这里实现冒泡排序算法
    }
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        solution.bubbleSort(arr);
        
        // 检查是否已排序
        boolean isSorted = true;
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                isSorted = false;
                break;
            }
        }
        
        if (isSorted) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
}}