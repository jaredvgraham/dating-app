package com.evanw.datebyrate.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpatialIndexCreator implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public SpatialIndexCreator(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception{
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_user_info_location ON user_info USING GIST (location)");
    }
}
