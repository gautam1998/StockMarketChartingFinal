package com.stockmarket.login.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import com.stockmarket.login.security.services.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import com.stockmarket.login.models.ConfirmationToken;
import com.stockmarket.login.models.ERole;
import com.stockmarket.login.models.Role;
import com.stockmarket.login.models.User;
import com.stockmarket.login.payload.request.LoginRequest;
import com.stockmarket.login.payload.request.SignupRequest;
import com.stockmarket.login.payload.request.UpdateRequest;
import com.stockmarket.login.payload.response.JwtResponse;
import com.stockmarket.login.payload.response.MessageResponse;
import com.stockmarket.login.repository.ConfirmationTokenRepository;
import com.stockmarket.login.repository.RoleRepository;
import com.stockmarket.login.repository.UserRepository;
import com.stockmarket.login.security.jwt.JwtUtils;
import com.stockmarket.login.security.services.EmailSenderService;
import com.stockmarket.login.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;
    
    @Autowired
    UserHelper DataService;

	
	private UserDetailsImpl userDetails;
	private String jwt;
	
	@GetMapping("/test")
    @LoadBalanced
    public String test() {
        return "company service is working";
    }
	
	@PostMapping("/signin")
	@LoadBalanced
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		if(userDetails.getIsEnabled() == true) {
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
		}
		return ResponseEntity.ok("Please get your account authorized");
	}

	@PostMapping("/signup")
	@LoadBalanced
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		Set<Role> roles = new HashSet<>();
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()),roles, false);

		Set<String> strRoles = signUpRequest.getRole();
		

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		
		 ConfirmationToken confirmationToken = new ConfirmationToken(user);

         confirmationTokenRepository.save(confirmationToken);

         SimpleMailMessage mailMessage = new SimpleMailMessage();
         mailMessage.setTo(user.getEmail());
         mailMessage.setSubject("Complete Registration!");
         mailMessage.setFrom("chand312902@gmail.com");
         mailMessage.setText("To confirm your account, please click here : "
         +"http://localhost:8082/api/auth/confirm-account?token="+confirmationToken.getConfirmationToken());

         emailSenderService.sendEmail(mailMessage);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	
	@GetMapping("/userDetails")
	@LoadBalanced
	public ResponseEntity<?> getUser() {
		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	@PutMapping("/userUpdate")
	@LoadBalanced
	public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateRequest updateRequest) {
		User user1 = userRepository.findById(userDetails.getId()).orElse(null);
		System.out.println("reached here");
		System.out.println(user1.getEmail());
		userRepository.deleteById(userDetails.getId());
		//String oldUsername = updateRequest.getEmail();
		userDetails.setEmail(updateRequest.getEmail());
		userDetails.setUsername(updateRequest.getUsername());
		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		

		//Set<String> hSet = new HashSet<String>(); 
		
		
				
		User user = new User(updateRequest.getUsername(), 
				 updateRequest.getEmail(),
				 encoder.encode(updateRequest.getPassword()),user1.getRoles(), false);
		
		userRepository.save(user);
		
		return ResponseEntity.ok(new JwtResponse(jwt, 
				 userDetails.getId(), 
				 userDetails.getUsername(), 
				 userDetails.getEmail(), 
				 roles));
	   
	  }
	
	
	@PutMapping("/userUpdate2/{username}")
	@LoadBalanced
	public Map<String, String> updateUser2(@PathVariable String username,@RequestBody User Data) {
		System.out.println("hello");
		//User user1 = userRepository.findByUsername(companyName).orElse(null);
		DataService.updateDataProfile(Data,username);
		Map<String, String> resultMap = new HashMap<>();
        resultMap.put("updateProfileStatus", "successful");
        return resultMap;

		
			   
	  }
	
	@GetMapping("/profile/{Dataname}")
	@LoadBalanced
    public ResponseEntity<User> retrieveDataProfile(@PathVariable("Dataname") String Dataname) {
        User DataDetails = DataService.retrieveDataProfile(Dataname);
        if(DataDetails!=null){
            return new ResponseEntity<>(DataDetails, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
	@RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
	@LoadBalanced
    public String confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        System.out.println("Hello I reached here ");
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
        	System.out.println("Hello I reached here 2");
        	User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            //modelAndView.setViewName("accountVerified");
        }
        return "hello";
        /*
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        */

        //return modelAndView;
    }

}
