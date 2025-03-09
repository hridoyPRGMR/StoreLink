package com.storelink.unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.storelink.dto.UserDto;
import com.storelink.model.Role;
import com.storelink.model.User;
import com.storelink.repository.UserRepository;
import com.storelink.services.RoleService;
import com.storelink.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleService roleService;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	private UserDto userDto;
	private Role mockRole;
	private User mockUser;

	@BeforeEach
	void setUp() {
		userDto = new UserDto("testUser@gmail.com", "Md Hridoy", "0176345", "testUser", "encodedPassword");

		mockRole = new Role();
		mockRole.setName("ROLE_USER");

		mockUser = userService.toEntity(userDto);
		mockUser.setRoles(Set.of(mockRole));
	}

	@Disabled
	@ParameterizedTest
	@CsvSource({ "hridoymia114", "hridoymia115", "hridoy" })
	public void testFindByUsername(String username) {
		assertNotNull(userService.findByUsername(username));
	}

	@Test
	public void testFindByUsername() {

		String username = "testUser";
		when(userRepository.findByUsername(username)).thenReturn(mockUser);

		User user = userService.findByUsername(username);

		assertNotNull(user);
		assertEquals("testUser", user.getUsername());
		verify(userRepository).findByUsername(username);
	}

	@Test
	void saveUser_ShouldSaveUserSuccessfully() {

		// mock behavior
		when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
		when(roleService.findByName("ROLE_USER")).thenReturn(mockRole);
		when(userRepository.save(any(User.class))).thenReturn(mockUser);

		User savedUser = userService.saveUser(userDto, "ROLE_USER");

		// asertions
		assertNotNull(savedUser);
		assertEquals("testUser", savedUser.getUsername());
		assertEquals("encodedPassword", savedUser.getPassword());
		assertTrue(savedUser.getRoles().contains(mockRole));

		// verify method calls
		verify(passwordEncoder).encode(userDto.getPassword());
		verify(roleService).findByName("ROLE_USER");
		verify(userRepository).save(any(User.class));

	}

	@Test
	void getUsers_ShouldGetUsersSuccessfully() {

		Page<User> mockPage = Mockito.mock(Page.class);
		when(mockPage.getContent()).thenReturn(List.of(mockUser));

		when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

		Page<User> result = userService.getUsers("test", "ROLE_USER", 0, 10);

		assertNotNull(result);
		assertFalse(result.getContent().isEmpty());
		assertEquals("testUser", result.getContent().get(0).getUsername());

		verify(userRepository).findAll(any(Specification.class), any(Pageable.class));
	}

}
