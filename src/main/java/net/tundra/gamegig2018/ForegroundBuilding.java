package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.scene.PhysicsObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Quaternionf;

public class ForegroundBuilding extends PhysicsObject {

    private boolean collapsable, toCollapse = false;
    private int height, width, depth;

    public ForegroundBuilding(Vector3f position, boolean collapsing, int width, int height, int depth) {
        super(position,
            Model.CUBE,
            new Quaternionf(),
            new Vector3f((float)width, (float)height, (float)depth),
            collapsing ? 100f : 0f);
        collapsable = collapsing;
        getBody().setGravity(new javax.vecmath.Vector3f(0,0,0));
        this.width  = width;
        this.height = height;
        this.depth  = depth;
    }

    @Override
    public void render(Game game, Graphics graphics) throws TundraException {
        if (collapsable) {
            graphics.setColour(new Vector3f(1f, 0.4f, 0.4f));
        } else {
            graphics.setColour(new Vector3f(0.6f, 0.6f, 0.6f));
        }

        graphics.drawModel(
            Model.CUBE,
            new Matrix4f().translate(getPosition()).rotate(getRotation()).scale(width, height, depth));

    }

    @Override
    public void update(Game game, float v) throws TundraException {
        if(collapsable && !toCollapse) {
            getBody().setGravity(new javax.vecmath.Vector3f(0,0,0));
            getBody().setLinearVelocity(new javax.vecmath.Vector3f(0,0,0));
        } else if (collapsable) {
            getBody().setGravity(new javax.vecmath.Vector3f(0,-5,0));

        }
    }

    public boolean getCollapsable() {
        return collapsable;
    }

    public boolean getToCollapse() {
        return toCollapse;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setToCollapse(boolean toCollapse) {
        this.toCollapse = toCollapse;
    }
}
