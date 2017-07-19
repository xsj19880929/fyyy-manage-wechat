package com.suryani.manage.schedule.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final int NTHREDS = 10;

    public static void main(String[] args) {
        // ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        // for (int i = 0; i < NTHREDS; i++) {
        // Runnable worker = new MyRunnable();
        // executor.execute(worker);
        // }
        // executor.shutdown();
        // while (!executor.isTerminated()) {
        // }
        // System.out.println(\"Finished all threads\");
        String content = "<html><head><title>预约查询</title><link rel=\"stylesheet\" type=\"text/css\" href=\"../data/forum.css\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"><style type=\"text/css\"><!--body {    background-color: #F4F4F4;}--></style></head><body topmargin=0><script language=\"JavaScript\" type=\"text/javascript\">alert(\"预约医生 预约日期    预约时间    预约方式    预约状态    联系电话\n\n钟红秀     2013-9-28   11:00       网络      预约成功    13860161320\n\")history.go(-1)</script></body></html>";
        System.out.println(getICard(content));
    }

    // 正则表达式获取医保卡号
    private static String getICard(String html) {
        if (html == null)
            return null;
        Pattern pattern = Pattern.compile("alert[\\s\\S]*\"\\)");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find())
            return matcher.group().replace("alert(\"", "").replace("\")", "");
        return null;
    }
}
