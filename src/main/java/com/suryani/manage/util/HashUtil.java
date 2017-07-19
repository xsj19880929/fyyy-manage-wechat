package com.suryani.manage.util;

import java.security.MessageDigest;
import java.util.BitSet;

public class HashUtil {
    /* BitSet初始分配2^24个bit */
    private static final int DEFAULT_SIZE = 1 << 25;
    /* 不同哈希函数的种子，一般应取质数 */
    private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };
    private BitSet bits = new BitSet(DEFAULT_SIZE);
    /* 哈希函数对象 */
    private SimpleHash[] func = new SimpleHash[seeds.length];

    public HashUtil() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }

    // 将字符串标记到bits中
    public void add(String value) {
        for (SimpleHash f : func) {
            bits.set(f.hash(value), true);
        }
    }

    // 判断字符串是否已经被bits标记
    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }

    /* 哈希函数类 */
    public static class SimpleHash {
        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        // hash函数，采用简单的加权和hash
        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
            // Math.abs( result % this.MAX_SIZE );
        }
    }

    public int hash(String str, int i) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte[] bytes = md5.digest(str.getBytes());
            int result = bytes[i];
            return result < 0 ? -result : result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 计算字符串MD5值
    public static String getMD5Data(String content) {
        try {
            byte[] src = content.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(src);

            return byte2hex(md5.digest()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String temp = "";
        for (int n = 0; n < b.length; n++) {
            temp = Integer.toHexString(b[n] & 0XFF);
            if (temp.length() == 1) {
                hs = hs + "0" + temp;
            } else {
                hs = hs + temp;
            }
        }

        return hs.toUpperCase();
    }

    public long Tianlhash(String Url) {
        long ulHashValue = 0;
        int iLength = Url.length();
        if (iLength < 0) {
            return 0;
        }
        if (iLength <= 256) {
            ulHashValue = 16777216 * (iLength - 1);
        } else {
            ulHashValue = 4278190080L;
        }
        int i;
        char ucChar;
        if (iLength <= 96) {
            for (i = 1; i <= iLength; i++) {
                ucChar = Url.charAt(i - 1);
                if (ucChar <= 'Z' && ucChar >= 'A') {
                    ucChar = (char) (((byte) ucChar + 32) & 0xff);
                }
                ulHashValue += (3 * i * ucChar * ucChar + 5 * i * ucChar + 7 * i + 11 * ucChar) % 16777216;
            }
        } else {
            for (i = 1; i <= 96; i++) {
                ucChar = Url.charAt(i + iLength - 96 - 1);
                if (ucChar <= 'Z' && ucChar >= 'A') {
                    ucChar = (char) (((byte) ucChar + 32) & 0xff);
                }
                ulHashValue += (3 * i * ucChar * ucChar + 5 * i * ucChar + 7 * i + 11 * ucChar) % 16777216;
            }
        }
        return ulHashValue;
    }

    public int ELFHash(String str, int number) {
        int hash = 0;
        long x = 0l;
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            hash = (hash << 4) + array[i];
            if ((x = (hash & 0xF0000000L)) != 0) {
                hash ^= (x >> 24);
                hash &= ~x;
            }
        }
        int result = (hash & 0x7FFFFFFF) % number;
        return result;
    }

    /**
     * JAVA自己带的算法
     */
    public static int java(String str) {
        int h = 0;
        int off = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            h = 31 * h + str.charAt(off++);
        }
        return h;
    }

    /**
     * 加法hash
     * 
     * @param key
     *            字符串
     * @param prime
     *            一个质数
     * @return hash结果
     */
    public static int additiveHash(String key, int prime) {
        int hash, i;
        for (hash = key.length(), i = 0; i < key.length(); i++)
            hash += key.charAt(i);
        return (hash % prime);
    }

    /**
     * 旋转hash
     * 
     * @param key
     *            输入字符串
     * @param prime
     *            质数
     * @return hash值
     */
    public static int rotatingHash(String key, int prime) {
        int hash, i;
        for (hash = key.length(), i = 0; i < key.length(); ++i)
            hash = (hash << 4) ^ (hash >> 28) ^ key.charAt(i);
        return (hash % prime);
        // return (hash ^ (hash>>10) ^ (hash>>20));
    }

    // 替代：
    // 使用：hash = (hash ^ (hash>>10) ^ (hash>>20)) & mask;
    // 替代：hash %= prime;

    /**
     * MASK值，随便找一个值，最好是质数
     */
    static int M_MASK = 0x8765fed1;

    /**
     * 一次一个hash
     * 
     * @param key
     *            输入字符串
     * @return 输出hash值
     */
    public static int oneByOneHash(String key) {
        int hash, i;
        for (hash = 0, i = 0; i < key.length(); ++i) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        // return (hash & M_MASK);
        return hash;
    }

    /**
     * Bernstein's hash
     * 
     * @param key
     *            输入字节数组
     * @param level
     *            初始hash常量
     * @return 结果hash
     */
    public static int bernstein(String key) {
        int hash = 0;
        int i;
        for (i = 0; i < key.length(); ++i)
            hash = 33 * hash + key.charAt(i);
        return hash;
    }

    //
    // // Pearson's Hash
    // char pearson(char[]key, ub4 len, char tab[256])
    // {
    // char hash;
    // ub4 i;
    // for (hash=len, i=0; i<len; ++i)
    // hash=tab[hash^key[i]];
    // return (hash);
    // }

    // // CRC Hashing，计算crc,具体代码见其他
    // ub4 crc(char *key, ub4 len, ub4 mask, ub4 tab[256])
    // {
    // ub4 hash, i;
    // for (hash=len, i=0; i<len; ++i)
    // hash = (hash >> 8) ^ tab[(hash & 0xff) ^ key[i]];
    // return (hash & mask);
    // }

    /**
     * Universal Hashing
     */
    public static int universal(char[] key, int mask, int[] tab) {
        int hash = key.length, i, len = key.length;
        for (i = 0; i < (len << 3); i += 8) {
            char k = key[i >> 3];
            if ((k & 0x01) == 0)
                hash ^= tab[i + 0];
            if ((k & 0x02) == 0)
                hash ^= tab[i + 1];
            if ((k & 0x04) == 0)
                hash ^= tab[i + 2];
            if ((k & 0x08) == 0)
                hash ^= tab[i + 3];
            if ((k & 0x10) == 0)
                hash ^= tab[i + 4];
            if ((k & 0x20) == 0)
                hash ^= tab[i + 5];
            if ((k & 0x40) == 0)
                hash ^= tab[i + 6];
            if ((k & 0x80) == 0)
                hash ^= tab[i + 7];
        }
        return (hash & mask);
    }

    /**
     * Zobrist Hashing
     */
    public static int zobrist(char[] key, int mask, int[][] tab) {
        int hash, i;
        for (hash = key.length, i = 0; i < key.length; ++i)
            hash ^= tab[i][key[i]];
        return (hash & mask);
    }

    // 32位FNV算法
    static int M_SHIFT = 0;

    /**
     * 32位的FNV算法
     * 
     * @param data
     *            数组
     * @return int值
     */
    public static int FNVHash(byte[] data) {
        int hash = (int) 2166136261L;
        for (byte b : data)
            hash = (hash * 16777619) ^ b;
        if (M_SHIFT == 0)
            return hash;
        return (hash ^ (hash >> M_SHIFT)) & M_MASK;
    }

    /**
     * 改进的32位FNV算法1
     * 
     * @param data
     *            数组
     * @return int值
     */
    public static int FNVHash1(byte[] data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (byte b : data)
            hash = (hash ^ b) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    /**
     * 改进的32位FNV算法1
     * 
     * @param data
     *            字符串
     * @return int值
     */
    public static int FNVHash1(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    /**
     * Thomas Wang的算法，整数hash
     */
    public static int intHash(int key) {
        key += ~(key << 15);
        key ^= (key >>> 10);
        key += (key << 3);
        key ^= (key >>> 6);
        key += ~(key << 11);
        key ^= (key >>> 16);
        return key;
    }

    /**
     * 混合hash算法，输出64位的值
     */
    public static long mixHash(String str) {
        long hash = str.hashCode();
        hash <<= 32;
        hash |= FNVHash1(str);
        return hash;
    }

    public long RSHash(String str) {
        int b = 378551;
        int a = 63689;
        long hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = hash * a + str.charAt(i);
            a = a * b;
        }
        return (hash & 0x7FFFFFFF);
    }

    public long JSHash(String str) {
        long hash = 1315423911;
        for (int i = 0; i < str.length(); i++) {
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        }
        return (hash & 0x7FFFFFFF);
    }

    public long PJWHash(String str) {
        long BitsInUnsignedInt = (long) (4 * 8);
        long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
        long OneEighth = (long) (BitsInUnsignedInt / 8);
        long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
        long hash = 0;
        long test = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash << OneEighth) + str.charAt(i);
            if ((test = hash & HighBits) != 0) {
                hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
            }
        }
        return (hash & 0x7FFFFFFF);
    }

    /**
     * ELF算法
     */
    public static int ELFHash(String str) {
        int hash = 0;
        int x = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + str.charAt(i);
            if ((x = (int) (hash & 0xF0000000L)) != 0) {
                hash ^= (x >> 24);
                hash &= ~x;
            }
        }

        return (hash & 0x7FFFFFFF);
    }

    public long BKDRHash(String str) {
        long seed = 131; // 31 131 1313 13131 131313 etc..
        long hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash * seed) + str.charAt(i);
        }
        return (hash & 0x7FFFFFFF);
    }

    public long SDBMHash(String str) {
        long hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
        }
        return (hash & 0x7FFFFFFF);
    }

    public long DJBHash(String str) {
        long hash = 5381;
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }
        return (hash & 0x7FFFFFFF);
    }

    public long DEKHash(String str) {
        long hash = str.length();
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }
        return (hash & 0x7FFFFFFF);
    }

    public long BPHash(String str) {
        long hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = hash << 7 ^ str.charAt(i);
        }
        return hash;
    }

    public long FNVHash(String str) {
        long fnv_prime = 0x811C9DC5;
        long hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash *= fnv_prime;
            hash ^= str.charAt(i);
        }
        return hash;
    }

    public long APHash(String str) {
        long hash = 0xAAAAAAAA;
        for (int i = 0; i < str.length(); i++) {
            if ((i & 1) == 0) {
                hash ^= ((hash << 7) ^ str.charAt(i) ^ (hash >> 3));
            } else {
                hash ^= (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
            }
        }
        return hash;
    }

    public static void main(String[] args) {
    }

}
