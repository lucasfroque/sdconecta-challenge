package com.sdconecta.saudedigital.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdconecta.saudedigital.dto.UserDTO;
import com.sdconecta.saudedigital.models.Crm;
import com.sdconecta.saudedigital.models.User;
import com.sdconecta.saudedigital.models.enums.AuthorizationStatus;
import com.sdconecta.saudedigital.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final Long ID = 1L;

    private static final String EMAIL = "joao@email.com";
    private static final String PASSWORD = "12345";
    private static final String NAME = "João";
    private static final String SURNAME = "Da Silva";
    private static final String MOBILE_PHONE = "11991234567";
    private static final AuthorizationStatus AUTHORIZATION_STATUS = AuthorizationStatus.WAITING_FOR_AUTHORIZATION;
    private static final String ROLE = "USER";
    User user;
    Optional<User> optionalUser;


    public static final String crmNumber = "1234";
    public static final String crmUf = "SP";
    public static final String crmSpecialty = "RADIOLOGIA E DIAGNÓSTICO POR IMAGEM";
    List<Crm> CRM = new ArrayList<>();


    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        start();
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void whenCreateShouldReturnCreated() throws Exception {
        when(service.create(any())).thenReturn(user);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenUpdateShouldReturnOk() throws Exception {
        when(service.create(any())).thenReturn(user);

        mockMvc.perform(put("/api/v1/users/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenFindAllShouldReturnListOfUser() throws Exception {
        when(service.findAll()).thenReturn(List.of(user, user));
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"))
                .andExpect(jsonPath("[0].id").value(ID))
                .andExpect(jsonPath("[0].email").value(EMAIL))
                .andExpect(jsonPath("[0].name").value(NAME))
                .andExpect(jsonPath("[0].surname").value(SURNAME))
                .andExpect(jsonPath("[0].mobilePhone").value(MOBILE_PHONE));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenFindByIdShouldReturnUser() throws Exception {
        when(service.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(ID))
                .andExpect(jsonPath("email").value(EMAIL))
                .andExpect(jsonPath("name").value(NAME))
                .andExpect(jsonPath("surname").value(SURNAME))
                .andExpect(jsonPath("mobilePhone").value(MOBILE_PHONE));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenDeleteShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
    }

    private void start(){
        user = new User(ID, EMAIL, PASSWORD, NAME, SURNAME, CRM, MOBILE_PHONE, ROLE, AUTHORIZATION_STATUS);
        CRM.add(new Crm(ID, crmNumber, crmUf, crmSpecialty, user));
        optionalUser = Optional.of(new User(ID, EMAIL, PASSWORD, NAME, SURNAME, CRM, MOBILE_PHONE, ROLE, AUTHORIZATION_STATUS));

    }
}
