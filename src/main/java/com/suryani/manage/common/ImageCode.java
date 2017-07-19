package com.suryani.manage.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author soldier
 */
public class ImageCode {
    public static void main(String[] args) {
        try {
//            BufferedImage image = ImageIO.read(new File("D:\\image\\DrawImage (3).png"));
//            List<BufferedImage> bufferedImageList = splitImage(removeBackground(image));
//            for (BufferedImage bufferedImage : bufferedImageList) {
//                ImageIO.write(bufferedImage, "png", new File("D:\\验证码\\spit\\" + System.currentTimeMillis() + ".png"));
//            }
//            String[] wordArray = new String[]{"无", "老", "倒", "色", "石", "答", "装", "林", "动"};
//            for (String word : wordArray) {
//                for (int i = 330; i <= 400; i++) {
//                    if (i % 5 == 0) {
//                        ImageIO.write(splitWidthImage(splitHeightImage(Rotate(drawString(word), i))), "png", new File("D:\\验证码\\wordnew\\" + word + i + ".png"));
//                    }
//                }
//            }
//            for (BufferedImage bufferedImage : bufferedImageList) {
//                check(bufferedImage);
//            }
//            imageToWord("见笑两带其又改定友", "1", "http://wechat.xmsmjk.com/zycapwxsehr/DrawImage?openid=oYNMFt49BHWpGRDCzqmtJg_vrfXg&_=0.16891295321519362");
//

//            HttpUriRequest request = RequestBuilder.get()
//                    .setUri("http://wechat.xmsmjk.com/zycapwxsehr/DrawImage?openid=oYNMFt49BHWpGRDCzqmtJg_vrfXg&_=0.16891295321519362")
//                    .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
//                    .build();
//            HttpResponse httpResponse = httpClient.execute(request);
//            imageToWord("见笑两带其又改定友", "1", httpResponse.getEntity().getContent());
//            ImageIO.write(Rotate(drawString("慢"), 340), "png", new File("D:\\验证码\\wordnew\\" + System.currentTimeMilli() + ".png"));
//            System.out.println(URLEncoder.encode("厦门市妇幼保健院", "UTF-8"));

//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost post = new HttpPost("http://wechat.xmsmjk.com/zycapwxsehr/HospitalNoteController/getRegister.do");
//            post.setHeader("Cookie", "JSESSIONID=42420FBE05B4709BCF49963F56106A2E");
//            post.setHeader("Host", "wechat.xmsmjk.com");
//            post.setHeader("Oid", "WDZJWU5JcW1RQTBvc2dUbHJnWFIvbW5nSEZOdVVJbTJoTzN6ZjBjK1U4TEx2TzVnRWdzYm5SMmFi%0D%0AaHFPaU81M0pBYmgrRS91aTVIZQ0KWURIWFYrUVo5Zz09");
//            post.setHeader("Ssid", "600865500445500594400905502");
//            post.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 4.4.4; HM NOTE 1LTEW Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 MicroMessenger/6.0.0.54_r849063.501 NetType/WIFI");
//            post.setHeader("Referer", "http://wechat.xmsmjk.com/zycapwxsehr/view/appointment/confirm.jsp");
//            //创建参数列表
//            List<NameValuePair> list = new ArrayList<NameValuePair>();
//            list.add(new BasicNameValuePair("orgCode", "350211G1001"));
//            list.add(new BasicNameValuePair("deptCode", "321"));
//            list.add(new BasicNameValuePair("docCode", "042"));
//            list.add(new BasicNameValuePair("sectionType", "AM "));
//            list.add(new BasicNameValuePair("startTime", "2017-07-12 11:00:00"));
//            list.add(new BasicNameValuePair("timeCode", "VyVg8Z1UX+Iw+jvnKBbg18cUWT4p7KJxLH2kOxnysYj35K7aQ8oYOsVly3JGr4Bx"));
//            list.add(new BasicNameValuePair("ssid", "600865500445500594400905502"));
//            list.add(new BasicNameValuePair("patientName", "许少军"));
//            list.add(new BasicNameValuePair("patientID", "500135400835500094400975500665400875500075400935500365"));
//            list.add(new BasicNameValuePair("patientPhone", "400935500594400845500484500035505"));
//            list.add(new BasicNameValuePair("patientSex", "男"));
//            list.add(new BasicNameValuePair("orgName", "厦门市妇幼保健院"));
//            list.add(new BasicNameValuePair("openID", "WDZJWU5JcW1RQTBvc2dUbHJnWFIvbW5nSEZOdVVJbTJoTzN6ZjBjK1U4TEx2TzVnRWdzYm5SMmFiaHFPaU81M0pBYmgrRS91aTVIZQ0KWURIWFYrUVo5Zz09"));
//            list.add(new BasicNameValuePair("deptName", "妇科门诊"));
//            list.add(new BasicNameValuePair("doctorName", "曹雅琴"));
//            list.add(new BasicNameValuePair("seq", "201707121100"));
//            list.add(new BasicNameValuePair("state", "123"));
//            list.add(new BasicNameValuePair("iptCode", "建原礼爬"));
//            //url格式编码
//            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
//            post.setEntity(uefEntity);
//            System.out.println("POST 请求...." + post.getURI());
//            //执行请求
//            HttpResponse httpResponse = httpClient.execute(post);
//            String responseStr = EntityUtils.toString(httpResponse.getEntity());
//            System.out.println(responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String imageToWord(String wordArray, String folder, String url) throws Exception {
        URL get = new URL(url);
        return imageToWord(wordArray, folder, get.openStream());
    }

    public static String imageToWord(String wordArray, String folder, InputStream inputStream) throws Exception {
        String path = "D:\\imagecode\\" + folder;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            File[] files = file.listFiles();
            for (File file1 : files) {
                file1.delete();
            }
        }
        StringBuffer wordBuffer = new StringBuffer();
        BufferedImage image = ImageIO.read(inputStream);
        List<BufferedImage> bufferedImageList = splitImage(removeInterferencePoint(removeBackground(image)));
        for (int k = 0; k < wordArray.length(); k++) {
            String word = wordArray.charAt(k) + "";
            for (int i = 330; i <= 400; i++) {
                if (i % 5 == 0) {
                    ImageIO.write(splitWidthImage(splitHeightImage(Rotate(drawString(word), i))), "png", new File(path + "\\" + word + i + ".png"));
                }
            }
        }
        for (BufferedImage bufferedImage : bufferedImageList) {
            String resultWord = check(bufferedImage, path);
            wordBuffer.append(resultWord);
        }
        return wordBuffer.toString();
    }

    public static BufferedImage removeBackground(BufferedImage img) throws Exception {
//        BufferedImage img = ImageIO.read(new File(picFile));
        List<Map.Entry<Integer, Integer>> arrayList = getTopRgb(img);
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (fourWordsColor(arrayList, img.getRGB(x, y)) != 1) {
                    img.setRGB(x, y, Color.white.getRGB());
                } else {
                    if (isWhite(img.getRGB(x, y)) == 1) {
                        img.setRGB(x, y, img.getRGB(x, y));
                    } else {
                        img.setRGB(x, y, Color.black.getRGB());
                    }

                }
            }
        }
        return img;
    }

    public static BufferedImage removeInterferencePoint(BufferedImage img) throws Exception {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (isWhite(img.getRGB(x, y)) == 0 && noWhileNumber(img, height, width, x, y) < 3) {
                    img.setRGB(x, y, Color.white.getRGB());
                } else {
                    img.setRGB(x, y, img.getRGB(x, y));
                }
            }
        }
        ImageIO.write(img, "png", new File("D:\\验证码\\spit\\" + System.currentTimeMillis() + ".png"));
        return img;
    }

    public static List getTopRgb(BufferedImage img) throws Exception {
        Map<Integer, Integer> map = new TreeMap<>();
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                map.put(img.getRGB(x, y), map.get(img.getRGB(x, y)) == null ? 0 : map.get(img.getRGB(x, y)) + 1);
            }
        }

        List arrayList = new ArrayList(map.entrySet());
        Collections.sort(arrayList, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map.Entry obj1 = (Map.Entry) o1;
                Map.Entry obj2 = (Map.Entry) o2;
                return ((Integer) obj2.getValue()).compareTo((Integer) obj1.getValue());
            }
        });


//        System.out.println(arrayList);
        return arrayList;
    }

    public static int fourWordsColor(List<Map.Entry<Integer, Integer>> arrayList, int rgb) throws Exception {

        for (int i = 1; i < (arrayList.size() >= 7 ? 7 : arrayList.size()); i++) {
            Map.Entry<Integer, Integer> m = arrayList.get(i);
            if (m.getKey() == rgb && isInterfereRGB(rgb) == 0) {
                return 1;
            }
        }
        return 0;
    }

    public static int noWhileNumber(BufferedImage img, int height, int width, int x, int y) {
        int noWhileNumber = 0;
        if (isWhite(img.getRGB(x, y)) == 0) {
            noWhileNumber++;
            if (height > (y + 1) && isWhite(img.getRGB(x, y + 1)) == 0) {
                noWhileNumber++;
            }
            if (height > (y + 2) && isWhite(img.getRGB(x, y + 2)) == 0) {
                noWhileNumber++;
            }
            if ((y - 1) >= 0 && isWhite(img.getRGB(x, y - 1)) == 0) {
                noWhileNumber++;
            }
            if ((y - 2) >= 0 && isWhite(img.getRGB(x, y - 2)) == 0) {
                noWhileNumber++;
            }
            if (width > (x + 1) && isWhite(img.getRGB(x + 1, y)) == 0) {
                noWhileNumber++;
            }
            if (width > (x + 2) && isWhite(img.getRGB(x + 2, y)) == 0) {
                noWhileNumber++;
            }
            if ((x - 1) >= 0 && isWhite(img.getRGB(x - 1, y)) == 0) {
                noWhileNumber++;
            }
            if ((x - 2) >= 0 && isWhite(img.getRGB(x - 2, y)) == 0) {
                noWhileNumber++;
            }
        }
        return noWhileNumber;
    }

    public static int isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRGB() == Color.black.getRGB()) {
            return 1;
        }
        return 0;
    }

    public static int isGreen(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRGB() == -16711936) {
            return 1;
        }
        return 0;
    }

    public static int isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRGB() == Color.white.getRGB()) {
            return 1;
        }
        return 0;
    }

    public static int isInterfereRGB(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRGB() == -16712192 || color.getRGB() == -16711936 || color.getRGB() == -16777216 || color.getRGB() == -285497 || color.getRGB() == -5464839 || color.getRGB() == -5641028 || color.getRGB() == -3557656 || color.getRGB() == -1250824 || color.getRGB() == -3494729 || color.getRGB() == -2576392 || color.getRGB() == -1) {
            return 1;
        }
        return 0;
    }

    public static List<BufferedImage> splitImage(BufferedImage img) throws Exception {
        List<BufferedImage> subImageList = new ArrayList<BufferedImage>();
        int width = img.getWidth();
        int height = img.getHeight();
        Map<Integer, Integer> heightMap = new LinkedHashMap<>();
        for (int x = 0; x < width; x++) {
            int count = 0;
            for (int y = 0; y < height; y++) {
                if (isWhite(img.getRGB(x, y)) == 1) {
                    count++;
                }
            }
            heightMap.put(x, count);
        }
        int spitWidthBegin = 0;
        int spitWidthEnd = 0;
        List<Integer> markWidthList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> mapEntry : heightMap.entrySet()) {
            if (mapEntry.getKey() == 0 && mapEntry.getValue() != height) {
                spitWidthBegin = mapEntry.getKey();
                markWidthList.add(spitWidthBegin);
            } else if (mapEntry.getKey() != 0 && mapEntry.getValue() != height && heightMap.get(mapEntry.getKey() - 1) == height) {
                if (mapEntry.getKey() - spitWidthBegin > 10 || spitWidthBegin == 0) {
                    spitWidthBegin = mapEntry.getKey();
                    markWidthList.add(spitWidthBegin);
                }

            }
            if (mapEntry.getKey() == width && mapEntry.getValue() != height) {
                spitWidthEnd = mapEntry.getKey();
                markWidthList.add(spitWidthEnd);
            } else if (mapEntry.getKey() != 0 && mapEntry.getValue() != height && heightMap.get(mapEntry.getKey() + 1) == height) {
                if (mapEntry.getKey() - spitWidthBegin > 10) {
                    spitWidthEnd = mapEntry.getKey() + 1;
                    markWidthList.add(spitWidthEnd);
                }

            }
        }

        //如果不是切割成四个字
        if (markWidthList.size() < 8) {
            Map<String, Integer> saveBetween = new HashMap<>();
            int tempMax = 0;
            for (int i = 0; i < markWidthList.size(); ) {
                if (markWidthList.get(i + 1) - markWidthList.get(i) > tempMax) {
                    tempMax = markWidthList.get(i + 1) - markWidthList.get(i);
                    saveBetween.put("index", i);
                    saveBetween.put("start", markWidthList.get(i));
                    saveBetween.put("end", markWidthList.get(i + 1));
                    saveBetween.put("tempMax", tempMax);
                }
                i = i + 2;
            }
            int middle = (saveBetween.get("start") + saveBetween.get("end")) / 2;
            markWidthList.add(saveBetween.get("index") + 1, middle);
            markWidthList.add(saveBetween.get("index") + 2, middle + 1);
        }
//        System.out.println(JSONBinder.toJSON(markWidthList));
        subImageList.add(splitHeightImage(img.getSubimage(markWidthList.get(0), 0, markWidthList.get(1) - markWidthList.get(0), height)));
        subImageList.add(splitHeightImage(img.getSubimage(markWidthList.get(2), 0, markWidthList.get(3) - markWidthList.get(2), height)));
        subImageList.add(splitHeightImage(img.getSubimage(markWidthList.get(4), 0, markWidthList.get(5) - markWidthList.get(4), height)));
        subImageList.add(splitHeightImage(img.getSubimage(markWidthList.get(6), 0, markWidthList.get(7) - markWidthList.get(6), height)));
        return subImageList;


    }

    public static BufferedImage splitWidthImage(BufferedImage img) throws Exception {
        int width = img.getWidth();
        int height = img.getHeight();
        Map<Integer, Integer> heightMap = new LinkedHashMap<>();
        for (int x = 0; x < width; x++) {
            int count = 0;
            for (int y = 0; y < height; y++) {
                if (isWhite(img.getRGB(x, y)) == 1) {
                    count++;
                }
            }
            heightMap.put(x, count);
        }
        int spitWidthBegin = 0;
        int spitWidthEnd = 0;
        List<Integer> markWidthList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> mapEntry : heightMap.entrySet()) {
            if (mapEntry.getKey() == 0 && mapEntry.getValue() != height) {
                spitWidthBegin = mapEntry.getKey();
                markWidthList.add(spitWidthBegin);
            } else if (mapEntry.getKey() != 0 && mapEntry.getValue() != height && heightMap.get(mapEntry.getKey() - 1) == height) {
                spitWidthBegin = mapEntry.getKey();
                markWidthList.add(spitWidthBegin);
            }
            if (mapEntry.getKey() == width && mapEntry.getValue() != height) {
                spitWidthEnd = mapEntry.getKey();
                markWidthList.add(spitWidthEnd);
            } else if (mapEntry.getKey() != 0 && mapEntry.getValue() != height && heightMap.get(mapEntry.getKey() + 1) == height) {
                spitWidthEnd = mapEntry.getKey() + 1;
                markWidthList.add(spitWidthEnd);
            }
        }
        return img.getSubimage(markWidthList.get(0), 0, markWidthList.get(markWidthList.size() - 1) - markWidthList.get(0), height);

    }

    public static BufferedImage splitHeightImage(BufferedImage img) throws Exception {
        int width = img.getWidth();
        int height = img.getHeight();
        Map<Integer, Integer> widthMap = new LinkedHashMap<>();
        for (int y = 0; y < height; y++) {
            int count = 0;
            for (int x = 0; x < width; x++) {
                if (isWhite(img.getRGB(x, y)) == 1) {
                    count++;
                }
            }
            widthMap.put(y, count);
        }

        int spitHeightBegin = 0;
        int spitHeightEnd = 0;
        List<Integer> markHeightList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> mapEntry : widthMap.entrySet()) {
            if (mapEntry.getKey() == 0 && mapEntry.getValue() != width) {
                spitHeightBegin = mapEntry.getKey();
                markHeightList.add(spitHeightBegin);
            } else if (mapEntry.getKey() != 0 && mapEntry.getValue() != width && widthMap.get(mapEntry.getKey() - 1) == width) {
                spitHeightBegin = mapEntry.getKey();
                markHeightList.add(spitHeightBegin);
            }
            if (mapEntry.getKey() == height && mapEntry.getValue() != width) {
                spitHeightEnd = mapEntry.getKey();
                markHeightList.add(spitHeightEnd);
            } else if (mapEntry.getKey() != 0 && mapEntry.getValue() != width && widthMap.get(mapEntry.getKey() + 1) == width) {
                spitHeightEnd = mapEntry.getKey() + 1;
                markHeightList.add(spitHeightEnd);
            }
        }
//        System.out.println(JSONBinder.toJSON(markHeightList));
        return img.getSubimage(0, markHeightList.get(0), width, markHeightList.get(markHeightList.size() - 1) - markHeightList.get(0));

    }

    public static BufferedImage drawString(String message) {
        int width = 50;
        int height = 50;
        int fontSize = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.black);
        g.setBackground(Color.white);
        Font f = new Font("宋体", Font.CENTER_BASELINE, fontSize);
        g.setFont(f);
        int len = message.length();
        g.drawString(message, (width - fontSize * len) / 2,
                (height + (int) (fontSize / 1.5)) / 2);
        g.dispose();
        return image;
    }

    public static BufferedImage Rotate(Image src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, rect_des.width, rect_des.height);
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }

    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    public static String check(BufferedImage img, String path) {
        String result = "";
        try {
            Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
            File dir = new File(path);
            File[] files = dir.listFiles();
            for (File file : files) {
                map.put(ImageIO.read(file), file.getName().charAt(0) + "");
            }
            int width = img.getWidth();
            int height = img.getHeight();
            int temp = 0;
            for (BufferedImage bi : map.keySet()) {
                int count = 0;
                int biWidth = bi.getWidth();
                int biHeight = bi.getHeight();
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        if (y < biHeight && x < biWidth && isBlack(img.getRGB(x, y)) == isBlack(bi.getRGB(x, y))) {
                            count++;
                        }

                    }
                }
//                System.out.println(map.get(bi) + "=" + count);
                if (temp < count) {
                    temp = count;
//                    System.out.println(map.get(bi) + "=" + count);
                    result = map.get(bi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(result);
        return result;
    }


}
