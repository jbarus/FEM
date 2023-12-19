package com.github.jbarus.fem.global;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.function.BiFunction;
import java.util.function.Function;

@Data
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
        surfaces = new RealMatrix[4];
        for (int i = 0; i < surfaces.length; i++) {
            surfaces[i] = MatrixUtils.createRealMatrix(numberOfPoints,4);
        }
        initGaussArr();
        initFunc();
        populateArrays();
    }

    private void populateArrays() {
        for (int i = 0; i < numberOfPoints*numberOfPoints; i++) {
            for (int j = 0; j < 4; j++) {
                dNdKsi.setEntry(i,j,dNdKsiFunc[j].apply(points[i/numberOfPoints]));
                dNdEta.setEntry(i,j,dNdEtaFunc[j].apply(points[i%numberOfPoints]));
            }
        }
        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = 0; j < 4; j++) {
                surfaces[0].setEntry(i,j,NFunc[j].apply(points[i],-1.0));
                surfaces[1].setEntry(i,j,NFunc[j].apply(1.0,points[i]));
                surfaces[2].setEntry(i,j,NFunc[j].apply(points[(numberOfPoints-1) - i],1.0));
                surfaces[3].setEntry(i,j,NFunc[j].apply(-1.0,points[(numberOfPoints-1) - i]));
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

        NFunc[0] = (x,y) -> 0.25 * (1-x) * (1-y);
        NFunc[1] = (x,y) -> 0.25 * (1+x) * (1-y);
        NFunc[2] = (x,y) -> 0.25 * (1+x) * (1+y);
        NFunc[3] = (x,y) -> 0.25 * (1-x) * (1+y);
    }

    private void initGaussArr() {
        if (numberOfPoints == 2) {
            points = new double[]{-1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)};
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
