package test;

import java.io.*;
import povmesh.mesh.*;
import povmesh.util.*;
import povmesh.vbo.*;
import processing.core.PApplet;
import processing.core.PShape;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.volume.*;

/**
 *
 * @author sid
 */
public class VBOTentacle extends PApplet {

    /**
     * A 3D Tentacle by <a href="http://www.local-guru.net/">guru</a>
     */
    TriangleMesh mesh;
    MeshToVBO gfx;
    PShape tentacle;
    ArcBall arcball;

    /**
     *
     */
    @Override
    public void setup() {
        size(500, 500, P3D);
        arcball = new ArcBall(this);
        arcball.constrain(Constrain.YAXIS);
        gfx = new MeshToVBO(this);
        VolumetricSpace volume = new VolumetricSpaceArray(new Vec3D(100, 200, 100), 100, 100, 100);
        IsoSurface surface = new ArrayIsoSurface(volume);
        mesh = new TriangleMesh();

        VolumetricBrush brush = new RoundBrush(volume, 10);
        for (int i = 0; i < 20; i++) {
            brush.setSize(i * 1.2f + 6);
            float x = cos(i * TWO_PI / 20) * 10;
            float y = sin(i * TWO_PI / 20) * 10;
            brush.drawAtAbsolutePos(new Vec3D(x, -25 + i * 7, y), 1);
        }

        for (int i = 4; i < 20; i += 4) {
            brush.setSize(i / 1.5f + 4);

            float x = cos(i * TWO_PI / 20) * (i * 1.2f + 16);
            float y = sin(i * TWO_PI / 20) * (i * 1.2f + 16);

            brush.drawAtAbsolutePos(new Vec3D(x, -25 + i * 7, y), 1);

            brush.setSize(i / 2 + 2);
            float x2 = cos(i * TWO_PI / 20) * (i * 1.2f + 18);
            float y2 = sin(i * TWO_PI / 20) * (i * 1.2f + 18);
            brush.drawAtAbsolutePos(new Vec3D(x2, -25 + i * 7, y2), -1.4f);
        }
        volume.closeSides();
        surface.reset();
        surface.computeSurfaceMesh(mesh, .5f);
        noStroke();
        tentacle = gfx.meshToVBO(mesh, true);
        tentacle.enableStyle();
        tentacle.fill(200, 0, 0);
        tentacle.ambient(40);
        tentacle.specular(40);
    }

    /**
     *
     */
    @Override
    public void draw() {
        background(100);
        lights();
        setupLights();
        translate(width / 2, height / 3, 0);
        arcball.update();
        shape(tentacle);
    }

    /**
     *
     */
    public void setupLights() {
        lights();
        ambientLight(80, 80, 80);
        directionalLight(100, 100, 100, -1, -1, 1);
        lightSpecular(30, 30, 30);
    }

    /**
     *
     */
    @Override
    public void keyPressed() {
        if (key == 'e') {
            String fileID = "Tentacle";
            POVMesh pm = new POVMesh(this);
            pm.beginSave(new File(sketchPath(fileID + ".inc")));
            pm.setTexture(Textures.RED);  // red with Phong texture
            pm.saveAsPOV(mesh, true);
            pm.endSave();
            exit();
        }
        if (key == 's') {
            saveFrame(sketchPath("Tentacle.png"));
        }
    }

    /**
     *
     * @param passedArgs
     */
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"test.VBOTentacle"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
