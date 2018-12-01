package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.scene.GameObject;
import net.tundra.core.scene.Trackable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BackgroundBuilding extends GameObject implements Trackable {
    private int width, height, depth;

    private Vector3f position;

    public BackgroundBuilding (Vector3f position, int width, int height, int depth) {
        this.position = position;
        this.height = height;
        this.width  = width;
        this.depth  = depth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void render(Game game, Graphics graphics) throws TundraException {
        graphics.setColour(new Vector3f(0.5f, 0.5f, 0.6f));
        graphics.drawModel(Model.CUBE,
            new Matrix4f().translate(getPosition()).scale(width, height, depth));
    }

    @Override
    public void update(Game game, float v) throws TundraException {

    }

    @Override
    public Vector3f getPosition() {
        return position;
    }
}
