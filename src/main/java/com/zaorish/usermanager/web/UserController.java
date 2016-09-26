package com.zaorish.usermanager.web;

import com.zaorish.usermanager.model.FilterCriteria;
import com.zaorish.usermanager.model.PaginationInformation;
import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.model.UserResource;
import com.zaorish.usermanager.service.UserService;
import com.zaorish.usermanager.web.exception.ResourceNotFoundException;
import com.zaorish.usermanager.web.validation.PagedResponse;
import com.zaorish.usermanager.web.validation.UserResourceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/users")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserService userService;

	private UserDataTransformer userDataTransformer;

	private UserResourceValidator validator;

	private LinkHeaderDecorator linkHeaderDecorator;

	@Autowired
	public UserController(UserService userService, UserDataTransformer userDataTransformer, UserResourceValidator validator, LinkHeaderDecorator linkHeaderDecorator) {
		this.userService = userService;
		this.userDataTransformer = userDataTransformer;
		this.validator = validator;
		this.linkHeaderDecorator = linkHeaderDecorator;
	}

	@InitBinder("userResource")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@ModelAttribute("paginationInformation")
	public PaginationInformation addPaginationInformation(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int size) {
		return new PaginationInformation(page, size);
	}

	@RequestMapping(method = POST)
	public ResponseEntity<Void> create(@RequestBody @Valid final UserResource resource) {
		logger.info("Received create request: {}", resource);
		userService.create(userDataTransformer.transformIntoUser(resource));
		return new ResponseEntity<>(configureLinkHeader(resource), CREATED);
	}
	private HttpHeaders configureLinkHeader(UserResource resource) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setLocation(linkTo(UserController.class).slash(resource.getNickname()).toUri());
		return headers;
	}

	@RequestMapping(method = GET)
	public ResponseEntity<PagedResponse<UserResource>> findPaginated(FilterCriteria criteria, PaginationInformation paginationInformation, UriComponentsBuilder uriBuilder) {
		logger.info("Received get request of {} and {}", criteria, paginationInformation);
		PagedResponse<User> users = userService.getAll(criteria, paginationInformation);
		PagedResponse<UserResource> resources = new PagedResponse<>(users.getTotal(), userDataTransformer.transformIntoUserResource(users.getResults()), users.getPage(), users.getSize());
		return new ResponseEntity<>(resources, configureLocationHeader(paginationInformation, uriBuilder), OK);
	}
	private HttpHeaders configureLocationHeader(PaginationInformation paginationInformation, UriComponentsBuilder uriBuilder) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Link", linkHeaderDecorator.decorateLinkHeader(uriBuilder, paginationInformation));
		return headers;
	}

	@RequestMapping(value = "/{nickname}", method = GET)
	public ResponseEntity<UserResource> read(@PathVariable("nickname") final String nickname) {
		User user = getUser(nickname);
		logger.info("Received get request for {}", user);
		return new ResponseEntity<>(userDataTransformer.transformIntoUserResource(user), OK);
	}

	@RequestMapping(value = "/{nickname}", method = PUT)
	public ResponseEntity<UserResource> update(@PathVariable("nickname") final String nickname, @RequestBody @Valid final UserResource resource) {
		logger.info("Received update request of {} to {}", getUser(nickname), resource);
		userService.update(nickname, userDataTransformer.transformIntoUser(resource));
		return new ResponseEntity<>(resource, OK);
	}

	@RequestMapping(value = "/{nickname}", method = DELETE)
	public ResponseEntity<Void> delete(@PathVariable("nickname") final String nickname) {
		User user = getUser(nickname);
		logger.info("Received delete request for {}", user);
		userService.delete(user);
		return new ResponseEntity<>(NO_CONTENT);
	}

	private User getUser(String nickname) {
		Optional<User> userOptional = userService.get(nickname);
		if (!userOptional.isPresent()) {
			throw new ResourceNotFoundException("User '" + nickname + "' was not found!");
		}
		return userOptional.get();
	}

}
