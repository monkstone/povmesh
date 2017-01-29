package test;

import processing.core.PApplet;
import processing.core.PShape;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;

/**
 *
 * @author Martin Prout
 */
public class VBOBoxTest extends PApplet {

    /**
     * <p>
     * POVSimpleExport demonstrates how to save a model as PovRAY mesh2 format
     * to a generic Java PrintWriter NB: uses createWriter convenience
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
    PShape[] shapes;

    @Override
    public void settings() {
        size(200, 200, P3D);
    }

    /**
     *
     */
    @Override
    public void setup() {
        AABB box = AABB.fromMinMax(new Vec3D(50.0f, 50.0f, 50.0f),
                new Vec3D(-50.0f, -50.0f, -50.0f));

        TriangleMesh[] meshArray = new TriangleMesh[1];

        meshArray[0] = (TriangleMesh) box.toMesh();
        // attempt to create a FileOutputStream and save to it 
        shapes = meshToRetained(meshArray, true);

//        String fileID = "FTest";//+(System.currentTimeMillis()/1000);
//        POVMesh pm = new POVMesh(this);//, Textures.MIRROR);
//        pm.beginSave(new File(sketchPath(fileID + ".inc")));
//        pm.setTexture(Textures.MIRROR);
//        pm.saveAsMesh(meshArray, false); // calculated normals are crap
//        pm.endSave();
//        exit();
    }

    /**
     *
     */
    @Override
    public void draw() {
        background(100);
        translate(width / 2, height / 2, -width / 2);
        for (PShape sh : shapes) {
            shape(sh);
        }

    }

    /**
     *
     * @param args
     */
    static public void main(String args[]) {
        PApplet.main(new String[]{"--bgcolor=#DFDFDF", "test.VBOBoxTest"});
    }

    PShape meshToRetained(Mesh3D mesh, boolean smth) {
        PShape retained = createShape();        
//        retained.fill(200, 200, 200);
//        retained.ambient(50);
//        retained.shininess(10);
//        retained.specular(50);
//        retained.enableStyle();
        retained.beginShape(TRIANGLES);
        if (smth) {
            mesh.computeVertexNormals();
            mesh.getFaces().stream().map((f) -> {
                retained.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                return f;
            }).map((f) -> {
                retained.vertex(f.a.x, f.a.y, f.a.z);
                return f;
            }).map((f) -> {
                retained.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                return f;
            }).map((f) -> {
                retained.vertex(f.b.x, f.b.y, f.b.z);
                return f;
            }).map((f) -> {
                retained.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                return f;
            }).forEachOrdered((f) -> {
                retained.vertex(f.c.x, f.c.y, f.c.z);
            });
        } else {
            mesh.getFaces().stream().map((f) -> {
                retained.normal(f.normal.x, f.normal.y, f.normal.z);
                return f;
            }).map((f) -> {
                retained.vertex(f.a.x, f.a.y, f.a.z);
                return f;
            }).map((f) -> {
                retained.vertex(f.b.x, f.b.y, f.b.z);
                return f;
            }).forEachOrdered((f) -> {
                retained.vertex(f.c.x, f.c.y, f.c.z);
            });
        }
        retained.endShape();
        return retained;
    }

    PShape[] meshToRetained(Mesh3D[] mesh, boolean smth) {
        PShape[] mshapes = new PShape[mesh.length];
        for (int i = 0; i < mesh.length; i++) {
            mshapes[i] = meshToRetained(mesh[i], smth);
        }
        return mshapes;
    }
}
