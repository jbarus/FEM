package com.github.jbarus.fem.integration;

import com.github.jbarus.fem.structures.Element;
import com.github.jbarus.fem.structures.Grid;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class SystemOfEquations {
    public void calculateGlobal(Grid grid){
        RealMatrix HGlobal = MatrixUtils.createRealMatrix(grid.getNodes().length,grid.getNodes().length);
        RealMatrix PGlobal = MatrixUtils.createRealMatrix(grid.getNodes().length,1);
        RealMatrix CGlobal = MatrixUtils.createRealMatrix(grid.getNodes().length,grid.getNodes().length);
        for(Element element : grid.getElements()){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    HGlobal.addToEntry(element.getId()[i]-1,element.getId()[j]-1,element.getH()[i][j]+element.getHBC()[i][j]);
                    CGlobal.addToEntry(element.getId()[i]-1,element.getId()[j]-1,element.getC()[i][j]);
                }
                PGlobal.addToEntry(element.getId()[i]-1,0,element.getP()[i][0]);
            }
        }
        System.out.println(Arrays.deepToString(CGlobal.getData()));
    }
}
