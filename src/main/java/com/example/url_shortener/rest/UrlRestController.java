package com.example.url_shortener.rest;

import com.example.url_shortener.dto.CustomUrlRequest;
import com.example.url_shortener.dto.ShortUrlResponse;
import com.example.url_shortener.dto.UrlMapper;
import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.entity.Url;
import com.example.url_shortener.exceptionhandling.ShortUrlNotFoundException;
import com.example.url_shortener.repository.UrlRepository;
import com.example.url_shortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@Tag(
        name = "URL Shortener API",
        description = """
                Provides authenticated operations for URL management:
                - Creation of random short URLs
                - Creation of custom short URLs
                - Fetching user's URLs
                - Redirecting via short URLs
                               \s
                All operations, except redirection, require authentication.
               \s"""
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UrlRestController {

    private final UrlService urlService;
    private final UrlRepository urlRepository;

    @Autowired
    public UrlRestController(UrlService urlService, UrlRepository urlRepository) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
    }

    @Operation(
            summary = "Fetch URLs",
            description = """
                    Returns list of URLs for authenticated User
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Request successful"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid User Credentials",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "path": "/api/urls",
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
    @GetMapping("/urls")
    public List<UrlResponse> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return urlRepository.findByUserUsername(username).stream().map(UrlMapper::toUrlResponse).collect(Collectors.toList());

    }

    @Operation(
            summary = "Generate a random short URL",
            description = """
                     Generates a short, randomized and unique alias for a given URL
                                        \s
                     **Validation Rules**
                     - URL must start with `http://` or `https://`
                                        \s
                     **Retention Policy**
                     - Short URLs older than 30 days are automatically deleted
                    \s"""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Short url created successfully"

            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Such custom URL already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "type": "about:blank",
                                                 "title": "Invalid URL Format",
                                                 "status": 400,
                                                 "detail": "Invalid URL",
                                                 "instance": "/api/shorten",
                                                 "timestamp": "timestamp"
                                             }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/shorten")
    public String shorten(@RequestBody String url) throws MalformedURLException {
        return urlService.generateShortUrl(url);
    }

    @Operation(
            summary = "Redirect to Original URL",
            description = """
                    Redirects user to the original URL corresponding to the provided custom short URL.
                    Increments the access count of the short URL upon each user redirection.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "302",
                    description = "Found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ShortUrl Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "about:blank",
                                                "title": "ShortUrl Not Found",
                                                "status": 404,
                                                "detail": "ShortUrl 'string' does not exist",
                                                "instance": "/api/string",
                                                "timestamp": "timestamp"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl) {
        Url url = urlService.incrementAccessCount(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getOriginalUrl())).build();
    }

    @Operation(
            summary = "Generate a custom short URL",
            description = """
                     Generates custom unique alias for a given URL
                                        \s
                     **Validation Rules**
                     - URL must start with `http://` or `https://`
                                        \s
                     **Retention Policy**
                     - Short URLs older than 30 days are automatically deleted
                    \s"""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Custom Short url created successfully"

            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid URL Provided",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "detail": "Url must be valid",
                                                "title": "Validation Failed",
                                                "status": 400,
                                                "timestamp": "timestamp"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Such custom URL already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "about:blank",
                                                "title": "Duplicate ShortUrl",
                                                "status": 409,
                                                "detail": "Custom short url 'Custom URL 'string' already exists' already exists",
                                                "instance": "/api/shorten/custom",
                                                "timestamp": "timestamp"
                                            }
                                            """
                            )
                    )
            )

    })
    @PostMapping("/shorten/custom")
    public ResponseEntity<ShortUrlResponse> shortenCustom(@RequestBody @Valid CustomUrlRequest body) throws MalformedURLException {
        String originalUrl = body.getOriginalUrl();
        String customShortUrl = body.getCustomShortUrl();
        String shortUrl = urlService.generateCustomShortUrl(originalUrl, customShortUrl);
        return ResponseEntity.ok(new ShortUrlResponse(shortUrl));
    }


}
