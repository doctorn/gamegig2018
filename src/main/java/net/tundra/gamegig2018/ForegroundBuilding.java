package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.resources.sprites.Sprite;
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
        Sprite current;
        if(width == 5) {
            current = GameWorld.BUILDING_5x10;
        }
        else if (width == 8) {
            current = GameWorld.BUILDING_8x10;
        }
        else if (width == 12) {
            current = GameWorld.BUILDING_12x10;
        }
        else if (width == 16) {
            current = GameWorld.BUILDING_16x10;
        }
        else {
            current = GameWorld.BUILDING_10x10;
        }

        //front face
        graphics.drawModel(
            Model.PLANE,
            current,
            new Matrix4f()
                .translate(getPosition().add(0,0,depth))
                .scale(width, depth, 1)
        );

        //up face
        graphics.setColour(new Vector3f(0.5f, 0.5f, 0.5f));
        graphics.drawModel(
            Model.PLANE,
            new Matrix4f()
                .translate(getPosition().add(0, height, 0))
                .rotateX(-(float)Math.PI / 2)
                .scale(width, depth, 1)
        );

        graphics.setColour(new Vector3f(0.8f, 0.8f, 0.8f));
        graphics.drawModel(
            Model.CUBE,
            new Matrix4f()
                .translate(getPosition().add(0, height, depth))
                .scale(width, 0.1f, 0.1f)
        );
        graphics.drawModel(
            Model.CUBE,
            new Matrix4f()
                .translate(getPosition().add(0, height, -depth))
                .scale(width, 0.1f, 0.1f)
        );
        graphics.drawModel(
            Model.CUBE,
            new Matrix4f()
                .translate(getPosition().add(width, height, 0))
                .rotateY((float)Math.PI / 2)
                .scale(depth, 0.1f, 0.1f)
        );
        graphics.drawModel(
            Model.CUBE,
            new Matrix4f()
                .translate(getPosition().add(-width, height, 0))
                .rotateY((float)Math.PI / 2)
                .scale(depth, 0.1f, 0.1f)
        );



        //side faces
        graphics.drawModel(
            Model.PLANE,
            GameWorld.BUILDING_10x10,
            new Matrix4f()
                .translate(getPosition().add(-width,0,0))
                .rotateY(-(float)Math.PI / 2)
                .scale(depth, height, 1)
        );
        graphics.drawModel(
            Model.PLANE,
            GameWorld.BUILDING_10x10,
            new Matrix4f()
                .translate(getPosition().add(width,0,0))
                .rotateY((float)Math.PI / 2)
                .scale(depth, height, 1)
        );

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
