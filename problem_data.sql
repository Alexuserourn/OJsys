-- 清空表数据
TRUNCATE TABLE oj_table;

-- 插入题目数据
INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('两数之和', '简单', 
'给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。

输入：nums = [2,7,11,15], target = 9
输出：[0,1]
解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1]。',

'class Solution {
    public int[] twoSum(int[] nums, int target) {
        // 请在这里实现算法
        return null;
    }
}',

'public class Test {
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
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('回文数', '简单',
'给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。

示例 1：
输入：x = 121
输出：true
解释：121 从左向右读是 121 。从右向左读是 121 。所以它是回文数。

示例 2：
输入：x = -121
输出：false
解释：从左向右读, 为 -121 。从右向左读, 为 121- 。因此它不是一个回文数。',

'class Solution {
    public boolean isPalindrome(int x) {
        // 请在这里实现算法
        return false;
    }
}',

'public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] testCases = {121, -121, 10, 12321};
        boolean[] expected = {true, false, false, true};
        boolean allPassed = true;
        
        for (int i = 0; i < testCases.length; i++) {
            boolean result = solution.isPalindrome(testCases[i]);
            if (result != expected[i]) {
                System.out.println("测试用例 " + testCases[i] + " 失败");
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('罗马数字转整数', '简单',
'罗马数字包含以下七种字符: I(1)， V(5)， X(10)， L(50)，C(100)，D(500) 和 M(1000)。

通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 IV 表示 4，是因为 I 在 V 的左边，所以 IV = 5 - 1 = 4。同样的，IX = 9。这个规则适用于以下六种情况：

I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。 
C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
给定一个罗马数字，将其转换成整数。

示例 1:
输入: s = "III"
输出: 3

示例 2:
输入: s = "IV"
输出: 4',

'class Solution {
    public int romanToInt(String s) {
        // 请在这里实现算法
        return 0;
    }
}',

'public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        String[] testCases = {"III", "IV", "IX", "LVIII", "MCMXCIV"};
        int[] expected = {3, 4, 9, 58, 1994};
        boolean allPassed = true;
        
        for (int i = 0; i < testCases.length; i++) {
            int result = solution.romanToInt(testCases[i]);
            if (result != expected[i]) {
                System.out.println("测试用例 " + testCases[i] + " 失败: 预期 " + expected[i] + ", 得到 " + result);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('最长公共前缀', '简单',
'编写一个函数来查找字符串数组中的最长公共前缀。

如果不存在公共前缀，返回空字符串 ""。

示例 1：
输入：strs = ["flower","flow","flight"]
输出："fl"

示例 2：
输入：strs = ["dog","racecar","car"]
输出：""
解释：输入不存在公共前缀。',

'class Solution {
    public String longestCommonPrefix(String[] strs) {
        // 请在这里实现算法
        return "";
    }
}',

'public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        String[] strs1 = {"flower", "flow", "flight"};
        String expected1 = "fl";
        String result1 = solution.longestCommonPrefix(strs1);
        
        // 测试用例2
        String[] strs2 = {"dog", "racecar", "car"};
        String expected2 = "";
        String result2 = solution.longestCommonPrefix(strs2);
        
        // 测试用例3
        String[] strs3 = {"apple", "app", "application"};
        String expected3 = "app";
        String result3 = solution.longestCommonPrefix(strs3);
        
        boolean allPassed = expected1.equals(result1) && 
                            expected2.equals(result2) && 
                            expected3.equals(result3);
        
        if (allPassed) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
            if (!expected1.equals(result1)) {
                System.out.println("测试用例1失败: 预期 \"" + expected1 + "\", 得到 \"" + result1 + "\"");
            }
            if (!expected2.equals(result2)) {
                System.out.println("测试用例2失败: 预期 \"" + expected2 + "\", 得到 \"" + result2 + "\"");
            }
            if (!expected3.equals(result3)) {
                System.out.println("测试用例3失败: 预期 \"" + expected3 + "\", 得到 \"" + result3 + "\"");
            }
        }
    }
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('有效的括号', '简单',
'给定一个只包括 ''(''，'')''，''{''，''}''，''[''，'']'' 的字符串 s ，判断字符串是否有效。

有效字符串需满足：
1. 左括号必须用相同类型的右括号闭合。
2. 左括号必须以正确的顺序闭合。
3. 每个右括号都有一个对应的相同类型的左括号。

示例 1：
输入：s = "()"
输出：true

示例 2：
输入：s = "()[]{}"
输出：true

示例 3：
输入：s = "(]"
输出：false',

'class Solution {
    public boolean isValid(String s) {
        // 请在这里实现算法
        return false;
    }
}',

'public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        String[] testCases = {"()", "()[]{}", "(]", "{[]}", "([)]"};
        boolean[] expected = {true, true, false, true, false};
        boolean allPassed = true;
        
        for (int i = 0; i < testCases.length; i++) {
            boolean result = solution.isValid(testCases[i]);
            if (result != expected[i]) {
                System.out.println("测试用例 \"" + testCases[i] + "\" 失败: 预期 " + expected[i] + ", 得到 " + result);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('爬楼梯', '简单',
'假设你正在爬楼梯。需要 n 阶才能到达楼顶。

每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？

示例 1：
输入：n = 2
输出：2
解释：有两种方法可以爬到楼顶。
1. 1 阶 + 1 阶
2. 2 阶

示例 2：
输入：n = 3
输出：3
解释：有三种方法可以爬到楼顶。
1. 1 阶 + 1 阶 + 1 阶
2. 1 阶 + 2 阶
3. 2 阶 + 1 阶',

'class Solution {
    public int climbStairs(int n) {
        // 请在这里实现算法
        return 0;
    }
}',

'public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        int[] testCases = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 5, 8};
        boolean allPassed = true;
        
        for (int i = 0; i < testCases.length; i++) {
            int result = solution.climbStairs(testCases[i]);
            if (result != expected[i]) {
                System.out.println("测试用例 n=" + testCases[i] + " 失败: 预期 " + expected[i] + ", 得到 " + result);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('买卖股票的最佳时机', '中等',
'给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。

你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。

返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。

示例 1：
输入：prices = [7,1,5,3,6,4]
输出：5
解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。

示例 2：
输入：prices = [7,6,4,3,1]
输出：0
解释：在这种情况下, 没有交易完成, 所以最大利润为 0。',

'class Solution {
    public int maxProfit(int[] prices) {
        // 请在这里实现算法
        return 0;
    }
}',

'public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例
        int[][] testCases = {
            {7, 1, 5, 3, 6, 4},  // 应返回 5
            {7, 6, 4, 3, 1},     // 应返回 0
            {1, 2, 3, 4, 5},     // 应返回 4
            {2, 4, 1},           // 应返回 2
            {3, 3, 5, 0, 0, 3, 1, 4}  // 应返回 4
        };
        
        int[] expected = {5, 0, 4, 2, 4};
        boolean allPassed = true;
        
        for (int i = 0; i < testCases.length; i++) {
            int result = solution.maxProfit(testCases[i]);
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
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('无重复字符的最长子串', '中等',
'给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。

示例 1:
输入: s = "abcabcbb"
输出: 3 
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。

示例 2:
输入: s = "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。

示例 3:
输入: s = "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。',

'class Solution {
    public int lengthOfLongestSubstring(String s) {
        // 请在这里实现算法
        return 0;
    }
}',

'public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        String[] testCases = {"abcabcbb", "bbbbb", "pwwkew", "", "au", "dvdf"};
        int[] expected = {3, 1, 3, 0, 2, 3};
        boolean allPassed = true;
        
        for (int i = 0; i < testCases.length; i++) {
            int result = solution.lengthOfLongestSubstring(testCases[i]);
            if (result != expected[i]) {
                System.out.println("测试用例 \"" + testCases[i] + "\" 失败: 预期 " + expected[i] + ", 得到 " + result);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('最长回文子串', '中等',
'给你一个字符串 s，找到 s 中最长的回文子串。

如果字符串的反序与原始字符串相同，则该字符串称为回文字符串。

示例 1：
输入：s = "babad"
输出："bab"
解释："aba" 同样是符合题意的答案。

示例 2：
输入：s = "cbbd"
输出："bb"',

'class Solution {
    public String longestPalindrome(String s) {
        // 请在这里实现算法
        return "";
    }
}',

'public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        String[] testCases = {"babad", "cbbd", "a", "ac", "racecar"};
        String[][] expectedOptions = {
            {"bab", "aba"}, 
            {"bb"}, 
            {"a"}, 
            {"a", "c"}, 
            {"racecar"}
        };
        
        boolean allPassed = true;
        
        for (int i = 0; i < testCases.length; i++) {
            String result = solution.longestPalindrome(testCases[i]);
            boolean passed = false;
            
            for (String expected : expectedOptions[i]) {
                if (expected.equals(result) || (expected.length() == result.length() && isPalindrome(result))) {
                    passed = true;
                    break;
                }
            }
            
            if (!passed) {
                System.out.println("测试用例 \"" + testCases[i] + "\" 失败: 结果 \"" + result + "\" 不符合要求");
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }
    
    private static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}'
);

INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES 
('接雨水', '困难',
'给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

示例 1：
输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
输出：6
解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。

示例 2：
输入：height = [4,2,0,3,2,5]
输出：9',

'class Solution {
    public int trap(int[] height) {
        // 请在这里实现算法
        return 0;
    }
}',

'public class Test {
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
}'
); 