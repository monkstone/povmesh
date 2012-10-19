/*
 * This library adds PovRAY export facility to toxiclibscore
 * Copyright (c) 2012 Martin Prout
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * http://creativecommons.org/licenses/LGPL/2.1/
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */
package povmesh.mesh;

import java.io.File;
import processing.core.PApplet;
import toxi.geom.AABB;
import toxi.geom.Vec3D;

/**
 * This class provides access to underlying TriangleMesh parameters to allow
 * export in PovRAY mesh2 format (with or without normals)
 *
 * @author Martin Prout
 */
public class POVMesh {

    private POVWriter pov;
    private Textures opt;
    private PApplet parent;
    /**
     *
     */
    static String VERSION = "0.58";

    /**
     * Default constructor this mesh no texture
     *
     * @param app
     */
    public POVMesh(PApplet app) {
        parent = app;
        parent.registerMethod("dispose", this);
    }

    /**
     * Allows the option to change texture option per mesh
     *
     * @param opt
     */
    public void setTexture(Textures opt) {
        this.opt = opt;
    }

    /**
     * Saves the mesh as PovRAY mesh2 format by appending it to the given mesh
     * {@link POVWriter} instance. Saves normals.
     *
     * @param meshArray
     */
    public void saveAsPOV(toxi.geom.mesh.TriangleMesh[] meshArray) {
        saveAsMesh(meshArray, true);
    }

    /**
     * Saves the mesh as PovRAY mesh2 format by appending it to the given mesh
     * {@link POVWriter} instance. Saves normals.
     *
     * @param mesh
     */
    public void saveAsPOV(toxi.geom.mesh.TriangleMesh mesh) {
        saveAsPOV(mesh, true);
    }

    /**
     * Saves the mesh as PovRAY mesh2 format to the given {@link PrintWriter}.
     * Without normals (when saveNormal is false), checks if option has changed,
     * changes option if required (implies serial use of saveAsPov)
     *
     * @param mesh
     * @param saveNormals boolean
     */
    public void saveAsPOV(toxi.geom.mesh.TriangleMesh mesh, boolean saveNormals) {
        AABB boundingBox = mesh.getBoundingBox();
        Vec3D min = boundingBox.getMin();
        Vec3D max = boundingBox.getMax();
        pov.boundingBox(min, max);
        if (opt != pov.getTexture()) {
            pov.setTexture(opt);
        }
        pov.beginMesh2(mesh.name);
        int vOffset = pov.getCurrVertexOffset();
        // vertices
        pov.total(mesh.vertices.size());
        for (toxi.geom.mesh.Vertex v : mesh.vertices.values()) {
            pov.vertex(v);
        }
        pov.endSection();
        // faces
        if (saveNormals) {
            // normals
            pov.beginNormals(mesh.vertices.size());
            for (toxi.geom.mesh.Vertex v : mesh.vertices.values()) {
                pov.normal(v.normal.getNormalized());
            }
            pov.endSection();
        }
        pov.beginIndices(mesh.faces.size());
        for (toxi.geom.mesh.Face f : mesh.faces) {
            pov.face(f.b.id + vOffset, f.a.id + vOffset, f.c.id + vOffset);

        }
        pov.endSection();
        pov.endSave();
    }

    /**
     * Saves the mesh as PovRAY mesh2 format to the given {@link PrintWriter}.
     * Without normals (when saveNormal is false) Check on option is included
     * for completeness
     *
     * @param meshArray
     * @param saveNormals boolean
     */
    public void saveAsMesh(toxi.geom.mesh.TriangleMesh[] meshArray, boolean saveNormals) {
        AABB boundingBox;
        Vec3D min;
        Vec3D max;

        if (opt != pov.getTexture()) {
            pov.setTexture(opt);
        }
        for (toxi.geom.mesh.TriangleMesh mesh : meshArray) {
            pov.beginMesh2(mesh.name);
            boundingBox = mesh.getBoundingBox();
            min = boundingBox.getMin();
            max = boundingBox.getMax();
            pov.boundingBox(min, max);
            int vOffset = pov.getCurrVertexOffset();
            // vertices
            pov.total(mesh.vertices.size());
            for (toxi.geom.mesh.Vertex v : mesh.vertices.values()) {
                pov.vertex(v);
            }
            pov.endSection();
            // faces
            if (saveNormals) {
                // normals
                pov.beginNormals(mesh.vertices.size());
                for (toxi.geom.mesh.Vertex v : mesh.vertices.values()) {
                    pov.normal(v.normal.getNormalized());
                }
                pov.endSection();
            }
            pov.beginIndices(mesh.faces.size());
            for (toxi.geom.mesh.Face f : mesh.faces) {
                pov.face(f.b.id + vOffset, f.a.id + vOffset, f.c.id + vOffset);
            }
            pov.endSection();
            pov.endSave();
        }
    }

    /**
     * Start writing *.inc file
     *
     * @param fileName main.inc File
     */
    public void beginSave(File fileName) {
        opt = Textures.RAW;
        this.pov = new POVWriter(fileName);
        pov.beginForeground();
    }

    /**
     * Finish writing *.inc file and close PrintWriter
     */
    public void endSave() {
        pov.endForeground();
    }

//    /**
//     *
//     * @param e
//     */
//    public void keyEvent(KeyEvent e) {
//        if (e.getAction() == KeyEvent.RELEASED) {
//            switch (e.getKey()) {
//                case 't':
//                case 'T':
//                    status = Tracing.EXPORTING;
//                    System.out.println("tracing");
//                    break;
//            }
//        }
//    }
    /**
     * Required by processing
     */
    public void dispose() {
        endSave();
    }

    /**
     * Required by processing
     *
     * @return version String
     */
    public String version() {
        return VERSION;
    }
}
