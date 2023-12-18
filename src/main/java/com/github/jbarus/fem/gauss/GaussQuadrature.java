package com.github.jbarus.fem.gauss;

import lombok.Getter;

import java.util.function.BiFunction;
import java.util.function.Function;

//Class that does everything connected to integration - calculating points,
//weights and solutions to 1D and 2D functions
@Getter
public class GaussQuadrature {
    double[] points;
    double[] weights;
    public GaussQuadrature(int n) {
        init(n);
    }

    private void init(int n) {
        if(n == 2){
            points = new double[]{-1 / Math.sqrt(3), 1 / Math.sqrt(3)};
            weights = new double[]{1, 1};
        }
        if (n == 3){
            points = new double[]{-Math.sqrt((3.0 / 5.0)), 0.0, Math.sqrt((3.0 / 5.0))};
            weights = new double[]{5.0 / 9.0, 8.0 / 9.0, 5.0 / 9.0};
        }
        if(n == 4){
            points = new double[]{Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))), -Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))), Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))),  -Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0)))};
            weights = new double[]{(18.0+Math.sqrt(30.0))/36.0, (18.0+Math.sqrt(30.0))/36.0, (18.0-Math.sqrt(30.0))/36.0, (18.0-Math.sqrt(30.0))/36.0};
        }
    }

    public double calculate1D(Function<Double,Double> function){
        double sum = 0;
        for (int i = 0; i < points.length; i++) {
            sum += function.apply(points[i])*weights[i];
        }
        return sum;
    }

    public double calculate2D(BiFunction<Double,Double,Double> function){
        double sum = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                sum += function.apply(points[i],points[j])*weights[i]*weights[j];
            }

        }
        return sum;
    }
}
