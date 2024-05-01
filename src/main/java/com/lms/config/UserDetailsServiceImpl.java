package com.lms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lms.model.User;
import com.lms.repository.UserRepository;



public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=userRepository.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("Could no find user");
		}
		if(user.isEnabled()==false){
			System.out.println("user password : " + user.getPassword());
			System.out.println("Not verified user");
			throw new UsernameNotFoundException("Could no find user");
		}
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		return customUserDetails;
	}

}
