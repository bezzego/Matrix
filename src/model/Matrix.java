package model;

public class Matrix {
    private double[][] data;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double getValue(int i, int j) {
        return data[i][j];
    }

    public void setValue(int i, int j, double value) {
        data[i][j] = value;
    }

    public double[][] getData() {
        return data;
    }

    public double get(int i, int j) {
        return data[i][j];
    }
}