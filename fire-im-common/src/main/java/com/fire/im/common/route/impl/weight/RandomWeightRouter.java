package com.fire.im.common.route.impl.weight;

import com.fire.im.common.route.Router;

import java.util.*;

/**
 * 随机权重路由
 * @Author: wangzc
 * @Date: 2020/11/25 10:27
 */

public class RandomWeightRouter implements Router {

    private TreeMap<Double,String> weightMap = new TreeMap<>();

    @Override
    public String chooseServer(List<String> values, String key) {
        values.stream().forEach(serverStr -> {
            Double weight = Integer.valueOf(serverStr.split(":")[3]).doubleValue(); //weight
            Double lastWeight = weightMap.size() == 0 ? 0 : weightMap.lastKey().doubleValue();
            this.weightMap.put(weight + lastWeight, serverStr);
        });
        return select();
    }

    public String select() {
        //累加值 * 0~1 random number
        Double random = this.weightMap.lastKey() * Math.random();
        SortedMap<Double, String> tailMap = this.weightMap.tailMap(random, false);
        return this.weightMap.get(tailMap.firstKey());
    }

//    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        list.add("1:1:1:1");
//        list.add("2:2:2:2");
//        list.add("3:3:3:3");
//        list.add("4:4:4:4");
//        list.add("5:5:5:5");
//        list.add("6:6:6:6");
//        list.add("7:7:7:7");
//        list.add("8:8:8:8");
//        list.add("9:9:9:9");
//        list.add("10:10:10:10");
//
//
//        List<String> count = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            String server = new WeightRouter().chooseServer(list, "");
//            count.add(server);
//        }
//
//        System.out.println("10：" + Collections.frequency(count, "10:10:10:10"));
//        System.out.println("9：" + Collections.frequency(count, "9:9:9:9"));
//        System.out.println("8：" + Collections.frequency(count, "8:8:8:8"));
//        System.out.println("7：" + Collections.frequency(count, "7:7:7:7"));
//        System.out.println("6：" + Collections.frequency(count, "6:6:6:6"));
//        System.out.println("5：" + Collections.frequency(count, "5:5:5:5"));
//        System.out.println("4：" + Collections.frequency(count, "4:4:4:4"));
//        System.out.println("3：" + Collections.frequency(count, "3:3:3:3"));
//        System.out.println("2：" + Collections.frequency(count, "2:2:2:2"));
//        System.out.println("1：" + Collections.frequency(count, "1:1:1:1"));
//
//    }
}
