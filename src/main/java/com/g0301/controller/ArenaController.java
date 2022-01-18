package com.g0301.controller;

import com.g0301.gui.Gui;
import com.g0301.model.*;
import com.g0301.state.KeyboardListener;
import com.g0301.viewer.ArenaViewer;

import java.io.IOException;

public class ArenaController extends GameController implements KeyboardListener {

    private final CarController carController;
    private final ArenaViewer arenaViewer;
    private final Gui gui;
    private Gui.ACTION movement = Gui.ACTION.RIGHT;

    public ArenaController(Gui gui, Arena arena) {
        super(arena);
        this.carController = new CarController(arena.getCar());
        this.gui = gui;
        this.arenaViewer = new ArenaViewer(gui, arena);
    }


    public void step() throws IOException {
        arenaViewer.draw();
        action(movement);
    }

    public void action(Gui.ACTION action) {
        Position currentPosition = carController.getCar().getPosition();
        Position nextPosition = carController.makeMovement(movement);

        if (!carController.getCar().collisionWithOwnTrail() && !getModel().wallCollision()) {
            carController.getCar().getTrailList().add(new Trail(currentPosition, "#FFFF00"));
            if(!getModel().enterPortalThroughExit(action) && !getModel().enterPortalThroughStart(action))
                carController.moveCar(nextPosition);
        }
    }

    @Override
    public void keyPressed(Gui.ACTION action) {
        movement = action;
    }

    public CarController getCarController() {
        return carController;
    }
}
