package it.xoxryze.ryzeAuth.utils;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.commands.*;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.listeners.*;

public class CustomLoader {
    private static RyzeAuth module;
    private static AuthTable authTable;

    public CustomLoader(RyzeAuth module, AuthTable authTable) {
        this.module = module;
        this.authTable = authTable;
    }

    public static void initListener() {
        try {
            module.getServer().getPluginManager().registerEvents(new PlayerQuit(module), module);
            module.getServer().getPluginManager().registerEvents(new PlayerMove(module), module);
            module.getServer().getPluginManager().registerEvents(new PlayerChat(module), module);
            module.getServer().getPluginManager().registerEvents(new PlayerCommand(module), module);
            module.getServer().getPluginManager().registerEvents(new PlayerInteract(module), module);
            module.getServer().getPluginManager().registerEvents(new PlayerJoin(authTable, module), module);
        } catch (Exception e) {
            LogUtils.logError(e, "An error occured in the listeners initialization.");
            module.getServer().getPluginManager().disablePlugin(module);
        }
        return;
    }

    public static void initCommands() {
        try {
            module.getCommand("register").setExecutor(new RegisterCommand(authTable, module));
            module.getCommand("login").setExecutor(new LoginCommand(authTable, module));
            module.getCommand("changepassword").setExecutor(new ChangepasswordCommand(module, authTable));
            module.getCommand("unregister").setExecutor(new UnregisterCommand(module, authTable));
            module.getCommand("adminauth").setExecutor(new AdminauthCommand(module, authTable));
            module.getCommand("adminauth").setTabCompleter(new AdminauthTabCompleter());
            module.getCommand("unlogin").setExecutor(new UnloginCommand(module));
        } catch (Exception e) {
            LogUtils.logError(e, "An error occured in the commands initialization.");
            module.getServer().getPluginManager().disablePlugin(module);
            return;
        }
    }

}
