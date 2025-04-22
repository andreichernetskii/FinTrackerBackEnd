    package com.example.finmanagerbackend.financial_transaction;
    
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
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.List;

    /**
     * API Controller: This class serves as the controller for handling financial transaction operations
     * (like income and expenses) via API endpoints. It also interacts with the AlertService
     * to check for triggered financial alerts after data modifications.
     */
    @RestController
    @RequestMapping( path = "api/v1/transactions" )
    public class FinancialTransactionController {

        private final FinancialTransactionService financialTransactionService;
    
        public FinancialTransactionController( FinancialTransactionService financialTransactionService ) {
            this.financialTransactionService = financialTransactionService;
        }

        /**
         * Adds a new financial transaction based on the provided data.
         * Checks for and returns any applicable alerts after the addition.
         * Corresponds to the POST /api/v1/transactions/ endpoint.
         *
         * @param financialTransactionDTO The DTO containing the details of the transaction to be created. Must be valid.
         * @return A ResponseEntity containing an ApiResponse with the created FinancialTransactionDTO,
         * a list of triggered AlertResponses, and HTTP status CREATED.
         */
        @PostMapping( "/" )
        public ResponseEntity<ApiResponse<FinancialTransactionDTO>> addNewFinancialTransaction(
                @Valid @RequestBody FinancialTransactionDTO financialTransactionDTO ) {

            FinancialTransactionDTO createdTransaction = financialTransactionService.addFinancialTransaction(financialTransactionDTO);

            ApiResponse<FinancialTransactionDTO> responseDto = ApiResponse.<FinancialTransactionDTO>builder()
                    .status("Success")
                    .results(createdTransaction)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        }

        /**
         * Updates an existing financial transaction identified by its ID.
         * Checks for and returns any applicable alerts after the update.
         * Corresponds to the PUT /api/v1/transactions/{transactionId} endpoint.
         *
         * @param transactionId The unique identifier of the transaction to update.
         * @param financialTransactionDto The DTO containing the updated details for the transaction. Must be valid.
         * @return A ResponseEntity containing an ApiResponse with the updated FinancialTransactionDTO,
         * a list of triggered AlertResponses, and HTTP status OK.
         */
        @PutMapping( "/{transactionId}" )
        public ResponseEntity<ApiResponse<FinancialTransactionDTO>> updateFinancialTransaction(
                @PathVariable Long transactionId,
                @Valid @RequestBody FinancialTransactionDTO financialTransactionDto ) {

            FinancialTransactionDTO result = financialTransactionService.updateFinancialTransaction( transactionId, financialTransactionDto );

            ApiResponse<FinancialTransactionDTO> responseDto = ApiResponse.<FinancialTransactionDTO>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        /**
         * Deletes a financial transaction identified by its ID.
         * Checks for and returns any applicable alerts after the deletion.
         * Corresponds to the DELETE /api/v1/transactions/{operationId} endpoint.
         *
         * @param operationId The unique identifier of the transaction to be deleted.
         * @return A ResponseEntity containing an ApiResponse indicating success (no data),
         * a list of triggered AlertResponses, and HTTP status OK.
         */
        @DeleteMapping( "/{operationId}" )
        public ResponseEntity<ApiResponse<Void>> deleteFinancialTransaction( @PathVariable( "operationId" ) Long operationId ) {

            financialTransactionService.deleteFinancialTransaction( operationId );

            ApiResponse<Void> responseDto = ApiResponse.<Void>builder()
                    .status("Success")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        /**
         * Retrieves a list of financial transactions based on optional filter criteria.
         * Corresponds to the GET /api/v1/transactions/ endpoint.
         * Allows filtering by year, month, transaction type, and category.
         * If no criteria are provided, it potentially returns all transactions (depends on service logic).
         *
         * @param year Optional filter criterion for the transaction year.
         * @param month Optional filter criterion for the transaction month (1-12).
         * @param financialTransactionType Optional filter criterion for the transaction type (e.g., INCOME, EXPENSE).
         * @param category Optional filter criterion for the transaction category.
         * @return A ResponseEntity containing an ApiResponse with a list of FinancialTransactionDTOs matching the criteria, and HTTP status OK.
         */
        @GetMapping( "/" )
        public ResponseEntity<ApiResponse<List<FinancialTransactionDTO>>> getOperationsOfPeriod(
                @RequestParam( name = "year", required = false ) Integer year,
                @RequestParam( name = "month", required = false ) Integer month,
                @RequestParam( name = "financialTransactionType", required = false ) FinancialTransactionType financialTransactionType,
                @RequestParam( name = "category", required = false ) String category ) {
    
            List<FinancialTransactionDTO> result = financialTransactionService.getOperationsByCriteria(
                year,
                month,
                financialTransactionType,
                category );

            ApiResponse<List<FinancialTransactionDTO>> responseDto = ApiResponse.<List<FinancialTransactionDTO>>builder()
                    .status("Success")
                    .results(result)
                    .build();
    
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        /**
         * Calculates and returns the total balance for transactions matching optional filter criteria.
         * Corresponds to the GET /api/v1/transactions/annual endpoint.
         * Allows filtering by year, month, transaction type, and category to define the scope of the balance calculation.
         *
         * @param year Optional filter criterion for the transaction year.
         * @param month Optional filter criterion for the transaction month (1-12).
         * @param financialTransactionType Optional filter criterion for the transaction type (e.g., INCOME, EXPENSE).
         * @param category Optional filter criterion for the transaction category.
         * @return A ResponseEntity containing an ApiResponse with the calculated balance (Double) and HTTP status OK.
         */
        @GetMapping( "/annual" )
        public ResponseEntity<ApiResponse<Double>> getAnnualBalance(
                @RequestParam( name = "year", required = false ) Integer year,
                @RequestParam( name = "month", required = false ) Integer month,
                @RequestParam( name = "financialTransactionType", required = false ) FinancialTransactionType financialTransactionType,
                @RequestParam( name = "category", required = false ) String category ) {
    
            Double result = financialTransactionService.getAnnualBalance( year, month, financialTransactionType, category );

            ApiResponse<Double> responseDto = ApiResponse.<Double>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        /**
         * Retrieves a list of all unique transaction categories available for the account.
         * Corresponds to the GET /api/v1/transactions/categories endpoint.
         *
         * @return A ResponseEntity containing an ApiResponse with a list of category names (String) and HTTP status OK.
         */
        @GetMapping( "/categories" )
        public ResponseEntity<ApiResponse<List<String>>> getCategories() {

            List<String> result = financialTransactionService.getCategories();

            ApiResponse<List<String>> responseDto = ApiResponse.<List<String>>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        /**
         * Retrieves a list of all available financial transaction types (e.g., INCOME, EXPENSE).
         * Corresponds to the GET /api/v1/transactions/types endpoint.
         *
         * @return A ResponseEntity containing an ApiResponse with a list of transaction type names (String) and HTTP status OK.
         */
        @GetMapping("/types")
        public ResponseEntity<ApiResponse<List<String>>> getTransactionTypes() {

            List<String> result = financialTransactionService.getTransactionTypes();

            ApiResponse<List<String>> responseDto = ApiResponse.<List<String>>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        /**
         * Retrieves a list of years for which financial transaction records exist.
         * Corresponds to the GET /api/v1/transactions/years endpoint.
         *
         * @return A ResponseEntity containing an ApiResponse with a list of years (Integer) and HTTP status OK.
         */
        @GetMapping("/years")
        public ResponseEntity<ApiResponse<List<Integer>>> getYears() {

            List<Integer> result = financialTransactionService.getYears();

            ApiResponse<List<Integer>> responseDto = ApiResponse.<List<Integer>>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        /**
         * Retrieves a list of months (typically 1-12) for which financial transaction records exist.
         * Corresponds to the GET /api/v1/transactions/months endpoint.
         *
         * @return A ResponseEntity containing an ApiResponse with a list of relevant months (Integer) and HTTP status OK.
         */
        @GetMapping("/months")
        public ResponseEntity<ApiResponse<List<Integer>>> getMonths() {

            List<Integer> result = financialTransactionService.getMonths();

            ApiResponse<List<Integer>> responseDto = ApiResponse.<List<Integer>>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }
