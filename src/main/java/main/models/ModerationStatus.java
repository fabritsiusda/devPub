package main.models;

import lombok.Getter;

@Getter
public enum ModerationStatus {
    NEW("NEW"), ACCEPTED("ACCEPTED"), DECLINED("DECLINED");
    private final String name;

    ModerationStatus(String name) {
        this.name = name;
    }
}
