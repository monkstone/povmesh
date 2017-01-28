/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package povmesh.vbo;

import processing.core.PApplet;
import processing.core.PShape;


/**
 *
 * @author sid
 */
public class MeshToVBO {

    PApplet parent;

    /**
     *
     * @param applet
     */
    public MeshToVBO(PApplet applet) {
        this.parent = applet;
        this.parent.registerMethod("dispose", this);
    }

    /**
     *
     * @param mesh
     * @param smth
     * @return
     */
    public PShape meshToVBO(toxi.geom.mesh.Mesh3D mesh, boolean smth) {
        parent.fill(200, 0, 0);
        PShape retained = parent.createShape();
        retained.beginShape(PShape.TRIANGLES);
        //retained.enableStyle();
        //retained.fill(parent.random(255), parent.random(255), parent.random(255));
        //retained.ambient(50);
        //retained.shininess(10);
       // retained.specular(50);
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

    /**
     *
     * @param mesh
     * @param smth
     * @return
     */
    public PShape[] meshToRetained(toxi.geom.mesh.Mesh3D[] mesh, boolean smth) {
        PShape[] rshapes = new PShape[mesh.length];
        for (int i = 0; i < mesh.length; i++) {
            rshapes[i] = meshToVBO(mesh[i], smth);
        }
        return rshapes;
    }

    /**
     *
     */
    public void dispose() {
         
    }
}
