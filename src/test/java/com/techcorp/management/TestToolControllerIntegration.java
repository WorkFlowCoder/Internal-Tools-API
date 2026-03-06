package com.techcorp.management;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcorp.management.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@SpringBootTest
class TestToolControllerIntegration {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired private CategoryRepository categoryRepository;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    this.objectMapper = new ObjectMapper();
  }

  @Test
  void shouldCreateToolSuccessfully() throws Exception {
    String toolJson =
        """
        {
          "name": "Linear",
          "description": "Issue tracking and project management",
          "vendor": "Linear",
          "website_url": "https://linear.app",
          "category_id": 1,
          "monthly_cost": 8.00,
          "owner_department": "Engineering",
          "active_users_count": 0
        }
        """;

    mockMvc
        .perform(post("/tools").contentType(MediaType.APPLICATION_JSON).content(toolJson))
        .andExpect(status().isCreated());
  }

  @Test
  void shouldReturn400WhenNameTooShort() throws Exception {
    String invalidJson =
        """
            {
                "name": "A",
                "vendor": "Test",
                "monthly_cost": 10.0,
                "owner_department": "HR",
                "category_id": 1
            }
            """;

    mockMvc
        .perform(post("/tools").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400WhenNameTooLong() throws Exception {
    String name = "A".repeat(102);
    String invalidJson =
        """
            {
                "name": "%s",
                "vendor": "Test",
                "monthly_cost": 10.0,
                "owner_department": "HR",
                "category_id": 1
            }
            """
            .formatted(name);

    mockMvc
        .perform(post("/tools").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400WhenUrlInvalid() throws Exception {
    String invalidJson =
        """
            {
                "name": "Linear",
                "vendor": "Test",
                "website_url": "www.test",
                "monthly_cost": 10.0,
                "owner_department": "HR",
                "category_id": 1
            }
            """;

    mockMvc
        .perform(post("/tools").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400WhenBadMonthlyCost() throws Exception {
    String invalidJson =
        """
            {
                "name": "Linear",
                "vendor": "Test",
                "monthly_cost": -10.0,
                "owner_department": "HR",
                "category_id": 1
            }
            """;

    mockMvc
        .perform(post("/tools").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400WhenBadMonthlyCostDigit() throws Exception {
    String invalidJson =
        """
            {
                "name": "Linear",
                "vendor": "Test",
                "monthly_cost": 100.404,
                "owner_department": "HR",
                "category_id": 1
            }
            """;

    mockMvc
        .perform(post("/tools").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400WhenBadCategory() throws Exception {
    String invalidJson =
        """
            {
                "name": "Linear",
                "vendor": "Test",
                "monthly_cost": 10.0,
                "owner_department": "HR",
                "category_id": 999
            }
            """;

    mockMvc
        .perform(post("/tools").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldGetToolSuccessfully() throws Exception {
    mockMvc.perform(get("/tools")).andExpect(status().isOk());
  }

  @Test
  void shouldGetIdToolSuccessfully() throws Exception {
    mockMvc
        .perform(get("/tools/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Slack"))
        .andExpect(jsonPath("$.website_url").value("https://slack.com"))
        .andExpect(jsonPath("$.category").exists());
  }

  @Test
  void shouldGetInvalidIdTool() throws Exception {
    mockMvc.perform(get("/tools/999")).andExpect(status().isNotFound());
  }

  @Test
  void shouldUpdateToolWithPut() throws Exception {
    String toolPutJson =
        """
            {
                "monthly_cost": 7.00,
                "status": "deprecated",
                "description": "Updated description after renewal"
                }
            """;

    mockMvc
        .perform(put("/tools/1").contentType(MediaType.APPLICATION_JSON).content(toolPutJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.description").value("Updated description after renewal"));
  }
}
