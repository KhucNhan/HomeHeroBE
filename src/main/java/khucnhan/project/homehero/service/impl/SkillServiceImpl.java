package khucnhan.project.homehero.service.impl;

import khucnhan.project.homehero.dto.response.SkillResponse;
import khucnhan.project.homehero.exception.BadRequestException;
import khucnhan.project.homehero.exception.ResourceNotFoundException;
import khucnhan.project.homehero.model.Skill;
import khucnhan.project.homehero.repository.SkillRepository;
import khucnhan.project.homehero.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public SkillResponse create(String name) {
        if (skillRepository.existsByName(name))
            throw new BadRequestException("Skill already exists: " + name);
        Skill skill = new Skill();
        skill.setName(name);
        return toResponse(skillRepository.save(skill));
    }

    @Override
    public List<SkillResponse> getAll() {
        return skillRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!skillRepository.existsById(id))
            throw new ResourceNotFoundException("Skill not found: " + id);
        skillRepository.deleteById(id);
    }

    public SkillResponse toResponse(Skill s) {
        return SkillResponse.builder().id(s.getId()).name(s.getName()).build();
    }
}
