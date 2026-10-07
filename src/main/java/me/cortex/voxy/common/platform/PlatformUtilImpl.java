package me.cortex.voxy.common.platform;

//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
//? } else if forge {
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.api.distmarker.Dist;
//?} else {
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
//?}

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class PlatformUtilImpl implements PlatformUtil {
    @Override
    public boolean isModLoaded(String modId) {
        //? if forge || neoforge {
        LoadingModList lm = LoadingModList.get();
        if (lm == null) return false;
        return lm.getModFileById(modId) != null;
        //? } else {
        return FabricLoader.getInstance().isModLoaded(modId);
        //? }
    }

    @Override
    public Path getModRootPath(String modId) {
        //? if forge || neoforge {
        LoadingModList lm = LoadingModList.get();
        if (lm == null) return null;
        ModFileInfo info = lm.getModFileById(modId);
        if (info == null) return null;

        return info.getFile().getSecureJar().getRootPath();
        //? } else {
        return FabricLoader.getInstance().getModContainer(modId).get().getRootPaths().get(0);
        //? }
    }

    @Override
    public String getModVersion(String modId) {
        //? if forge || neoforge {
        LoadingModList lm = LoadingModList.get();
        if (lm == null) return null;
        ModFileInfo info = lm.getModFileById(modId);
        if (info == null) return null;

        return info.versionString();
        //? } else {
        return FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getVersion().getFriendlyString();
        //? }
    }

    @Override
    public Path getReplayStoragePath(boolean flashbackInstalled) {
        return null;
    }

    @Override
    public Path getConfigDir() {
        //? if forge || neoforge {    
        return FMLPaths.CONFIGDIR.get();
        //? } else {
        return FabricLoader.getInstance().getConfigDir();
        //? }
    }

    @Override
    public boolean isDedicatedServer() {
        //? if forge || neoforge {
        return FMLLoader.getDist() == Dist.DEDICATED_SERVER;
        //? } else {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
        //? }
    }
}
