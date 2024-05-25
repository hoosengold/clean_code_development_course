import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .param("username", "testuser")
                .param("email", "test@example.com")
                .param("password", "securePassword")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("User registered successfully"));
    }

    @Test
    public void testRegisterUser_MissingUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .param("email", "test@example.com")
                .param("password", "securePassword")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Missing parameters"));
    }

    @Test
    public void testRegisterUser_MissingPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .param("username", "testuser")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Missing parameters"));
    }

    @Test
    public void testRegisterUser_MissingEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .param("username", "testuser")
                .param("password", "securePassword")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Missing parameters"));
    }
}
