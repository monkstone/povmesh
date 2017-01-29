package test;

import java.io.*;
import povmesh.mesh.*;
import povmesh.util.ArcBall;
import povmesh.vbo.*;
import processing.core.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.math.*;
import toxi.volume.*;

/**
 *
 * @author sid
 */
public class ImplicitFunction extends PApplet {

    /**
     * This example implements a custom VolumetricSpace using an implicit
     * function to calculate each voxel. This is slower than the default array
     * or HashMap based implementations, but also has much less memory
     * requirements and so might be an interesting and more viable approach for
     * very highres voxel spaces (e.g. >32 million voxels). This implementation
     * here also demonstrates how to achieve an upper boundary on the iso value
     * (in addition to the one given and acting as lower threshold when
     * computing the iso surface)
     *
     * Usage: move mouse to rotate camera w: toggle wireframe on/off -/=: zoom
     * in/out l: apply laplacian mesh smooth e: export PovRAY mesh2
     */

    /* 
     * Copyright (c) 2010 Karsten Schmidt (modified by Martin Prout 2012)
     * 
     * This library is free software; you can redistribute it and/or
     * modify it under the terms of the GNU Lesser General Public
     * License as published by the Free Software Foundation; either
     * version 2.1 of the License, or (at your option) any later version.
     * 
     * http://creativecommons.org/licenses/LGPL/2.1/
     * 
     * This library is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
     * Lesser General Public License for more details.
     * 
     * You should have received a copy of the GNU Lesser General Public
     * License along with this library; if not, write to the Free Software
     * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
     */
    int RES = 64;
    float ISO = 0.2f;
    float MAX_ISO = 0.66f;
    ArcBall arcball;
    WETriangleMesh mesh;
    PShape implicit;
    MeshToVBO gfx;
    float currZoom = 1;
    boolean isWireframe;

    @Override
    public void settings() {
        size(1280, 720, P3D);
    }

    /**
     *
     */
    @Override
    public void setup() {
        arcball = new ArcBall(this);
        gfx = new MeshToVBO(this); // tool to convert mesh to an vbo object (PShape)
        VolumetricSpace vol = new EvaluatingVolume(new Vec3D(400, 400, 400), RES, MAX_ISO);
        IsoSurface isosurface = new HashIsoSurface(vol);
        mesh = new WETriangleMesh("iso0");
        isosurface.computeSurfaceMesh(mesh, ISO);
        noStroke();
        implicit = gfx.meshToVBO(mesh, true);
    }

    /**
     *
     */
    @Override
    public void draw() {
        background(0);
        setupLights();
        translate(width / 2, height / 2, 0);
        arcball.update();
        shape(implicit);
    }

    /**
     * 'S' or 's' saves geometry to a PovRAY *.inc file, each mesh is exported
     * as a mesh2 object with texture. The mesh2 objects are grouped as a
     * declared object, with named as as "mesh_objects". If no Options used to
     * create POVMesh, defaults to RAW ie no texture. GLASS, METAL and PHONG use
     * a sequence of rainbow colors with finish RED and WHITE use single color
     * for all mesh2 objects and a PHONG finish. RANDOM, TWOTONE are also valid
     * options, see reference
     */
    @Override
    public void keyPressed() {
        switch (key) {
            case 'l':
            case 'L':
                new LaplacianSmooth().filter(mesh, 1);
                implicit = gfx.meshToVBO(mesh, true); // need to update PShape for display
                break;
            case 's':
            case 'S':
                POVMesh pm = new POVMesh(this);
                pm.beginSave(new File(sketchPath("implicit.inc")));
                pm.setTexture(Textures.WHITE);
                pm.saveAsPOV(mesh, true);
                pm.endSave();
                exit();
                break;
        }
    }

    class EvaluatingVolume extends VolumetricSpace {

        private final float FREQ = PI * 3.8f;
        private float upperBound;
        private SinCosLUT lut;

        public EvaluatingVolume(Vec3D scale, int res, float upperBound) {
            this(scale, res, res, res, upperBound);
        }

        public EvaluatingVolume(Vec3D scale, int resX, int resY, int resZ, float upperBound) {
            super(scale, resX, resY, resZ);
            this.upperBound = upperBound;
            this.lut = new SinCosLUT();
        }

        @Override
        public void clear() {
            // nothing to do here
        }

        @Override
        public final float getVoxelAt(int i) {
            return getVoxelAt(i % resX, (i % sliceRes) / resX, i / sliceRes);
        }

        public final float getVoxelAt(int x, int y, int z) {
            float val = 0;
            if (x > 0 && x < resX1 && y > 0 && y < resY1 && z > 0 && z < resZ1) {
                float xx = (float) x / resX - 0.5f;
                float yy = (float) y / resY - 0.5f;
                float zz = (float) z / resZ - 0.5f;
                val = lut.cos(xx * FREQ) * lut.sin(yy * FREQ) + lut.cos(yy * FREQ) * lut.sin(zz * FREQ) + lut.cos(zz * FREQ) * lut.sin(xx * FREQ);
                if (val > upperBound) {
                    val = 0;
                }
            }
            return val;
        }
    }

    /**
     *
     */
    public void setupLights() {
        lights();
        ambientLight(80, 80, 80);
        directionalLight(100, 100, 100, -1, -1, 1);
        ambient(122, 122, 122);
        lightSpecular(30, 30, 30);
        specular(122, 122, 122);
        shininess(0.7f);
    }

    /**
     *
     * @param passedArgs
     */
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"test.ImplicitFunction"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
