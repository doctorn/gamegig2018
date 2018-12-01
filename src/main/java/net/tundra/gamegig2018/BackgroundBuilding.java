package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.resources.sprites.Sprite;
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

        Vector3f dummy = new Vector3f();
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
                .translate(getPosition().add(0, height/2, depth, dummy))
                .scale(width, depth, 1)
        );
        graphics.drawModel(
            Model.PLANE,
            current,
            new Matrix4f()
                .translate(getPosition().add(0, -height/2, depth, dummy))
                .scale(width, depth, 1)
        );

        //up face
        graphics.setColour(new Vector3f(0.5f, 0.5f, 0.5f));
        graphics.drawModel(
            Model.PLANE,
            new Matrix4f()
                .translate(getPosition().add(0, height/2, 0, dummy))
                .rotateX(-(float)Math.PI / 2)
                .scale(width, depth, 1)
        );
        graphics.setColour(new Vector3f(0.8f, 0.8f, 0.8f));
        graphics.drawModel(
            Model.CUBE,
            new Matrix4f()
                .translate(getPosition().add(0, height, depth, dummy))
                .scale(width, 0.1f, 0.1f)
        );
        graphics.drawModel(
            Model.CUBE,
            new Matrix4f()
                .translate(getPosition().add(0, height, -depth, dummy))
                .scale(width, 0.1f, 0.1f)
        );
        graphics.drawModel(
            Model.CUBE,
            new Matrix4f()
                .translate(getPosition().add(width, height, 0, dummy))
                .rotateY((float)Math.PI / 2)
                .scale(depth, 0.1f, 0.1f)
        );
        graphics.drawModel(
            Model.CUBE,
            new Matrix4f()
                .translate(getPosition().add(-width, height, 0, dummy))
                .rotateY((float)Math.PI / 2)
                .scale(depth, 0.1f, 0.1f)
        );

        //side faces
        graphics.drawModel(
            Model.PLANE,
            GameWorld.BUILDING_10x10,
            new Matrix4f()
                .translate(getPosition().add(-width, height/2,0, dummy))
                .rotateY(-(float)Math.PI / 2)
                .scale(depth, height/2, 1)
        );
        graphics.drawModel(
            Model.PLANE,
            GameWorld.BUILDING_10x10,
            new Matrix4f()
                .translate(getPosition().add(-width, -height/2,0, dummy))
                .rotateY(-(float)Math.PI / 2)
                .scale(depth, height/2, 1)
        );
        graphics.drawModel(
            Model.PLANE,
            GameWorld.BUILDING_10x10,
            new Matrix4f()
                .translate(getPosition().add(width, height/2,0, dummy))
                .rotateY((float)Math.PI / 2)
                .scale(depth, height/2, 1)
        );
        graphics.drawModel(
            Model.PLANE,
            GameWorld.BUILDING_10x10,
            new Matrix4f()
                .translate(getPosition().add(width, -height/2,0, dummy))
                .rotateY((float)Math.PI / 2)
                .scale(depth, height/2, 1)
        );
    }

    @Override
    public void update(Game game, float v) throws TundraException {

    }

    @Override
    public Vector3f getPosition() {
        return position;
    }
}
