package util;

import model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_PATH = "users.txt";

    public static void saveUser(User user) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
        
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(user.toString());
            bw.newLine();
        }
    }

    public static List<User> loadUsers() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            file.createNewFile();
            return users;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                users.add(new User(
                    userData[0], 
                    userData[1], 
                    userData[2], 
                    userData[3], 
                    userData[4]
                ));
            }
        }
        return users;
    }

    public static void updateUserPassword(User user) throws IOException {
        List<User> users = loadUsers();
        boolean found = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(user.getEmail())) {
                users.set(i, user);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IOException("Пользователь не найден");
        }

        try (FileWriter fw = new FileWriter(FILE_PATH);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (User u : users) {
                bw.write(u.toString());
                bw.newLine();
            }
        }
    }
}