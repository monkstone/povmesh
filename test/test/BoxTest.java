package test;

import java.io.File;
import povmesh.mesh.POVMesh;
import povmesh.mesh.Textures;
import povmesh.util.*;
import povmesh.vbo.MeshToVBO;
import processing.core.PApplet;
import processing.core.PShape;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.geom.mesh.TriangleMesh;

/**
 *
 * @author Martin Prout
 */
public class BoxTest extends PApplet {

    /**
     * <p>POVSimpleExport demonstrates how to save a model as PovRAY mesh2
     * format to a generic Java PrintWriter NB: uses createWriter convenience
     * method</p>
     */

    /*
     * Copyright (c) 2012 Martin Prout
     *
     * This library is free software; you can redistribute it and/or modify it
     * under the terms of the GNU Lesser General Public License as published by
     * the Free Software Foundation; either version 2.1 of the License, or (at
     * your option) any later version.
     *
     * http://creativecommons.org/licenses/LGPL/2.1/
     *
     * This library is distributed in the hope that it will be useful, but
     * WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
     * General Public License for more details.
     *
     * You should have received a copy of the GNU Lesser General Public License
     * along with this library; if not, write to the Free Software Foundation,
     * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
     */
    public MeshToVBO gfx;
    TriangleMesh mesh0;
    PShape aligned;
    ArcBall arcball;
    
    /**
     *
     */
    @Override
    public void setup() {
        size(200, 200, P3D);
        arcball = new ArcBall(this);
        AABB box = AABB.fromMinMax(new Vec3D(50.0f, 50.0f, 50.0f),
                new Vec3D(-50.0f, -50.0f, -50.0f));

        TriangleMesh[] meshArray = new TriangleMesh[1];
        mesh0 = (TriangleMesh) box.toMesh();
        meshArray[0] = mesh0;
        // attempt to create a FileOutputStream and save to it 
        gfx = new MeshToVBO(this);


        String fileID = "FTest";//+(System.currentTimeMillis()/1000);
        POVMesh pm = new POVMesh(this);//, Textures.MIRROR);
        pm.beginSave(new File(sketchPath(fileID + ".inc")));
        pm.setTexture(Textures.MIRROR);
        pm.saveAsMesh(meshArray, false); // calculated normals are crap
        pm.endSave();
        //exit();
        aligned = gfx.meshToVBO(mesh0, false);
        
    }

    /**
     *
     */
    @Override
    public void draw() {
        background(100);
        translate(width / 2, height / 2, -width / 2);
        arcball.update();
        shape(aligned);
    }

    /**
     *
     * @param args
     */
    static public void main(String args[]) {
        PApplet.main(new String[]{"test.BoxTest"});
    }
}
