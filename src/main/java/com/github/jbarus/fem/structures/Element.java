package com.github.jbarus.fem.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Class that holds id's of each vertex that belongs to that element
public class Element {
    private int[] id;
    double[][] H;
    double[][] HBC;
    public double[][] P;
    double[][] C;
}
