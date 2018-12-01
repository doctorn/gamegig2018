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
        //front face
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                graphics.drawModel(
                    Model.PLANE,
                    GameWorld.BUILDING.getSprite(0,0),
                    new Matrix4f().translate(getPosition().add(2*i - width + 1, 2*j - height + 1, depth)));
            }
        }

        //up face
        /*
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < depth; j++) {
                graphics.drawModel(
                    Model.PLANE,
                    GameWorld.BUILDING.getSprite(0,0),
                    new Matrix4f().translate(getPosition()
                        .add(2*i - width + 1, height, 2*j - depth + 1))
                        .rotateX(-(float)Math.PI / 2));
            }
        }*/
        graphics.drawModel(
            Model.PLANE,
            GameWorld.BUILDING.getSprite(0,0),
            new Matrix4f()
                .translate(getPosition().add(0, height, 0))
                .rotateX(-(float)Math.PI / 2)
                .scale(width, depth, 1)
        );

        //side faces
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < depth; j++) {
                graphics.drawModel(
                    Model.PLANE,
                    GameWorld.BUILDING.getSprite(0,0),
                    new Matrix4f().translate(getPosition()
                        .add(- width,2*i -  height + 1, 2*j - depth + 1))
                        .rotateY(-(float)Math.PI / 2));
                graphics.drawModel(
                    Model.PLANE,
                    GameWorld.BUILDING.getSprite(0,0),
                    new Matrix4f().translate(getPosition()
                        .add(width,2*i -  height + 1, 2*j - depth + 1))
                        .rotateY((float)Math.PI / 2));
            }
        }

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
