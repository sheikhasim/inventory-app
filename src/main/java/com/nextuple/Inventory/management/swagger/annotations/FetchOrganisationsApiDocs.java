package com.nextuple.Inventory.management.swagger.annotations;


import com.nextuple.Inventory.management.model.Organization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Fetch/Search users of a tenant filtered according to query parameter",
        description = "Fetch/Search users of a tenant filtered according to query parameter",
        parameters = {
                @Parameter(name = "tenantId", description = "ID of the tenant"),
                @Parameter(name = "cid", description = "ID of the client"),
                @Parameter(name = "params", description = "Query Parameters", examples = {
                        @ExampleObject(name = "username", value = "<username>", description = "A String contained in username, or the complete username"),
                        @ExampleObject(name = "firstName", value = "<firstName>", description = "A String contained in firstName, or the complete firstName"),
                        @ExampleObject(name = "lastName", value = "<lastName>", description = "A String contained in lastName, or the complete lastName"),
                        @ExampleObject(name = "email", value = "<email>", description = "A String contained in email, or the complete email"),
                        @ExampleObject(name = "first", value = "<first>", description = "Pagination offset"),
                        @ExampleObject(name = "max", value = "<max>", description = "Maximum results size (defaults to 100)"),
                        @ExampleObject(name = "status", value = "<status>", description = "Representing if user is active or inactive")


                })
        })

@ApiResponses(
        value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ok",
                        content = {
                                @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Organization.class))
                        }
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request",
                        content = {
                                @Content(mediaType = "application/json",
                                        examples = {
                                                @ExampleObject(name = "If some parameter value is invalid",
                                                        value = "{\"error\":{\"code\":404,\"status\":\"Bad Request\",\"message\":\"unable to decode request, invalid value for parameter : <parameter> \"}}"),

                                                @ExampleObject(name = "If some query parameter is invalid",
                                                        value = "{\"error\":{\"code\":404,\"status\":\"Bad Request\",\"message\":\"unable to decode request, invalid query parameter : <query_parameter>\"}}")
                                        })
                        }

                ),

                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = {
                                @Content(mediaType = "application/json",
                                        examples = {
                                                @ExampleObject(name = "No token passed",
                                                        value = "{\"message\":\"Unauthorized \"}")
                                        })
                        }
                ),

                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden",
                        content = {
                                @Content(mediaType = "application/json",
                                        examples = {
                                                @ExampleObject(name = "Invalid token passed",
                                                        value = "{\"message\":\"invalid authorization request \"}")
                                        })
                        }
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = {
                                @Content(mediaType = "application/json",
                                        examples = {
                                                @ExampleObject(
                                                        name = "Tenant is missing or invalid Tenant",
                                                        value = "{\"error\":{\"code\":404,\"status\":\"Not Found\",\"message\":\"invalid or missing tenant \\\"<tenant_id>\\\"\"}}"),

                                                @ExampleObject(
                                                        name = "Client id is missing or invalid",
                                                        value = "{\"error\":{\"code\":404,\"status\":\"Not Found\",\"message\":\"no client found with ID \\\"<client_id>\\\" \"}}")

                                        })
                        }

                )

        }
)
public @interface FetchOrganisationsApiDocs {
}
