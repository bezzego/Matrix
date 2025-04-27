package model;

import java.time.LocalDateTime;

public class SavedMatrix {
    private String name;
    private Matrix matrix;
    private String userEmail;
    private LocalDateTime savedDate;

    public SavedMatrix(String name, Matrix matrix, String userEmail) {
        this.name = name;
        this.matrix = matrix;
        this.userEmail = userEmail;
        this.savedDate = LocalDateTime.now();
    }

    // Геттеры
    public String getName() { return name; }
    public Matrix getMatrix() { return matrix; }
    public String getUserEmail() { return userEmail; }
    public LocalDateTime getSavedDate() { return savedDate; }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s",
            name, userEmail, savedDate, matrix.toString());
    }
}