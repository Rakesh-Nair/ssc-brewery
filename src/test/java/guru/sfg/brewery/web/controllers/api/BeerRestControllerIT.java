package guru.sfg.brewery.web.controllers.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import guru.sfg.brewery.web.controllers.BaseIT;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

	@Test
	void deleteBeer() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311").header("Api-Key", "admin")
				.header("Api-Secret", "admin")).andExpect(status().isOk());
	}
	
	@Test
	void deleteBeerBadCredentials() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311").header("Api-Key", "admin")
				.header("Api-Secret", "admin1")).andExpect(status().isUnauthorized());
	}

	@Test
	void deleteBeerHttpBasic() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311").with(httpBasic("admin", "admin")))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void deleteBeerNoAuth() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void findBeers() throws Exception {
		mockMvc.perform(get("/api/v1/beer/")).andExpect(status().isOk());
	}

	@Test
	void findBeerById() throws Exception {
		mockMvc.perform(get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")).andExpect(status().isOk());
	}

	@Test
	void findBeerByUpc() throws Exception {
		mockMvc.perform(get("/api/v1/beerUpc/0631234200036")).andExpect(status().isOk());
	}
}
