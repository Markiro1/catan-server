package com.ashapiro.catanserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_to_lobby")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString(exclude = "user")
public class UserToLobby {

    @Id
    private Long id;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lobby_id")
    private LobbyEntity lobby;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserToLobby.ConnectionStatus status;

    @Column(name = "is_host")
    private Boolean isHost;

    public void addUserAndLobby(UserEntity user, LobbyEntity lobby) {
        user.setUserToLobby(this);
        lobby.getUsersToLobby().add(this);
        this.user = user;
        this.lobby = lobby;
    }

    public enum ConnectionStatus {
        CONNECTED,
        CONNECTION_WAITING
    }
}
