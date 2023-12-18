package com.github.jbarus.fem.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Class that holds data for grid that is used
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalData {
    private double simulationTime;
    private double simulationStepTime;
    private double conductivity;
    private double alfa;
    private double tot;
    private double initialTemp;
    private double density;
    private double specificHeat;
    private int nodes;
    private int elements;
}
