package com.github.jbarus.fem.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Class that holds whole grid - all nodes and elements
public class Grid {
    private Element[] elements;
    private Node[] nodes;

    //Grid has constant size through whole lifetime so we can set those sizes in constructor
    public Grid(int nN, int nE) {
        nodes = new Node[nN];
        elements = new Element[nE];
    }
}
