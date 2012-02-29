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

import toxi.geom.Vec3D;

/**
 *
 * @author Martin Prout
 */
public interface POVInterface {

    /**
     * Output start of face_indices
     *
     * @param count
     */
    void beginIndices(int count);

    /**
     * Begin the mesh2 output as a PovRAY declaration
     *
     * @param name
     */
    void beginMesh2(String name);

    /**
     * Output start of normal_vectors
     *
     * @param count
     */
    void beginNormals(int count);

    /**
     * close the mesh declaration
     */
    void endSave();

    /**
     * End the current section ie vertex_vector, normal_vector or face_indices
     */
    void endSection();

    /**
     * Write face indices as as vector
     *
     * @param a
     * @param b
     * @param c
     */
    void face(int a, int b, int c);

    /**
     * Track the number of normals written to file
     *
     * @return current normal offset
     */
    int getCurrNormalOffset();

    /**
     * Track the number of vertices written to file
     *
     * @return vertex offset
     */
    int getCurrVertexOffset();

    /**
     * Required to keep in sync with current option
     *
     * @return option Textures
     */
    Textures getTexture();

    /**
     * Write normal as PovRAY vector
     *
     * @param n
     */
    void normal(Vec3D n);

    /**
     * Required to keep in sync with current option
     *
     * @param opt
     */
    void setTexture(Textures opt);

    /**
     * Used to output total count vertex_vector, normal_vector & face_indices
     *
     * @param count
     */
    void total(int count);

    /**
     * Write vertex as PovRAY vector
     *
     * @param v
     */
    void vertex(Vec3D v);
    
}
