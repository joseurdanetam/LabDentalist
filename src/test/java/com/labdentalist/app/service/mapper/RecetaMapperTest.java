package com.labdentalist.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecetaMapperTest {

    private RecetaMapper recetaMapper;

    @BeforeEach
    public void setUp() {
        recetaMapper = new RecetaMapperImpl();
    }
}
