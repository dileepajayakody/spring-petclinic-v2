/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.system;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration for the Spring PetClinic sample application.
 * <p>
 * Once the application is running, the generated documentation can be accessed at:
 * <ul>
 * <li>OpenAPI JSON: <a href="http://localhost:8080/v3/api-docs">/v3/api-docs</a></li>
 * <li>Swagger UI:
 * <a href="http://localhost:8080/swagger-ui.html">/swagger-ui.html</a></li>
 * </ul>
 */
@Configuration(proxyBeanMethods = false)
class OpenApiConfig {

	@Bean
	OpenAPI petClinicOpenAPI() {
		return new OpenAPI()
			.info(new Info().title("Spring PetClinic API")
				.description("REST and web endpoints exposed by the Spring PetClinic sample application. "
						+ "PetClinic is primarily a server-rendered Spring MVC / Thymeleaf application; "
						+ "this documentation also lists the HTML endpoints used by the UI.")
				.version("4.0.0-SNAPSHOT")
				.contact(new Contact().name("Spring PetClinic Community")
					.url("https://github.com/spring-projects/spring-petclinic"))
				.license(new License().name("Apache License, Version 2.0")
					.url("https://www.apache.org/licenses/LICENSE-2.0")))
			.externalDocs(new ExternalDocumentation().description("Spring PetClinic on GitHub")
				.url("https://github.com/spring-projects/spring-petclinic"));
	}

}
