package com.detection.maize.disease.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
  info = @Info(version = "API Version 1.0",
  title = "Maize disease detection API Reference",
  description = "This api  houses all the logic that is required " +
          "when interacting the maize leaf disease detection mobile client" +
          "It contains mainly four parts. \n1. User account\n2. Diseases api\n3. Neural network model\n4. Community section",
  contact = @Contact(
    name = "Augustine Simwela",
    url = "https://github.com/ASIMWELA",
    email = "augastinesimwela@gmail.com"
  )))
@SecurityScheme(
        name = "api",
        scheme = "basic",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)
class OpenAPIConfiguration {
}