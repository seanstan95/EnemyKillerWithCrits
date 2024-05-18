package org.seanstan95.enemykiller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Setup {
    //Initializing these here means you only have to update each value in 1 place instead of multiple
    static final double defaultCrit = 0.2;
    static final int defaultDamage = 2, defaultRequiredCrits = 3;
    static final Enemy defaultEnemy = new Enemy("Orc", 10);
    static final String defaultString = "You did nothing, how exciting.";

    /**
     * Loads settings.txt and sets required crits, crit chance, player damage, list of enemies, and list of "do nothing" lines.
     * <br><br>Method does error checking for every scenario ranging from file not found, invalid format for a setting, etc.
     * <br><br>Please report any uncovered bugs to the developer :)
     */
    public static void loadSettings() {
        System.out.println("Loading data from settings.txt...");
        defaultEnemy.reset(); //if restart is clicked, this is necessary to revive the default enemy

        String damage_line, crit_chance_line, required_crits_line, enemies_line, do_nothing_line;
        Scanner settingsFile;

        try{
            settingsFile = new Scanner(new File("src/main/resources/settings.txt"));
        }catch(FileNotFoundException fnfe) {
            loadAllDefault("Error: Settings file not found, loading all defaults.");
            return;
        }

        try {
            //Indexes are hardcoded here to allow for setting up each line in one go, at risk of error if settings are renamed in future
            damage_line = settingsFile.nextLine().substring(14);
            crit_chance_line = settingsFile.nextLine().substring(12);
            required_crits_line = settingsFile.nextLine().substring(15);
            enemies_line = settingsFile.nextLine().substring(8);
            do_nothing_line = settingsFile.nextLine().substring(17);
            settingsFile.close();
        } catch(NoSuchElementException nsee) {
            loadAllDefault("Error: couldn't load a line from settings file, loading all defaults.");
            return;
        }

        if(damage_line.isEmpty() || crit_chance_line.isEmpty() || required_crits_line.isEmpty() || enemies_line.isEmpty() || do_nothing_line.isEmpty()) {
            loadAllDefault("Error: At least one setting was left empty, loading all defaults.");
            return;
        }

        try {
            EnemyKillerController.playerDamage = Integer.parseInt(damage_line);
            EnemyKillerController.critChance = Double.parseDouble(crit_chance_line);
            EnemyKillerController.requiredCrits = Integer.parseInt(required_crits_line);
        } catch(NumberFormatException nfe) {
            System.out.println("Error: couldn't parse player damage, crit chance, or required crits, loading their defaults (2, 20%, and 3).");
            EnemyKillerController.playerDamage = defaultDamage;
            EnemyKillerController.critChance = defaultCrit;
            EnemyKillerController.requiredCrits = defaultRequiredCrits;
        }

        //Enemies and do nothing lines are separated by -. Looks weird in settings.txt, but it works :shrug:
        String[] enemies = enemies_line.split("-");
        String[] nothing_lines = do_nothing_line.split("-");

        try {
            for (String enemy : enemies) {
                //Because of the earlier error checking, there will be at least 1 enemy
                //However, it might not be formatted right, hence checking for : before continuing
                if(!enemy.contains(":")) {
                    System.out.println("Error loading an enemy, skipping it.");
                    continue; //a rare (in my opinion) instance where continue is useful
                }

                String[] enemySplit = enemy.split(":");
                Enemy thisEnemy = new Enemy(enemySplit[0], Integer.parseInt(enemySplit[1]));
                EnemyKillerController.enemies.add(thisEnemy);
            }
        } catch(NumberFormatException nfe) {
            System.out.println("Error loading an enemy health value. Loading default enemy.");
            EnemyKillerController.enemies.add(defaultEnemy);
        }

        //Edge case where the only enemy in settings.txt is invalid format
        if(EnemyKillerController.enemies.isEmpty()) {
            System.out.println("Error: all listed enemies were invalid. Loading default enemy.");
            EnemyKillerController.enemies.add(defaultEnemy);
        }

        Collections.addAll(EnemyKillerController.doNothingLines, nothing_lines); //shortcut method to cut out loop
        System.out.println("Setup successful. Loading app...");
    }

    /**
     * Helper method that simplifies loading all settings to their default value.
     * @param error error message reporting what went wrong.
     */
    private static void loadAllDefault(String error) {
        System.out.println(error);
        EnemyKillerController.playerDamage = defaultDamage;
        EnemyKillerController.critChance = defaultCrit;
        EnemyKillerController.requiredCrits = defaultRequiredCrits;
        EnemyKillerController.enemies.add(defaultEnemy);
        EnemyKillerController.doNothingLines.add(defaultString);
    }
}