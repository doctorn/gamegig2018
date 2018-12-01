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
    private int height, width;

    public ForegroundBuilding(Vector3f position, boolean collapsing, int width, int height) {
        super(position,
            Model.CUBE,
            new Quaternionf(),
            new Vector3f((float)width, (float)height, 1f),
            collapsing ? 100f : 0f);
        collapsable = collapsing;
        getBody().setGravity(new javax.vecmath.Vector3f(0,0,0));
        this.width = width;
        this.height = height;

    }

    @Override
    public void render(Game game, Graphics graphics) throws TundraException {
        if (collapsable) {
            graphics.setColour(new Vector3f(0.6f, 0.6f, 0.6f));
        } else {
            graphics.setColour(new Vector3f(0.6f, 0.6f, 0.6f));
        }

        graphics.drawModel(
            Model.CUBE,
            new Matrix4f().translate(getPosition()).scale(0.1f).rotate(getRotation()));

    }

    @Override
    public void update(Game game, float v) throws TundraException {
        if(collapsable && !toCollapse) {
            getBody().setGravity(new javax.vecmath.Vector3f(0,0,0));
            getBody().setLinearVelocity(new javax.vecmath.Vector3f(0,0,0));
        } else if (collapsable) {
            //System.out.println("Should collapse");
            getBody().setGravity(new javax.vecmath.Vector3f(0,-10,0));
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
