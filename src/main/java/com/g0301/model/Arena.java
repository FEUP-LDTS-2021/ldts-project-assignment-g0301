package com.g0301.model;

import com.g0301.gui.Gui;
import com.g0301.controller.CarController;

import java.util.*;

public class Arena {

    private int width;
    private int height;
    private Car car = new Car(new Position(20, 30), "#FF0000");
    private Car bot = new Car(new Position(100, 30), "#3AFF33");
    private final List<Wall> walls = new ArrayList<>();
    private final List<Portal> portals = new ArrayList<>();
    private CarController carController = new CarController(car);
    private final BoostBar boostBar = new BoostBar(new Position(3, 57), "#00B600");

    /**
     * @brief Initializes the arena and the walls that delimit it
     * @param width The arena's width
     * @param height The arena's height
     */
    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        createWalls();
        createPortals();
        boostBar.createBoostBar();
    }

    /**
     * @return The car of the arena
     */
    public Car getCar() {
        return car;
    }

    public Car getBot() {
        return bot;
    }

    /**
     * @return The walls from the arena
     */
    public List<Wall> getWalls() { return walls; }

    /**
     * @return The arena's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The arena's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @brief Create the walls
     */
    private void createWalls() {
        for (int i = 0; i < width; i++) {
            walls.add(new Wall(new Position(i, 2), "#FFFFFF"));
            walls.add(new Wall(new Position(i, height - 1 - 2), "#FFFFFF"));
        }
        for (int j = 2; j < height - 2; j++) {
            walls.add(new Wall(new Position(0, j), "#FFFFFF"));
            walls.add(new Wall(new Position(width - 1, j), "#FFFFFF"));
        }
    }

    /**
     * @return Inspects if the player crash (true) into a wall and if he does so dies
     */
    public boolean wallCollision(){
        for(Wall wall : walls){
            if(wall.getPosition().equals(car.getPosition())) {
                return true;
            }
            if (wall.getPosition().equals(bot.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public void createPortals() {
        portals.add(new Portal(new Position(65, 30), new Position(10, 30), "#FF00FF"));
        portals.add(new Portal(new Position(35, 40), new Position(40, 10), "#FF0000"));
        portals.add(new Portal(new Position(10, 45), new Position(70, 50), "#00FFFF"));
    }

    public List<Portal> getPortals() {
        return portals;
    }

    public boolean enterPortalThroughStart(Gui.ACTION action) {
        for (Portal portal : portals) {
            if (car.getPosition().equals(portal.getPosition())) {
                car.setPosition(portal.getSecondPosition());
                car.setPosition(carController.makeMovement(action));
                return true;
            }
        }
        return false;
    }

    public boolean enterPortalThroughExit(Gui.ACTION action) {
        for (Portal portal : portals) {
            if (car.getPosition().equals(portal.getSecondPosition())) {
                car.setPosition(portal.getPosition());
                car.setPosition(carController.makeMovement(action));
                return true;
            }
        }
        return false;
    }

    public BoostBar getBoostBar() {
        return boostBar;
    }
}
