package vn.host.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;

@Configuration
public class GraphQLExceptionConfig {
    @Bean
    public DataFetcherExceptionResolverAdapter dataFetcherExceptionResolver() {
        return new DataFetcherExceptionResolverAdapter() {
            @Override
            protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
                return GraphqlErrorBuilder.newError(env)
                        .message("ERR: " + ex.getClass().getSimpleName() + " - " + ex.getMessage())
                        .build();
            }
        };
    }
}