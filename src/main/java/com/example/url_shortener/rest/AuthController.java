package com.example.url_shortener.rest;

import com.example.url_shortener.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Authentication API",
        description = """
                Provides operations for authentication
                - JWT token generation
                """
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Operation(
            summary = "Generate JWT Token",
            description = """
                    Generates JWT token for authenticated user,
                    Required Basic Authentication
                    """,
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token Generated Successfully",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject(
                                    value = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoia2lib3JjaHhhbGkiLCJleHAiOjE3NDQ2MzUzMjIsImlhdCI6MTc0NDYzMTcyMiwic2NvcGUiOiJST0xFX1BSRU1JVU0ifQ.kIbU5eTZld6FaBpWDB2LjYEFD7B0IoCD7Rc3IxDQsqQOWi39MQeCABwsYnxT5TUxtlGwrZccCaL2ZhHwMhHVWfIUjAz8pD3MxknQ_3OLIT8G2CNXLsejcSFhJzVe0URY0uI1s1jEyASG6VlkGCJQoGj__jYxV27V_BdJTjpkklNJ9luaFaQhUUU0Gg4-Hko8r3_zLTf5JaFUR4i1K1ARpbrtXMHKLYznbwHV9xdEbk3ueWqSaOE9-W2f3_0lfUQuDffdU_wl_rPQIeuAp9f_Fgb2_4Ub5vy00DFgNnRxnIZIsuzCofW6tgrP_-8S4ZArc0mpsImBSeueCVhcuyEyvw"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid User Credentials",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "path": "/api/token",
                                                "error": "Unauthorized",
                                                "message": "User Details not found for the user: string",
                                                "timestamp": "timestamp",
                                                "status": 401
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/token")
    public String token(Authentication authentication) {
        LOG.debug("Token requested for user: {}", authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.debug("Generated token: {}", token);
        return token;
    }
}
