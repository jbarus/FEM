package com.github.jbarus.fem.integration;

import com.github.jbarus.fem.global.GlobalData;
import com.github.jbarus.fem.global.UniversalElement;
import com.github.jbarus.fem.structures.Element;
import com.github.jbarus.fem.structures.Node;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixHBC {
    UniversalElement universalElement;
    GlobalData globalData;
    public MatrixHBC(UniversalElement universalElement, GlobalData globalData){
        this.universalElement = universalElement;
        this.globalData = globalData;
    }

    public void calculateHBC(Element element, Node[] nodes){

        double[] det = new double[4];
        for (int i = 0; i < 4; i++) {
            det[i] = Math.sqrt(Math.pow(nodes[(i+1)%4].getX()-nodes[i%4].getX(),2.0)+Math.pow(nodes[(i+1)%4].getY()-nodes[i%4].getY(),2.0));
        }

        RealMatrix HBCPartial = MatrixUtils.createRealMatrix(4,4);
        RealMatrix HBC = MatrixUtils.createRealMatrix(4,4);
        for (int i = 0; i < 4; i++) {
            if(nodes[i%4].getBC() != 0 && nodes[i%4].getBC() == nodes[(i+1)%4].getBC()){
                for (int j = 0; j < universalElement.getNumberOfPoints(); j++) {
                    HBCPartial = HBCPartial.add(universalElement.getSurfaces()[i].getRowMatrix(j).transpose().multiply(universalElement.getSurfaces()[i].getRowMatrix(j)).scalarMultiply(universalElement.getWeights()[j]));
                }
                HBC = HBC.add(HBCPartial.scalarMultiply(globalData.getAlfa()).scalarMultiply(det[i]/2.0));
            }
            HBCPartial = MatrixUtils.createRealMatrix(4,4);
        }

        element.setHBC(HBC.getData());
    }
}
