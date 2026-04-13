package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.UserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;
import vn.hoidanit.jobhunter.util.error.EmailAlreadyExistsException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO handleCreateUser(User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + user.getEmail());
        }
        User u = this.userRepository.save(user);
        UserDTO uDTO = new UserDTO();
        uDTO.setName(u.getName());
        uDTO.setEmail(u.getEmail());
        uDTO.setId(u.getId());
        uDTO.setGender(u.getGender().toString());
        uDTO.setAge(u.getAge());
        uDTO.setAddress(u.getAddress());
        uDTO.setCreatedAt(u.getCreatedAt());
        uDTO.setUpdatedAt(u.getUpdatedAt());
        return uDTO;
    }

    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public ResultPaginationDTO fetAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageUsers.getNumber() + 1);
        mt.setPageSize(pageUsers.getSize());

        mt.setPages(pageUsers.getTotalPages());
        mt.setTotal(pageUsers.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUsers.getContent());
        return rs;
    }

    public UserDTO handleUpdateUser(UserDTO userUpdate) {
        Optional<User> t = this.userRepository.findById(userUpdate.getId());

        if (t.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User userUp = t.get();

        userUp.setName(userUpdate.getName());
        userUp.setGender(GenderEnum.valueOf(userUpdate.getGender()));
        userUp.setAge(userUpdate.getAge());
        userUp.setAddress(userUpdate.getAddress());

        User updatedUser = this.userRepository.save(userUp);

        UserDTO uDTO = new UserDTO();
        uDTO.setId(updatedUser.getId());
        uDTO.setName(updatedUser.getName());
        uDTO.setEmail(updatedUser.getEmail());
        uDTO.setGender(updatedUser.getGender().toString());
        uDTO.setAge(updatedUser.getAge());
        uDTO.setAddress(updatedUser.getAddress());
        uDTO.setCreatedAt(updatedUser.getCreatedAt());
        uDTO.setUpdatedAt(updatedUser.getUpdatedAt());

        return uDTO;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

}
