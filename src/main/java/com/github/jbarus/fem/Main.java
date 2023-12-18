package com.github.jbarus.fem;

import com.github.jbarus.fem.data.DataLoader;
import com.github.jbarus.fem.data.GlobalData;
import com.github.jbarus.fem.data.Grid;
import com.github.jbarus.fem.gauss.GaussQuadrature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        DataLoader dataLoader = new DataLoader("src/main/resources/Test1_4_4.txt");
        GlobalData globalData = new GlobalData();
        dataLoader.loadGlobalData(globalData);
        Grid grid = new Grid(globalData.getNodes(), globalData.getElements());
        dataLoader.loadGrid(grid);



    }
}