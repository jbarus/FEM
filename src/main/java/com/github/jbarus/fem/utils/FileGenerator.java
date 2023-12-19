package com.github.jbarus.fem.utils;

import com.github.jbarus.fem.structures.Grid;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class FileGenerator {
    public static void generateFiles(double[][] results, Grid grid){
        for (int i = 0; i < results.length; i++) {
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/foo"+i+".vtk"));
                writer.write("# vtk DataFile Version 2.0");
                writer.newLine();
                writer.write("Unstructured Grid Example");
                writer.newLine();
                writer.write("ASCII");
                writer.newLine();
                writer.write("DATASET UNSTRUCTURED_GRID");
                writer.newLine();
                writer.newLine();
                writer.write("POINTS " + grid.getNodes().length + " float");
                writer.newLine();
                for (int j = 0; j < grid.getNodes().length; j++) {
                    writer.write(grid.getNodes()[j].getX() +" "+grid.getNodes()[j].getY()+" "+"0");
                    writer.newLine();
                }
                writer.newLine();
                writer.write("CELLS " + grid.getElements().length + " " + grid.getElements().length*5);
                writer.newLine();
                for (int j = 0; j < grid.getElements().length; j++) {
                    writer.write("4" + " "+(grid.getElements()[j].getId()[0]-1)+" "+(grid.getElements()[j].getId()[1]-1)+" "+(grid.getElements()[j].getId()[2]-1)+" "+(grid.getElements()[j].getId()[3]-1));
                    writer.newLine();
                }
                writer.newLine();
                writer.write("CELL_TYPES " + grid.getElements().length);
                writer.newLine();
                for (int j = 0; j < grid.getElements().length; j++) {
                    writer.write("9");
                    writer.newLine();
                }
                writer.newLine();
                writer.write("POINT_DATA " + grid.getNodes().length);
                writer.newLine();
                writer.write("SCALARS Temp float 1");
                writer.newLine();
                writer.write("LOOKUP_TABLE default");
                writer.newLine();
                for (int j = 0; j < results[0].length; j++) {
                    writer.write(String.valueOf(results[i][j]));
                    writer.newLine();
                }

                writer.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
