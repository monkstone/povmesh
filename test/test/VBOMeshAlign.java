package test;

import java.io.File;
import povmesh.mesh.POVMesh;
import povmesh.mesh.Textures;
import processing.core.PApplet;
import processing.core.PShape;
import toxi.geom.AABB;
import toxi.geom.Matrix4x4;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;

/**
 *
 * @author Martin Prout
 */
public class VBOMeshAlign extends PApplet {

    /**
     * This example shows how to dynamically create a simple box mesh and align
     * it with a given direction vector using the pointTowards() method of the
     * TriangleMesh class.
     */
    /*
     * Copyright (c) 2010 Karsten Schmidt
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
    Vec3D BOX_SIZE = new Vec3D(2.5f, 2.5f, 10);
    float SCALE = 130;
    // TriangleMesh[] boxes = new TriangleMesh[300];
    TriangleMesh[] boxes = new TriangleMesh[300];
    PShape[] shapes;
    boolean record = false;

    @Override
    public void settings() {
        size(400, 400, P3D);
    }

    /**
     *
     */
    @Override
    public void setup() {
        for (int i = 0; i < boxes.length; i++) {
            // create a new direction vector for each box
            Vec3D dir = new Vec3D(cos(i * TWO_PI / 75), sin(i * TWO_PI / 50), sin(i * TWO_PI / 25)).normalize();
            // create a position on a sphere, using the direction vector
            Vec3D pos = dir.scale(SCALE);
            // create a box mesh at the origin
            Mesh3D bm = new AABB(new Vec3D(), BOX_SIZE).toMesh();
            TriangleMesh b = (TriangleMesh) bm;
            // align the Z axis of the box with the direction vector
            b.pointTowards(dir);
            // move the box to the correct position
            b.transform(new Matrix4x4().translateSelf(pos.x, pos.y, pos.z));
            b.setName(String.format("obj%d", i));
            boxes[i] = b;
        }
        noStroke();
        shapes = meshToRetained(boxes, false);
        frameRate(15);
    }

    /**
     *
     */
    @Override
    public void draw() {
        if (record) {
            noLoop();
            String fileID = "mesh_align";
            POVMesh pm = new POVMesh(this);
            pm.beginSave(new File(sketchPath(fileID + ".inc")));
            pm.setTexture(Textures.GLASS);
            pm.saveAsMesh(boxes, false); // calculated normals are crap
            pm.endSave();
            exit();
        } else {
            background(51);
            lights();
            lightSpecular(80, 80, 80);
            directionalLight(80, 80, 80, 0, 0, -1);
            ambientLight(50, 50, 50);
            translate(width / 2, height / 2, 0);
            rotateX(mouseY * 0.01f);
            rotateY(mouseX * 0.01f);
            for (PShape sh : shapes) {
                shape(sh);
            }

        }
    }

    /**
     *
     */
    @Override
    public void keyPressed() {
        if (key == 's') {
            record = true;
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        PApplet.main(new String[]{"--bgcolor=#DFDFDF", "test.VBOMeshAlign"});
    }

    PShape meshToVBO(Mesh3D mesh, boolean smth) {

        PShape retained = createShape();
        // retained.disableStyle();

        retained.beginShape(TRIANGLES);
        retained.fill(random(255), random(255), random(255));
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
        PShape[] rshapes = new PShape[mesh.length];
        for (int i = 0; i < mesh.length; i++) {
            rshapes[i] = meshToVBO(mesh[i], smth);
        }
        return rshapes;
    }
}
