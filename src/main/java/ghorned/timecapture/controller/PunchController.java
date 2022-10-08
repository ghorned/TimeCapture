package ghorned.timecapture.controller;

import ghorned.timecapture.entity.Punch;
import ghorned.timecapture.service.PunchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/punch")
public class PunchController {

    @Autowired
    private PunchService punchService;

    @Operation(summary = "Gets all punches. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Punch.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<Punch>> getAll() {
        List<Punch> punches = punchService.getAll();
        return new ResponseEntity<>(punches, HttpStatus.OK);
    }

    @Operation(summary = "Gets punch by id. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punch found",
                    content = {@Content(schema = @Schema(implementation = Punch.class))}),
            @ApiResponse(responseCode = "400", description = "Punch not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<Punch> getById(@PathVariable("id") int id) {
        Punch punch = punchService.getById(id);
        return new ResponseEntity<>(punch, HttpStatus.OK);
    }

    @Operation(summary = "Adds new punch. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punch added",
                    content = {@Content(schema = @Schema(implementation = Punch.class))}),
            @ApiResponse(responseCode = "400", description = "Punch could not be added",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<Punch> add(@Valid @RequestBody Punch addedPunch) {
        Punch punch = punchService.add(addedPunch);
        return new ResponseEntity<>(punch, HttpStatus.OK);
    }

    @Operation(summary = "Updates existing punch by id. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punch updated",
                    content = {@Content(schema = @Schema(implementation = Punch.class))}),
            @ApiResponse(responseCode = "400", description = "Punch could not be updated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Punch> update(@PathVariable("id") int id, @Valid @RequestBody Punch updatedPunch) {
        Punch punch = punchService.update(id, updatedPunch);
        return new ResponseEntity<>(punch, HttpStatus.OK);
    }

    @Operation(summary = "Deletes existing punch by id. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punch deleted",
                    content = {@Content(schema = @Schema(implementation = Punch.class))}),
            @ApiResponse(responseCode = "400", description = "Punch could not be deleted",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Punch> delete(@PathVariable("id") int id) {
        Punch punch = punchService.delete(id);
        return new ResponseEntity<>(punch, HttpStatus.OK);
    }
}
