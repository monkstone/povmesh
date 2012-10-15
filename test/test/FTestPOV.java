package test;

import java.io.File;
import povmesh.mesh.POVMesh;
import povmesh.mesh.Textures;
import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.geom.mesh.TriangleMesh;
import toxi.processing.ToxiclibsSupport;

/**
 * 
 * @author Martin Prout
 */
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
    ToxiclibsSupport gfx;
    TriangleMesh mesh0, mesh1, mesh2;
    /**
     *
     */
    @Override
    public void setup() {
        size(200, 200, P3D);
        gfx = new ToxiclibsSupport(this);
        // define a rounded cube using the SuperEllipsoid surface function
        AABB vert = AABB.fromMinMax(new Vec3D(-1.0f, -3.5f, -1.0f), 
                new Vec3D(1.0f, 3.5f, 1.0f));
        AABB box = AABB.fromMinMax(new Vec3D(1.0f, 1.5f, -1.0f), 
                new Vec3D(3.0f, 3.5f, 1.0f));
        AABB box2 = AABB.fromMinMax(new Vec3D(1.0f, -2.5f, -1.0f), 
                new Vec3D(3.0f, -0.5f, 1.0f));
    //    TriangleMesh[] meshArray = new TriangleMesh[3];
        mesh0 = (TriangleMesh) box.toMesh();
        mesh1 = ((TriangleMesh) vert.toMesh());
        mesh2 = ((TriangleMesh) box2.toMesh());
        // attempt to create a FileOutputStream and save to it
        mesh0.addMesh(mesh1);
        mesh0.addMesh(mesh2);
        mesh0.computeFaceNormals();
        mesh0.computeVertexNormals();

        String fileID = "FTest";//+(System.currentTimeMillis()/1000);
        POVMesh pm = new POVMesh(this);//, Textures.MIRROR);
        pm.beginSave(new File(sketchPath(fileID + ".inc")));
        pm.setTexture(Textures.CHROME);
        pm.saveAsPOV(mesh0.faceOutwards(), true); // calculated normals are crap
//        pm.setTexture(Textures.RED);
//        pm.saveAsPOV(mesh1, false); 
//        pm.setTexture(Textures.WHITE);
//        pm.saveAsPOV(mesh2, false); 
        pm.endSave();
     //   exit();
    }
    
    /**
     *
     */
    @Override
    public void draw(){        
        translate(width/2, height/2);
        scale(10);
        rotateY(radians(20));
        gfx.chooseStrokeFill(false, TColor.WHITE, TColor.RED);
        gfx.mesh(mesh0);    
    }

    /**
     * 
     * @param args
     */
    static public void main(String args[]) {
        PApplet.main(new String[]{"--bgcolor=#DFDFDF", "test.FTestPOV"});
    }
}
