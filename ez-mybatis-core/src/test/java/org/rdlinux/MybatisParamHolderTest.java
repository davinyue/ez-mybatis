package org.rdlinux;

import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

import java.util.HashMap;

public class MybatisParamHolderTest {
    public static void main(String[] args) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(new HashMap<>());
        String name = paramHolder.getMybatisParamName("a", "a", false);
        System.out.println(name);
        name = paramHolder.getMybatisParamName("a", "a", false);
        System.out.println(name);
        name = paramHolder.getMybatisParamName("a", "a", false);
        System.out.println(name);
        name = paramHolder.getMybatisParamName("a", "a", false);
        System.out.println(name);
        int oldCount = 10;
        //新容量 = 旧容量>>1 + 旧容量
        for (int i = 0; ; i++) {
            int kr = oldCount >> 1;
            oldCount = oldCount + kr;
            System.out.println("扩容次数" + (i + 1) + ", 扩容后容量" + oldCount);
            if (oldCount >= 65536) {
                break;
            }
        }
    }
}
