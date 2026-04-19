package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill skill) {
        List<Skill> skills = this.skillRepository.findAll();
        for (Skill s : skills) {
            if (s.getName().equals(skill.getName())) {
                return null;
            }
        }
        return this.skillRepository.save(skill);
    }

    public Skill handleUpdateSkill(Skill skill) {
        Optional<Skill> skillOptional = this.skillRepository.findById(skill.getId());
        Skill s = skillOptional.get() != null ? skillOptional.get() : null;
        if (s != null) {
            s.setName(skill.getName());
            s.setUpdatedAt(skill.getUpdatedAt());
            s.setUpdatedBy(skill.getUpdatedBy());
            return this.skillRepository.save(s);
        }
        return null;
    }

    public ResultPaginationDTO fetchAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkills = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageSkills.getNumber() + 1);
        mt.setPageSize(pageSkills.getSize());

        mt.setPages(pageSkills.getTotalPages());
        mt.setTotal(pageSkills.getTotalElements());

        rs.setMeta(mt);
        List<Skill> skills = pageSkills.getContent();
        rs.setResult(skills);
        return rs;
    }
}
