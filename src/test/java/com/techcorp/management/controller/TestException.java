package com.techcorp.management.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techcorp.management.repository.ToolRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class TestException {

  private MockMvc mockMvc;

  @MockitoBean private ToolRepository toolRepository;

  @Autowired private WebApplicationContext webApplicationContext;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  // 1. TEST VALIDATION (400)
  @Test
  public void createTool_ShouldReturn400_WhenDataInvalid() throws Exception {
    String invalidJson =
        """
                {
                  "name": "a",
                  "monthly_cost": -10.0,
                  "website_url": "not-a-url"
                }
                """;

    mockMvc
        .perform(post("/tools").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("Validation failed")));
  }

  // 2. TEST 404 NOT FOUND
  @Test
  public void getToolById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
    when(toolRepository.findById(999L)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/tools/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("Tool not found")))
        .andExpect(jsonPath("$.message", is("Le tool avec l'id 999 n'existe pas!")));
  }

  @Test
  public void getTools_ShouldReturn500_WhenDatabaseCrashes() throws Exception {
    // On dit à Mockito d'intercepter TOUT appel à findAll avec n'importe quelle Specification
    when(toolRepository.findAll(
            ArgumentMatchers.<Specification<com.techcorp.management.entity.Tool>>any()))
        .thenThrow(new RuntimeException("Database connection failed"));
    mockMvc
        .perform(get("/tools"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error", is("Internal server error")))
        .andExpect(jsonPath("$.message", is("Database connection failed")));
  }
}
