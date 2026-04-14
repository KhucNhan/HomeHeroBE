package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.response.SkillResponse;
import java.util.List;

public interface SkillService {
    SkillResponse create(String name);
    List<SkillResponse> getAll();
    void delete(Long id);
}