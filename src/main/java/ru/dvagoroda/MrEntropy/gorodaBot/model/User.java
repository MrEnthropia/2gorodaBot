package ru.dvagoroda.MrEntropy.gorodaBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

@Entity(name = "usersTable")
@Data
public class User {

    @Id
    private Long chatId;

    private String firstName;
    private String lastName;
    private String userName;

    private Timestamp registeredAt;
}
