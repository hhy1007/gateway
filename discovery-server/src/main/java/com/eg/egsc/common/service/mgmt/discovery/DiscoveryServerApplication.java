/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.eg.egsc.common.service.mgmt.discovery;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * DiscoveryServerApplication
 * @author guofeng
 * @since 2018年1月29日
 */
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryServerApplication extends SpringBootServletInitializer {

	/* (non-Javadoc)
	 * @see org.springframework.boot.web.support.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
	 */
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder builder) {
		return builder.sources(DiscoveryServerApplication.class);
	}

	/**
	 * TODO
	 * @param args void
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder(DiscoveryServerApplication.class)
				.web(true).run(args);
	}

}
