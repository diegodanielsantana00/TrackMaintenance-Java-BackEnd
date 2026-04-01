package com.danieldiego.trackMaintenance.adapter.controller;

import com.danieldiego.trackMaintenance.adapter.dto.ApiResponse;
import com.danieldiego.trackMaintenance.adapter.dto.user.login.LoginRequest;
import com.danieldiego.trackMaintenance.adapter.dto.user.register.RegisterRequest;
import com.danieldiego.trackMaintenance.application.dto.user.login.LoginUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserOutput;
import com.danieldiego.trackMaintenance.application.usecase.user.login.LoginUserUseCase;
import com.danieldiego.trackMaintenance.application.usecase.user.register.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<RegisterUserOutput> register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
                request.name(),
                request.email(),
                request.password()
        );

        RegisterUserOutput output = registerUserUseCase.execute(command);

        return ApiResponse.success(201, "User registered successfully", output);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates with email and password, returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<RegisterUserOutput> login(@Valid @RequestBody LoginRequest request) {
        LoginUserCommand command = new LoginUserCommand(request.email(), request.password());
        RegisterUserOutput output = loginUserUseCase.execute(command);
        return ApiResponse.success(200, "Login successful", output);
    }
}
