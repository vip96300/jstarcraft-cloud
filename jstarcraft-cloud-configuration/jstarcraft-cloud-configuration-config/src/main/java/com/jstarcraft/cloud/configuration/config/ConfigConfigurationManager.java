package com.jstarcraft.cloud.configuration.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.cloud.bootstrap.BootstrapApplicationListener;
import org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.context.support.StandardServletEnvironment;

import com.jstarcraft.cloud.configuration.Configuration;
import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;

/**
 * Spring Cloud Config配置管理器
 * 
 * <pre>
 * PropertySourceLocator用于引导配置
 * ContextRefresher用于刷新配置
 * </pre>
 * 
 * @author Birdy
 *
 */
public class ConfigConfigurationManager extends ContextRefresher implements ConfigurationManager {

    private static final String REFRESH_ARGS_PROPERTY_SOURCE = "refreshArgs";

    private static final String[] DEFAULT_PROPERTY_SOURCES = new String[] { CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME, "defaultProperties" };

    private static final Set<String> STANDARD_SOURCES = new HashSet<>(Arrays.asList(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME, StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME, StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME, "configurationProperties"));

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Map<String, Object> properties;

    public ConfigConfigurationManager(ConfigurableApplicationContext context, RefreshScope scope) {
        super(context, scope);
        properties = getProperties(context.getEnvironment().getPropertySources());
    }

    @Override
    public Set<String> refresh() {
        Lock write = lock.writeLock();
        try {
            write.lock();
            Set<String> changes = refreshEnvironment();
            RefreshScope scope = getScope();
            scope.refreshAll();
            return changes;
        } finally {
            write.unlock();
        }
    }

    private PropertySource getConfigPropertySource(MutablePropertySources propertySources) {
        PropertySource propertySource = propertySources.get(PropertySourceBootstrapConfiguration.BOOTSTRAP_PROPERTY_SOURCE_NAME);
        if (propertySource == null) {
            return null;
        }
        if (propertySource instanceof CompositePropertySource) {
            for (PropertySource<?> source : ((CompositePropertySource) propertySource).getPropertySources()) {
                if (source.getName().equals("configService")) {
                    return source;
                }
            }
        }
        return null;
    }

    @Override
    public Set<String> refreshEnvironment() {
        Lock write = lock.writeLock();
        try {
            write.lock();
            ConfigurableApplicationContext context = getContext();
            Map<String, Object> before = getProperties(context.getEnvironment().getPropertySources());
            buildEnvironment();
            Map<String, Object> after = getProperties(context.getEnvironment().getPropertySources());
            PropertySource propertySource = getConfigPropertySource(context.getEnvironment().getPropertySources());
            properties = after;
            Set<String> changes = changes(before, after).keySet();
            context.publishEvent(new EnvironmentChangeEvent(context, changes));
            return changes;
        } finally {
            write.unlock();
        }
    }

    private ConfigurableApplicationContext buildEnvironment() {
        ConfigurableApplicationContext capture = null;
        ConfigurableApplicationContext context = getContext();
        try {
            StandardEnvironment environment = copyEnvironment(context.getEnvironment());
            SpringApplicationBuilder builder = new SpringApplicationBuilder(Empty.class).bannerMode(Mode.OFF).web(WebApplicationType.NONE).environment(environment);
            builder.application().setListeners(Arrays.asList(new BootstrapApplicationListener(), new ConfigFileApplicationListener()));
            capture = builder.run();
            if (environment.getPropertySources().contains(REFRESH_ARGS_PROPERTY_SOURCE)) {
                environment.getPropertySources().remove(REFRESH_ARGS_PROPERTY_SOURCE);
            }
            MutablePropertySources sources = context.getEnvironment().getPropertySources();
            String current = null;
            for (PropertySource<?> source : environment.getPropertySources()) {
                String name = source.getName();
                if (sources.contains(name)) {
                    current = name;
                }
                if (!this.STANDARD_SOURCES.contains(name)) {
                    if (sources.contains(name)) {
                        sources.replace(name, source);
                    } else {
                        if (current != null) {
                            sources.addAfter(current, source);
                        } else {
                            sources.addFirst(source);
                            current = name;
                        }
                    }
                }
            }
        } finally {
            ConfigurableApplicationContext current = capture;
            while (current != null) {
                try {
                    current.close();
                } catch (Exception exception) {
                    // Ignore;
                }
                if (current.getParent() instanceof ConfigurableApplicationContext) {
                    current = (ConfigurableApplicationContext) current.getParent();
                } else {
                    break;
                }
            }
        }
        return capture;
    }

    private StandardEnvironment copyEnvironment(ConfigurableEnvironment environment) {
        StandardEnvironment copy = new StandardEnvironment();
        MutablePropertySources sources = copy.getPropertySources();
        for (String name : DEFAULT_PROPERTY_SOURCES) {
            if (environment.getPropertySources().contains(name)) {
                if (sources.contains(name)) {
                    sources.replace(name, environment.getPropertySources().get(name));
                } else {
                    sources.addLast(environment.getPropertySources().get(name));
                }
            }
        }
        copy.setActiveProfiles(environment.getActiveProfiles());
        copy.setDefaultProfiles(environment.getDefaultProfiles());
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("spring.jmx.enabled", false);
        properties.put("spring.main.sources", "");
        sources.addFirst(new MapPropertySource(REFRESH_ARGS_PROPERTY_SOURCE, properties));
        return copy;
    }

    private Map<String, Object> changes(Map<String, Object> before, Map<String, Object> after) {
        Map<String, Object> changes = new HashMap<String, Object>();
        for (String key : before.keySet()) {
            if (!after.containsKey(key)) {
                changes.put(key, null);
            } else if (!equal(before.get(key), after.get(key))) {
                changes.put(key, after.get(key));
            }
        }
        for (String key : after.keySet()) {
            if (!before.containsKey(key)) {
                changes.put(key, after.get(key));
            }
        }
        return changes;
    }

    private boolean equal(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return left.equals(right);
    }

    private Map<String, Object> getProperties(MutablePropertySources sources) {
        Map<String, Object> properties = new HashMap<String, Object>();
        List<PropertySource<?>> elements = new ArrayList<PropertySource<?>>();
        for (PropertySource<?> element : sources) {
            elements.add(0, element);
        }
        for (PropertySource<?> element : elements) {
            if (!this.STANDARD_SOURCES.contains(element.getName())) {
                setProperties(element, properties);
            }
        }
        return properties;
    }

    private void setProperties(PropertySource<?> source, Map<String, Object> properties) {
        if (source instanceof CompositePropertySource) {
            try {
                List<PropertySource<?>> elements = new ArrayList<PropertySource<?>>();
                for (PropertySource<?> element : ((CompositePropertySource) source).getPropertySources()) {
                    elements.add(0, element);
                }
                for (PropertySource<?> element : elements) {
                    setProperties(element, properties);
                }
            } catch (Exception exception) {
                return;
            }
        } else if (source instanceof EnumerablePropertySource) {
            for (String key : ((EnumerablePropertySource<?>) source).getPropertyNames()) {
                properties.put(key, source.getProperty(key));
            }
        }
    }

    @Override
    public Configuration getConfiguration(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerMonitor(ConfigurationMonitor monitor, boolean synchronous) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(ConfigurationMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
