package test;

import povmesh.util.ArcBall;
import povmesh.vbo.*;
import processing.core.*;
import toxi.geom.*;
import toxi.geom.mesh.*;

/**
 *
 * @author sid
 */
public class MeshAlign extends PApplet {

    /**
     * This example shows how to dynamically create a simple box mesh and align
     * it with a given direction vector using the pointTowards() method of the
     * TriangleMesh class.
     */

    /* 
     * Copyright (c) 2010 Karsten Schmidt
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
    PShape meshes;
    MeshToVBO toVBO;
    ArcBall arcball;
    Vec3D BOX_SIZE = new Vec3D(5, 5, 50);
    float SCALE = 200;
    TriangleMesh[] boxes = new TriangleMesh[600];

    /**
     *
     */
    @Override
    public void setup() {
        size(600, 600, P3D);
        arcball = new ArcBall(this);
        toVBO = new MeshToVBO(this);
        colorMode(RGB, 1.0f, 1.0f, 1.0f);
        noStroke();
        meshes = createShape(PShape.GROUP);
        meshes.enableStyle();
        meshes.ambient(0.3f);
        meshes.specular(0.3f);
        meshes.specular(0.2f);
        for (int i = 0; i < boxes.length; i++) {
            // create a new direction vector for each box
            Vec3D dir = new Vec3D(cos(i * TWO_PI / 75), sin(i * TWO_PI / 50), sin(i * TWO_PI / 25)).normalize();
            // create a position on a sphere, using the direction vector
            Vec3D pos = dir.scale(SCALE);
            // create a box mesh at the origin
            TriangleMesh b = (TriangleMesh) new AABB(new Vec3D(), BOX_SIZE).toMesh();
            // align the Z axis of the box with the direction vector
            b.pointTowards(dir);
            // move the box to the correct position
            b.transform(new Matrix4x4().translateSelf(pos.x, pos.y, pos.z));
            PShape temp = toVBO.meshToVBO(b, false);
            temp.fill(random(1.0f), random(1.0f), random(1.0f));
            meshes.addChild(temp);
        }
    }

    /**
     *
     */
    @Override
    public void draw() {
        background(0.1f, 0.1f, 0.2f);
        setupLights();
        translate(width / 2, height / 2, 0);
        arcball.update();
        shape(meshes);  
        PShape[] hap = meshes.getChildren();
        for (PShape hip : hap){
        int total = hip.getVertexCount();
        System.out.println(total);
        }
    }
    
        /**
     *
     */
    public void setupLights() {
        lights();
        ambientLight(0.4f, 0.4f, 0.4f);
        directionalLight(0.2f, 0.2f, 0.2f, -1, -1, 1);
        ambient(0.2f, 0.2f, 0.2f);
        lightSpecular(0.2f, 0.2f, 0.2f);
        specular(0.2f, 0.2f, 0.2f);
        shininess(0.3f);
    }

    /**
     *
     * @param passedArgs
     */
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"test.MeshAlign"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
