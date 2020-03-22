package nl.accoda.platform.ingestionsecuritech.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.accoda.platform.ingestionsecuritech.config.properties.ApplicationProperties;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectDateModule;
import nl.accoda.platform.ingestionsecuritech.dto.ProxyScanDTO;
import nl.accoda.platform.ingestionsecuritech.model.base.SecuritechDocumentScan;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {

    private final Environment environment;
    private final ApplicationProperties applicationProperties;

    public AppConfig(Environment environment, ApplicationProperties applicationProperties) {
        this.environment = environment;
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public Queue proxyScanQueue() {
        return new Queue(applicationProperties.getQueue().getName(), false);
    }

    @Bean
    public RabbitTemplate proxyScanTemplate() {
        RabbitTemplate template = new RabbitTemplate();
        template.setMessageConverter(jsonMessageConverter());
        template.setConnectionFactory(connectionFactory());
        return template;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(
                environment.getProperty("spring.rabbitmq.host"),
                Integer.parseInt(environment.getProperty("spring.rabbitmq.port")));

        cachingConnectionFactory.setUsername(environment.getProperty("spring.rabbitmq.username"));
        cachingConnectionFactory.setPassword(environment.getProperty("spring.rabbitmq.password"));
        return cachingConnectionFactory;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JsonMapper mapper = new JsonMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new DrConnectDateModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());
        return jsonConverter;
    }


    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("proxyScanDTO", ProxyScanDTO.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }


}
