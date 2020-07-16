package pl.mg.projects.players.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mg.projects.players.dto.Direction;
import pl.mg.projects.players.dto.PaginationDto;
import pl.mg.projects.players.dto.PlayerDto;
import pl.mg.projects.players.dto.SortField;
import pl.mg.projects.players.entities.Team;
import pl.mg.projects.players.services.PlayerService;
import pl.mg.projects.players.services.TeamService;

import java.util.Objects;

@RestController
@RequestMapping(path = "/player")
public class PlayerController {

    private final PlayerService playerService;

    private final TeamService teamService;

    @Autowired
    public PlayerController(PlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<PaginationDto<PlayerDto>> getAllPlayers(@RequestParam(name = "perPage") Integer perPage,
                                                                  @RequestParam(name = "page") Integer page,
                                                                  @RequestParam(name = "sortBy") SortField sortField,
                                                                  @RequestParam(name = "team", required = false) Team team,
                                                                  @RequestParam(name = "playerPosition", required = false) String playerPosition,
                                                                  @RequestParam(name = "playerName", required = false) String playerName,
                                                                  @RequestParam(name = "direction") Direction direction) {
        if (Objects.nonNull(playerName) && !playerName.trim().equals("")) {
            PaginationDto<PlayerDto> players = playerService.findByName(perPage, page, sortField, direction, playerName);
            if (players.getContent().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            else return ResponseEntity.status(HttpStatus.OK).body(players);
        } else if (Objects.nonNull(playerPosition) && !playerPosition.trim().equals("")) {
            PaginationDto<PlayerDto> players = playerService.findByPosition(perPage, page, sortField, direction, playerPosition);
            if (players.getContent().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            else return ResponseEntity.status(HttpStatus.OK).body(players);
        } else {
            PaginationDto<PlayerDto> players = playerService.findByTeam(perPage, page, sortField, direction, team);
            if (players.getContent().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            else return ResponseEntity.status(HttpStatus.OK).body(players);
        }
    }
}