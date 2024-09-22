package com.example.demo.user;






import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.ChatUser;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repo.ChatUserRepo;
import com.example.demo.repo.UserRepository;



import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final ChatUserRepo repository;
    
    private final UserRepository urepo;


    public void saveUser(ChatUser user) {
    
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    public void disconnect(ChatUser user) {
        var storedUser = repository.findById(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }
   
    public List<ChatUser> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }
    
   
	public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.urepo.findAll(pageable);
	}
}