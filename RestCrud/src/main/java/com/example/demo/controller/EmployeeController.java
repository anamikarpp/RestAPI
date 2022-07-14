package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.security.AuthenticationRequest;
import com.example.demo.security.AuthenticationResponse;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.MyUserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class EmployeeController {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@RequestMapping(value="/authenticate",method= RequestMethod.POST)
	public ResponseEntity<?>createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)throws Exception{
		try {
		authenticationManager.authenticate(
				
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
				);
		}
		catch(BadCredentialsException e)
		{
			throw new Exception("Incorrect username or password",e);
		}
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt=jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}
	
	//get employees
	@GetMapping("/employees")
	public List<Employee> getAllEmployee()
	{
		return this.employeeRepository.findAll();
	}
	
	//get employee by id
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value="id") Long employeeId)
	throws ResourceNotFoundException{
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee not for this id ::"+employeeId));
				return ResponseEntity.ok().body(employee);
	}
	
	
	//save employee
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee)
	{
		return this.employeeRepository.save(employee);
	}
	//update employee 
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value="id") Long employeeId, @Validated @RequestBody Employee employeeDetails) throws ResourceNotFoundException
	{
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee not for this id ::"+employeeId));
		
		employee.setEmailId(employeeDetails.getEmailId());
		employee.setFirstName(employeeDetails.getLastName());
		employee.setLastName(employeeDetails.getLastName());
		final Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}
	//delete employee
	
	@DeleteMapping("/employees/{id}")
	public Map<String,Boolean> deleteEmployee( @PathVariable(value="id") Long employeeId) throws ResourceNotFoundException 
	{
		
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(()-> new ResourceNotFoundException("Employee not for this id ::"+employeeId));
		
		employeeRepository.delete(employee);
		
		Map<String,Boolean> response = new HashMap<>();
		response.put("deleted",Boolean.TRUE);
		
		return response;
		
	}
}
