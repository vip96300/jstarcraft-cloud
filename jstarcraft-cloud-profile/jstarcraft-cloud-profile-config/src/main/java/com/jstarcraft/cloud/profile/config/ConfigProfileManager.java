package com.jstarcraft.cloud.profile.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.SpringConfigurator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * Config配置管理器
 * 
 * @author Birdy
 *
 */
public class ConfigProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigProfileManager.class);

    private RestTemplate config;

    private String address;

    private Object[] arguments;

    public ConfigProfileManager(RestTemplate config, String address, String profile, String label) {
        this.config = config;
        this.address = address + "/{name}/{profile}/{label}";
        this.arguments = new String[] { null, profile, label };
    }

    @Override
    public Configurator getConfiguration(String name) {
        arguments[0] = name;
        ResponseEntity<Environment> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.parseMediaType(V2_JSON)));
            final HttpEntity<Void> entity = new HttpEntity<>((Void) null, headers);
            response = config.exchange(address, HttpMethod.GET, entity, Environment.class, arguments);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw exception;
            }
        } catch (ResourceAccessException exception) {
            throw exception;
        }
        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        Environment environment = response.getBody();
        CompositePropertySource properties = new CompositePropertySource(name);
        for (PropertySource property : environment.getPropertySources()) {
            properties.addPropertySource(new MapPropertySource(property.getName(), (Map<String, Object>) property.getSource()));
        }
        SpringConfigurator configurator = new SpringConfigurator(properties);
        return configurator;
    }

    @Override
    public void registerMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
