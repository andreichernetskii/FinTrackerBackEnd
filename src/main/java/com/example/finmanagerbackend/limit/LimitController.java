package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API Controller: This class serves as the controller for handling limit-related operations
 * via API endpoints.
 */
@RestController
@RequestMapping( "/api/v1/limits" )
public class LimitController {

    private final LimitService limitService;

    public LimitController( LimitService limitService ) {
        this.limitService = limitService;
    }

    /**
     * Adds a new limit based on the provided data.
     * Corresponds to the POST /api/v1/limits/ endpoint.
     *
     * @param limitDTO The DTO containing the details of the limit to be created. Must be valid.
     * @return A ResponseEntity containing an ApiResponse with the created LimitDTO and HTTP status CREATED.
     */
    @PostMapping( "/" )
    public ResponseEntity<ApiResponse<LimitDTO>> addNewLimit( @Valid @RequestBody LimitDTO limitDTO ) {

        LimitDTO createdLimit = limitService.addLimit(limitDTO); // Assuming the service returns LimitDTO

        ApiResponse<LimitDTO> responseDto = ApiResponse.<LimitDTO>builder()
                .status("Success")
                .results(createdLimit)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Updates an existing limit identified by its ID.
     * Corresponds to the PUT /api/v1/limits/{limitId} endpoint.
     *
     * @param limitId The unique identifier of the limit to update.
     * @param limitDTO The DTO containing the updated details for the limit. Must be valid.
     * @return A ResponseEntity containing an ApiResponse with the updated LimitDTO and HTTP status OK.
     */
    @PutMapping( "/{limitId}" )
    public ResponseEntity<ApiResponse<LimitDTO>> updateLimit(
            @PathVariable( "limitId" ) Long limitId,
            @RequestBody LimitDTO limitDTO ) {

        LimitDTO updatedLimit = limitService.updateLimit(limitId, limitDTO);

        ApiResponse<LimitDTO> responseDto = ApiResponse.<LimitDTO>builder()
                .status("Success")
                .results(updatedLimit)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Deletes a limit identified by its ID.
     * Corresponds to the DELETE /api/v1/limits/{limitId} endpoint.
     *
     * @param limitId The unique identifier of the limit to be deleted.
     * @return A ResponseEntity containing an ApiResponse indicating success (no data), and HTTP status OK.
     */
    @DeleteMapping( "/{limitId}" )
    public ResponseEntity<ApiResponse<Void>> deleteLimit( @PathVariable( "limitId" ) Long limitId ) {

        limitService.deleteLimit(limitId);

        ApiResponse<Void> responseDto = ApiResponse.<Void>builder()
                .status("Success")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Retrieves a list of all limits.
     * Corresponds to the GET /api/v1/limits/ endpoint.
     *
     * @return A ResponseEntity containing an ApiResponse with a list of LimitDTOs and HTTP status OK.
     */
    @GetMapping( "/" )
    public ResponseEntity<ApiResponse<List<LimitDTO>>> getLimits() {

        List<LimitDTO> limits = limitService.getLimits(); // Assuming the service returns List<LimitDTO>

        ApiResponse<List<LimitDTO>> responseDto = ApiResponse.<List<LimitDTO>>builder()
                .status("Success")
                .results(limits)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Retrieves a list of available limit types (e.g., "MONTHLY", "YEARLY").
     * Corresponds to the GET /api/v1/limits/types endpoint.
     *
     * @return A ResponseEntity containing an ApiResponse with a list of limit type names (String) and HTTP status OK.
     */
    @GetMapping( "/types" )
    public ResponseEntity<ApiResponse<List<String>>> getLimitTypes() {

        List<String> limitTypes = limitService.getLimitTypes(); // Service returns List<String>

        ApiResponse<List<String>> responseDto = ApiResponse.<List<String>>builder()
                .status("Success")
                .results(limitTypes)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
