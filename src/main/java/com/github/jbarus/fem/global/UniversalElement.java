package com.github.jbarus.fem.global;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.function.BiFunction;
import java.util.function.Function;

public class UniversalElement {
    private RealMatrix dNdEta;
    private RealMatrix dNdKsi;
    private RealMatrix[] surfaces;
    private double[] points;
    private double[] weights;
    private RealMatrix NValues;
    private int numberOfPoints;

    Function<Double, Double>[] dNdEtaFunc = new Function[4];
    Function<Double, Double>[] dNdKsiFunc = new Function[4];
    BiFunction<Double, Double, Double>[] NFunc = new BiFunction[4];

    public UniversalElement(int numberOfPoints){
        this.numberOfPoints = numberOfPoints;
        dNdEta = MatrixUtils.createRealMatrix(numberOfPoints*numberOfPoints,4);
        dNdKsi = MatrixUtils.createRealMatrix(numberOfPoints*numberOfPoints,4);
        initGaussArr();
        initFunc();
        populateArrays();
    }

    private void populateArrays() {
        for (int i = 0; i < dNdKsi.getRowDimension(); i++) {
            for (int j = 0; j < dNdKsi.getColumnDimension(); j++) {
                /*ksi[i][j] = ksiFunc[j].apply(points[i % size]);
                eta[i][j] = etaFunc[j].apply(points[i % size]);*/
                System.out.println(i/2);
                dNdKsi.setEntry(i,j,dNdKsiFunc[j].apply(points[i/numberOfPoints]));
                dNdEta.setEntry(i,j,dNdEtaFunc[j].apply(points[i%numberOfPoints]));
            }
        }
    }

    private void initFunc() {
        dNdKsiFunc[0] = x -> -0.25 * (1 - x);
        dNdKsiFunc[1] = x -> 0.25 * (1 - x);
        dNdKsiFunc[2] = x -> 0.25 * (1 + x);
        dNdKsiFunc[3] = x -> -0.25 * (1 + x);

        dNdEtaFunc[0] = x -> -0.25 * (1 - x);
        dNdEtaFunc[1] = x -> -0.25 * (1 + x);
        dNdEtaFunc[2] = x -> 0.25 * (1 + x);
        dNdEtaFunc[3] = x -> 0.25 * (1 - x);
    }

    private void initGaussArr() {
        if (numberOfPoints == 2) {
            points = new double[]{-1 / Math.sqrt(3), 1 / Math.sqrt(3)};
            weights = new double[]{1, 1};
        }
        if (numberOfPoints == 3) {
            points = new double[]{-Math.sqrt((3.0 / 5.0)), 0.0, Math.sqrt((3.0 / 5.0))};
            weights = new double[]{5.0 / 9.0, 8.0 / 9.0, 5.0 / 9.0};
        }
        if (numberOfPoints == 4) {
            points = new double[]{-1.0 * Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))), -1.0 * Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))),
                    Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))), Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0)))};
            weights = new double[]{(18.0-Math.sqrt(30.0))/36.0, (18.0+Math.sqrt(30.0))/36.0, (18.0+Math.sqrt(30.0))/36.0, (18.0-Math.sqrt(30.0))/36.0};
        }
    }
}
