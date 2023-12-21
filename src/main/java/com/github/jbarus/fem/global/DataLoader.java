package com.github.jbarus.fem.global;

import com.github.jbarus.fem.structures.Element;
import com.github.jbarus.fem.structures.Grid;
import com.github.jbarus.fem.structures.Node;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class DataLoader {
    private File file;
    public DataLoader(String path){
        file = new File(path);
    }

    //Code that loads global variables such as conductivity from file
    public void loadGlobalData(GlobalData globalData){
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }
        while (!scanner.hasNext("\\*Node")){
            String key;
            double value;
            key = scanner.next();
            if(scanner.hasNextDouble()){
                value = scanner.nextDouble();
            }else{
                scanner.next();
                value = scanner.nextDouble();
            }
            try {
                Method method = globalData.getClass().getMethod("set"+key, globalData.getClass().getMethod("get"+key).getReturnType());
                if(method.getParameterTypes()[0].getName().equals(int.class.getName()))
                    method.invoke(globalData,(int)value);
                else
                    method.invoke(globalData,value);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    //Code that loads mesh from file - elements and nodes
    public void loadGrid(Grid grid){
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }

        //Part responsible for loading nodes
        while (!scanner.hasNext(Pattern.quote("*Node"))){
            scanner.next();
        }
        scanner.next();
        for (int i = 0; i < grid.getNodes().length; i++) {
            scanner.next();
            Node node = new Node();
            node.setX(Double.parseDouble(scanner.next().replace(",","")));
            node.setY(Double.parseDouble(scanner.next().replace(",","")));
            grid.getNodes()[i] = node;
        }

        //Part responsible for loading elements
        while (!scanner.hasNext(Pattern.quote("*Element,"))){
            scanner.next();
        }
        scanner.next();
        scanner.next();
        for (int i = 0; i < grid.getElements().length; i++) {
            scanner.next();
            Element element = new Element();
            element.setId(new int[4]);
            element.getId()[0] = Integer.parseInt(scanner.next().replace(",",""));
            element.getId()[1] = Integer.parseInt(scanner.next().replace(",",""));
            element.getId()[2] = Integer.parseInt(scanner.next().replace(",",""));
            element.getId()[3] = Integer.parseInt(scanner.next().replace(",",""));
            grid.getElements()[i] = element;
        }

        //Part responsible for loading elements
        while (!scanner.hasNext(Pattern.quote("*BC"))){
            scanner.next();
        }
        scanner.next();
        while (scanner.hasNext()){
            grid.getNodes()[Integer.parseInt(scanner.next().replace(",",""))-1].setBC(1);
        }
    }

    public void testFile(){
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }
        while(scanner.hasNext()){
            System.out.println(scanner.next());
        }
    }
}
