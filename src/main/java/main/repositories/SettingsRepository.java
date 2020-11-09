package main.repositories;

import main.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Setting, Integer> {
}
