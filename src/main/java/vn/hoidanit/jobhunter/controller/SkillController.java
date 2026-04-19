package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.SkillService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.BadRequestException;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final UserService userService;
    private final SkillService skillService;

    public SkillController(SkillService skillService, UserService userService) {
        this.skillService = skillService;
        this.userService = userService;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> handleCreateSkill(@RequestBody Skill skill) throws BadRequestException {
        if (skill.getName() == null || skill.getName().isEmpty()) {
            throw new BadRequestException("name không được để trống");
        }
        Skill s = this.skillService.handleCreateSkill(skill);
        if (s == null) {
            throw new BadRequestException("Skill name = " + skill.getName() + " đã tồn tại");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(s);
        }
    }

    @PutMapping("/skills")
    public ResponseEntity<Skill> handleUpdateSkill(@RequestBody Skill skill) {
        Skill s = this.skillService.handleUpdateSkill(skill);
        return ResponseEntity.status(HttpStatus.OK).body(s);
    }

    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkills(spec, pageable));
    }

}
