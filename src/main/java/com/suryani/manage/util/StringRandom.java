package com.suryani.manage.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soldier
 */
public class StringRandom {
    public static List<String> openIds = new ArrayList<>();

    //    public static String getOpenId() {
//        String words = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789-_";
//        StringBuffer val = new StringBuffer("oYNMFt");
//        Random random = new Random();
//        for (int i = 0; i < 22; i++) {
//            val.append(words.charAt(random.nextInt(64)));
//        }
////        System.out.println(val.toString());
//        return val.toString();
//    }
    public static synchronized String getOpenId() {
        if (openIds.isEmpty()) {
//            openIds.add("oYNMFt_TGwYNDASVIDN5QilLF_iY");
//            openIds.add("oYNMFtx4EfFLC0v2n6wbYpWq_NG8");
//            openIds.add("oYNMFt84fPlbh-1L39yB86PDgY2s");
//            openIds.add("oYNMFt1rjo2Wqh7yGUNR4pYXvtYg");
            openIds.add("c1ZGVkFqM3ZoRWpBMk56Tldsd1ZwR25nSEZOdVVJbTJoTzN6ZjBjK1U4SUs2Vjg0SEFES3h6OGZsUVZ5a0sySDdza0sxK1JhK0drSQ0KWStuLzU0a3Y1Zz09");
            openIds.add("cWNXWnMrd2hqejBtYk9QdTJwU2dqS2orOUl3a1N4UVQ5V1k5djB6SndCS2dlM0c2ZUxyVWhtVis4c3I3R2MrcDlqQlhTaXNEeVI3ZQ0KN3lOUVBtQmlEZz09");
            openIds.add("ak5hZHI5Y2I5VUFYTVVvbndSWlMwd2ZwMTdHZXlvSGd1eERSVGkwVG5CMGVuZVZSbmoyYkc0a1dhbm1NWVREbVpUUDQvaWFDb3dhbw0KWm9LdGtnd3RaQT09");
//            openIds.add("oYNMFt0O83mYPNNcC0z0CHApWVxU");
//            openIds.add("oYNMFt10Dt4ALLT5qcBvNZJbUKPw");
//            openIds.add("oYNMFt0w3S4Mo53_Gq3miBK2ZRYg");
//            openIds.add("oYNMFt4dyL_5oDaM7HR2qGv4T1jc");
//            openIds.add("oYNMFtygz_EYqGYsMiIWcyOO11JA");
        }
        return openIds.remove(0);
    }

//    public static String getNumber() {
//        String words = "0123456789";
//        StringBuffer val = new StringBuffer("0.");
//        Random random = new Random();
//        for (int i = 0; i < 16; i++) {
//            val.append(words.charAt(random.nextInt(10)));
//        }
////        System.out.println(val.toString());
//        return val.toString();
//    }
//
//    public static void main(String[] args) {
//        System.out.println(getNumber());
//    }

}
