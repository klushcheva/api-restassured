package config;

import org.aeonbits.owner.Config;

public interface ApiConfig extends Config {
    @Key("baseUrl")
    @DefaultValue("http://api.travelpayouts.com")
    String baseUrl();

    @Key("apiToken")
    @DefaultValue("89d5f13230d62775277e66c46d660030")
    String apiToken();
}
