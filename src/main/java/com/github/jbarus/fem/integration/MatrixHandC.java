package com.github.jbarus.fem.integration;

import com.github.jbarus.fem.global.GlobalData;
import com.github.jbarus.fem.global.UniversalElement;
import com.github.jbarus.fem.structures.Element;
import com.github.jbarus.fem.structures.Node;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class MatrixHandC {
    private UniversalElement universalElement;
    private GlobalData globalData;

    public MatrixHandC(UniversalElement universalElement, GlobalData globalData) {
        this.universalElement = universalElement;
        this.globalData = globalData;
    }

    public void calculateHandC(Element element, Node[] nodes){

        RealMatrix[] jacobianMatrix = new RealMatrix[universalElement.getNumberOfPoints()*universalElement.getNumberOfPoints()];
        calculateJacobianMatrix(jacobianMatrix,nodes);

        double[] detJacobian = new double[universalElement.getNumberOfPoints()*universalElement.getNumberOfPoints()];
        calculateDetJacobian(detJacobian,jacobianMatrix);

        RealMatrix[] reverseJacobianMatrix = new RealMatrix[universalElement.getNumberOfPoints()* universalElement.getNumberOfPoints()];
        calculateReverseJacobianMatrix(reverseJacobianMatrix,jacobianMatrix);

        RealMatrix dNdX = MatrixUtils.createRealMatrix(new double[universalElement.getNumberOfPoints()*universalElement.getNumberOfPoints()][4]);
        RealMatrix dNdY = MatrixUtils.createRealMatrix(new double[universalElement.getNumberOfPoints()*universalElement.getNumberOfPoints()][4]);
        calculateDXandDY(dNdX, dNdY, reverseJacobianMatrix);

        RealMatrix[] HPartial = new RealMatrix[universalElement.getNumberOfPoints()*universalElement.getNumberOfPoints()];
        RealMatrix[] CPartial = new RealMatrix[universalElement.getNumberOfPoints()*universalElement.getNumberOfPoints()];
        calculatePartialH(HPartial,CPartial,dNdX,dNdY,detJacobian);

        RealMatrix C = MatrixUtils.createRealMatrix(4,4);
        RealMatrix H = MatrixUtils.createRealMatrix(4,4);
        for (int i = 0; i < universalElement.getNumberOfPoints() * universalElement.getNumberOfPoints(); i++) {
            HPartial[i] = HPartial[i].scalarMultiply(universalElement.getWeights()[i% universalElement.getNumberOfPoints()]).scalarMultiply(universalElement.getWeights()[1/ universalElement.getNumberOfPoints()]);
            H = H.add(HPartial[i]);

            CPartial[i] = CPartial[i].scalarMultiply(universalElement.getWeights()[i% universalElement.getNumberOfPoints()]).scalarMultiply(universalElement.getWeights()[1/ universalElement.getNumberOfPoints()]);
            C= C.add(CPartial[i]);
        }
        System.out.println(Arrays.deepToString(C.getData()));
        element.setC(C.getData());
        element.setH(H.getData());
    }

    private void calculatePartialH(RealMatrix[] H, RealMatrix[] C, RealMatrix dNdX, RealMatrix dNdY, double[] detJacobian) {
        for (int i = 0; i < H.length; i++) {
            H[i] = dNdX.getRowMatrix(i).transpose().multiply(dNdX.getRowMatrix(i));
            H[i] = H[i].add(dNdY.getRowMatrix(i).transpose().multiply(dNdY.getRowMatrix(i)));
            H[i] = H[i].scalarMultiply(globalData.getConductivity()).scalarMultiply(detJacobian[i]);

            C[i] = universalElement.getNValues().getRowMatrix(i).transpose().multiply(universalElement.getNValues().getRowMatrix(i));
            C[i] = C[i].scalarMultiply(globalData.getSpecificHeat()).scalarMultiply(globalData.getDensity()).scalarMultiply(detJacobian[i]);
        }
    }

    private void calculateDXandDY(RealMatrix dNdX, RealMatrix dNdY,RealMatrix[] reverseJacobianMatrix) {
        RealMatrix temp;
        RealMatrix matrix;
        for (int i = 0; i < dNdX.getRowDimension(); i++) {
            for (int j = 0; j < 4; j++) {
                temp = reverseJacobianMatrix[i];
                matrix = MatrixUtils.createRealMatrix(2,1);
                matrix.setEntry(0,0,universalElement.getDNdKsi().getEntry(i,j));
                matrix.setEntry(1,0,universalElement.getDNdEta().getEntry(i,j));
                matrix = temp.multiply(matrix);
                dNdX.setEntry(i,j,matrix.getEntry(0,0));
                dNdY.setEntry(i,j,matrix.getEntry(1,0));
            }

        }
    }

    private void calculateDetJacobian(double[] detJacobian, RealMatrix[] jacobianMatrix) {
        for (int i = 0; i < detJacobian.length; i++) {
            detJacobian[i] = (new LUDecomposition(jacobianMatrix[i])).getDeterminant();
        }
    }

    private void calculateReverseJacobianMatrix(RealMatrix[] reverseJacobianMatrix, RealMatrix[] jacobianMatrix) {
        for (int i = 0; i < reverseJacobianMatrix.length; i++) {
            reverseJacobianMatrix[i] = MatrixUtils.inverse(jacobianMatrix[i]);
        }
    }

    private void calculateJacobianMatrix(RealMatrix[] jacobianMatrix, Node[] nodes) {
        for (int i = 0; i < jacobianMatrix.length; i++) {
            double[][] temp = new double[2][2];
            temp[0][0] = nodes[0].getX() * universalElement.getDNdKsi().getEntry(i,0) + nodes[1].getX() * universalElement.getDNdKsi().getEntry(i,1) + nodes[2].getX() * universalElement.getDNdKsi().getEntry(i,2) + nodes[3].getX() * universalElement.getDNdKsi().getEntry(i,3);
            temp[0][1] = nodes[0].getY() * universalElement.getDNdKsi().getEntry(i,0) + nodes[1].getY() * universalElement.getDNdKsi().getEntry(i,1) + nodes[2].getY() * universalElement.getDNdKsi().getEntry(i,2) + nodes[3].getY() * universalElement.getDNdKsi().getEntry(i,3);
            temp[1][0] = nodes[0].getX() * universalElement.getDNdEta().getEntry(i,0) + nodes[1].getX() * universalElement.getDNdEta().getEntry(i,1) + nodes[2].getX() * universalElement.getDNdEta().getEntry(i,2) + nodes[3].getX() * universalElement.getDNdEta().getEntry(i,3);
            temp[1][1] = nodes[0].getY() * universalElement.getDNdEta().getEntry(i,0) + nodes[1].getY() * universalElement.getDNdEta().getEntry(i,1) + nodes[2].getY() * universalElement.getDNdEta().getEntry(i,2) + nodes[3].getY() * universalElement.getDNdEta().getEntry(i,3);
            jacobianMatrix[i] = MatrixUtils.createRealMatrix(temp);
        }
    }
}
