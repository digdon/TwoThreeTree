package ca.buccaneer.twothreetree.test;

import java.util.Arrays;
import java.util.List;

import ca.buccaneer.twothreetree.TwoThreeTree;

public class Tree23Test {

    public static void main(String[] args) {
        TwoThreeTree<Integer> tree = new TwoThreeTree<>(null);

//        Integer[] values = new Integer[1000];
//        for (int i = 0; i < values.length; i++) {
//            values[i] = i;
//        }

        for (int i = 0; i < 100; i++) {
            System.out.print("adding " + i + "...");
            System.out.flush();
            tree.add(i);
            Integer item = tree.find(i);
            System.out.println(item);
        }
        
        tree.display();
    }
}
