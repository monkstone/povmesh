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

import java.io.*;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import toxi.geom.Vec3D;

/**
 * Fairly bare bones PovRAY 3D format exporter. Purely handles the writing of
 * data to the .inc file, as a mesh2 object. Logic heavily borrowed from toxis
 * STL/obj writer, but adjusted for PovRAY and external use/
 *
 * @see POVMesh#saveAsPOV(TriangleMesh)
 */
public class POVWriter implements POVInterface {

    final String COMMA = ", ";
    final String eol = System.getProperty("line.separator");
    private float adjust = 0.0f; //we must only hava one adjustment
    private boolean adjustIsSet = false;
    /**
     *
     */
    public final String VERSION = "0.58";
    /**
     *
     */
    protected PrintWriter povWriter;
    /**
     * Track the number of vertices written to file
     */
    protected int numVerticesWritten = 0;
    /**
     * Track the number of normals written to file
     */
    protected int numNormalsWritten = 0;
    private volatile Textures opt;
    private EnumSet<Textures> declaredOpt;
    private String spath;

    /**
     * Handles File input
     *
     * @param meshObj
     */
//    public POVWriter(PrintWriter pw) {
//        this.opt = Textures.RAW;
//        povWriter = pw;
//        declaredOpt = EnumSet.of(Textures.RAW);
//        handleBeginSave();
//    }
    public POVWriter(File meshObj) {
        this.opt = Textures.RAW;
        spath = meshObj.getParent();
        try {
            this.povWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(meshObj), "UTF8")));
        } catch (IOException ex) {
            Logger.getLogger(POVWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        declaredOpt = EnumSet.of(Textures.RAW);
        handleBeginSave();
    }

    /**
     * Handles PrintWriter input
     *
     * @param pw
     * @param opt
     */
//    public POVWriter(PrintWriter pw, Textures opt) {
//        this.opt = opt;
//        povWriter = pw;
//        declaredOpt = EnumSet.of(opt);
//        handleBeginSave();
//    }
    /**
     * close the mesh declaration
     */
    @Override
    public void endSave() {
        declaredOpt.add(opt);
        switch (opt) {
            case GLASS:
                povWriter.println(TextureBuilder.getRCTrans(9));
                povWriter.println(TextureBuilder.FINISH5);
                break;
            case METAL:
                povWriter.println(TextureBuilder.getRCOpaque(9));
                povWriter.println(TextureBuilder.MFINISHE);
                break;
            case PHONG:
                povWriter.println(TextureBuilder.getRCOpaque(9));
                povWriter.println(TextureBuilder.PHONG);
                break;
            case RED:
                povWriter.println(TextureBuilder.getRed());
                povWriter.println(TextureBuilder.PHONG);
                break;
            case WHITE:
                povWriter.println(TextureBuilder.getWhite());
                povWriter.println(TextureBuilder.PHONG);
                break;
            case RANDOM:
                povWriter.println(TextureBuilder.getRandRCTrans());
                povWriter.println(TextureBuilder.FINISH5);
                break;
            case TWOTONE:
                povWriter.println(TextureBuilder.getTwoTone());
                povWriter.println(TextureBuilder.PHONG);
                break;
            case MIRROR:
                povWriter.println(TextureBuilder.getBlack());
                povWriter.println(TextureBuilder.MIRROR);
                break;
            case MARBLE:
                povWriter.println(TextureBuilder.getMarble());
                break;
            case CHROME:
                povWriter.println(TextureBuilder.getChrome());
                break;
            default:
                break;
        }
        povWriter.println("}"); // end of mesh2 
    }

    /**
     * A side effect of this method is to set the value of the y adjust. This 
     * value is used to set the lowest value of y to be 0, and hence expect all 
     * other y values to be positive (to cope with the totally crap convention 
     * in processing where up is negative). Works best for a single mesh object
     * in the sketch, or when lowest object in sketch is given first.
     * @param min
     * @param max
     */
    public void boundingBox(Vec3D min, Vec3D max) {
        float lowest = (min.y * -1 < max.y * -1)? min.y : max.y;
        if (!adjustIsSet) {  // adjust must only be set once for a sketch
            adjust = (Math.abs(0 - lowest) > 0.0001f)? lowest : adjust;
            adjustIsSet = true;
        }
        StringBuilder pov = new StringBuilder(100);
        pov.append("// bounding_box").append(buildVector(min));             
        pov.append(buildVector(max)).append(eol);
        povWriter.print(pov.append(eol));
    }

    /**
     * Write face indices as as vector
     *
     * @param a
     * @param b
     * @param c
     */
    @Override
    public void face(int a, int b, int c) {
        povWriter.println(buildVector(a, b, c));
    }

    /**
     * Track the number of normals written to file
     *
     * @return current normal offset
     */
    @Override
    public int getCurrNormalOffset() {
        return numNormalsWritten;
    }

    /**
     * Track the number of vertices written to file
     *
     * @return vertex offset
     */
    @Override
    public int getCurrVertexOffset() {
        return numVerticesWritten;
    }

    /**
     * Helper function for writing declared textures to an *.inc file
     *
     * @param pw PrintWriter
     * @param opt expected to be an EnumSet rather than an enum
     */
    protected final void declareTexture(PrintWriter pw, Textures opt) {
        switch (opt) {
            case GLASS:
            case RANDOM:
                pw.println(TextureBuilder.declareRCTrans());
                pw.println(TextureBuilder.DFINISH5);
                break;
            case METAL:
                pw.println(TextureBuilder.declareRCOpaque());
                pw.println(TextureBuilder.DMFINISHE);
                break;
            case PHONG:
                pw.println(TextureBuilder.declareRCOpaque());
                pw.println(TextureBuilder.DPHONG);
                break;
            case MARBLE:
                pw.println(TextureBuilder.declareMarble());
                break;
            case CHROME:
                pw.println(TextureBuilder.DMFINISHE);
                break;
            case RED:
            case TWOTONE:
            case WHITE:
                pw.println(TextureBuilder.DPHONG);
                break;
            default:
                break;
        }

    }

    /**
     * Print a header, and initialise counts
     */
    protected final void handleBeginSave() {
        povWriter.println("// generated by POVExport v" + VERSION);
        //    declareTexture();
    }

    /**
     * Begin the mesh2 output as a PovRAY declaration
     *
     * @param name
     */
    @Override
    public void beginMesh2(String name) {
        numVerticesWritten = 0;
        numNormalsWritten = 0;
        StringBuilder pov = new StringBuilder(30);
        pov.append("mesh2{");
        pov.append(eol);
        pov.append(String.format("/** %s */%s", name, eol));
        pov.append("\tvertex_vectors {");
        povWriter.println(pov);
    }

    /**
     *
     * @param v
     */
    public void beginBox(Vec3D v) {
        StringBuilder pov = new StringBuilder("box{");
        pov.append('<').append(v.x);
        pov.append(COMMA).append(v.y);
        pov.append(COMMA).append(v.z);
        pov.append('>').append(COMMA);
        povWriter.print(pov);
    }

    /**
     *
     * @param v
     */
    public void endBox(Vec3D v) {
        StringBuilder pov = new StringBuilder();
        pov.append('<').append(v.x);
        pov.append(COMMA).append(v.y);
        pov.append(COMMA).append(v.z);
        pov.append('>').append("}");
        povWriter.println(pov);
    }

    /**
     * End the current section ie vertex_vector, normal_vector or face_indices
     */
    @Override
    public void endSection() {
        povWriter.println("\t}");
    }

    /**
     * Output start of normal_vectors
     *
     * @param count
     */
    @Override
    public void beginNormals(int count) {
        povWriter.println("\tnormal_vectors{");
        total(count);
    }

    /**
     * Output start of face_indices
     *
     * @param count
     */
    @Override
    public void beginIndices(int count) {
        povWriter.println("\tface_indices{");
        total(count);
    }

    /**
     * Used to output total count vertex_vector, normal_vector & face_indices
     *
     * @param count
     */
    @Override
    public void total(int count) {
        povWriter.println(String.format("\t%d,", count));
    }

    /**
     * Write normal as PovRAY vector
     *
     * @param n
     */
    @Override
    public void normal(Vec3D n) {
        povWriter.println(buildVector(n));
        numNormalsWritten++;
    }

    /**
     * Write vertex as PovRAY vector
     *
     * @param v
     */
    @Override
    public void vertex(Vec3D v) {
        povWriter.println(buildVector(v));
        numVerticesWritten++;
    }

    /**
     * This version was strictly for scale, translate and rotate
     *
     * @param a
     * @param b
     * @param c
     * @return povray vector (floats)
     */
//    private StringBuilder buildVector(float a, float b, float c) {
//        StringBuilder my_vector = new StringBuilder(120);
//        my_vector.append('<');
//        my_vector.append(a).append(COMMA);
//        my_vector.append(b).append(COMMA);
//        return my_vector.append(c).append('>');
//    }

    /**
     * This version is strictly for face indices (possibly normals/uv)
     *
     * @param a
     * @param b
     * @param c
     * @return povray vector (integers)
     */
    private StringBuilder buildVector(int a, int b, int c) {
        StringBuilder my_vector = new StringBuilder(120);
        my_vector.append('\t').append('<');
        my_vector.append(a).append(COMMA);
        my_vector.append(b).append(COMMA);
        my_vector.append(c).append('>');
        return my_vector.append(COMMA);
    }

    /**
     * The Y and Z coordinates are multiplied by -1 to handle the conversion of
     * processing coordinate system to PovRAY coordinate system. Further the y
     * coordinate is adjusted to give non-negative values (but only if the object 
     * with lowest value is processed first)
     * @param v
     * @return povray vector
     */
    private StringBuilder buildVector(Vec3D v) {
        StringBuilder my_vector = new StringBuilder(120);
        my_vector.append('\t').append('<');
        my_vector.append(v.x).append(COMMA);
        my_vector.append(v.y * -1 + adjust).append(COMMA);
        my_vector.append(v.z * -1).append('>');
        return my_vector.append(COMMA);
    }

    /**
     * Begin writing union of mesh objects
     */
    protected void beginForeground() {
        povWriter.append("#declare mesh_objects = union {");
        povWriter.append(eol);
    }

    /**
     * Finish writing union of mesh objects Write declared texture to
     * "my_texture.inc" file
     */
    protected void endForeground() {
        povWriter.append("}");
        povWriter.append(eol);

        String outFile = spath + File.separator + "my_texture.inc";
        // if (declaredOpt.size() > 1) { // guard against only RAW
        try {
            PrintWriter pw;
            pw = new PrintWriter(new File(outFile), "UTF8");
            try {
                pw.append(String.format("// %s%s", outFile, eol));
                for (Textures dopt : declaredOpt) {
                    declareTexture(pw, dopt);
                }
            } finally {
                pw.flush();
                pw.close();
                povWriter.flush();
                povWriter.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(POVWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Required to keep in sync with current option
     *
     * @param opt Textures
     */
    @Override
    public void setTexture(Textures opt) {
        this.opt = opt;
    }

    /**
     * Required to keep in sync with current option
     *
     * @return option Textures
     */
    @Override
    public Textures getTexture() {
        return this.opt;
    }
}
