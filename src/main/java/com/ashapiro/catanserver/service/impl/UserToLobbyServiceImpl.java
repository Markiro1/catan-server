package com.ashapiro.catanserver.service.impl;

import com.ashapiro.catanserver.entity.LobbyEntity;
import com.ashapiro.catanserver.entity.UserEntity;
import com.ashapiro.catanserver.entity.UserToLobby;
import com.ashapiro.catanserver.repository.LobbyRepository;
import com.ashapiro.catanserver.repository.UserToLobbyRepository;
import com.ashapiro.catanserver.service.UserToLobbyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserToLobbyServiceImpl implements UserToLobbyService {

    private final UserToLobbyRepository userToLobbyRepository;

    private final LobbyRepository lobbyRepository;

    @Transactional
    @Override
    public UserToLobby createSession(UserEntity user, LobbyEntity lobby) {
        UserToLobby userToLobby = new UserToLobby();
        userToLobby.addUserAndLobby(user, lobby);
        userToLobby.setStatus(UserToLobby.ConnectionStatus.CONNECTION_WAITING);
        user.getUserToLobby().setIsHost(lobby.getUsersToLobby().size() == 1);
        return userToLobbyRepository.save(userToLobby);
    }

    @Transactional
    @Override
    public void deleteByUserId(Long userId) {
        userToLobbyRepository.deleteByUserId(userId);
    }

    @Transactional
    @Override
    public void removeUserFromLobby(UserEntity user) {
        UserToLobby userToLobby = user.getUserToLobby();
        if (userToLobby != null) {
            user
                    .getUserToLobby()
                    .getLobby()
                    .removeByUser(user);

            userToLobbyRepository.delete(userToLobby);

            LobbyEntity lobby = userToLobby.getLobby();
            if (lobby.getUsersToLobby().isEmpty()) {
                lobbyRepository.delete(lobby);
            }
        }
    }
}
