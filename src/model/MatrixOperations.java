// File: src/model/MatrixOperations.java
package model;

import java.util.List;

/**
 * Класс MatrixOperations содержит статические методы для выполнения основных операций над матрицами:
 * сложение, умножение, транспонирование, вычисление определителя, нахождение обратной матрицы и др.
 */

public class MatrixOperations {
    /**
     * Метод add — выполняет поэлементное сложение двух матриц одинаковых размеров.
     * Выбрасывает исключение, если размеры не совпадают.
     */
    public static Matrix add(Matrix a, Matrix b) {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) {
            throw new IllegalArgumentException("Dimensions must match for addition");
        }
        Matrix result = new Matrix(a.getRows(), a.getCols());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                result.setValue(i, j, a.getValue(i, j) + b.getValue(i, j));
            }
        }
        return result;
    }

    /**
     * Метод multiply — выполняет умножение двух матриц по классическим правилам линейной алгебры.
     * Число столбцов первой матрицы должно быть равно числу строк второй.
     */
    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.getCols() != b.getRows()) {
            throw new IllegalArgumentException("Cols of A must equal rows of B");
        }
        Matrix result = new Matrix(a.getRows(), b.getCols());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                double sum = 0;
                for (int k = 0; k < a.getCols(); k++) {
                    sum += a.getValue(i, k) * b.getValue(k, j);
                }
                result.setValue(i, j, sum);
            }
        }
        return result;
    }

    /**
     * Метод transpose — возвращает транспонированную матрицу (поворот по главной диагонали).
     */
    public static Matrix transpose(Matrix m) {
        Matrix t = new Matrix(m.getCols(), m.getRows());
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                t.setValue(j, i, m.getValue(i, j));
            }
        }
        return t;
    }

    /**
     * Метод sumAll — поочерёдно складывает все матрицы в списке.
     * Все матрицы должны быть одного размера.
     */
    public static Matrix sumAll(List<Matrix> list) {
        if (list.isEmpty()) throw new IllegalArgumentException("No matrices provided");
        Matrix sum = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            sum = add(sum, list.get(i));
        }
        return sum;
    }

    /**
     * Метод chainMultiply — выполняет последовательное умножение всех матриц в списке.
     * Требует совместимости размеров для каждой пары матриц.
     */
    public static Matrix chainMultiply(List<Matrix> list) {
        if (list.isEmpty()) throw new IllegalArgumentException("No matrices provided");
        Matrix prod = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            prod = multiply(prod, list.get(i));
        }
        return prod;
    }

    /**
     * Метод scalarMultiply — умножает каждое значение в матрице на заданный скаляр.
     */
    public static Matrix scalarMultiply(Matrix m, double scalar) {
        Matrix result = new Matrix(m.getRows(), m.getCols());
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                result.setValue(i, j, m.getValue(i, j) * scalar);
            }
        }
        return result;
    }

    /**
     * Метод determinant — вычисляет определитель квадратной матрицы рекурсивным методом.
     */
    public static double determinant(Matrix m) {
        int n = m.getRows();
        if (n != m.getCols()) throw new IllegalArgumentException("Matrix must be square");
        return detRecursive(m);
    }

    /**
     * Вспомогательный метод detRecursive — рекурсивно вычисляет определитель.
     * Используется для матриц размером 2x2 и больше.
     */
    private static double detRecursive(Matrix m) {
        int n = m.getRows();
        if (n == 1) return m.getValue(0, 0);
        if (n == 2) {
            return m.getValue(0,0) * m.getValue(1,1) - m.getValue(0,1) * m.getValue(1,0);
        }
        double det = 0;
        for (int col = 0; col < n; col++) {
            det += Math.pow(-1, col) * m.getValue(0, col) * detRecursive(minor(m, 0, col));
        }
        return det;
    }

    /**
     * Метод minor — возвращает минор матрицы, исключая заданную строку и столбец.
     * Используется при вычислении определителя и обратной матрицы.
     */
    private static Matrix minor(Matrix m, int row, int col) {
        int n = m.getRows();
        Matrix minor = new Matrix(n - 1, n - 1);
        for (int i = 0, mi = 0; i < n; i++) {
            if (i == row) continue;
            for (int j = 0, mj = 0; j < n; j++) {
                if (j == col) continue;
                minor.setValue(mi, mj, m.getValue(i, j));
                mj++;
            }
            mi++;
        }
        return minor;
    }

    /**
     * Метод inverse — находит обратную матрицу по методу алгебраических дополнений.
     * Выбрасывает исключение, если матрица не квадратная или сингулярная.
     */
    public static Matrix inverse(Matrix m) {
        int n = m.getRows();
        if (n != m.getCols()) throw new IllegalArgumentException("Matrix must be square");
        double det = determinant(m);
        if (det == 0) throw new IllegalArgumentException("Matrix is singular");
        Matrix cof = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cof.setValue(i, j, Math.pow(-1, i + j) * detRecursive(minor(m, i, j)));
            }
        }
        Matrix adj = transpose(cof);
        return scalarMultiply(adj, 1.0 / det);
    }
}