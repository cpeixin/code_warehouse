package leetcode;

import java.util.HashMap;
import java.util.Map;

class Stone {
    public static int numJewelsInStones(String J, String S) {
        Map<Character, Integer> map = new HashMap<>();
        int count = 0;
        for (int i = 0; i < S.length(); i++) {
            map.merge(S.charAt(i), 1, Integer::sum);
        }
        for (int j = 0; j < J.length(); j++) {
            if (map.containsKey(J.charAt(j))){
                count += map.get(J.charAt(j));
            }
        }
        return count;
    }


    public static void main(String[] args) {
        int res = numJewelsInStones("aA", "aAAbbbb");
        System.out.println(res);
    }
}