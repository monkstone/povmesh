import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

import povmesh.vbo.*; 
import povmesh.mesh.*; 
import povmesh.util.*; 
import toxi.geom.AABB; 
import toxi.geom.Vec3D; 
import toxi.geom.mesh.TriangleMesh; 
import toxi.processing.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class FTest extends PApplet {








/*
* Copyright (c) 2012 Martin Prout
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your Texture) any later version.
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
 
 /**
 * FTest demonstrates how to save a model as PovRAY mesh2
 * format to a Java File, name should have *.inc extension for
 * use in PovRAY. A mesh2 object is defined for each
 * mesh, with a different texture defined for each (if reqd) 
 * The name of the declared object/objects is "mesh_objects"
 */
TriangleMesh mesh0, mesh1, mesh2; // three meshes for demonstration purpose
MeshToVBO gfx;
PShape shape0, shape1, shape2;
ArcBall arcball;

public void setup() {
  size(200, 200, P3D);
  arcball = new ArcBall(this);
  gfx = new MeshToVBO(this);
  // define a "F" badly (upside down in processing for one thing)
  AABB vert = AABB.fromMinMax(new Vec3D(-1.0f, -4.5f, -1.0f), new Vec3D(1.0f, 3.5f, 1.0f));
  AABB box1 = AABB.fromMinMax(new Vec3D(1.0f, 1.5f, -1.0f), new Vec3D(3.0f, 3.5f, 1.0f));
  AABB box2 = AABB.fromMinMax(new Vec3D(1.0f, -2.5f, -1.0f), new Vec3D(3.0f, -0.5f, 1.0f));
  mesh0 = (TriangleMesh) box1.toMesh();    // these meshes could be merged, and are kept separate 
  mesh1 = (TriangleMesh) vert.toMesh(); // just for demonstration purposed
  mesh2 = (TriangleMesh) box2.toMesh();
  shape0 = gfx.meshToVBO(mesh0, false);
  shape1 = gfx.meshToVBO(mesh1, false);
  shape2 = gfx.meshToVBO(mesh2, false);  
}

public void draw() {
  background(200);
  setupLights();
  translate(width/2, height/2);
  scale(15);
  arcball.update();
  shape(shape0);
  shape(shape1);
  shape(shape2);
}

/**
 * 'e' saves geometry to a PovRAY *.inc file, each mesh is exported as a mesh2 object
 *  with texture. The mesh2 objects are grouped as a declared object, with named as
 * as "mesh_objects".
 * If no Texture used to create POVMesh, defaults to RAW ie no texture.
 * GLASS, METAL and PHONG use a sequence of rainbow colors with finish
 * RED and WHITE use single color for all mesh2 objects and a PHONG finish.
 * RANDOM, TWOTONE are also valid Texture, see reference.
 * 's' saves processing frame
 */

public void keyPressed() {
  if (key == 'e') {
    String fileID = "FTest";
    POVMesh pm = new POVMesh(this);
    pm.beginSave(new File(sketchPath(fileID + ".inc")));
    pm.setTexture(Textures.WHITE); // a perfect mirror surface
    pm.saveAsPOV(new TriangleMesh[]{mesh0, mesh1, mesh2});     // calculated normals are crap for boxes
    pm.endSave();
    exit();
  }
  if (key == 's') {
    saveFrame(sketchPath("Fpde.png"));
  }
}

public void setupLights() {
  lights();
  ambientLight(80, 80, 80);
  directionalLight(100, 100, 100, -1, -1, 1);
  ambient(40);
  lightSpecular(30, 30, 30);
  specular(60);
  shininess(0.7f);
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "FTest" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
