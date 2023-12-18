package com.github.jbarus.fem;

import com.github.jbarus.fem.global.DataLoader;
import com.github.jbarus.fem.global.GlobalData;
import com.github.jbarus.fem.global.UniversalElement;
import com.github.jbarus.fem.structures.Grid;

import java.lang.reflect.InvocationTargetException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        DataLoader dataLoader = new DataLoader("src/main/resources/Test1_4_4.txt");
        GlobalData globalData = new GlobalData();
        dataLoader.loadGlobalData(globalData);
        Grid grid = new Grid(globalData.getNodes(), globalData.getElements());
        dataLoader.loadGrid(grid);
        UniversalElement universalElement = new UniversalElement(3);


    }
}