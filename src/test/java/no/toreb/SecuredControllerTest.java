package no.toreb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(value = SecuredController.class, includeFilters = @ComponentScan.Filter(classes = EnableWebSecurity.class))
@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SecuredControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void secured1_noUser_unauthorized() throws Exception {
        mvc.perform(get("/secured/secured1")).andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    public void secured1_userWithoutRole_forbidden() throws Exception {
        mvc.perform(get("/secured/secured1")).andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "ROLE1")
    @Test
    public void secured1_userWithRole_allowed() throws Exception {
        mvc.perform(get("/secured/secured1")).andExpect(status().isOk());
    }

    @WithMockUser(roles = "SECRET")
    @Test
    public void secured2_correctRoleButNotCorrectArgument_forbidden() throws Exception {
        mvc.perform(get("/secured/secured2/hello"))
           .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "SECRET")
    @Test
    public void secured2_correctRoleAndCorrectArgument_allowed() throws Exception {
        mvc.perform(get("/secured/secured2/secret"))
           .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ROLE")
    @Test
    public void createEvent_noRole_forbidden() throws Exception {
        final String eventJson = "{ \"type\": \"test\", \"description\": \"Test event!\" }";
        mvc.perform(post("/secured").contentType(MediaType.APPLICATION_JSON_UTF8).content(eventJson))
           .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "WRITE")
    @Test
    public void createEvent_writeRoleNoExtra_created() throws Exception {
        final String eventJson = "{ \"type\": \"test\", \"description\": \"Test event!\" }";
        mvc.perform(post("/secured").contentType(MediaType.APPLICATION_JSON_UTF8).content(eventJson))
           .andExpect(status().isCreated());
    }

    @WithMockUser(roles = "WRITE")
    @Test
    public void createEvent_writeRoleWithExtra_forbidden() throws Exception {
        final String eventJson = "{ \"type\": \"test\", \"description\": \"Test event!\", \"extra\": \"a string\" }";
        mvc.perform(post("/secured").contentType(MediaType.APPLICATION_JSON_UTF8).content(eventJson))
           .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {"WRITE", "WRITE_EXTRA"})
    @Test
    public void createEvent_writeExtraRoleWithExtra_created() throws Exception {
        final String eventJson = "{ \"type\": \"test\", \"description\": \"Test event!\", \"extra\": \"a string\" }";
        mvc.perform(post("/secured").contentType(MediaType.APPLICATION_JSON_UTF8).content(eventJson))
           .andExpect(status().isCreated());
    }
}