package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.JobDTO;
import vn.hoidanit.jobhunter.service.JobService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.response.JobUpdateDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<JobDTO> handleCreateJob(@RequestBody Job job) {
        JobDTO jDTO = this.jobService.handleCreateAJob(job);
        return ResponseEntity.status(HttpStatus.OK).body(jDTO);
    }

    @PutMapping("/jobs")
    public ResponseEntity<JobUpdateDTO> handleUpdateJob(@RequestBody Job job) {
        JobUpdateDTO jUpdateDTO = this.jobService.handleUpdateJob(job);
        return ResponseEntity.status(HttpStatus.OK).body(jUpdateDTO);
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> handleDeleteJob(@PathVariable(name = "id") long id) throws IdInvalidException {
        Job j = this.jobService.getJobById(id);
        if (j == null) {
            throw new IdInvalidException("Not found job");
        }
        this.jobService.handleDeleteJobById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getAJob(@PathVariable(name = "id") long id) throws IdInvalidException {
        Job j = this.jobService.getJobById(id);
        if (j == null) {
            throw new IdInvalidException("Not found job");
        }
        return ResponseEntity.status(HttpStatus.OK).body(j);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<Job> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.fetAllJobs(spec, pageable));
    }
}
