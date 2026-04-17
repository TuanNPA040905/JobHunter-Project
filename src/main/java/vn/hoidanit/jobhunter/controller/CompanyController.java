package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.service.CompanyService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies/create")
    public ResponseEntity<Company> createNewCompany(@RequestBody Company company) {
        if (company.getName().isEmpty()) {

        }
        this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/companies")
    @ApiMessage("fetch companies")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<Company> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompanies(spec, pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> putMethodName(@Valid @RequestBody Company company) {
        Company com = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.ok(com);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable(name = "id") Long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("id must be less than 1500");
        }

        this.companyService.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
