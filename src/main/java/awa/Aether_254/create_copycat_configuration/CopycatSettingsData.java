package awa.Aether_254.create_copycat_configuration;

public interface CopycatSettingsData {
    boolean copycatConfig$collision();

    boolean copycatConfig$lightOcclusion();

    int copycatConfig$brightness();

    void copycatConfig$set(boolean collision, boolean lightOcclusion, int brightness);
}
