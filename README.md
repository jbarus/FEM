## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Illustrations](#illustrations)

## General info
Main task of the program is to provide simple calculations related to heat transfer. 
It used, among others: Fourier-Kirchhoff equations or the principles of the finite element method.

## Technologies
Project is created with:
* Java version: 21.0
* Lombok library version: 1.18.30
* Commons Math library version: 3.6.1

## Setup
This project uses Maven, so to run it locally use following commands:
```
mvn package
mvn exec:java
```

## Illustrations
Program can print output to console for further analysis:\
<img src="https://github.com/jbarus/FEM/assets/57799873/e5287999-ef03-4a25-8cbd-6bb9ef3be1a1">

or format output as files:\
<img src="https://github.com/jbarus/FEM/assets/57799873/408031da-94ab-40ae-9ef2-5ca53a893526">

so it can be used in ParaView:\
<img src="https://github.com/jbarus/FEM/assets/57799873/8407206d-af12-4dec-a9bc-2f690ea3ffb1">