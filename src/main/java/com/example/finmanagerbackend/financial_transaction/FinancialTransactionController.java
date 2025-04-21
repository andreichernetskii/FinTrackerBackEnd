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
     * Api Controller: This class serves as a controller for handling income and expense operations through API endpoints.
     */
    @RestController
    @RequestMapping( path = "api/v1/transactions" )
    public class FinancialTransactionController {

        private final FinancialTransactionService financialTransactionService;
    
        // Constructor: Initializes the controller with an instance of IncomeExpenseService.
        public FinancialTransactionController( FinancialTransactionService financialTransactionService ) {
            this.financialTransactionService = financialTransactionService;
        }
    
        // Endpoint to add a new income or expense operation.
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

        // Endpoint to update an existing income or expense operation.
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
    
        // Endpoint to delete an income or expense operation based on the operationId.
        @DeleteMapping( "/{operationId}" )
        public ResponseEntity<ApiResponse<Void>> deleteFinancialTransaction( @PathVariable( "operationId" ) Long operationId ) {

            financialTransactionService.deleteFinancialTransaction( operationId );

            ApiResponse<Void> responseDto = ApiResponse.<Void>builder()
                    .status("Success")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    
        // Endpoint to retrieve a list of income and expense operations based on specified criteria.
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
    
        // Endpoint to retrieve the annual balance based on specified criteria.
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
    
        // Endpoint to retrieve a list of categories.
        @GetMapping( "/categories" )
        public ResponseEntity<ApiResponse<List<String>>> getCategories() {

            List<String> result = financialTransactionService.getCategories();

            ApiResponse<List<String>> responseDto = ApiResponse.<List<String>>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    
        @GetMapping("/types")
        public ResponseEntity<ApiResponse<List<String>>> getTransactionTypes() {

            List<String> result = financialTransactionService.getTransactionTypes();

            ApiResponse<List<String>> responseDto = ApiResponse.<List<String>>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    
        @GetMapping("/years")
        public ResponseEntity<ApiResponse<List<Integer>>> getYears() {

            List<Integer> result = financialTransactionService.getYears();

            ApiResponse<List<Integer>> responseDto = ApiResponse.<List<Integer>>builder()
                    .status("Success")
                    .results(result)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    
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
