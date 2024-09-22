package com.example.demo.repo;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Status;
import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	Optional<User> findByEmail(String email);
	public boolean existsByEmailAndPassword(String un, String pwd);
	Optional<User> findByEmailAndPassword(String email, String password);
	
	List<User> findAllByStatus(Status status);
	public String findImgByUserName(String nickName);
	
	public Optional<User> findByUserName(String username);
	
	User findByVirificationToken(String token);
	
}
