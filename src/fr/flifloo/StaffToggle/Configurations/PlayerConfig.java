package fr.flifloo.StaffToggle.Configurations;

public enum PlayerConfig {
    STATE(".state"),
    INVENTORY(".inv"),
    ARMOR(".armor"),
    LEVEL(".level"),
    EXP(".exp"),
    GAMEMODE(".gameMode");

    String value;

    PlayerConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
