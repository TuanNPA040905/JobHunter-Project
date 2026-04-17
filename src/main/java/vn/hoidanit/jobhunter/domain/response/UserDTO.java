package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private long id;
    private String name;
    private String email;
    private String gender;
    private String address;
    private int age;
    private Instant createdAt;
    private Instant updatedAt;
    private CompanyDTO company;

    public UserDTO() {
    }

    public UserDTO(long id, String name, String gender, String address, int age, CompanyDTO company) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {

        this.updatedAt = Instant.now();
    }

    public static class CompanyDTO {

        public CompanyDTO() {
        }

        private long id;

        public long getId() {
            return id;
        }

        public CompanyDTO(long id) {
            this.id = id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }

}
