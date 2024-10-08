package io.opengemini.client.reactor;

import io.opengemini.client.api.AuthConfig;
import io.opengemini.client.api.AuthType;
import io.opengemini.client.api.BatchConfig;
import io.opengemini.client.api.OpenGeminiConst;
import io.opengemini.client.api.OpenGeminiException;
import org.jetbrains.annotations.NotNull;

public class OpenGeminiReactorClientFactory {
    public static OpenGeminiReactorClient create(@NotNull Configuration configuration) throws OpenGeminiException {
        if (configuration.getAddresses() == null || configuration.getAddresses().isEmpty()) {
            throw new OpenGeminiException("at least one address is required");
        }
        AuthConfig authConfig = configuration.getAuthConfig();
        if (authConfig != null) {
            if (authConfig.getAuthType() == AuthType.TOKEN && (authConfig.getToken() == null
                    || authConfig.getToken().isEmpty())) {
                throw new OpenGeminiException("invalid auth config due to empty token");
            }
            if (authConfig.getAuthType() == AuthType.PASSWORD) {
                if (authConfig.getUsername() == null || authConfig.getUsername().isEmpty()) {
                    throw new OpenGeminiException("invalid auth config due to empty username");
                }
                if (authConfig.getPassword() == null || authConfig.getPassword().length == 0) {
                    throw new OpenGeminiException("invalid auth config due to empty password");
                }
            }
        }
        BatchConfig batchConfig = configuration.getBatchConfig();
        if (batchConfig != null) {
            if (batchConfig.getBatchInterval() <= 0) {
                throw new OpenGeminiException("batch enabled, batch interval must be great than 0");
            }
            if (batchConfig.getBatchSize() <= 0) {
                throw new OpenGeminiException("batch enabled, batch size must be great than 0");
            }
        }
        if (configuration.getTimeout() == null || configuration.getTimeout().isNegative()) {
            configuration.setTimeout(OpenGeminiConst.DEFAULT_TIMEOUT);
        }
        if (configuration.getConnectTimeout() == null || configuration.getConnectTimeout().isNegative()) {
            configuration.setConnectTimeout(OpenGeminiConst.DEFAULT_CONNECT_TIMEOUT);
        }
        return new OpenGeminiReactorClient(configuration);
    }
}
