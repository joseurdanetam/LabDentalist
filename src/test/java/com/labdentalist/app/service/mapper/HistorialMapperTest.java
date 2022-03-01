package com.labdentalist.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistorialMapperTest {

    private HistorialMapper historialMapper;

    @BeforeEach
    public void setUp() {
        historialMapper = new HistorialMapperImpl();
    }
}
