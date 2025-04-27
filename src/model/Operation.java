package model;

import java.time.LocalDateTime;

public class Operation {
    private int id;
    private String operationType;
    private Matrix matrix1;
    private Matrix matrix2;
    private Matrix result;
    private LocalDateTime timestamp;
    private String userEmail;

    public Operation(String operationType, Matrix matrix1, Matrix matrix2, Matrix result, String userEmail) {
        this.operationType = operationType;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
        this.timestamp = LocalDateTime.now();
        this.userEmail = userEmail;
    }

    // Геттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOperationType() { return operationType; }
    public Matrix getMatrix1() { return matrix1; }
    public Matrix getMatrix2() { return matrix2; }
    public Matrix getResult() { return result; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getUserEmail() { return userEmail; }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s",
            id, operationType, timestamp, userEmail,
            matrix1.toString(), matrix2 != null ? matrix2.toString() : "null");
    }
}