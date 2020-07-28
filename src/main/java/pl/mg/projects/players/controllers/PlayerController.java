package pl.mg.projects.players.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mg.projects.players.dto.Direction;
import pl.mg.projects.players.dto.PaginationDto;
import pl.mg.projects.players.dto.PlayerDto;
import pl.mg.projects.players.dto.SortField;
import pl.mg.projects.players.services.playerServices.playerGET.PlayerService;

import java.util.Objects;

@RestController
@RequestMapping(path = "/player")
public class PlayerController {

    private final PlayerService playerService;


    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<PaginationDto<PlayerDto>> getAllPlayers(@RequestParam(name = "perPage") Integer perPage,
                                                                  @RequestParam(name = "page") Integer page,
                                                                  @RequestParam(name = "sortBy") SortField sortField,
                                                                  @RequestParam(name = "teamName", required = false) String teamName,
                                                                  @RequestParam(name = "position", required = false) String position,
                                                                  @RequestParam(name = "playerName", required = false) String playerName,
                                                                  @RequestParam(name = "direction") Direction direction) {
        if (Objects.nonNull(playerName) && !playerName.trim().equals("")) {
            PaginationDto<PlayerDto> players = playerService.findByPlayerName(perPage, page, sortField, direction, playerName);
            if (players.getContent().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            else return ResponseEntity.status(HttpStatus.OK).body(players);
        } else if (Objects.nonNull(position) && !position.trim().equals("")) {
            PaginationDto<PlayerDto> players = playerService.findByPosition(perPage, page, sortField, direction, position);
            if (players.getContent().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            else return ResponseEntity.status(HttpStatus.OK).body(players);
        } else {
            PaginationDto<PlayerDto> players = playerService.findByTeamName(perPage, page, sortField, direction, teamName);
            if (players.getContent().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            else return ResponseEntity.status(HttpStatus.OK).body(players);
        }
    }
}


