package test;

import povmesh.mesh.POVMesh;
import processing.core.PApplet;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.geom.mesh.TriangleMesh;

public class FTestPOV extends PApplet {

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
    @Override
    public void setup() {
        // define a rounded cube using the SuperEllipsoid surface function
        AABB vert = AABB.fromMinMax(new Vec3D(-1.0f, -3.5f, -1.0f), 
                new Vec3D(1.0f, 3.5f, 1.0f));
        AABB box = AABB.fromMinMax(new Vec3D(1.0f, 1.5f, -1.0f), 
                new Vec3D(3.0f, 3.5f, 1.0f));
        AABB box2 = AABB.fromMinMax(new Vec3D(1.0f, -2.5f, -1.0f), 
                new Vec3D(3.0f, -0.5f, 1.0f));
        TriangleMesh[] meshArray = new TriangleMesh[3];
        meshArray[0] = (TriangleMesh) box.toMesh();
        meshArray[1] = ((TriangleMesh) vert.toMesh());
        meshArray[2] = ((TriangleMesh) box2.toMesh());
        // attempt to create a FileOutputStream and save to it 

        String fileID = "FTest";//+(System.currentTimeMillis()/1000);
        POVMesh pm = new POVMesh(this);
        pm.beginSave(createWriter(sketchPath(fileID + ".inc")));
        pm.saveAsPOV(meshArray, false); // calculated normals are crap
        pm.endSave();
        exit();
    }

    static public void main(String args[]) {
        PApplet.main(new String[]{"--bgcolor=#DFDFDF", "test.FTestPOV"});
    }
}
