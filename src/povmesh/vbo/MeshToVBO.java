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
        PShape retained = parent.createShape(PShape.TRIANGLES);
        retained.enableStyle();
        retained.fill(parent.random(255), parent.random(255), parent.random(255));
        retained.ambient(50);
        retained.shininess(10);
        retained.specular(50);
        if (smth) {
            mesh.computeVertexNormals();
            for (toxi.geom.mesh.Face f : mesh.getFaces()) {
                retained.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                retained.vertex(f.a.x, f.a.y, f.a.z);
                retained.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                retained.vertex(f.b.x, f.b.y, f.b.z);
                retained.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                retained.vertex(f.c.x, f.c.y, f.c.z);
            }
        } else {
            for (toxi.geom.mesh.Face f : mesh.getFaces()) {
                retained.normal(f.normal.x, f.normal.y, f.normal.z);
                retained.vertex(f.a.x, f.a.y, f.a.z);
                retained.vertex(f.b.x, f.b.y, f.b.z);
                retained.vertex(f.c.x, f.c.y, f.c.z);
            }
        }
        retained.end();
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
