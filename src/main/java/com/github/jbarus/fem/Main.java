package com.github.jbarus.fem;

import com.github.jbarus.fem.global.DataLoader;
import com.github.jbarus.fem.global.GlobalData;
import com.github.jbarus.fem.global.UniversalElement;
import com.github.jbarus.fem.integration.MatrixHandC;
import com.github.jbarus.fem.integration.MatrixHBCandP;
import com.github.jbarus.fem.integration.SystemOfEquations;
import com.github.jbarus.fem.structures.Element;
import com.github.jbarus.fem.structures.Grid;
import com.github.jbarus.fem.structures.Node;
import com.github.jbarus.fem.utils.FileGenerator;

import java.util.Arrays;
import java.util.Collections;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        DataLoader dataLoader = new DataLoader("src/main/resources/Test2_4_4_MixGrid.txt");
        GlobalData globalData = new GlobalData();
        dataLoader.loadGlobalData(globalData);
        Grid grid = new Grid(globalData.getNodes(), globalData.getElements());
        dataLoader.loadGrid(grid);
        UniversalElement universalElement = new UniversalElement(2);
        MatrixHandC matrixHandC = new MatrixHandC(universalElement,globalData);
        MatrixHBCandP matrixHBCandP = new MatrixHBCandP(universalElement,globalData);
        SystemOfEquations systemOfEquations = new SystemOfEquations(grid,globalData);
        for(Element element : grid.getElements()){
            Node[] nodes = new Node[4];
            for (int i = 0; i < 4; i++) {
                nodes[i] = grid.getNodes()[element.getId()[i]-1];
            }
            matrixHandC.calculateHandC(element,nodes);
            matrixHBCandP.calculateHBC(element,nodes);
            //System.out.println(Arrays.deepToString(element.getP()));
        }
        systemOfEquations.calculateGlobal();

        double[][] simulatedTemperature = systemOfEquations.simulateTemperature(10);
        /*for (double[] doubles : simulatedTemperature) {
            System.out.println(Arrays.toString(doubles));
        }*/
        FileGenerator.generateFiles(simulatedTemperature,grid);

        for (int i = 0; i < simulatedTemperature.length; i++) {
            System.out.println(Arrays.stream(simulatedTemperature[i]).max().getAsDouble()+" "+Arrays.stream(simulatedTemperature[i]).min().getAsDouble());
        }

        //FileGenerator.generateFiles(simulatedTemperature,grid);
    }
}