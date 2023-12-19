package com.github.jbarus.fem.integration;

import com.github.jbarus.fem.global.GlobalData;
import com.github.jbarus.fem.structures.Element;
import com.github.jbarus.fem.structures.Grid;
import org.apache.commons.math3.linear.*;

import java.util.Arrays;

public class SystemOfEquations {
    RealMatrix HGlobal;
    RealMatrix PGlobal;
    RealMatrix CGlobal;
    Grid grid;
    GlobalData globalData;

    public SystemOfEquations(Grid grid, GlobalData globalData) {
        this.grid = grid;
        this.globalData = globalData;
        HGlobal = MatrixUtils.createRealMatrix(grid.getNodes().length,grid.getNodes().length);
        PGlobal = MatrixUtils.createRealMatrix(grid.getNodes().length,1);
        CGlobal = MatrixUtils.createRealMatrix(grid.getNodes().length,grid.getNodes().length);
    }

    public void calculateGlobal(){
        for(Element element : grid.getElements()){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    HGlobal.addToEntry(element.getId()[i]-1,element.getId()[j]-1,element.getH()[i][j]+element.getHBC()[i][j]);
                    CGlobal.addToEntry(element.getId()[i]-1,element.getId()[j]-1,element.getC()[i][j]);
                }
                PGlobal.addToEntry(element.getId()[i]-1,0,element.getP()[i][0]);
            }
        }
    }

    public double[][] simulateTemperature(int length){
        RealMatrix L = MatrixUtils.createRealMatrix(16,16);
        RealMatrix P = MatrixUtils.createRealMatrix(16,1);
        L = HGlobal.add(CGlobal.scalarMultiply(1.0/globalData.getSimulationStepTime()));
        double[] start = new double[grid.getNodes().length];
        for (int i = 0; i < start.length; i++) {
            start[i] = 100;
        }
        P = PGlobal.add(CGlobal.scalarMultiply(1.0/globalData.getSimulationStepTime()).multiply(MatrixUtils.createColumnRealMatrix(start)));

        DecompositionSolver solver = new LUDecomposition(L).getSolver();
        RealVector constants = new ArrayRealVector(P.getColumn(0));
        RealVector solution = solver.solve(constants);

        double[][] out = new double[length][grid.getNodes().length];
        out[0] = solution.toArray();
        for (int i = 1; i < length; i++) {
            P = PGlobal.add(CGlobal.scalarMultiply(1.0/globalData.getSimulationStepTime()).multiply(MatrixUtils.createColumnRealMatrix(solution.toArray())));
            constants = new ArrayRealVector(P.getColumn(0));
            solution = solver.solve(constants);
            out[i] = solution.toArray();
        }
        return out;
    }
}
