package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.JobDTO;
import vn.hoidanit.jobhunter.domain.response.JobUpdateDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public JobDTO handleCreateAJob(Job job) {
        JobDTO jDTO = new JobDTO();
        jDTO.setName(job.getName());
        jDTO.setLocation(job.getLocation());
        jDTO.setSalary(job.getSalary());
        jDTO.setLevel(job.getLevel());
        jDTO.setDescription(job.getDescription());
        jDTO.setStartDate(job.getStartDate());
        jDTO.setEndDate(job.getEndDate());
        jDTO.setActive(job.isActive());
        List<Long> id = new ArrayList<>();
        for (Skill s : job.getSkills()) {
            id.add(s.getId());
        }
        List<Skill> skills = this.skillRepository.findByIdIn(id);
        job.setSkills(skills);
        Job j = this.jobRepository.save(job);
        jDTO.setId(j.getId());
        List<JobDTO.SkillDTO> skillDTOs = new ArrayList<>();
        for (Skill s : skills) {
            skillDTOs.add(new JobDTO.SkillDTO(s.getId()));
        }
        jDTO.setSkills(skillDTOs);
        return jDTO;
    }

    public JobUpdateDTO handleUpdateJob(Job job) {
        Optional<Job> jobOptional = this.jobRepository.findById(job.getId());
        Job j = jobOptional.isPresent() ? jobOptional.get() : null;
        if (j != null) {
            j.setName(job.getName());
            j.setLocation(job.getLocation());
            j.setSalary(job.getSalary());
            j.setLevel(job.getLevel());
            j.setDescription(job.getDescription());
            j.setStartDate(job.getStartDate());
            j.setEndDate(job.getEndDate());
            j.setActive(job.isActive());
            List<Long> id = new ArrayList<>();
            for (Skill s : job.getSkills()) {
                id.add(s.getId());
            }
            List<Skill> skills = this.skillRepository.findByIdIn(id);
            j.setSkills(skills);
            this.jobRepository.save(j);

            JobUpdateDTO jDTO = new JobUpdateDTO();
            jDTO.setName(j.getName());
            jDTO.setLocation(j.getLocation());
            jDTO.setSalary(j.getSalary());
            jDTO.setLevel(j.getLevel());
            jDTO.setDescription(j.getDescription());
            jDTO.setStartDate(j.getStartDate());
            jDTO.setEndDate(j.getEndDate());
            jDTO.setActive(j.isActive());

            List<JobUpdateDTO.SkillDTO> skillDTOs = new ArrayList<>();
            for (Skill skill : j.getSkills()) {
                skillDTOs.add(new JobUpdateDTO.SkillDTO(skill.getName()));
            }
            jDTO.setId(j.getId());
            jDTO.setSkills(skillDTOs);
            return jDTO;
        }
        return null;
    }

    public void handleDeleteJobById(long id) {
        this.jobRepository.deleteById(id);
    }

    public Job getJobById(long id) {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        return jobOptional.isPresent() ? jobOptional.get() : null;
    }

    public ResultPaginationDTO fetAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJobs = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageJobs.getNumber() + 1);
        mt.setPageSize(pageJobs.getSize());

        mt.setPages(pageJobs.getTotalPages());
        mt.setTotal(pageJobs.getTotalElements());

        rs.setMeta(mt);
        List<Job> jobs = pageJobs.getContent();
        rs.setResult(jobs);
        return rs;
    }
}
