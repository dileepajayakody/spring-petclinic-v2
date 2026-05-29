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
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class OpenApiConfiguration {

	@Bean
	OpenAPI petClinicOpenApi() {
		return new OpenAPI()
			.info(new Info().title("Spring PetClinic API")
				.description("OpenAPI documentation for the machine-facing endpoints exposed by Spring PetClinic.")
				.version("v1")
				.contact(new Contact().name("Spring PetClinic")
					.url("https://github.com/spring-projects/spring-petclinic"))
				.license(new License().name("Apache License 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
			.externalDocs(new ExternalDocumentation().description("Project repository")
				.url("https://github.com/spring-projects/spring-petclinic"))
			.addServersItem(new Server().url("/").description("Current application server"));
	}

}
