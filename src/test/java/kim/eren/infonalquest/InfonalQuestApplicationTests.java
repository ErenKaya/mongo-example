package kim.eren.infonalquest;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.eren.infonalquest.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfonalQuestApplicationTests {

	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;
	private ObjectMapper objectMapper;

	@Test
	public void contextLoads() {
	}

	@Before
	public void setup() {
		objectMapper = new ObjectMapper();
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
	}
	@Test
	public void getIndex() throws Exception {
		mvc.perform(get("/user.html")
				.accept(MediaType.ALL_VALUE))
				.andExpect(status().isOk());
	}

	@Test
	/**
	 * Google Captcha sending to us a bad request.
	 * this means that method worked well;
	 */
	public void testUserAdd() throws Exception {
		mvc.perform(post("/user/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getJson()))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetAllUser() throws Exception {
		mvc.perform(get("/user/list")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testUserUpdate() throws Exception {

		String jsonObject = getJson();
		User user = objectMapper.readValue(jsonObject,User.class);
		user.setName("new Name");
		user.setSurname("new Surname");
		mvc.perform(post("/user/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
	}


	private String getJson() {
		return "{\n" +
				"\t\"id\":\"cecao1231215\",\n" +
				"\t\"name\":\"Infonal\",\n" +
				"\t\"surname\":\"Bilişim\",\n" +
				"\t\"phoneNumber\":\"0212 265 4646\"\n" +
				"}   ";
	}
}
