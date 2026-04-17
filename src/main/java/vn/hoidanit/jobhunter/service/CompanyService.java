package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public void handleCreateCompany(Company company) {
        this.companyRepository.save(company);
    }

    public ResultPaginationDTO fetchAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompanies = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCompanies.getTotalPages());
        mt.setTotal(pageCompanies.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompanies.getContent());
        return rs;
    }

    public Company handleUpdateCompany(Company companyUpdate) {
        Optional<Company> companyOptional = this.companyRepository.findById(companyUpdate.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setLogo(companyUpdate.getLogo());
            currentCompany.setAddress(companyUpdate.getAddress());
            currentCompany.setDescription(companyUpdate.getDescription());
            currentCompany.setName(companyUpdate.getName());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

    public Optional<Company> getCompanyById(Long id) {
        return this.companyRepository.findById(id);
    }

    public void handleDeleteCompany(Long id) {
        this.companyRepository.deleteById(id);
    }

    @Transactional
    public void deleteById(long id) {
        Company c = this.companyRepository.findById(id).isPresent() ? this.companyRepository.findById(id).get() : null;
        if (c != null) {
            this.userRepository.deleteByCompany(c);
            this.companyRepository.deleteById(id);
        }
    }
}
