package main.services;

import main.api.responses.SettingResponse;
import main.mappers.SettingMapper;
import main.models.Setting;
import main.repositories.SettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService {

    private final SettingsRepository repository;

    public SettingsService(SettingsRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<SettingResponse> getSettings() {
        List<Setting> settings = repository.findAll();
        SettingResponse response = SettingMapper.mapSettingsToResponse(settings, SettingResponse.class);
        return response != null ? new ResponseEntity<>(response, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
