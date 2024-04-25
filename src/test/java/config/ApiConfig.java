package config;

import org.aeonbits.owner.Config;

public interface ApiConfig extends Config {
    @Key("baseUrl")
    @DefaultValue("http://api.travelpayouts.com")
    String baseUrl();
}
