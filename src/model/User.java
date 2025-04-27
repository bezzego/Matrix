package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private List<Operation> operations = new ArrayList<>();

    public User(String firstName, String lastName, String email, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return firstName + "," + lastName + "," + email + "," + phone + "," + password;
    }

    /**
     * Возвращает список операций пользователя.
     */
    public List<Operation> getOperations() {
        return operations;
    }

    /**
     * Добавляет операцию в историю пользователя.
     */
    public void addOperation(Operation operation) {
        operations.add(operation);
    }
}