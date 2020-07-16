package pl.mg.projects.players.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.mg.projects.players.dto.Direction;
import pl.mg.projects.players.dto.PaginationDto;
import pl.mg.projects.players.dto.PlayerDto;
import pl.mg.projects.players.dto.SortField;
import pl.mg.projects.players.entities.Player;
import pl.mg.projects.players.entities.Team;
import pl.mg.projects.players.repositories.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void addPlayer(Long id, String name, String position, Team team) {

        Player newPlayer = new Player();
        newPlayer.setId(id);
        newPlayer.setPlayerName(name);
        newPlayer.setPosition(position);
        newPlayer.setTeam(team);
        playerRepository.save(newPlayer);
    }

    @Override
    public List<String> getAllPlayers() {
        List<String> allPlayersNames = new ArrayList<String>();
        List<Player> allPlayers = playerRepository.getAllBy();
        for (Player player : allPlayers) {
            allPlayersNames.add(player.getPlayerName());
        }
        return allPlayersNames;
    }

    @Override
    public List<PlayerDto> mapPlayerEntityToDto(Page<Player> allPlayers) {
        return allPlayers
                .stream()
                .map(p -> new PlayerDto(p.getId(), p.getPlayerName(), p.getPosition()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllPlayersOfTeam(Team team) {
        List<String> allPlayersOfTeamNames = new ArrayList<String>();
        List<Player> allPlayers = playerRepository.getAllByTeam(team);
        for (Player player : allPlayers) {
            allPlayersOfTeamNames.add(player.getPlayerName());
        }
        return allPlayersOfTeamNames;
    }

    @Override
    public PaginationDto<PlayerDto> findByName(Integer perPage, Integer page, SortField sortField, Direction direction, String playerName) {
        Pageable pageRequest = createPageRequest(perPage, page, sortField, direction);
        List<PlayerDto> responseDtoList = mapPlayerEntityToDto(playerRepository.getAllByPlayerNameStartsWith(pageRequest, playerName));
        return new PaginationDto<>(responseDtoList, (long) responseDtoList.size());
    }

    @Override
    public PaginationDto<PlayerDto> findByTeam(Integer perPage, Integer page, SortField sortField, Direction direction, Team team) {
        Pageable pageRequest = createPageRequest(perPage, page, sortField, direction);
        List<PlayerDto> responseDtoList = mapPlayerEntityToDto(playerRepository.getAllByTeamOrderByTeam(team, pageRequest));
        return new PaginationDto<>(responseDtoList, (long) responseDtoList.size());
    }

    @Override
    public PaginationDto<PlayerDto> findByPosition(Integer perPage, Integer page, SortField sortField, Direction direction, String playerPosition) {
        Pageable pageRequest = createPageRequest(perPage, page, sortField, direction);
        List<PlayerDto> responseDtoList = mapPlayerEntityToDto(playerRepository.getAllByPosition(pageRequest, playerPosition));
        return new PaginationDto<>(responseDtoList, (long) responseDtoList.size());
    }

    @Override
    public Pageable createPageRequest(Integer perPage, Integer page, SortField sortField, Direction direction) {
        PageRequest pageRequest;
        Sort.Direction sortDirection;
        if (direction.equals(Direction.asc)) {
            sortDirection = Sort.Direction.ASC;
        } else {
            sortDirection = Sort.Direction.DESC;
        }
        if (sortField.equals(SortField.teamName)) {
            pageRequest = PageRequest.of(page, perPage, sortDirection, "teamName");
        } else if (sortField.equals(SortField.playerPosition)) {
            pageRequest = PageRequest.of(page, perPage, sortDirection, "playerPosition");
        } else {
            pageRequest = PageRequest.of(page, perPage, sortDirection, "name");
        }
        return pageRequest;
    }
}
