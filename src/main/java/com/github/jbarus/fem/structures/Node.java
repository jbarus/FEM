package com.github.jbarus.fem.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Class that holds coordinates of each one node in the grid
public class Node {
    private double x;
    private double y;
    private int BC;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
