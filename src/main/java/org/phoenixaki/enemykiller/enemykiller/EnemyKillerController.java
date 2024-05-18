package org.phoenixaki.enemykiller.enemykiller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyKillerController {
    public Button attackButton, walkButton, victoryButton; //for enabling/disabling
    public Enemy currentEnemy;
    public int enemiesDead = 0, critsSoFar = 0;
    public Label enemyInfo, victory1, victory2, victory3, status1, status2, critLabel;
    public Random RNG = new Random();

    //These are static because they are referenced directly from Setup.java
    public static double critChance;
    public static int requiredCrits, playerDamage;
    public static List<Enemy> enemies = new ArrayList<>();
    public static List<String> doNothingLines = new ArrayList<>(); //list of possible lines for clicking "do nothing"

    /**
     * Loads information from settings.txt and initializes values. This method is re-used when "restart" is clicked.
     */
    public void initialize() {
        //These are here because the restart button uses initialize(), and this ensures they are cleared
        enemies.clear();
        doNothingLines.clear();

        Setup.loadSettings(); //sets critChance, requiredCrits, playerDamage, enemies, and doNothingLines via loading settings.txt

        critLabel.setText("Crits: 0/" + requiredCrits); //not reset to blank like the others
        critLabel.setTextFill(Color.color(1, 0, 0)); //red
        Label[] allLabels = {enemyInfo, victory1, victory2, victory3, status1, status2}; //Array makes resetting each label significantly simpler
        for (Label label : allLabels) {
            label.setText("");
        }

        //Reset game progress values
        currentEnemy = enemies.get(0);
        enemiesDead = 0;
        critsSoFar = 0;

        //Reset buttons to original state
        attackButton.setDisable(true);
        walkButton.setDisable(false);
        victoryButton.setDisable(true);
    }

    /**
     * Deals damage to current enemy based on the player's damage value (including checking if a crit happened).
     * Attack button is only active when an enemy is active.
     */
    public void attack() {
        //Assuming normal non-crit damage at start cuts back on a condition
        int damage = playerDamage;
        status1.setText("Not a critical...");

        double randomValue = RNG.nextDouble(1);
        if (randomValue <= critChance) { //if value is in range of being a crit
            critsSoFar++;
            damage *= 2;

            status1.setText("Critical!");
            if(critsSoFar <= requiredCrits)
                critLabel.setText("Crits: " + critsSoFar + "/" + requiredCrits); //only update if not past required amount yet

            //Update critLabel color based on total crit amount
            if(critsSoFar >= requiredCrits) {
                critLabel.setTextFill(Color.color(0, 0.73, 0)); //dark-ish green to match the rest
            } else {
                critLabel.setTextFill(Color.color(1, 0, 0)); //fully red
            }
        }

        //Deal damage now that crit logic is done
        currentEnemy.takeDamage(damage);
        status2.setText(String.format("You dealt %d damage to the %s.", damage, currentEnemy.name));
        enemyInfo.setText(currentEnemy.toString()); //toString gives formatted string with name and health

        //Update buttons if enemy is dead (and check if game is over now)
        if (currentEnemy.health <= 0) {
            enemiesDead++;
            attackButton.setDisable(true);
            walkButton.setDisable(false);

            //Nested here because this condition is only relevant when an enemy just died
            if (enemiesDead >= enemies.size()) {
                attackButton.setDisable(true);
                walkButton.setDisable(true);
                victoryButton.setDisable(false);
            }
        }
    }

    /**
     * Loads the next enemy from the list of enemies. Walk button is disabled once all enemies are dead.
     */
    public void walk() {
        currentEnemy = enemies.get(enemiesDead); //enemiesDead starts at 0, so it can be used as an index here
        enemyInfo.setText(currentEnemy.toString()); //toString gives formatted string with name and health
        attackButton.setDisable(false);
        walkButton.setDisable(true);
    }

    /**
     * Insults the player by choosing a random sentence from the list. This button is always active.
     */
    public void doNothing() {
        int randomValue = RNG.nextInt(doNothingLines.size()); //gives an index from 0 to size-1
        status1.setText(doNothingLines.get(randomValue));
        status2.setText("");
    }

    /**
     * Displays end-of-game text in the form of endings, depending on if enough crits happened.
     * Victory button is only active when all enemies are dead.
     */
    public void victory() {
        if (critsSoFar >= requiredCrits) {
            victory1.setText("Wow, you got " + critsSoFar + " crits!");
            victory2.setText("I am impressed by your immense 'skill'.");
            victory3.setText("Now go away and be lucky somewhere else.");
        } else {
            victory1.setText("For defeating all the enemies, you win nothing!");
            victory2.setText("Let's be real, all you did was spam a few buttons...");
            victory3.setText("You only got " + critsSoFar + " crits. Try getting at least " + requiredCrits + " crits.");
        }
    }

    /**
     * Quits the game via closing the window.
     * @param event Used to get a reference to the window, so it can be closed.
     */
    public void quit(ActionEvent event) {
        Button caller = (Button)event.getSource();
        Stage stage = (Stage)caller.getScene().getWindow();
        stage.close();
    }
}