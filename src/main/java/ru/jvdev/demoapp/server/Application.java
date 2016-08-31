package ru.jvdev.demoapp.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.codec.Hex;

import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.entity.User;
import ru.jvdev.demoapp.server.repository.TaskRepository;
import ru.jvdev.demoapp.server.repository.UserRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    private String realm = "Digest Realm";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createUsersAndTasks();
    }

    private void createUsersAndTasks() {
        userRepository.save(new HashSet<User>() {{
            add(new User("Ilshat", "Latypov", "ilatypov", "4495", Role.ADMIN));
            add(new User("Michael", "Scott", "mscott", "1080", Role.MANAGER));
        }});

        taskRepository.save(new HashSet<Task>() {{
            add(new Task("Deploy to PERF"));
            add(new Task("Upload data"));
            add(new Task("Write script"));
        }});
    }

    private String encodePassword(String username, String password) {
        return md5Hex(username + ":" + realm + ":" + password);
    }

    private String md5Hex(String data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(data.getBytes())));
    }
}